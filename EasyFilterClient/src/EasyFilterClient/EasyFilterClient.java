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
public class EasyFilterClient
{
    /**
     * Main client method
     */
    public static void main(String[] args)
        throws UnknownHostException, IOException
    {
        // read the input file
        String fileName = "images/garfield2.pgm";
        EasyImageReader inputFile = new EasyImageReader(fileName);

//        // test: verificam fisierul de configurare
//        EasyPropertiesReader props = new EasyPropertiesReader("config/config.ini");
//        System.out.println("config: " + props.readProperty("important", "p1"));

        // get server IP
        // test: verificare tirmitere fisier
        String serverIP = new String("127.0.0.1");
        int serverPort = 5001;
        TestSendFile tsf = new TestSendFile(serverIP, serverPort);
        
        // send request

        // todo: fileName trebuie parsat: sa ramana doar numele fisierului,
        // fara folderele din fata.
        // Test send file to server
        tsf.sendFile(new Package(
            inputFile.getMagicNumber(),
            inputFile.getWidth(),
            inputFile.getHeight(),
            inputFile.getMaxGrayValue(),
            inputFile.getImage(),
            fileName,
            0
        ));

//        // test: Write the output file
//        EasyImageWriter eiw = new EasyImageWriter(
//            inputFile.getWidth(), inputFile.getHeight(),
//            inputFile.getMaxGrayValue(), inputFile.image, "images/test.pgm"
//        );
//        eiw.write();
    }
}