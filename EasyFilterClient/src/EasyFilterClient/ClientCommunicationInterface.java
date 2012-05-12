/*
 * Proiect PDSD - EasyFilter
 *
 * @author Gherghescu Teo, 343 C1
 * @author Stoean Bogdan, 343 C1
 * @author Marin Alexandru, 343 C1
 *
 */
package EasyFilterClient;

/**
 * Interface for Client Communication modules
 */
public interface ClientCommunicationInterface {
    /**
     * Opens a connection to a server
     */
    public void openConnection();

    /**
     * Sends a request to a server
     *
     * @param Package instance to be sent
     */
    public void sendRequest(Package pkg);

    /**
     * Sends a request to a server
     *
     * @return Package instance sent by the server
     */
    public Package receiveRequest();
}