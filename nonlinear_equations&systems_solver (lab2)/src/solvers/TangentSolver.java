package solvers;

import data.Equation;

import java.util.function.DoubleFunction;

/**
 * Решает нелинейное уравнение методом касательных
 */
public class TangentSolver {
    private double a;
    private double b;
    private double epsilon;
    private Equation equation;

    private double x;
    private int iterations;
    private long execTime;

    public double getX(){
        return x;
    }
    public int getIterations(){ return iterations; }
    public double getExecTime(){ return execTime; }

    private static final double DX = 0.0001;

    private static DoubleFunction<Double> derive(DoubleFunction<Double> f) {
        return (x) -> (f.apply(x + DX) - f.apply(x)) / DX;
    }

    public TangentSolver(double a, double b, double epsilon, Equation equation){
        this.a = a;
        this.b = b;
        this.epsilon = epsilon;
        this.equation = equation;
        this.iterations = 0;
    }

    /**
     * Находит x - решение уравнения и замеряет количество итераций и время работы метода
     */
    public void solve(){
        execTime = System.currentTimeMillis();
        x = (a+b)/2;
        DoubleFunction<Double> fDerive = derive(equation.getFunc());
        double FA = equation.getFunc().apply(x);
        double FS = fDerive.apply(x);

        while(FS != 0 && Math.abs(FA) >= epsilon){
            FA = equation.getFunc().apply(x);
            fDerive = derive(equation.getFunc());
            FS = fDerive.apply(x);
            x = x - (FA/FS);
            iterations++;
        }
        execTime = System.currentTimeMillis() - execTime;
    }

}
