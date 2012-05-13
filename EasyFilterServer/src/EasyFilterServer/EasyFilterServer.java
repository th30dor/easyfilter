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
import java.net.UnknownHostException;
import java.util.Vector;

/**
 * Main server class
 */
public class EasyFilterServer
{
    // vector of server IPs
    private static Vector<String> serverIPs;

    public static void main(String[] args)
        throws UnknownHostException, IOException
    {
        // Read the other servers` IPs from config.ini
        EasyFilterServer.readServerIPs();
//        OneThreadPerSocket otps = new OneThreadPerSocket(new BlockingTcpConnection());
//        otps.acceptConnections();
        // asteptam sa primim un fisier
//        TestReceiveFile trf = new TestReceiveFile(5001);
//        trf.listen();
    }

    /**
     * Reads the server IPs from the config file
     * Remembers them locally
     */
    public static void readServerIPs ()
    {
        int servers_number;
        String current_server_name;
        // init vector
        EasyFilterServer.setServerIPs(new Vector<String>());

        // Read config settings for servers
        EasyPropertiesReader props = new EasyPropertiesReader("config/config.ini");
        servers_number = Integer.parseInt(props.readProperty("Servers", "servers_number"));

        // Remember server IPs
        for (int i = 1; i <= servers_number; i++ ) {
            current_server_name = props.readProperty("Servers", "server" + i);
            EasyFilterServer.getServerIPs().add(current_server_name);
        }
    }

    // ~~~~~~~~ Getters and Setters ~~~~~~~~~~

    public static Vector<String> getServerIPs() {
        return EasyFilterServer.serverIPs;
    }

    private static void setServerIPs(Vector<String> serverIPs) {
        EasyFilterServer.serverIPs = serverIPs;
    }
}