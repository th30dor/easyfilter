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
            Package pkg = (Package)this.getCi().receiveFile();

//            System.out.println("server primeste filename: "
//                + pkg.getFileName()
//                + " si request type: " + pkg.getRequestType());

            // determine the type of the request (image or end connection)
            switch (pkg.getRequestType()) {
            case 0:
                // apply filter
                pkg.setImage(
                    FilterApplier.applyFilter(
                        pkg.getWidth(), pkg.getHeight(), pkg.getImage()
                    )
                );

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
    // ~~~~~~~~ Getters and Setters ~~~~~~~~~~

    public CommunicationInterface getCi() {
        return ci;
    }

    private void setCi(CommunicationInterface ci) {
        this.ci = ci;
    }
}