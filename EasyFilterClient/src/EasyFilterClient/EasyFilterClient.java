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
        // TODO code application logic here
        EasyImageReader inputFile = new EasyImageReader("images/garfield2.pgm");
        EasyImageWriter eiw = new EasyImageWriter(inputFile.getWidth(), inputFile.getHeight(), inputFile.getMaxGrayValue(), inputFile.image, "images/test.pgm");
        eiw.write();
    }
}
