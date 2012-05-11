/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package easyfilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Provides functionalities for reading an images
 */
public class EasyImageReader {
    /**
     * 
     */
    public int[][] image;
    
    private int width;
    
    private int height;
    
    private int maxGrayValue;

    /**
     * 
     * @param fileName 
     */
    public EasyImageReader (String fileName) {
        this.getFileContents(fileName);
    }

    public void getFileContents (String fileName) {
        Scanner sc2 = null;
        int wordCount = 0;
        
        try {
            sc2 = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();  
        }
        /**
         * Read image header and contents using the Scanner class
         * Save the image in the image[][] matrix
         */
        int i = 0,j = 0;
        while (sc2.hasNextLine()) {
            Scanner s2 = new Scanner(sc2.nextLine());
            int position, lineWidth;
            while (s2.hasNext()) {
                String s = s2.next();
                // Ignore comments
                if (s.startsWith("#")) {
                    break;
                }

                switch(wordCount) {
                case 0:
                    break;
                case 1:
                    this.setWidth(Integer.parseInt(s));
                    System.out.println("wi"+this.getWidth());
                    break;
                case 2:
                    this.setHeight(Integer.parseInt(s));
                    this.image = new int[this.getHeight()][this.getWidth()];
                    
                    System.out.println("h"+this.getHeight());
                    break;
                case 3:
                    this.setMaxGrayValue(Integer.parseInt(s));
                    
                    System.out.println("m"+this.getMaxGrayValue());
                    break;
                default:
                    position = wordCount - 3;
                    lineWidth = this.getWidth();
                    //System.out.println("linewei: "+(position)/lineWidth +"|"+ (position - 1)%lineWidth);
                    //this.image[(position)/lineWidth][(position - 1) % lineWidth] = Integer.parseInt(s);
                    this.image[i][j] = Integer.parseInt(s);
                     j++;

                    if(j > this.getWidth() - 1){
                        j = 0;
                        i++;
                    }
                    if(i > this.getHeight() - 1)
                        i = 0;
                    break;
                }
                
                wordCount ++;
            }
        }
    }
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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
}
