/*
 * Proiect PDSD - EasyFilter
 *
 * @author Gherghescu Teo, 343 C1
 * @author Stoean Bogdan, 343 C1
 * @author Marin Alexandru, 343 C1
 *
 */
package EasyFilterServer;

/**
 * Provides functionalities for applying a filter to a given image or part of
 * image
 */
public class FilterApplier {
    static int[][] filter = {{-1, -1, -1}, {-1, 9, -1}, {-1, -1, -1}};
    static int factor = 9;
    static int bias = 0;

    /**
     * Applies a filter to a given image or part of image
     *
     * @param width  image width
     * @param height image height
     * @param img    image matrix (pgm format)
     *
     * @return void
     */
    static int[][] applyFilter(int width, int height, int[][] img){
        int i, j, aux;
        int[][] modifiedImg = new int[height][width];

        for(i = 1; i < height - 1; i++ ) {
            for(j = 1; j < width - 1; j++) {
                aux = FilterApplier.filter[0][0] * img[i-1][j-1]
                    + FilterApplier.filter[0][1] * img[i-1][j]
                    + FilterApplier.filter[0][2] * img[i-1][j+1]
                    + FilterApplier.filter[1][0] * img[i][j-1]
                    + FilterApplier.filter[1][1] * img[i][j]
                    + FilterApplier.filter[1][2] * img[i][j+1]
                    + FilterApplier.filter[2][0] * img[i+1][j-1]
                    + FilterApplier.filter[2][1] * img[i+1][j]
                    + FilterApplier.filter[2][2] * img[i+1][j+1];

                aux = aux / FilterApplier.factor;
                aux = aux + FilterApplier.bias;

                if ( aux > 255 ) {
                    aux = 255;
                } else if ( aux < 0 ) {
                    aux = 0;
                }
                modifiedImg[i][j] = aux;
            }
        }

        return modifiedImg;
    }
}