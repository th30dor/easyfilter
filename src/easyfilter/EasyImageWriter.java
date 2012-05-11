/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package easyfilter;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 *
 * @author TEO
 */
public final class EasyImageWriter {
    
    public EasyImageWriter(int width, int height, int maxGrayValue, int image[][], String fileName) {
         this.write(width, height, maxGrayValue, image, fileName);
    }

    public void write (int width, int height, int maxGrayValue, int image[][], String fileName) {
        try{
            int i,j;
            // Create file 
            FileWriter fstream = new FileWriter(fileName);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("P2\n");
            out.write(width + " " + height + "\n");
            out.write(maxGrayValue + "\n");
            
            for(i = 0; i < height; i++)
                for(j = 0; j < width; j++)
                    out.write(image[i][j]+"\n");
            
            //Close the output stream
            out.close();
        } catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }
    
}
