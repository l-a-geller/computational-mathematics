package math;

@FunctionalInterface
public interface FunctionOf2Args<A, B, R>  {
    R apply(A a, B b);
}
