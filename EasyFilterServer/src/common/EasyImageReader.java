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
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Provides functionalities for reading an image
 */
public class EasyImageReader
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
     * Constructor
     *
     * @param fileName name of the file to be read
     */
    public EasyImageReader (String fileName)
    {
        this.getFileContents(fileName);
    }

    /**
     * Reads the contents of a file
     *
     * @param fileName name of the file to be read
     *
     * @return void
     */
    public void getFileContents (String fileName)
    {
        Scanner sc2 = null, s2;
        int wordCount = 0, i = 0, j = 0;
        int position, lineWidth;

        // Open the file for reading
        try {
            sc2 = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Read image header and contents using the Scanner class
        // Save the image in the image[][] matrix
        while (sc2.hasNextLine()) {
            s2 = new Scanner(sc2.nextLine());
            while (s2.hasNext()) {
                String s = s2.next();
                // Ignore comments
                if (s.startsWith("#")) {
                    break;
                }

                switch(wordCount) {
                case 0:
                    this.setMagicNumber(s);
                    break;
                case 1:
                    this.setWidth(Integer.parseInt(s));
                    break;
                case 2:
                    this.setHeight(Integer.parseInt(s));
                    this.image = new int[this.getHeight()][this.getWidth()];
                    break;
                case 3:
                    this.setMaxGrayValue(Integer.parseInt(s));
                    break;
                default:
                    position = wordCount - 3;
                    lineWidth = this.getWidth();
                    this.image[i][j] = Integer.parseInt(s);
                    j ++;

                    if (j > this.getWidth() - 1){
                        j = 0;
                        i ++;
                    }
                    if(i > this.getHeight() - 1) {
                        i = 0;
                    }
                    break;
                }

                wordCount ++;
            }
        }

        //close Scanner
        sc2.close();
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
        return this.image;
    }
}