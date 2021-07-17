package data;

import java.util.ArrayList;
import java.util.function.DoubleFunction;

/**
 * Контейнер для хранения системы уравнений в виде набора функции
 */
public class SystemOfEquations {

    private int dimensions;
    public int getDimensions(){return dimensions;}

    private ArrayList<DoubleFunction<Double>> funcs = new ArrayList();

    public ArrayList<DoubleFunction<Double>> getFuncs(){ return funcs; }

    public SystemOfEquations(int dim, DoubleFunction<Double> f1, DoubleFunction<Double> f2){
        this.dimensions = dim;
        funcs.add(f1);
        funcs.add(f2);
    }
}
