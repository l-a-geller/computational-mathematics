import java.util.function.DoubleFunction;

public class RectangleMethodIntegrator {

    private int n;
    public int getN() { return n; }
    private double difference;
    public double getDifference() { return difference; }

    /**
     * Вычисляет определенный интеграл методом прямоугольников
     * с использованием оценки Рунге для оценки понрешностей
     * @param function - интегрируемая функция
     * @param n - начальное количество точек разбиения
     * @param a - левый предел интегрирования
     * @param b - правый предел интегрирования
     * @param eps - точность
     * @param mode: 0 - используется метод левых прямоугольников
     *              1 - средних
     *              2 - правых
     * @return значение определенного интеграла
     */
    public double integrate(DoubleFunction<Double> function, int n, double a, double b, double eps, int mode){
        double sum = 0, prevSum = 0, a0 = a;
        double delta = 0;
        do {
            prevSum = sum;
            sum = 0;
            a = a0;
            delta = (b - a0)/n;
            if (mode == 1) a += (delta / 2); // middle
            if (mode == 2) a += delta; // right
            for (int i=0; i<n; i++){
                sum += (delta * function.apply(a));
                a += delta;
            }
            n *= 2;
            difference = Math.abs( (prevSum - sum) / (Math.pow(2, 2) - 1));
        } while (difference > eps);
        this.n = n / 2;
        return sum;
    }
}
