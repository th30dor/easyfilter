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
     * The protocol read from the config file
     * Used in instanceFactory()
     */
    private String protocol;

    /**
     * Constructor
     *
     * @param ci instance of a class that implements CommunicationInterface
     */
    public OneThreadPerSocket(CommunicationInterface ci, String requestType)
    {
        this.setCi(ci);
        this.setProtocol(requestType);
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
            CommunicationInterface localCi = instanceFactory();
            if (localCi.connectionAccepted()) {
                // receive the package from the client
                common.Package pkg = (common.Package)localCi.receiveFile();
                System.out.println("received file; before client thread");
                ClientThread wt = new ClientThread(localCi, pkg);
                wt.start();
            }
        }
    }

    /**
     * Sets the connection type based on the configuration settings in
     * "config/config.ini"
     *
     * @return new CommunicationInterface instance
     */
    CommunicationInterface instanceFactory()
    {
        if (this.getProtocol().equals("tcp")) {
            return new ClientBlockingTcpConnection();
        } else if (this.getProtocol().equals("nio")) {
//            return new ClientNonBlockingTcpConnection();
        } else {
            //TODO alte cazuri
        }

        return null;
    }

    // ~~~~~~~~ Getters and Setters ~~~~~~~~~~

    public CommunicationInterface getCi() {
        return ci;
    }

    private void setCi(CommunicationInterface ci) {
        this.ci = ci;
    }

    public String getProtocol() {
        return protocol;
    }

    private void setProtocol(String requestType) {
        this.protocol = requestType;
    }
}