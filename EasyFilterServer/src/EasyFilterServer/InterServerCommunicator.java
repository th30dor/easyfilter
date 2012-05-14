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

/**
 * Handles the communication and processing between servers
 */
public class InterServerCommunicator extends Thread
{
    /**
     * Instance of a class that implements CommunicationInterface
     * Will be set in the constructor
     */
    private CommunicationInterface ci;

    public InterServerCommunicator(CommunicationInterface ci) {
        this.setCi(ci);
    }

    /**
     * Receives a chunk from other servers, processes it and sends it back
     *
     * @return void
     */
    @Override
    public void run()
    {
        // the received chunk from another server
        common.Package pkg = null;

        this.getCi().openConnection();
        while (true) {
            // receive the chunk
            if (this.getCi().connectionAccepted()) {
                pkg = (common.Package)this.getCi().receiveFile();
            }

            // process the chunk
            pkg.setImage(FilterApplier.applyFilter(
                pkg.getWidth(), pkg.getHeight(), pkg.getImage()
            ));

            // send the processed chunk back
            ci.sendFile(pkg);

            // close the connection to the server that sent the chunk
            ci.closeConnection();
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