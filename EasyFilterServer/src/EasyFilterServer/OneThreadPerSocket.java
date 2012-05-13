/*
 * Proiect PDSD - EasyFilter
 *
 * @author Gherghescu Teo, 343 C1
 * @author Stoean Bogdan, 343 C1
 * @author Marin Alexandru, 343 C1
 *
 */
package EasyFilterServer;

import common.EasyPropertiesReader;

/**
 * Handles the one thread per socket request type
 */
public class OneThreadPerSocket
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

        while(true) {
            // creates a new thread for each new connection
            CommunicationInterface localCi = instanceFactory();
            if (localCi.connectionAccepted()){
                ClientThread wt = new ClientThread(localCi);
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
         // read the request type from the configuration settings
         EasyPropertiesReader epr = new EasyPropertiesReader("config/config.ini");
         String requestType = epr.readProperty("Settings", "RequestType");

         // set the connection type
         if(requestType.equals("tcp")) {
             return new BlockingTcpConnection();
         } else {
             //TODO alte cazuri
             return new BlockingTcpConnection();
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