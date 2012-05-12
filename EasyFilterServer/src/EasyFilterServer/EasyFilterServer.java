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

/**
 * Main server class
 */
public class EasyFilterServer
{
    public static void main(String[] args)
        throws UnknownHostException, IOException
    {
        // verificam proprietati din config.ini
//        EasyPropertiesReader props = new EasyPropertiesReader("config/config.ini");
//        System.out.println("config: " + props.readProperty("important", "abcd"));

        OneThreadPerSocket otps = new OneThreadPerSocket(new BlockingTcpConnection());
        otps.acceptConnections();
        // asteptam sa primim un fisier
//        TestReceiveFile trf = new TestReceiveFile(5001);
//        trf.listen();
    }
}