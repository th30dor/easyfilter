/*
 * Proiect PDSD - EasyFilter
 *
 * @author Gherghescu Teo, 343 C1
 * @author Stoean Bogdan, 343 C1
 * @author Marin Alexandru, 343 C1
 *
 */
package common;

import java.io.File;
import org.ini4j.*;

/**
 * Reads properties from the configuration file ( "config/config.ini" )
 */
public class EasyPropertiesReader
{
    /**
     * Ini file parser
     */
    private Ini ini;

    /**
     * Constructor
     *
     * @param fileName
     */
    public EasyPropertiesReader(String fileName)
    {
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
     * @param section name of the section
     * @param key     name of the key
     *
     * @return String corresponding value
     */
    public String readProperty(String section, String key)
    {
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