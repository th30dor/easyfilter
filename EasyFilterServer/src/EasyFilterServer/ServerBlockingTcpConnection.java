/*
 * Proiect PDSD - EasyFilter
 *
 * @author Gherghescu Teo, 343 C1
 * @author Stoean Bogdan, 343 C1
 * @author Marin Alexandru, 343 C1
 *
 */
package EasyFilterServer;

import EasyFilterServer.Communication.ClientBlockingTcpConnection;
import common.EasyPropertiesReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the blocking TCP connection module for server-server
 */
public class ServerBlockingTcpConnection extends ClientBlockingTcpConnection
{
    /**
     * The server socket
     */
    ServerSocket nonStaticServerSocket;

    /**
     * Opens a socket with the data read from the config file;
     * Used to communicate with the client/other server instances
     */
    @Override
    public void openConnection()
    {
        try {
            EasyPropertiesReader epr = new EasyPropertiesReader("config/config.ini");

            // Open server socket
            this.setServerSocket(
                new ServerSocket(Integer.parseInt(epr.readProperty("Servers", "port")))
            );
        } catch (IOException ex) {
            Logger.getLogger(ClientBlockingTcpConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // ~~~~~~~~ Getters and Setters ~~~~~~~~~~

    @Override
    public ServerSocket getServerSocket() {
        return this.nonStaticServerSocket;
    }

    private void setServerSocket(ServerSocket serverSocket) {
        this.nonStaticServerSocket = serverSocket;
    }
}