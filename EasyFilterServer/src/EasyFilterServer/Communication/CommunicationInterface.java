/*
 * Proiect PDSD - EasyFilter
 *
 * @author Gherghescu Teo, 343 C1
 * @author Stoean Bogdan, 343 C1
 * @author Marin Alexandru, 343 C1
 *
 */
package EasyFilterServer.Communication;

/**
 * Interface for communication modules
 */
public interface CommunicationInterface
{
    /**
     * Opens a socket with the data read from the config file;
     * Used to communicate with the client/other server instances
     */
    void openConnection();

    /**
     * Whether the connection is successfully created or not
     *
     * @return bool
     */
    boolean connectionAccepted();

    /**
     * Sends a file to either a client or other servers
     *
     * @param obj the object to be sent
     *
     * @return void
     */
    void sendFile(Object obj);

    /**
     * Receives an Object from either a client or other servers
     *
     * @return the object received
     */
    Object receiveFile();

    /**
     * Closes the connection to a client / server
     */
    public void closeConnection ();
}