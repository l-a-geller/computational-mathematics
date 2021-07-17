import exceptions.MatrixCreateException;
import matrixCreators.*;
import solver.GaussSeidelSolver;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static double[][] matrix = null;
    private static double[] answerMatrix = null;
    private static double accuracy = 0;

    /**
     * Interacts with the user and launches the solver
     * @param args command line arguments standing for matrix creation mode
     */
    public static void main(String[] args) {

        if (args.length > 0) {
            switch (args[0]) {
                case "-f":
                    if (args.length > 1) loadMatrixFromFile(args[1]);
                    else System.out.println("Filename not provided, type filename after flag -f");
                    break;
                case "-r":
                    if (args.length > 1) createRandomMatrix(args[1]);
                    else System.out.println("Random matrix dimension not provided, type dimension after flag -r");
                    break;
                case "-u":
                    createUserMatrix();
                    break;
                default: printHelp();
            }

            if (matrix != null && answerMatrix != null) {
                if(accuracy == 0) getAccuracy();
                printData(matrix, answerMatrix);
                try{
                    double[] x = GaussSeidelSolver.solve(matrix, answerMatrix, accuracy);
                    double[] errors = GaussSeidelSolver.getErrors();
                    printResult(x, errors);
                } catch (MatrixCreateException e) {
                    System.out.println(e.getMessage());
                }
            }

        }

        else printHelp();

    }

    private static void getAccuracy() {
        Scanner accuracyScanner = new Scanner(System.in);
        System.out.println("Enter accuracy:");
        while (true) {
            try {
                accuracy = Double.parseDouble(accuracyScanner.nextLine().replace(',', '.'));
                if ( accuracy <= 0 || accuracy > 1 ) throw new MatrixCreateException("Incorrect! Accuracy must be a positive double < 1. Check your file again");
                break;
            } catch (MatrixCreateException e){
                System.out.println(e.getMessage());
            } catch (NumberFormatException e){
                System.out.println("Incorrect! Accuracy must be a double");
            }
        }
    }

    private static void loadMatrixFromFile(String filename) {
        FileMatrixLoader fml = null;
        try {
            fml = new FileMatrixLoader(filename);
            matrix = fml.getMatrix();
            answerMatrix = fml.getAnswerMatrix();
            accuracy = fml.getAccuracy();
        } catch (FileNotFoundException e) {
            System.out.println("Cannot found a file with a name specified");
        } catch (MatrixCreateException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void createRandomMatrix(String inputDimension) {
        try {
            RandomMatrixCreator rmc = new RandomMatrixCreator(inputDimension);
            matrix = rmc.createMatrix();
            answerMatrix = rmc.createAnswerMatrix();
        } catch (NumberFormatException e){
            System.out.println("Matrix size must be an Integer");
        } catch (MatrixCreateException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void createUserMatrix() {
        UserInputMatrixCreator uimc = new UserInputMatrixCreator();
        matrix = uimc.getMatrix();
        answerMatrix = uimc.getAnswerMatrix();
    }

    private static void printResult(double[] x, double[] errors) {
        for (int i=0; i<x.length; i++) {
            System.out.println("x" + i + ": " + x[i] + " +- " + errors[i]);
        }
        System.out.println("Solution found in " + GaussSeidelSolver.getIterations() + " iterations");
    }

    private static void printData(double[][] matrix, double[] answerMatrix) {
        System.out.println("Matrix: ");
        for (double[] line : matrix) {
            System.out.println(Arrays.toString(line));
        }
        System.out.println("Answer Matrix: ");
        System.out.println(Arrays.toString(answerMatrix));
    }

    private static void printHelp() {
        System.out.println("\nWrong key, please enter:\n" +
                "-f fileName - to load a matrix from file\n" +
                "-u - to input a matrix manually\n" +
                "-r size - to create a random matrix with size n");
    }
}
