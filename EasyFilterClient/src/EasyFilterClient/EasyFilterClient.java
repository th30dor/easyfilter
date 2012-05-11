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
import common.Package;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Main client class
 */
public class EasyFilterClient {

    /**
     * Main method
     */
    public static void main(String[] args)
        throws UnknownHostException, IOException
    {
        // Read the input file
        EasyImageReader inputFile = new EasyImageReader("images/garfield2.pgm");

        // test: verificam fisierul de configurare
        EasyPropertiesReader props = new EasyPropertiesReader("config/config.ini");
        System.out.println("config: " + props.readProperty("important", "p1"));

        // test: verificare tirmitere fisier
        String serverIP = new String("127.0.0.1");
        int serverPort = 5001;
        TestSendFile tsf = new TestSendFile(serverIP, serverPort);

        // Test send file to server
        tsf.sendFile(new Package(
            inputFile.getMagicNumber(),
            inputFile.getWidth(),
            inputFile.getHeight(),
            inputFile.getMaxGrayValue(),
            inputFile.getImage()
        ));

//        // test: Write the output file
//        EasyImageWriter eiw = new EasyImageWriter(
//            inputFile.getWidth(), inputFile.getHeight(),
//            inputFile.getMaxGrayValue(), inputFile.image, "images/test.pgm"
//        );
//        eiw.write();
    }
}