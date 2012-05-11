/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyFilterServer;

/**
 *
 * @author alex
 */
public class EasyFilterServer {
    public static void main(String[] args) {
        EasyPropertiesReader props = new EasyPropertiesReader("config/config.ini");
        System.out.println("config: " + props.readProperty("important", "abcd"));
    }
}
