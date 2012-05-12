/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyFilterServer;

/**
 *
 * @author TEO
 */
public interface CommunicationInterface {
    
    /**
     * Opens a socket with the data read from the config file;
     * used to communicate with the client/other server instances
     */
    void openConnection();
    
    /**
     * 
     * @return true if a new connection is made
     * blocks otherwise
     */
    boolean connectionAccepted();
    
    /**
     * sends a file to either the client or other servers
     * 
     * @param obj the object to be sent
     */
    void sendFile(Object obj);
    
    /**
     * receives an Object
     * @return the object received
     */
    Object receiveFile();
    
    
}
