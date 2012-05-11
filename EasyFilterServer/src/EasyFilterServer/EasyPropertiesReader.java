/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyFilterServer;


import java.io.File;
import org.ini4j.*;

/**
 *
 * @author alex
 */
public class EasyPropertiesReader {
    /**
     * Ini file parser
     */
    private Ini ini;

    public EasyPropertiesReader(String fileName) {
        try {
            this.setIni(new Ini(new File(fileName)));
        } catch (Exception e) {
            System.err.println("Exception opening the testConfigFile:" + e);
            e.printStackTrace();
        return;
        }
    }

    /**
     * Reads a property from the ini file
     *
     * @param section String name of the section
     * @param key     String name of the key
     * @return String corresponding value
     */
    public String readProperty(String section, String key) {
        Ini.Section sc = this.getIni().get(section);
        return sc.get(key);
    }

    // ~~~~~~~~ Getters and Setters ~~~~~~~~~~

    public Ini getIni() {
        return ini;
    }

    public void setIni(Ini ini) {
        this.ini = ini;
    }

}
