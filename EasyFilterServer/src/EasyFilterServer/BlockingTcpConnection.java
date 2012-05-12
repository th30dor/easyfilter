/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyFilterServer;

import common.EasyPropertiesReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TEO
 */
public class BlockingTcpConnection implements CommunicationInterface extends Object {

     static ServerSocket serverSocket;
     private Socket clientSocket = null;
    
    @Override
    public void openConnection() {
        try {
            EasyPropertiesReader epr = new EasyPropertiesReader("config/config.ini");
            
            // Open connection to server
            this.setServerSocket(new ServerSocket(Integer.parseInt(epr.readProperty("Address", "port"))));
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(BlockingTcpConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    @Override
    public boolean connectionAccepted() {
        
            try {
                // open client connection
                this.setClientSocket(this.getServerSocket().accept());
                 
            } catch (Exception e) {
                System.err.println("Eroare la accept: " + e);
                e.printStackTrace();
                return false;
            }
            return true;
    }


    @Override
    public void sendFile(Object obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object receiveFile()
    {
        
        InputStream is = null;
        try {
            is = clientSocket.getInputStream();
        } catch (IOException ex) {
            Logger.getLogger(BlockingTcpConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(is);
        } catch (IOException ex) {
            Logger.getLogger(BlockingTcpConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        // read the image as an object from the client
        common.Package pkg = null;
        try {
            pkg = (common.Package)ois.readObject();
        } catch (IOException ex) {
            Logger.getLogger(BlockingTcpConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BlockingTcpConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return pkg;
    }
    
    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        BlockingTcpConnection.serverSocket = serverSocket;
    }
        
    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    
}
