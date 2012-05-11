/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyFilterClient;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * todo del getteri si setteri inutili
 * @author alex
 */
public class TestSendFile {
    private String serverIP;
    private int serverPort;

    Socket clientSocket;
    
    /**
     * Constructor 
     * 
     * @param serverIP
     * @param serverPort 
     */
    public TestSendFile(String serverIP, int serverPort)
        throws UnknownHostException, IOException
    {
        this.serverIP = serverIP;
        this.serverPort = serverPort;

        // Open connection to server
        this.clientSocket  = new Socket(this.serverIP, this.serverPort);
    }
    
    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
    
    /**
     * Metoda care trimite o poza sub forma unui pachet 
     */
    public void sendFile(Package pkg) throws IOException {
        // output stream - used to send the file
        OutputStream os = this.clientSocket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);

        // actual sending of the image object
        oos.writeObject(pkg);

        oos.close();
        os.close();
    }
    
    public void sendFile ( String s) throws IOException {
        // output stream - used to send the file
        OutputStream os = this.clientSocket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);

        // actual sending of the image object
        oos.writeObject(s);

        oos.close();
        os.close();
    }
    
}
