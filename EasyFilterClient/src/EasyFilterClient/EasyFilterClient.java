/*
 * Proiect PDSD - EasyFilter
 *
 * @author Gherghescu Teo, 343 C1
 * @author Stoean Bogdan, 343 C1
 * @author Marin Alexandru, 343 C1
 *
 */
package EasyFilterClient;

import EasyFilterClient.Communication.BlockingTcpConnection;
import common.EasyPropertiesReader;
import common.EasyImageReader;
import common.EasyImageWriter;
import common.Package;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Main client class
 */
public class EasyFilterClient
{
    /**
     * Main client method
     */
    public static void main(String[] args)
        throws UnknownHostException, IOException
    {
        // todo: de luat din interfata grafica
        String fileName = "images/client_garfield.pgm";
        String configFilePath = "config/config.ini";
        common.Package pkg = null;

//        // get server IP and port from the config file
//        EasyPropertiesReader props = new EasyPropertiesReader(configFilePath);
//        String serverIP = props.readProperty("Address", "IP");
//        int serverPort = Integer.parseInt(props.readProperty("Address", "port"));

        // simulate graphic interface for blocking TCP
        BlockingTcpConnection btc = new BlockingTcpConnection();

        // test 3x send-receive new files + close
            System.out.println("conn open");
            for ( int i = 0; i < 1; i ++ ) {
                // open a new connection for each new request
                btc.openConnection();
                // send request to server
                btc.sendRequest(
                    EasyFilterClient.preparePackage(fileName, 0)
                );
                System.out.println("request sent");
                // receive file from server
                pkg = btc.receiveRequest();
                System.out.println("request received");
            }

//         test send-receive existing files
//        System.out.println("conn open");
//        for ( int i = 1; i <= 3; i ++ ) {
//            fileName = new String("garfield"+i+".pgm");
//
//            // send request to server
//            btc.sendRequest(EasyFilterClient.preparePackage(fileName, 1));
//            System.out.println("request sent");
//
//            // receive file from server
//            pkg = btc.receiveRequest();
//            System.out.print("request type recvd:");
//            System.out.println(pkg.getRequestType());
//        }

//         send the package to a server
//         todo remove test:
//         send request
//        TestSendFile tsf = new TestSendFile(serverIP, serverPort);
//        tsf.sendFile(EasyFilterClient.preparePackage(fileName, 0));

//        // test: Write the output file
            EasyImageWriter eiw = new EasyImageWriter(pkg);
            eiw.write();
    }

    /**
     * Prepares a package before sending it to a server
     *
     * @param filePath    path to the local file to be uploaded
     * @param requestType request type
     *                    0    - get new modified image
     *                    1    - get an image already saved on the server
     *                    -1   - error code ( file not found )
     *                    1337 - announces the server to close the connection
     * @return the package that will be sent to the server
     */
    public static Package preparePackage(String filePath, int requestType)
    {
        // the package that will be sent
        Package pkg = null;

        switch (requestType) {
            default: case 0:
                // read the input file
                EasyImageReader inputFile = new EasyImageReader(filePath);
                // create a package with the read image
                pkg = new Package(
                    inputFile.getMagicNumber(),
                    inputFile.getWidth(),
                    inputFile.getHeight(),
                    inputFile.getMaxGrayValue(),
                    inputFile.getImage(),
                    EasyFilterClient.parseFilePath(filePath),
                    requestType
                );
                break;
            case 1:
                // create a package with only an image name
                pkg = new Package(EasyFilterClient.parseFilePath(filePath), 1);
                break;
            case 1337:
                pkg = new Package(filePath, requestType);
                break;
        }
//        System.out.println("client trimite filename: "
//                + EasyFilterClient.parseFilePath(filePath)
//                + " si request type: " + requestType);
        return pkg;
    }

    /**
     * Gets the file name from a path
     *
     * @param filePath
     *
     * @return file name
     */
    private static String parseFilePath(String filePath)
    {
        if ( filePath == null) {
            return filePath;
        }

        // unix
        int lastIndex = filePath.lastIndexOf("/");
        if (lastIndex == -1) {
            // windows
            lastIndex = filePath.lastIndexOf("\\");
        }
        return filePath.substring(lastIndex+1);
    }
}