/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyFilterServer;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 *
 * @author alex
 */
public class EasyFilterServer {
    public static void main(String[] args)
        throws UnknownHostException, IOException
    {
        // verificam proprietati din config.ini
        EasyPropertiesReader props = new EasyPropertiesReader("config/config.ini");
        System.out.println("config: " + props.readProperty("important", "abcd"));
        
        // asteptam sa primim un fisier
        TestReceiveFile trf = new TestReceiveFile(5001);
        trf.listen();
    }
}
