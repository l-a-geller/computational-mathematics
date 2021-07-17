import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.function.DoubleFunction;

/**
 * Осуществляет взаимодействие с пользователем (получение:
 *      интегрируемой функции,
 *      нижнего и верхнего пределов интегрирования,
 *      начального числа точек разбиения
 *      и точности
 * )
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double a, b, eps;
        int n = 0;
        DoubleFunction<Double> functionToIntegrate = null;
        int input = 0;
        System.out.println("Выберите функцию, интеграл которой необходимо вычислить методом прямоугольников:");
        System.out.println("1: f(x) = x^2, нет точек разрыва");
        System.out.println("2: f(x) = sin(x)/x, x = 0 - точка разрыва 1 рода");
        System.out.println("3: f(x) = 1/x, x = 0 - точка разрыва 2 рода");
        input = getIntUserChoice(sc, 1, 3, "Пожалуйста введите 1, 2 или 3 - номер нужной функции");

        System.out.println("Введите нижний предел интегрирования:");
        a = getDoubleUserChoice(sc, -1000, 1000, "Пожалуйста введите нижний предел интегрирования: [-1000;1000]");
        System.out.println("Введите верхний предел интегрирования:");
        b = getDoubleUserChoice(sc, -1000, 1000, "Пожалуйста введите верхний предел интегрирования: [-1000;1000]");

        if (input == 1) {
            functionToIntegrate = (x) -> Math.pow(x, 2) + 4*x + 4;
        } else if (input == 2) {
            functionToIntegrate = (x) -> (Math.sin(x)/x);
        } else if (input == 3) {
            if (a <= 0 && b >= 0) System.out.println("В отрезок интегрирования входит точка разрыва 2 порядка");
            else functionToIntegrate = (x) -> 1 / x;
        }

        if (functionToIntegrate != null) {
            System.out.println("Введите начальное количество узлов разбиения:");
            n = getIntUserChoice(sc, 1, 100, "Пожалуйста введите количество узлов: [1;100]");
            System.out.println("Введите точность:");
            eps = getDoubleUserChoice(sc, 0.0001, 1, "Пожалуйста введите точность - [0,0001;1]");
            RectangleMethodIntegrator rmi = new RectangleMethodIntegrator();
            double result = rmi.integrate(functionToIntegrate, n, a, b, eps, 0);
            if (!Double.isNaN(result)) {
                System.out.println("Определенный левый интеграл функции = " + result);
                System.out.println("Значение найдено на разбиении n = " + rmi.getN());
                System.out.println("Погрешность = " + rmi.getDifference());
            } else {
                System.out.println("Не удалось посчитать левый интеграл функции");
            }
            result = rmi.integrate(functionToIntegrate, n, a, b, eps, 1);
            if (!Double.isNaN(result)) {
                System.out.println("Определенный средний интеграл функции = " + result);
                System.out.println("Значение найдено на разбиении n = " + rmi.getN());
                System.out.println("Погрешность = " + rmi.getDifference());
            } else {
                System.out.println("Не удалось посчитать средний интеграл функции");
            }
            result = rmi.integrate(functionToIntegrate, n, a, b, eps, 2);
            if (!Double.isNaN(result)) {
                System.out.println("Определенный правый интеграл функции = " + result);
                System.out.println("Значение найдено на разбиении n = " + rmi.getN());
                System.out.println("Погрешность = " + rmi.getDifference());
            } else {
                System.out.println("Не удалось посчитать правый интеграл функции");
            }
        }
    }

    /**
     * Получить от пользователя int число
     * @param leftBorder - нижняя граница доступного промежутка чисел
     * @param rightBorder - верхняя граница доступного промежутка чисел
     * @param errorMessage - сообщение для вывода при некорректном вводе
     * @return int полученнное число
     */
    private static int getIntUserChoice(Scanner sc, int leftBorder, int rightBorder, String errorMessage){
        int input;
        while(true){
            try{
                input = sc.nextInt();
                if (input < leftBorder || input > rightBorder) throw new InputMismatchException();
                break;
            }catch(InputMismatchException e){
                System.out.println(errorMessage);
                sc.nextLine();
            }
        }
        return input;
    }

    /**
     * Получить от пользователя double число
     * @param leftBorder - нижняя граница доступного промежутка чисел
     * @param rightBorder - верхняя граница доступного промежутка чисел
     * @param errorMessage - сообщение для вывода при некорректном вводе
     * @return double введеное число
     */
    private static double getDoubleUserChoice(Scanner sc, double leftBorder, double rightBorder, String errorMessage){
        double input;
        while(true){
            try{
                input = sc.nextDouble();
                if (input < leftBorder || input > rightBorder) throw new InputMismatchException();
                break;
            }catch(InputMismatchException e){
                System.out.println(errorMessage);
                sc.nextLine();
            }
        }
        return input;
    }
}
