/*
 * Proiect PDSD - EasyFilter
 *
 * @author Gherghescu Teo, 343 C1
 * @author Stoean Bogdan, 343 C1
 * @author Marin Alexandru, 343 C1
 *
 */
package common;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Provides functionalities for writing an image
 */
public final class EasyImageWriter {
    /**
     * int image magicNumber
     */
    private String magicNumber;

    /**
     * int image width
     */
    private int width;

    /**
     * int image height
     */
    private int height;

    /**
     * int image maxGrayValue
     */
    private int maxGrayValue;

    /**
     * Holds the pgm image values
     */
    private int[][] image;

    /**
     * String file name
     */
    private String fileName;

    /**
     * Constructor
     *
     * @param magicNumber  pgm magic number
     * @param width        width of the image
     * @param height       height of the image
     * @param maxGrayValue max gray value of the pgm
     * @param image[][]    the pgm image values
     * @param fileName     file name
     */
    public EasyImageWriter (String magicNumber, int width, int height,
        int maxGrayValue, int image[][], String fileName
    ) {
        this.setMagicNumber(magicNumber);
        this.setWidth(width);
        this.setHeight(height);
        this.setMaxGrayValue(maxGrayValue);
        this.setImage(image);
        this.setFileName(fileName);
    }

    /**
     * Writes the image into a file
     *
     * @return void
     */
    public void write () {
        try {
            int i,j;
            // Create file
            FileWriter fstream = new FileWriter(this.getFileName());
            BufferedWriter out = new BufferedWriter(fstream);

            // Write image header info
            out.write(this.getMagicNumber() + "\n");
            out.write(this.getWidth() + " " + this.getHeight() + "\n");
            out.write(getMaxGrayValue() + "\n");

            // Write image contents
            for(i = 0; i < this.getHeight(); i++) {
                for(j = 0; j < this.getWidth(); j++) {
                    out.write(this.getImage()[i][j]+"\n");
                }
            }
        } catch (Exception e) { //Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    // ~~~~~~~~ Getters and Setters ~~~~~~~~~~

    public String getMagicNumber() {
        return magicNumber;
    }

    private void setMagicNumber(String magicNumber) {
        this.magicNumber = magicNumber;
    }

    public int getWidth() {
        return width;
    }

    private void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    private void setHeight(int height) {
        this.height = height;
    }

    public int getMaxGrayValue() {
        return maxGrayValue;
    }

    private void setMaxGrayValue(int maxGrayValue) {
        this.maxGrayValue = maxGrayValue;
    }

    public int[][] getImage() {
        return image;
    }

    private void setImage(int[][] image) {
        this.image = image;
    }

    public String getFileName() {
        return fileName;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
