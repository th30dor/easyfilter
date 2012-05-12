/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyFilterServer;

/**
 *
 * @author TEO
 */
public class FilterApplier {
    
    static  int[][] filter;
    static  int factor;
    static  int bias;
    
    static void setFilter(int[][] my_filter,int my_factor,int my_bias){
        int i,j;
        filter  = new int[3][3]; 
        for(i = 0; i < 3; i++)
            for(j = 0; j < 3; j++)
                FilterApplier.filter[i][j] = my_filter[i][j];
        
        FilterApplier.factor = my_factor;
        FilterApplier.bias = my_bias;
                
        
    }
    
    static void applyFilter(int width, int height, int[][] img){
        int i,j,k;
        int aux;
        System.out.println("height " + height);
        System.out.println("width " + width);
        for(i = 1; i < height - 1; i++ )
            for(j = 1; j < width - 1; j++){
                aux =   FilterApplier.filter[0][0] * img[i-1][j-1] +
                        FilterApplier.filter[0][1] * img[i-1][j]   +
                        FilterApplier.filter[0][2] * img[i-1][j+1] +
                        FilterApplier.filter[1][0] * img[i][j-1]   +
                        FilterApplier.filter[1][1] * img[i]  [j]   +
                        FilterApplier.filter[1][2] * img[i]  [j+1] +
                        FilterApplier.filter[2][0] * img[i+1][j-1] +
                        FilterApplier.filter[2][1] * img[i+1][j]   +
                        FilterApplier.filter[2][2] * img[i+1][j+1];
                
                aux = aux / FilterApplier.factor;
                aux = aux + FilterApplier.bias;
                
                img[i][j] = aux;
                        
            }
                
    }
    
}
