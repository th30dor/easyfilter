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
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * test: primeste un fisier
 */
public class TestReceiveFile {
    /**
     * server port number
     */
    private int serverPort;

    /**
     * the socket that will be used to receive files from clients
     */
    ServerSocket serverSocket;

    /**
     * Constructor
     *
     * @param serverPort server port number
     */
    public TestReceiveFile(int serverPort)
        throws UnknownHostException, IOException
    {
        this.serverPort = serverPort;

        // Open connection to server
        this.setServerSocket(new ServerSocket(this.getServerPort()));
    }

    /**
     * Sends a picture as a Package object
     *
     * @return void
     */
    public void listen() throws IOException {
        while (true) {
            Socket clientSocket = null;

            try {
                // open client connection
                clientSocket = this.serverSocket.accept();
                this.readData(clientSocket);
            } catch (Exception e) {
                System.err.println("Eroare la accept: " + e);
                e.printStackTrace();
            }
        }
    }

    /**
     * Reads the contents of the file sent by the client
     *
     * @param clientSocket the socket used for the communication with the client
     *
     * @return void
     */
    public void readData(Socket clientSocket)
        throws IOException, ClassNotFoundException
    {
        // open streams for reading and writing
        InputStream is = clientSocket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);

        // read the image as an object from the client
        Package pkg = (Package)ois.readObject();

        // test purpose only: write the image
        EasyImageWriter eiw = new EasyImageWriter(
            pkg.getMagicNumber(), pkg.getWidth(), pkg.getHeight(),
            pkg.getMaxGrayValue(), pkg.getImage(),"my_test.pgm"
        );
        eiw.write();
    }

    // ~~~~~~~~ Getters and Setters ~~~~~~~~~~

    public int getServerPort() {
        return serverPort;
    }

    private void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    private void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
}