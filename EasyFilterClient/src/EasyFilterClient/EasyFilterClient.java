/*
 * Proiect PDSD - EasyFilter
 *
 * @author Gherghescu Teo, 343 C1
 * @author Stoean Bogdan, 343 C1
 * @author Marin Alexandru, 343 C1
 *
 */
package EasyFilterClient;

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
        String fileName = "images/garfield2.pgm";
        String configFilePath = "config/config.ini";
        common.Package pkg = null;

//        // get server IP and port from the config file
//        EasyPropertiesReader props = new EasyPropertiesReader(configFilePath);
//        String serverIP = props.readProperty("Address", "IP");
//        int serverPort = Integer.parseInt(props.readProperty("Address", "port"));

        // simulate graphic interface for blocking TCP
        BlockingTcpConnection btc = new BlockingTcpConnection();
        btc.openConnection();

        // test 3x send-receive + close
//        System.out.println("conn open");
//        for ( int i = 0; i < 3; i ++ ) {
//            // send request to server
//            btc.sendRequest(EasyFilterClient.preparePackage(fileName, 0));
//            System.out.println("request sent");
//            // receive file from server
//            pkg = btc.receiveRequest();
//            System.out.println("request recvd");
//        }
//        // send close request to server
        btc.sendRequest(EasyFilterClient.preparePackage(fileName, 1337));
//        System.out.println("conn closed");

//         send the package to a server
//         todo remove test:
//         send request
//        TestSendFile tsf = new TestSendFile(serverIP, serverPort);
//        tsf.sendFile(EasyFilterClient.preparePackage(fileName, 0));

//        // test: Write the output file
//        EasyImageWriter eiw = new EasyImageWriter(pkg);
//        eiw.write();
    }

    /**
     * Prepares a package before sending it to a server
     *
     * @param filePath    path to the local file to be uploaded
     * @param requestType request type
     *                    0 - get new modified image
     *                    1 - get an image already saved on the server
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
                pkg = new Package(filePath, requestType);
                break;
        }

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
        // unix
        int lastIndex = filePath.lastIndexOf("/");
        if (lastIndex == -1) {
            // windows
            lastIndex = filePath.lastIndexOf("\\");
        }
        return filePath.substring(lastIndex+1);
    }
}