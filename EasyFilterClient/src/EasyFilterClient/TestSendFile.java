/*
 * Proiect PDSD - EasyFilter
 *
 * @author Gherghescu Teo, 343 C1
 * @author Stoean Bogdan, 343 C1
 * @author Marin Alexandru, 343 C1
 *
 */
package EasyFilterClient;

import common.Package;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Clasa de test pentru trimiterea de fisiere
 */
public class TestSendFile
{
    private String serverIP;
    private int serverPort;
    Socket clientSocket;

    /**
     * Constructor
     *
     * @param serverIP   server IP
     * @param serverPort server port
     */
    public TestSendFile(String serverIP, int serverPort)
        throws UnknownHostException, IOException
    {
        this.setServerIP(serverIP);
        this.setServerPort(serverPort);

        // Open connection to server
        this.setClientSocket(
            new Socket(this.getServerIP(), this.getServerPort())
        );
    }

    /**
     * Method that sends a picture as a Package object
     *
     * @param pkg contains the image that will be sent
     *
     * @return void
     */
    public void sendFile(Package pkg) throws IOException
    {
        // output stream - used to send the file
        OutputStream os = this.clientSocket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);

        // actual sending of the image object
        oos.writeObject(pkg);

        oos.close();
        os.close();
    }

    // ~~~~~~~~ Getters and Setters ~~~~~~~~~~

    public String getServerIP() {
        return serverIP;
    }

    private void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    private void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public int getServerPort() {
        return serverPort;
    }

    private void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

}
