package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import math.FunctionOf2Args;
import math.RectangleIntegrator;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public ComboBox equationChooser;
    public TextField xLeft;
    public TextField xRight;
    public TextField yLeft;
    public TextField yRight;
    public TextField xCount;
    public TextField yCount;
    public TextField accuracy;
    public TextArea log;
    public TextArea answer;

    ObservableList<String> equationOptions = FXCollections.observableArrayList(
            "xy",
            "sin(x) * y",
            "1/x * y^2",
            "x * sin(y) / y"
    );

    List<FunctionOf2Args<Double, Double, Double>> functions = Arrays.asList(
            ( (x,y) -> (x*y) ),
            ( (x,y) -> (Math.sin(x)*y) ),
            ( (x,y) -> (1/x * Math.pow(y, 2) )),
            ( (x,y) -> (x * Math.sin(y)/y ))
    );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        equationChooser.getItems().addAll(equationOptions);
    }

    public void inputSubmit() {

        FunctionOf2Args<Double, Double, Double> pickedFunction = functions.get(0);
        double xL = -3;
        double xR = 3;
        double yL = -3;
        double yR = 3;
        int xN = 5;
        int yN = 5;
        double eps = 0.1;

        log.clear();
        answer.clear();

        int equationIndex = equationChooser.getSelectionModel().selectedIndexProperty().get();
        if (equationIndex >= 0 && equationIndex < functions.size()) {
            pickedFunction = functions.get(equationIndex);
        } else {
            log.setText("Вы не выбрали функцию. Установлено значение по умолчанию = " + equationOptions.get(0) + "\n");
        }

        try {
            xL = Double.parseDouble(xLeft.getText());
            if (xL < -20 || xL > 20) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            xL = -3;
            log.appendText("Вы ввели некорректное значение X-координаты левой точки. Установлено значение по умолчанию = -3\n");
        }
        try {
            xR = Double.parseDouble(xRight.getText());
            if (xR < -20 || xR > 20 || xR <= xL) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            xR = xL + 6;
            log.appendText("Вы ввели некорректное значение X-координаты правой точки. Установлено значение по умолчанию = " + xR + "\n");
        }

        try {
            yL = Double.parseDouble(yLeft.getText());
            if (yL < -20 || yL > 20) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            yL = -3;
            log.appendText("Вы ввели некорректное значение Y-координаты левой точки. Установлено значение по умолчанию = -3\n");
        }
        try {
            yR = Double.parseDouble(yRight.getText());
            if (yR < -20 || yR > 20 || yR <= yL) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            yR = yL + 6;
            log.appendText("Вы ввели некорректное значение X-координаты правой точки. Установлено значение по умолчанию = " + yR + "\n");
        }

        try {
            xN = Integer.parseInt(xCount.getText());
            if (xN < 2 || xN > 20) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            xN = 5;
            log.appendText("Вы ввели некорректное количество точек по X. Установлено значение по умолчанию = 5\n");
        }

        try {
            yN = Integer.parseInt(yCount.getText());
            if (yN < 2 || yN > 20) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            yN = 5;
            log.appendText("Вы ввели некорректное количество точек по Y. Установлено значение по умолчанию = 5\n");
        }

        try {
            eps = Double.parseDouble(accuracy.getText());
            if (eps <= 0.00000005 || eps > 1) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            eps = 0.1;
            log.appendText("Вы ввели некорректное значение точности. Установлено значение по умолчанию = 0.1\n");
        }

        if (equationIndex == 2 && xL <= 0 && xR >= 0) {
            answer.setText("В отрезок интегрирования входит точка разрыва 2 порядка (0), \nвыберите другой отрезок интегриовния \n");
        } else if (equationIndex == 3 && yL <= 0 && yR >= 0) {
            RectangleIntegrator integrator = new RectangleIntegrator();
            double res1 = integrator.integrate(pickedFunction, xN, yN, xL, xR, yL,-0.0000001, eps);
            int xCount1 = integrator.getXN();
            int yCount1 = integrator.getYN();
            double diff1 = integrator.getDifference();
            double res2 = integrator.integrate(pickedFunction, xN, yN, xL, xR,0.0000001, yR,  eps);
            answer.setText("Значение двойного интеграла получено: " + (res1 + res2) + "\n");
            answer.appendText("Значение найдено на разбиении n = " + (integrator.getXN() + xCount1) + " точек по x\n");
            answer.appendText("Значение найдено на разбиении n = " + (integrator.getYN() + yCount1) + " точек по y\n");
            answer.appendText("Погрешность = " + (integrator.getDifference() + diff1));
        } else {
            RectangleIntegrator integrator = new RectangleIntegrator();
            double res = integrator.integrate(pickedFunction, xN, yN, xL, xR, yL, yR, eps);
            answer.setText("Значение двойного интеграла получено: " + res + "\n");
            answer.appendText("Значение найдено на разбиении n = " + integrator.getXN() + " точек по x\n");
            answer.appendText("Значение найдено на разбиении n = " + integrator.getYN() + " точек по y\n");
            answer.appendText("Погрешность = " + integrator.getDifference());
        }
    }
}
