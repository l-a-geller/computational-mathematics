package matrixCreators;

import exceptions.MatrixCreateException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Scans a matrix from a file
 */
public class FileMatrixLoader {

    private double[][] matrix;
    private double[] answerMatrix;
    private double accuracy;
    public double[][] getMatrix() {
        return matrix;
    }
    public double[] getAnswerMatrix() {
        return answerMatrix;
    }
    public double getAccuracy() { return accuracy; }

    public FileMatrixLoader(String fileName) throws FileNotFoundException, MatrixCreateException {

        Scanner matrixScanner = new Scanner(new File(fileName));
        int size = 0;
        if (matrixScanner.hasNextInt()) {
            size = matrixScanner.nextInt();
            if(size < 1 || size > 20 ) throw new MatrixCreateException("Matrix dimension must be a positive integer <= 20. Check your file again");
            matrix = new double[size][size];
            for (int i=0; i<size; i++){
                for (int j=0; j<size; j++){
                    if (matrixScanner.hasNextDouble()) matrix[i][j] = matrixScanner.nextDouble();
                    else throw new MatrixCreateException("Please provide all " + Math.pow(size, 2.0) + " elements of matrix in file. Please use a comma to enter double values");
                }
            }
            answerMatrix = new double[size];
            for (int i=0; i<matrix.length; i++){
                if (matrixScanner.hasNextDouble()) answerMatrix[i] = matrixScanner.nextDouble();
                else throw new MatrixCreateException("Please provide all " + size + " elements of answer matrix in file");
            }
            try{
                accuracy = matrixScanner.nextDouble();
                if( accuracy <= 0 || accuracy >= 1 ) throw new MatrixCreateException("Accuracy must be a positive double < 1. Check your file again");
            }catch (InputMismatchException e){
                throw new MatrixCreateException("Accuracy must be a double");
            }

        }else {
            throw new MatrixCreateException("Matrix dimension must be an integer. Check your file again");
        }
    }

}
