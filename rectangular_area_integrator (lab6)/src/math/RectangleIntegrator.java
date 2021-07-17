package math;

public class RectangleIntegrator {

    private int xN;
    private int yN;
    private double difference;

    public int getXN() {
        return xN;
    }

    public int getYN() {
        return yN;
    }

    public double getDifference() {
        return difference;
    }

    /**
     * Вычисляет определенный интеграл по прямоугольной области
     * @param function - интегрируемая функция
     * @param xn - количество точек разбиения по x
     * @param yn - количество точек разбиения по y
     * @param xa - начало отрезка интегрирования по x
     * @param xb - конец отрезка интегрирования по x
     * @param ya - начало отрезка интегрирования по y
     * @param yb - конец отрезка интегрирования по y
     * @param eps - точность
     * @return значение определенного интеграла
     */
    public double integrate(FunctionOf2Args<Double, Double, Double> function, int xn, int yn,
                            double xa, double xb, double ya, double yb, double eps){
        double sum = 0;
        double prevSum;
        double hX;
        double hY;
        do {
            hX = (xb - xa) / xn;
            hY = (yb - ya) / yn;
            prevSum = sum;
            sum = 0;
            for (double it = xa; it < xb; it += hX) {
                for (double it2 = ya; it2 < yb; it2 += hY) {
                    sum += function.apply(it + hX / 2, it2 + hY / 2);
                }
            }
            sum *= (hX * hY);
            if (xn >= 100 && yn >= 100) break;
            if (xn < 100) xn *= 2;
            if (yn < 100) yn *= 2;
            difference = Math.abs((prevSum - sum) / (Math.pow(2, 2) - 1));
        } while (difference > eps);
        xN = xn;
        yN = yn;
        return sum;
    }
}
