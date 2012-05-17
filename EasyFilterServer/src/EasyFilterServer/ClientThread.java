/*
 * Proiect PDSD - EasyFilter
 *
 * @author Gherghescu Teo, 343 C1
 * @author Stoean Bogdan, 343 C1
 * @author Marin Alexandru, 343 C1
 *
 */
package EasyFilterServer;

import EasyFilterServer.Communication.CommunicationInterface;
import EasyFilterServer.Communication.ClientBlockingTcpConnection;
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
     * Instance of a class that implements CommunicationInterface
     */
    private common.Package pkg;

    /**
     * Constructor
     *
     * @param ci instance of a class that implements CommunicationInterface
     */
    public ClientThread(CommunicationInterface ci, common.Package pkg)
    {
        this.setCi(ci);
        this.setPkg(pkg);
    }

    /**
     * Receives data from a client and writes them into an output file
     *
     * @return void
     */
    @Override
    public void run()
    {
        System.out.println("server primeste filename: "
            + this.getPkg().getFileName()
            + " si request type: " + this.getPkg().getRequestType());

        // determine the type of the request (image or end connection)
        switch (this.getPkg().getRequestType()) {
        case 0:
            try {
                // handle the image transformation on all the servers
                this.processImageOnServers(this.getPkg());
            } catch (Exception ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }

            // send the file back to the client
            this.getCi().sendFile(this.getPkg());

            // todo remove test:
            // Writes the modified image with the same name
            EasyImageWriter eiw = new EasyImageWriter(this.getPkg());
            eiw.write();
            break;
        case 1:
            // search image locally and return it to the client
            this.searchAndSendLocalImage(this.getPkg().getFileName());
            break;
        default:
            // exit while in order to end client connection
            break;
        } // end switch

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
        common.Package pkg;
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
        System.out.println("start processImageOnServers ");
        int i, j, k, chunkSize, packageOrder;
        int servers_number = EasyFilterServer.getServerIPs().size();
        int [][] partialImage;
        common.Package chunkPackage = null;
        OutputStream os = null;
        InputStream is = null;
        ObjectInputStream ois = null;

        // open sockets to other servers
        Vector<Socket> serverList = new Vector<Socket>();
        for (i = 0; i < servers_number; i++) {
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
                System.out.println("Before server oos.writeObject");
                oos.writeObject(chunkPackage);
                System.out.println("After server oos.writeObject");
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

        // overwrite the initial image with the last chunk
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

        // overwrite the last chunk
        for (i = servers_number * chunkSize; i < pkg.getHeight(); i ++) {
            for (k = 0; k < pkg.getWidth(); k ++ ) {
                pkg.getImage()[i][k] = partialImage[i - servers_number * chunkSize][k];
            }
        }
        
        // send the image to all the other servers
        for (i = 0; i < servers_number; i++) {
            try {
                os = serverList.get(i).getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(pkg);
            } catch (IOException ex) {
                Logger.getLogger(ClientBlockingTcpConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    } // end processImageOnServers

    // ~~~~~~~~ Getters and Setters ~~~~~~~~~~

    public Package getPkg() {
        return pkg;
    }

    private void setPkg(Package pkg) {
        this.pkg = pkg;
    }

    public CommunicationInterface getCi() {
        return ci;
    }

    private void setCi(CommunicationInterface ci) {
        this.ci = ci;
    }
}