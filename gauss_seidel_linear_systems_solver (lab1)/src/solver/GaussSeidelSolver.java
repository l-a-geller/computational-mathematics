package solver;

import exceptions.MatrixCreateException;

import java.util.Arrays;
import java.util.OptionalDouble;

public class GaussSeidelSolver {

    private static int iterations;
    private static double[] errors;
    public static int getIterations() { return iterations; }
    public static double[] getErrors() { return errors; }

    /**
     * Solves a linear system by Gauss-Seidel method having its coefficients interpreted as a matrix
     * @param matrix - original matrix
     * @param answerMatrix - free element column values
     * @param accuracy
     * @return values of system unknown variables
     * @throws MatrixCreateException if matrix for some reason is not diagonally dominant
     */
    public static double[] solve(double[][] matrix, double[] answerMatrix, double accuracy) throws MatrixCreateException {

        if(is_diagonally_dominant(matrix)){
            double[] x = new double[matrix.length];
            double[] previousX;
            iterations = 0;
            do {
                previousX = Arrays.copyOf(x, x.length);
                for (int i=0; i<matrix.length; i++){
                    double sum = 0;
                    for(int j=0; j<matrix.length; j++) {
                        if (j != i) sum += matrix[i][j] * x[j];
                    }
                    x[i] = (answerMatrix[i] - sum) / matrix[i][i];
                    iterations++;
                }
            }while (!converge(previousX, x, accuracy));
            return x;
        }else {
            throw new MatrixCreateException("Sorry, the matrix is not diagonally dominant");
        }
    }

    /**
     * Checks if matrix is diagonally dominant
     * @param matrix - original matrix
     * @return true if matrix is diagonally_dominant, false if not
     * @throws MatrixCreateException if at least one matrix line consists of zeros
     */
    private static boolean is_diagonally_dominant(double[][] matrix) throws MatrixCreateException {

        boolean atLeastOneStrictlyDominant = false;
        for(int i=0; i<matrix.length; i++){

            int lineDominant = lineValid(matrix[i]);
            if(lineDominant > 0){
                if(lineDominant > 1) atLeastOneStrictlyDominant = true;
                int lineMaxElementIndex = getRowMaxByIndex(matrix[i]);
                if(lineMaxElementIndex > i) {
                    swapRows(matrix, i, lineMaxElementIndex);
                }else if (lineMaxElementIndex < i){
                    int maxIndexOfLeft = getRowMaxByIndex(Arrays.copyOfRange(matrix[i], i, matrix[i].length));
                    if (matrix[i][i + maxIndexOfLeft] == matrix[i][lineMaxElementIndex]) swapRows(matrix, i, maxIndexOfLeft);
                    else return false;
                }
            }else return false;
        }
        return atLeastOneStrictlyDominant;
    }


    /**
     * Checks if a matrix line has a maximal element equal or greater sum of irs elements
     * @param line - a matrix line
     * @return 2 if maximal element > sum of elements
     *         1 if maximal element = sum of elements
     *         0 if maximal element < sum of elements or doesn't exist
     */
    private static int lineValid(double[] line){
        double[] absLine = Arrays.copyOf(line, line.length);
        for (int i=0; i<line.length; i++){ absLine[i] = Math.abs(absLine[i]); }
        OptionalDouble maxLineElementOptional = Arrays.stream(absLine).max();
        if(maxLineElementOptional.isPresent()) {
            double maxLineElement = maxLineElementOptional.getAsDouble();
            double lineSum = Arrays.stream(absLine).sum();
            lineSum -= maxLineElement;
            if (maxLineElement > lineSum) return 2;
            return (maxLineElement >= lineSum) ? 1:0;
        }
        return 0;
    }

    /**
     * Calculates a largest element modulo in a row
     * @param row - row to search for maximum
     * @return largest element's index in a row
     * @throws MatrixCreateException if row consists only of zeros
     */
    private static int getRowMaxByIndex(double[] row) throws MatrixCreateException {
        int index = 0;
        double max = row[0];
        for(int i=1; i<row.length; i++){
            if (Math.abs(row[i]) > Math.abs(max)) {
                max = row[i];
                index = i;
            }
        }
        if (max == 0) throw new MatrixCreateException("Matrix line cannot consist only of zeros");
        return index;
    }

    /**
     * Swaps rows with numbers indexA and indexB of matrix
     */
    private static void swapRows(double[][] matrix, int indexA, int indexB){
        for (int i=0; i<matrix.length; i++){
            double tmp = matrix[i][indexA];
            matrix[i][indexA] = matrix[i][indexB];
            matrix[i][indexB] = tmp;
        }
    }

    /**
     * Checks if the maximal difference between old and new values of each variable is less than given accuracy
     * @param previousX - an array of old values
     * @param x - an array of new values
     * @param epsilon - given accuracy
     * @return true if difference < accuracy
     *         false if not
     */
    private static boolean converge(double[] previousX, double[] x, double epsilon){
        int n = x.length;
        double[] differenceArray = new double[n];
        for(int i=0; i<n; i++){
            differenceArray[i] = x[i] - previousX[i];
        }
        if (Arrays.stream(differenceArray).max().getAsDouble() < epsilon){
            errors = differenceArray;
            return true;
        }
        return false;
    }
}
