/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyFilterClient;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 *
 * @author TEO
 */
public class EasyFilterClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
        throws UnknownHostException, IOException
    {
        // Read the input file
        EasyImageReader inputFile = new EasyImageReader("images/garfield2.pgm");

        // verificam fisierul de configurare
        EasyPropertiesReader props = new EasyPropertiesReader("config/config.ini");
        System.out.println("config: " + props.readProperty("important", "p1"));
        
        // verificare tirmitere fisier
        String serverIP = new String("127.0.0.1");
        int serverPort = 5001;
        TestSendFile tsf = new TestSendFile(serverIP, serverPort);

//        tsf.sendFile(new Package(
//            inputFile.getWidth(),
//            inputFile.getHeight(),
//            inputFile.getMaxGrayValue(),
//            inputFile.getImage()
//        ));
//        
        tsf.sendFile("muie");
        
//        
//        // Write the output file
//        EasyImageWriter eiw = new EasyImageWriter(
//            inputFile.getWidth(), inputFile.getHeight(),
//            inputFile.getMaxGrayValue(), inputFile.image, "images/test.pgm"
//        );
//        eiw.write();
    }
}
