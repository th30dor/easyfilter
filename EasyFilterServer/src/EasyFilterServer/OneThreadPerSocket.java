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
//import EasyFilterServer.Communication.ClientNonBlockingTcpConnection;

/**
 * Handles the one thread per socket request type
 */
public class OneThreadPerSocket
{
    /**
     * Instance of a class that implements CommunicationInterface
     * Will be set in the constructor
     */
    private CommunicationInterface ci;

    /**
     * Constructor
     *
     * @param ci instance of a class that implements CommunicationInterface
     */
    public OneThreadPerSocket(CommunicationInterface ci)
    {
        this.setCi(ci);
    }

    /**
     * Opens the server socket and creates a new thread for each new
     * connection
     *
     * @return void
     */
    public void acceptConnections()
    {
        // opens the server socket
        this.getCi().openConnection();
        System.out.println("opened connection");
        while(true) {
            // creates a new communication instance for each new connection
            CommunicationInterface localCi = EasyFilterServer.instanceFactory();
            if (localCi.connectionAccepted()) {
                // receive the package from the client
                common.Package pkg = (common.Package)localCi.receiveFile();
                System.out.println("received file; before client thread");
                ClientThread wt = new ClientThread(localCi, pkg);
                wt.start();
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