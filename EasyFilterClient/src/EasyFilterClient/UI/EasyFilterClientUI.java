/*
 * Proiect PDSD - EasyFilter
 *
 * @author Gherghescu Teo, 343 C1
 * @author Stoean Bogdan, 343 C1
 * @author Marin Alexandru, 343 C1
 *
 */
package EasyFilterClient.UI;

import EasyFilterClient.Communication.*;
import EasyFilterClient.EasyFilterClient;
import common.EasyPropertiesReader;
import java.util.Vector;
import javax.swing.JFrame;

/**
 * Main user interface for EasyClient
 */
public class EasyFilterClientUI extends JFrame {
    private EasyFilterClientInter mainPanel;

    /**
     * The protocol read from the config file
     * Used in instanceFactory()
     */
    private static String protocol;
    
    /**
     * The client IP read from the config file
     */
    private static String clientIP;
    
    /**
     * The server IP read from the config file
     */
    private static String serverIP;
    
    
    /**
     * The server IP read from the config file
     */
    private static int serverPort;
    
    /**
     * Constructor
     */
    public EasyFilterClientUI() {
        //mainPanel = new MainPanel();
        // todo selectare tip conexiune
        EasyFilterClientUI.readConfigInfo();
        ClientCommunicationInterface ci = EasyFilterClientUI.instanceFactory();
        mainPanel = new EasyFilterClientInter(ci);
//        mainPanel = new EasyFilterClientInter(new BlockingUdpConnection());
        setSize(400, 400);
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        add(mainPanel);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Main method
     *
     * @param args arguments to be run with
     */
    public static void main(String args[]) {
        EasyFilterClientUI ui = new EasyFilterClientUI();
    }
    
    /**
     * Sets the connection type based on the configuration settings in
     * "config/config.ini"
     *
     * @return new CommunicationInterface instance
     */
    public static ClientCommunicationInterface instanceFactory()
    {
        if (EasyFilterClientUI.getProtocol().equals("tcp")) {
            return new BlockingTcpConnection();
        } else if (EasyFilterClientUI.getProtocol().equals("nio")) {
            return new BlockingTcpConnection();
        } else if (EasyFilterClientUI.getProtocol().equals("udp")){
            return new BlockingUdpConnection();
        }

        return null;
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

        EasyPropertiesReader props = new EasyPropertiesReader("config/config.ini");
        // read the request type from the configuration settings
        EasyFilterClientUI.setProtocol(props.readProperty("Settings", "Protocol"));
        // read client address
        EasyFilterClientUI.setClientIP(props.readProperty("Settings", "localIP"));
        EasyFilterClientUI.setServerIP(props.readProperty("Address", "IP"));
        EasyFilterClientUI.setServerPort(Integer.parseInt(props.readProperty("Address", "port")));
    }
    
    public static String getProtocol() {
        return protocol;
    }

    private static void setProtocol(String requestType) {
        EasyFilterClientUI.protocol = requestType;
    }

    public static String getClientIP() {
        return clientIP;
    }

    private static void setClientIP(String clientIP) {
        EasyFilterClientUI.clientIP = clientIP;
    }
    
    public static String getServerIP() {
        return serverIP;
    }

    private static void setServerIP(String clientIP) {
        EasyFilterClientUI.serverIP = clientIP;
    }

    public static int getServerPort() {
        return serverPort;
    }

    private static void setServerPort(int clientPort) {
        EasyFilterClientUI.serverPort = clientPort;
    }
}