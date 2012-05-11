/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyFilterClient;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 *
 * @author TEO
 */
public final class EasyImageWriter {

    int width;
    int height;
    int maxGrayValue;
    int image[][];
    String fileName;

    public EasyImageWriter(int width, int height, int maxGrayValue, int image[][], String fileName) {
         //this.write(width, height, maxGrayValue, image, fileName);
        this.setFileName(fileName);
        this.setHeight(height);
        this.setImage(image);
        this.setWidth(width);
        this.setMaxGrayValue(maxGrayValue);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int[][] getImage() {
        return image;
    }

    public void setImage(int[][] image) {
        this.image = image;
    }

    public int getMaxGrayValue() {
        return maxGrayValue;
    }

    public void setMaxGrayValue(int maxGrayValue) {
        this.maxGrayValue = maxGrayValue;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void write () {
        try {
            int i,j;
            // Create file
            FileWriter fstream = new FileWriter(this.getFileName());
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("P2\n");
            out.write(this.getWidth() + " " + this.getHeight() + "\n");
            out.write(getMaxGrayValue() + "\n");

            for(i = 0; i < this.getHeight(); i++)
                for(j = 0; j < this.getWidth(); j++)
                    out.write(this.getImage()[i][j]+"\n");

        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }
}
