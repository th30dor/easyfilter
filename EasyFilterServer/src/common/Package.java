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
public class Package implements Serializable
{
    /**
     * image magicNumber
     */
    private String magicNumber;

    /**
     * image width
     */
    private int width;

    /**
     * image height
     */
    private int height;

    /**
     * image maxGrayValue
     */
    private int maxGrayValue;

    /**
     * Holds the pgm image values
     */
    private int[][] image;

    /**
     * file name
     */
    private String fileName;

    /**
     * request type
     * 0    - get new modified image
     * 1    - get an image already saved on the server
     * -1   - error code ( file not found )
     */
    private int requestType;

    /**
     * Constructor
     *
     * @param magicNumber  pgm magic number
     * @param width        width of the image
     * @param height       height of the image
     * @param maxGrayValue max gray value of the pgm
     * @param image[][]    the pgm image values
     * @param fileName     file name
     * @param requestType  request type
     *                     0    - get new modified image
     *                     1    - get an image already saved on the server
     *                     -1   - error code ( file not found )
     */
    public Package (String magicNumber, int width, int height,
        int maxGrayValue, int image[][], String fileName, int requestType
    ) {
        this.setMagicNumber(magicNumber);
        this.setWidth(width);
        this.setHeight(height);
        this.setMaxGrayValue(maxGrayValue);
        this.setImage(image);
        this.setFileName(fileName);
        this.setRequestType(requestType);
    }
    
    /**
     * Constructor for EasyImageReader
     *
     * @param EasyImageReader read file data
     * @param fileName        file name
     * @param requestType     request type
     *                        0    - get new modified image
     *                        1    - get an image already saved on the server
     *                        -1   - error code ( file not found )
     */
    public Package (EasyImageReader eir, String fileName, int requestType)
    {
        this.setMagicNumber(eir.getMagicNumber());
        this.setWidth(eir.getWidth());
        this.setHeight(eir.getHeight());
        this.setMaxGrayValue(eir.getMaxGrayValue());
        this.setImage(eir.getImage());
        this.setFileName(fileName);
        this.setRequestType(requestType);
    }

    /**
     * Constructor for an empty request
     *
     * @param fileName    file name
     * @param requestType request type
     *                    0    - get new modified image
     *                    1    - get an image already saved on the server
     *                    -1   - error code ( file not found )
     */
    public Package (String fileName, int requestType)
    {
        this.setMagicNumber(null);
        this.setWidth(0);
        this.setHeight(0);
        this.setMaxGrayValue(0);
        this.setImage(null);
        this.setFileName(fileName);
        this.setRequestType(requestType);
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

    public void setImage(int[][] image) {
        this.image = image;
    }

    public String getFileName() {
        return fileName;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getRequestType() {
        return requestType;
    }

    private void setRequestType(int requestType) {
        this.requestType = requestType;
    }
}