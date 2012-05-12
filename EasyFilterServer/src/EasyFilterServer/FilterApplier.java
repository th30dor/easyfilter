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
     * Apply a filter to a given image or part of image
     * The result is stored in the modified img argument.
     *
     * @param width  image width
     * @param height image height
     * @param img    image matrix (pgm format)
     *
     * @return void
     */
    static int[][] applyFilter(int width, int height, int[][] img){
        int i, j, aux;

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

                img[i][j] = aux;
            }
        }

        return img;
    }
}