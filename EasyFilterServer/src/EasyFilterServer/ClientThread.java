/*
 * Proiect PDSD - EasyFilter
 *
 * @author Gherghescu Teo, 343 C1
 * @author Stoean Bogdan, 343 C1
 * @author Marin Alexandru, 343 C1
 *
 */
package EasyFilterServer;

import common.EasyImageReader;
import common.EasyImageWriter;
import common.Package;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Receives data from a client
 * Handles the processing of the received image
 * @todo to be continued
 */
class ClientThread extends Thread
{
    /**
     * Instance of a class that implements CommunicationInterface
     * Will be set in the constructor
     */
    CommunicationInterface ci;

    /**
     * Constructor
     *
     * @param ci instance of a class that implements CommunicationInterface
     */
    public ClientThread(CommunicationInterface ci)
    {
        this.setCi(ci);
    }

    /**
     * Receives data from a client and writes them into an output file
     *
     * @return void
     */
    @Override
    public void run()
    {
        boolean clientIsAlive = true;

        // receive packages from the client
        while (clientIsAlive) {
            common.Package pkg = (common.Package)this.getCi().receiveFile();

//            System.out.println("server primeste filename: "
//                + pkg.getFileName()
//                + " si request type: " + pkg.getRequestType());

            // determine the type of the request (image or end connection)
            switch (pkg.getRequestType()) {
            case 0:
                try {
                    // handle the image transformation on all the servers
                    this.processImageOnServers(pkg);
                } catch (Exception ex) {
                    Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                }

                // send the file back to the client
                this.getCi().sendFile(pkg);

                // todo remove test:
                // Writes the modified image with the same name
                EasyImageWriter eiw = new EasyImageWriter(pkg);
                eiw.write();
                break;
            case 1:
                // search image locally and return it to the client
                this.searchAndSendLocalImage(pkg.getFileName());
                break;
            default: case 1337:
                // exit while in order to end client connection
                clientIsAlive = false;
                break;
            } // end switch
        } // end while

        // end client connection
        this.getCi().closeConnection();
    }

    /**
     * Search image locally and return it to the client
     *
     * @param fileName name of the image to be searched locally
     *
     * @return void
     */
    public void searchAndSendLocalImage (String fileName)
    {
        Package pkg;
        String filePath = "images/" + fileName;
        try {
            // search for the image locally
            FileInputStream fis = new FileInputStream(filePath);
//            System.out.println("server recv filename "+fileName);

            EasyImageReader eir = new EasyImageReader(filePath);
            // request type is 1, because this method is only called
            // when a client requests an existing image
            pkg = new Package(eir, fileName, 1);
            // the file input stream is closed after EasyImageReader
            // finishes reading
        } catch (FileNotFoundException ex) {
            // if the file was not found, create a new empty package
            pkg = new Package(fileName, -1);
        }

        // send the file back to the client
        this.getCi().sendFile(pkg);
    }
    
    /**
     * Handle the image transformation on all the servers
     * Modifies the original pkg image
     * 
     * @param pkg image received from the client
     * 
     * @return void
     */
    private void processImageOnServers(common.Package pkg)
        throws UnknownHostException, IOException
    {
        int i, j, k, chunkSize, packageOrder;
        int servers_number = EasyFilterServer.getServerIPs().size();
        int [][] partialImage;
        common.Package chunkPackage = null;
        OutputStream os = null;
        InputStream is = null;
        ObjectInputStream ois = null;
        
        // open sockets to other servers
        Vector<Socket> serverList = new Vector<Socket>();
        for (i = 0; i <= servers_number; i++) {
            serverList.add(new Socket(
                EasyFilterServer.getServerIPs().elementAt(i),
                EasyFilterServer.getServersListenPort()
            ));
        }
        
        // get the number of lines in a chunk for each server;
        // the local server will transform the last chunk
        chunkSize = pkg.getHeight() / (servers_number + 1);
        
        // send image chunks to other servers
        for (i = 0; i < servers_number; i++) {
            // create partial image
            partialImage = new int[chunkSize][pkg.getWidth()];
            for (j = i * chunkSize; j < (i + 1) * chunkSize; j ++ ) {
                for (k = 0; k < pkg.getWidth(); k++) {
                    partialImage[j - i * chunkSize][k] = pkg.getImage()[j][k];
                }
            }
            
            // 100 + i refers to the server order
            chunkPackage = new common.Package(
                pkg.getMagicNumber(),
                pkg.getWidth(),
                chunkSize,
                pkg.getMaxGrayValue(),
                partialImage,
                pkg.getFileName(),
                100 + i
            );
            
            // send the package to the other servers
            // todo modifica in apleuri de interfata ( aici se foloseste TCP specific )
            try {
                os = serverList.get(i).getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                // actual sending of the image chunk object
                oos.writeObject(chunkPackage);
            } catch (IOException ex) {
                Logger.getLogger(ClientBlockingTcpConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        } // for servers_number 
        
        // process the last chunk locally
        // create partial image
        partialImage = new int[pkg.getHeight() - chunkSize][pkg.getWidth()];
        // i is the last sent line
        for (j = i * chunkSize; j < pkg.getHeight(); j ++ ) {
            for (k = 0; k < pkg.getWidth(); k++) {
                // process last chunk
                partialImage[j - i * chunkSize][k] = pkg.getImage()[j][k];
            }
        }
        // overwrite the initial image
        partialImage = FilterApplier.applyFilter(
            pkg.getWidth(), pkg.getHeight() - chunkSize, partialImage
        );
        
        // get chunks from other servers
        for (i = 0; i < servers_number; i++) {
            is = serverList.get(i).getInputStream();
            ois = new ObjectInputStream(is);
            try {
                chunkPackage = (common.Package)ois.readObject();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // get the package position in the initial image
            packageOrder = chunkPackage.getRequestType() - 100;
            
            // overwrite the initial image with the current chunk
            for (j = 0; j < chunkPackage.getHeight(); j ++) {
                for (k = 0; k < chunkPackage.getWidth(); k ++ ) {
                    pkg.getImage()[j + packageOrder * chunkSize][k] = chunkPackage.getImage()[j][k]; 
                }
            }
        }
    }
    
    // ~~~~~~~~ Getters and Setters ~~~~~~~~~~

    public CommunicationInterface getCi() {
        return ci;
    }

    private void setCi(CommunicationInterface ci) {
        this.ci = ci;
    }
}