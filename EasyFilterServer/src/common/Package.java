/*
 * Proiect PDSD - EasyFilter
 *
 * @author Gherghescu Teo, 343 C1
 * @author Stoean Bogdan, 343 C1
 * @author Marin Alexandru, 343 C1
 *
 */
package common;

import java.io.Serializable;

/**
 * Wrapper for sending and receiving image files
 */
public class Package implements Serializable {
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
     * Constructor
     *
     * @param magicNumber  pgm magic number
     * @param width        width of the image
     * @param height       height of the image
     * @param maxGrayValue max gray value of the pgm
     * @param image[][]    the pgm image values
     */
    public Package (String magicNumber, int width, int height,
        int maxGrayValue, int image[][]
    ) {
        this.setMagicNumber(magicNumber);
        this.setWidth(width);
        this.setHeight(height);
        this.setMaxGrayValue(maxGrayValue);
        this.setImage(image);
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
}
