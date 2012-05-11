/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyFilterServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * todo del getteri si setteri inutili
 * @author alex
 */
public class TestReceiveFile {
    private int serverPort;
    ServerSocket serverSocket;
    
    /**
     * Constructor 
     * 
     * @param serverPort 
     */
    public TestReceiveFile(int serverPort)
        throws UnknownHostException, IOException
    {
        this.serverPort = serverPort;

        // Open connection to server
        this.serverSocket  = new ServerSocket(this.serverPort);
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
    public void listen() throws IOException {
        while (true) {
            Socket clientSocket = null;

            try {
                // acceptam conexiunea de la client
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
     */
    public void readData(Socket clientSocket)
        throws IOException, ClassNotFoundException
    {
        InputStream is = clientSocket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        
        // citeste imaginea ca obiect de la client
//        Package pkg = (Package)ois.readObject();
        String s = (String)ois.readObject();
        System.out.println("s: "+s);
        
//        System.out.println("pkg: "+pkg.getHeight() + "|"+pkg.getWidth());
    }
}
