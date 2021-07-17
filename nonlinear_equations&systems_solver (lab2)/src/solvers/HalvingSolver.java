package solvers;

import data.Equation;
import exceptions.EquationException;

/**
 * Решает нелинейное уравнение методом деления пополам
 */
public class HalvingSolver {
    private double a;
    private double b;
    private double epsilon;
    private Equation equation;

    private double x;
    private double yA;
    private double yX;

    private int iterations;
    private long execTime;

    public double getX(){
        return x;
    }
    public int getIterations(){ return iterations; }
    public double getExecTime(){ return execTime; }

    public HalvingSolver(double a, double b, double epsilon, Equation equation){
        this.a = a;
        this.b = b;
        this.epsilon = epsilon;
        this.equation = equation;
    }

    /**
     * Находит x - решение уравнения и замеряет количество итераций и время работы,
     * если значения функции на концах промежутка поиска корней разных знаков
     * @throws EquationException в противном случае
     * также проверяет, являются ли решениями уравнения концы промежутка поиска корней
     */
    public void solve() throws EquationException {
        execTime = System.nanoTime();
        yA = equation.getFunc().apply(a);
        double endsMultiplication = getEndsMultiplication();
        if (endsMultiplication < 0) {
            iterations = 0;
            do {
                x = (a + b) / 2;
                yX = equation.getFunc().apply(x);
                if (yA * yX > 0) a = x;
                else b = x;
                iterations++;
            } while (Math.abs(yX) > epsilon);
            execTime = (System.nanoTime() - execTime)/1000;
        } else if (endsMultiplication == 0) {
            if (equation.getFunc().apply(a) == 0) x = a;
            else x = b;
        } else throw new EquationException("Значения функции на концах промежутка поиска корней\nдолжны быть разных знаков");
    }

    /**
     * @return Произведение значений функции на концах промежутка поиска корней
     */
    private double getEndsMultiplication(){
        return equation.getFunc().apply(a)*equation.getFunc().apply(b);
    }
}
