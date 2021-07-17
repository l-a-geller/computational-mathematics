package matrixCreators;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Creates a matrix from user input
 */
public class UserInputMatrixCreator {

    private double[][] matrix;
    private double[] answerMatrix;

    public double[][] getMatrix() {
        return matrix;
    }
    public double[] getAnswerMatrix() {
        return answerMatrix;
    }

    public UserInputMatrixCreator(){
        Scanner matrixScanner = new Scanner(System.in);
        int n = getSize(matrixScanner);
        matrix = createMatrix(matrixScanner, n);
        answerMatrix = createAnswerMatrix(matrixScanner, n);
    }

    private int getSize(Scanner matrixScanner){
        System.out.println("Enter matrix size:");
        int n = 0;
        while(true){
            try{
                n = matrixScanner.nextInt();
                if(n > 20 || n < 1) throw new InputMismatchException();
                matrixScanner.nextLine();
                break;
            }catch (InputMismatchException e){
                System.out.println("Incorrect! Enter a positive integer <= 20");
                matrixScanner.nextLine();
            }
        }
        return n;
    }

    private double[][] createMatrix(Scanner matrixScanner, int n){
        double[][] matrix = new double[n][n];
        for(int i=0; i<n; i++){
            System.out.println("Enter " + n + " space separated elements of matrix - line " + i);
            fillLine(matrix[i], matrixScanner, n);
        }
        return matrix;
    }

    private double[] createAnswerMatrix(Scanner matrixScanner, int n){
        double[] answerMatrix = new double[n];
        System.out.println("Enter " + n + " space separated elements of answer matrix");
        fillLine(answerMatrix, matrixScanner, n);
        return answerMatrix;
    }

    private void fillLine(double[] matrixLine, Scanner matrixScanner, int n){
        while (true){
            try{
                String[] newLineElements = matrixScanner.nextLine().split(" ");
                for(int j=0; j<n; j++){
                    double element = Double.parseDouble(newLineElements[j].replace(',', '.'));
                    matrixLine[j] = element;
                }
                break;
            }catch (NumberFormatException | ArrayIndexOutOfBoundsException e){
                System.out.println("Incorrect! Enter " + n + " space separated numbers");
            }
        }
    }

}
