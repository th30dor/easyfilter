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
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the blocking TCP connection module
 */
public class BlockingTcpConnection implements CommunicationInterface
{
    /**
     * The server socket
     */
    static ServerSocket serverSocket;

    /**
     * The client socket
     */
    private Socket clientSocket = null;

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
                new ServerSocket(Integer.parseInt(epr.readProperty("Address", "port")))
            );
        } catch (IOException ex) {
            Logger.getLogger(BlockingTcpConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Whether the connection is successfully created or not
     *
     * @return bool
     */
    @Override
    public boolean connectionAccepted()
    {
        try {
            // open client connection
            this.setClientSocket(this.getServerSocket().accept());
        } catch (Exception e) {
            System.err.println("Accept error: " + e);
            return false;
        }
        return true;
    }

    /**
     * Sends a file to either a client or other servers
     *
     * @param obj the object to be sent
     *
     * @return void
     */
    @Override
    public void sendFile(Object obj)
    {
        OutputStream os = null;
        try {
            os = this.getClientSocket().getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            // actual sending of the image object
            oos.writeObject(obj);
        } catch (IOException ex) {
            Logger.getLogger(BlockingTcpConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Closes the connection to a client / server
     */
    @Override
    public void closeConnection ()
    {
        try {
            this.getClientSocket().close();
        } catch (IOException ex) {
            Logger.getLogger(BlockingTcpConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Receives an Object from either a client or other servers
     *
     * @return the object received
     */
    @Override
    public Object receiveFile()
    {
        InputStream is = null;
        ObjectInputStream ois = null;
        common.Package pkg = null;

        // open the object input stream
        try {
            is = clientSocket.getInputStream();
        } catch (IOException ex) {
            Logger.getLogger(BlockingTcpConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            ois = new ObjectInputStream(is);
        } catch (IOException ex) {
            Logger.getLogger(BlockingTcpConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        // read the image as an object from the client
        try {
            pkg = (common.Package)ois.readObject();
        } catch (IOException ex) {
            Logger.getLogger(BlockingTcpConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BlockingTcpConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return pkg;
    }

    // ~~~~~~~~ Getters and Setters ~~~~~~~~~~

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    private void setServerSocket(ServerSocket serverSocket) {
        BlockingTcpConnection.serverSocket = serverSocket;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    private void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
}