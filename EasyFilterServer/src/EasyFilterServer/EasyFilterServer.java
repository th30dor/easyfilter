/*
 * Proiect PDSD - EasyFilter
 *
 * @author Gherghescu Teo, 343 C1
 * @author Stoean Bogdan, 343 C1
 * @author Marin Alexandru, 343 C1
 *
 */
package EasyFilterServer;

import EasyFilterServer.Communication.ClientBlockingTcpConnection;
import EasyFilterServer.Communication.ClientNonBlockingTcpConnection;
import common.EasyPropertiesReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Vector;

/**
 * Main server class
 */
public class EasyFilterServer
{
    /**
     * vector of server IPs
     */
    private static Vector<String> serverIPs;

    /**
     * the port on which all servers communicate with each other
     */
    private static int serversListenPort;

    /**
     * The protocol read from the config file
     * Used in instanceFactory()
     */
    private static String protocol;

    /**
     * Main server function
     *
     * @param args
     * @throws UnknownHostException
     * @throws IOException
     */
    public static void main(String[] args)
        throws UnknownHostException, IOException
    {
        // Read the other servers` IPs and request type from config.ini
        EasyFilterServer.readConfigInfo();

        // accept connections from the other servers
        InterServerCommunicator isc = new InterServerCommunicator(new ServerBlockingTcpConnection());
        isc.start();

        // accept connections from the clients on a separate thread

//        OneThreadPerSocket otps = new OneThreadPerSocket(
//            new ClientBlockingTcpConnection(),
//            EasyFilterServer.getProtocol()
//        );
//        otps.acceptConnections();
//
        //todo de mutat si aici pkg
        ThreadReadPoolWrite trpw = new ThreadReadPoolWrite(
            new ClientBlockingTcpConnection(),
            EasyFilterServer.getProtocol()
        );
        trpw.acceptConnections();

//        ThreadReadPoolWrite trpw = new ThreadReadPoolWrite(
//            new ClientNonBlockingTcpConnection(),
//            EasyFilterServer.getProtocol()
//        );

        // asteptam sa primim un fisier
//        TestReceiveFile trf = new TestReceiveFile(5001);
//        trf.listen();
    }

    /**
     * Reads the server IPs from the config file and remembers them locally
     * The local server is not included in the config file
     * Also reads the request type
     */
    public static void readConfigInfo ()
    {
        int servers_number;
        String current_server_name;
        // init vector
        EasyFilterServer.setServerIPs(new Vector<String>());

        EasyPropertiesReader props = new EasyPropertiesReader("config/config.ini");
        // read the request type from the configuration settings
        EasyFilterServer.setProtocol(props.readProperty("Settings", "Protocol"));
        // read config settings for servers
        EasyFilterServer.setServersListenPort(Integer.parseInt(props.readProperty("Servers", "port")));
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

    public static int getServersListenPort() {
        return serversListenPort;
    }

    private static void setServersListenPort(int serversListenPort) {
        EasyFilterServer.serversListenPort = serversListenPort;
    }

    public static String getProtocol() {
        return protocol;
    }

    private static void setProtocol(String requestType) {
        EasyFilterServer.protocol = requestType;
    }
}