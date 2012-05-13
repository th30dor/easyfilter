/*
 * Proiect PDSD - EasyFilter
 *
 * @author Gherghescu Teo, 343 C1
 * @author Stoean Bogdan, 343 C1
 * @author Marin Alexandru, 343 C1
 *
 */
package EasyFilterServer;

import common.EasyImageWriter;
import common.Package;

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
                // todo search image locally && return it to the client
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

    // ~~~~~~~~ Getters and Setters ~~~~~~~~~~

    public CommunicationInterface getCi() {
        return ci;
    }

    private void setCi(CommunicationInterface ci) {
        this.ci = ci;
    }
}