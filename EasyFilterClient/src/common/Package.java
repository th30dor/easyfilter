/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.Serializable;

/**
 *
 * @author alex
 */
public class Package implements Serializable {

    private int width;

    private int height;

    private int maxGrayValue;

    public int[][] image;

    public Package(int width, int height, int maxGrayValue  , int[][] image) {
        this.width = width;
        this.height = height;
        this.maxGrayValue = maxGrayValue;
        this.image = image;
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
}
