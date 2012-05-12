/*
 * Proiect PDSD - EasyFilter
 *
 * @author Gherghescu Teo, 343 C1
 * @author Stoean Bogdan, 343 C1
 * @author Marin Alexandru, 343 C1
 *
 */
package EasyFilterClient;

import common.EasyPropertiesReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the blocking TCP connection module
 */
public class BlockingTcpConnection implements ClientCommunicationInterface
{
    /**
     * The client socket
     */
    private Socket clientSocket = null;

    /**
     * Opens a connection to a server
     */
    @Override
    public void openConnection()
    {
        try {
            EasyPropertiesReader epr = new EasyPropertiesReader("config/config.ini");

            // Open client socket
            this.setClientSocket(
                new Socket(
                    epr.readProperty("Address", "IP"),
                    Integer.parseInt(epr.readProperty("Address", "port"))
                )
            );
        } catch (IOException ex) {
            Logger.getLogger(BlockingTcpConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sends a request to a server
     *
     * @param Package instance to be sent
     */
    @Override
    public void sendRequest(common.Package pkg)
    {
        OutputStream os = null;
        try {
            os = this.getClientSocket().getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            // actual sending of the image object
            oos.writeObject(pkg);
            oos.close();
            os.close();
        } catch (IOException ex) {
            Logger.getLogger(BlockingTcpConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(BlockingTcpConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Receive an image from the server
     *
     * @return Package instance sent by the server
     */
    @Override
    public common.Package receiveRequest()
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

    public Socket getClientSocket() {
        return clientSocket;
    }

    private void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
}