package math;

import java.util.function.DoubleFunction;

public class NewtonInterpolator {

    private double[] points;
    private double[] values;
    private double h;

    public NewtonInterpolator(double[] points, double[] values, double h) {
        this.points = points;
        this.values = values;
        this.h = h;
    }

    /**
     * Вычисляет значения коэффициентов интерполянта многочленом Ньютона
     * @return итоговую функцию-интерполянт
     */
    public DoubleFunction<Double> interpolate() {
        int n = points.length;
        double[][] finiteDifferences = new double[n][n];
        double[] a = new double[n];
        for (int i=0; i<n; i++) finiteDifferences[0][i] = values[i];
        for (int i=1; i<n; i++)
            for (int j=0; j<n-i; j++)
                finiteDifferences[i][j] = finiteDifferences[i-1][j+1] - finiteDifferences[i-1][j];

        for (int i=0; i<n; i++) a[i] = finiteDifferences[i][0]/(Math.pow(h, i) * factorial(i));

        return  x -> {
            double result = a[0];
            for (int i=1; i<n; i++) {
                double iterResult = a[i];
                for (int j=0; j<i; j++)  iterResult *= (x - points[j]);
                result += iterResult;
            }
            return result;
        };
    }

    /**
     * Вычисляет факториал
     * @param bound - факториал какого числа будет вычислен
     */
    private double factorial(int bound) {
        int j, fact = 1;
        for(j=1; j<=bound; j++)
            fact = fact*j;
        return fact;
    }
}
