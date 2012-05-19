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
import common.EasyPropertiesReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class ThreadReadPoolWrite extends OneThreadPerSocket
{
    /**
     * Thread Pool
     */
    private ExecutorService clientThreadPool;

    /**
     * Maximum number of threads; read from the config file
     */
    private int maxNumberOfThreads;

    /**
     * Constructor
     *
     * @param ci Instance of a class that implements CommunicationInterface
     *           Will be set in the constructor
     */
    public ThreadReadPoolWrite(CommunicationInterface ci)
    {
        super(ci);

        // get number of threads from the config file
        EasyPropertiesReader props = new EasyPropertiesReader("config/config.ini");
        this.setMaxNumberOfThreads(
            Integer.parseInt(props.readProperty("Settings", "MaxThreads"))
        );

        this.setClientThreadPool(
            Executors.newFixedThreadPool(this.getMaxNumberOfThreads())
        );
    }

    /**
     * Opens the server socket and creates a new thread for each new
     * connection
     *
     * @return void
     */
    @Override
    public void acceptConnections()
    {
        // opens the server socket
        this.getCi().openConnection();
        Logger.getLogger(ThreadReadPoolWrite.class.getName()).log(Level.INFO, " Connection opened");

        while(true) {
            // creates a new thread for each new connection
            CommunicationInterface localCi = EasyFilterServer.instanceFactory();
            if (localCi.connectionAccepted()) {
                // receive the package from the client
                common.Package pkg = (common.Package)localCi.receiveFile();
                Logger.getLogger(ThreadReadPoolWrite.class.getName()).log(Level.INFO, " Receieved file from client");
                this.getClientThreadPool().submit(new ClientThread(localCi, pkg));
            }
        }
    }

    // ~~~~~~~~ Getters and Setters ~~~~~~~~~~

    public ExecutorService getClientThreadPool() {
        return clientThreadPool;
    }

    private void setClientThreadPool(ExecutorService clientThreadPool) {
        this.clientThreadPool = clientThreadPool;
    }

    public int getMaxNumberOfThreads() {
        return maxNumberOfThreads;
    }

    private void setMaxNumberOfThreads(int maxNumberOfThreads) {
        this.maxNumberOfThreads = maxNumberOfThreads;
    }
}