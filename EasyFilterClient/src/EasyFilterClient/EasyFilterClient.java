/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyFilterClient;

/**
 *
 * @author TEO
 */
public class EasyFilterClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Read the input file
        EasyImageReader inputFile = new EasyImageReader("images/garfield2.pgm");

        EasyPropertiesReader props = new EasyPropertiesReader("config/config.ini");
        System.out.println("config: " + props.readProperty("important", "p1"));

        // Write the output file
        EasyImageWriter eiw = new EasyImageWriter(
            inputFile.getWidth(), inputFile.getHeight(),
            inputFile.getMaxGrayValue(), inputFile.image, "images/test.pgm"
        );
        eiw.write();
    }
}
