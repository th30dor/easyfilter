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
class WorkerThread extends Thread{
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
    public WorkerThread(CommunicationInterface ci)
    {
        this.setCi(ci);
    }

    /**
     * Receives data from a client and writes them into an output file
     *
     * @return void
     */
    @Override
    public void run() {
        Package p = (Package)this.getCi().receiveFile();
        // Writes the modified image with the same name
        EasyImageWriter eiw = new EasyImageWriter(p);
        eiw.write();
    }

    // ~~~~~~~~ Getters and Setters ~~~~~~~~~~

    public CommunicationInterface getCi() {
        return ci;
    }

    private void setCi(CommunicationInterface ci) {
        this.ci = ci;
    }
}