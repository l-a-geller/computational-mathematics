package matrixCreators;

import exceptions.MatrixCreateException;

/**
 * Creates a random diagonally dominant matrix with size provided by user
 */
public class RandomMatrixCreator {

    private int size = 0;

    public RandomMatrixCreator(String inputDimension) throws NumberFormatException, MatrixCreateException {
        size = getSize(inputDimension);
    }

    private static final int RANGE = 10;

    private int getSize(String input) throws NumberFormatException, MatrixCreateException {
        int size = Integer.parseInt(input);
        if (size < 1 || size > 20) throw new MatrixCreateException("Matrix dimension must be a positive integer <= 20");
        return Integer.parseInt(input);
    }

    public double[][] createMatrix() {

        double[][] matrix = new double[size][size];
        double lineSum;
        for(int i = 0; i < size; i++){
            lineSum = 0;
            for(int j = 0; j < size; j++){
                if (j != i) {
                    matrix[i][j] = (int)(Math.random()*RANGE);
                    lineSum += matrix[i][j];
                }
            }
            matrix[i][i] = (int)(lineSum + Math.random()*RANGE);
        }
        return matrix;
    }

    public double[] createAnswerMatrix(){
        double[] matrix = new double[size];
        for(int i = 0; i < size; i++){
            matrix[i] = (int)(Math.random()*RANGE);
        }
        return matrix;
    }
}
