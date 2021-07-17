package solvers;

import data.SystemOfEquations;
import exceptions.EquationException;

import java.util.Arrays;

/**
 * Решает систему из 2 нелинейных уравнений с 2 переменными методом простых итераций
 */
public class SimpleIterationsSolver {
    private SystemOfEquations sof;
    private double[] answers;
    private double eps;

    public double[] getAnswers(){
        return answers;
    }

    public SimpleIterationsSolver(SystemOfEquations sof, double eps){
        this.eps = eps;
        this.sof = sof;
        answers = new double[2];
    }

    /**
     * Находит решение системы системы
     */
    public void solve() {
        double[] previousX;
        do {
            previousX = Arrays.copyOf(answers, answers.length);
            answers[0] = sof.getFuncs().get(0).apply(answers[1]);
            answers[1] = sof.getFuncs().get(1).apply(answers[0]);
        } while (!isConverged(previousX, answers));
    }

    private boolean isConverged(double[] previousX, double[] x){
        int n = previousX.length;
        for (int i=0; i<n; i++) {
            if (Math.abs(x[i] - previousX[i]) > eps) return false;
        }
        return true;
    }
}
