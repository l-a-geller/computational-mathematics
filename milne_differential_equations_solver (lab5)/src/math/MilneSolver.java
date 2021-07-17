package math;

public class MilneSolver {

    private FunctionOf2Args<Double, Double, Double> function;
    private double x0;
    private double y0;
    private double h;
    private int n;
    private double eps;

    private double[] x;
    private double[] y;
    private double[] functionValues;

    public double[] getX() { return x; }
    public double[] getY() { return y; }

    /**
     * @param function - интегрируемая функция
     * @param x0 - начальный x
     * @param y0 - значение в начальном x
     * @param h - шаг
     * @param eps - точность
     * @param n - количество точек
     */
    public MilneSolver(FunctionOf2Args<Double, Double, Double> function, double x0, double y0, double h, double eps, int n){
        this.function = function;
        this.x0 = x0;
        this.y0 = y0;
        this.h = h;
        this.n = n;
        this.eps = eps;
        x = new double[n+1];
        y = new double[n+1];
        functionValues = new double[n+1];
    }

    /**
     * Находит y-координаты точек графика решения дифф. уравнения методом Милна
     */
    public void solve(){
        x[0] = x0;
        y[0] = y0;
        functionValues[0] = f(x[0], y[0]);
        getAcceleratingPoints();
        for (int i=3; i<n; i++) {
            double prediction = y[i-3];
            prediction += (4/3.0) * h * 2 * functionValues[i-2];
            prediction -= (4/3.0) * h * functionValues[i-1];
            prediction += (4/3.0) * h * 2 * functionValues[i];
            x[i+1] = x[i] + h;
            double correction = prediction;
            y[i+1] = correction;
            int iterations = 0;
            double a;
            do {
                a = correction;
                functionValues[i+1] = f(x[i+1], a);
                correction = y[i-1];
                correction += (h/3) * functionValues[i-1];
                correction += (h/3) * 4 * functionValues[i];
                correction += (h/3) * functionValues[i+1];
                iterations++;
            } while (Math.abs(correction - a) >= eps && iterations <= 200000);
            y[i+1] = correction;
        }
    }

    /**
     * Получает разгоночные точки методом Рунге-Кутты
     */
    private void getAcceleratingPoints() {
        double k0, k1, k2, k3;
        int i=1;
        double dy;
        for (; i<4; i++) {
            k0 = h * f(x[i-1], y[i-1]);
            k1 = h * f(x[i-1] + h/2, y[i-1] + k0/2);
            k2 = h * f(x[i-1] + h/2, y[i-1] + k1/2);
            k3 = h * f(x[i-1] + h, y[i-1] + k2);
            dy = (k0 + 2*k1 + 2*k2 + k3)/6;
            x[i] = x[i-1] + h;
            y[i] = y[i-1] + dy;
            functionValues[i] = f(x[i], y[i]);
        }
    }

    private double f(double x, double y){
        return function.apply(x, y);
    }
}
