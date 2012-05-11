/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package easyfilter;

/**
 *
 * @author TEO
 */
public class EasyFilter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        EasyImageReader inputFile = new EasyImageReader("D:\\garfield2.pgm");
        EasyImageWriter eiw = new EasyImageWriter(inputFile.getWidth(), inputFile.getHeight(), inputFile.getMaxGrayValue(), inputFile.image, "D:\\test.pgm");
    }
}
