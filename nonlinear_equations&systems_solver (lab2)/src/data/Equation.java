package data;

import java.util.function.DoubleFunction;

/**
 * Контейнер для хранения уравнения в виде функции
 */
public class Equation {
    private DoubleFunction<Double> func;

    public DoubleFunction<Double> getFunc() {
        return func;
    }

    public Equation(DoubleFunction<Double> func){
        this.func = func;
    }
}
