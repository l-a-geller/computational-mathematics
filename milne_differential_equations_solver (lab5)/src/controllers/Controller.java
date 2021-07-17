package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import math.FunctionOf2Args;
import math.MilneSolver;
import math.NewtonInterpolator;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.DoubleFunction;

public class Controller implements Initializable {
    public ComboBox<String> equationChooser;
    public TextArea log;
    public TextField leftPoint;
    public TextField rightPoint;
    public TextField accuracy;
    public TextField y0;
    public LineChart canvas;

    ObservableList<String> equationOptions = FXCollections.observableArrayList(
            "cos(x) - y",
            "x^2",
            "cos(x*y)"
    );
    List<FunctionOf2Args<Double, Double, Double>> functions = Arrays.asList(
            (x, y) -> Math.cos(x) - y,
            (x, y) -> (Math.pow(x, 2)),
            (x, y)-> Math.cos(x*y)
    );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        equationChooser.getItems().addAll(equationOptions);
    }

    public void inputSubmit() {
        FunctionOf2Args<Double, Double, Double> pickedFunction = functions.get(0);
        double left = -3;
        double right = 3;
        double yLeft = 0;
        double eps = 0.1;

        log.clear();
        canvas.getData().clear();

        int equationIndex = equationChooser.getSelectionModel().selectedIndexProperty().get();
        if (equationIndex >= 0 && equationIndex < functions.size()) {
            pickedFunction = functions.get(equationIndex);
        } else {
            log.setText("Вы не выбрали функцию. Установлено значение по умолчанию = " + equationOptions.get(0) + "\n");
        }

        try {
            left = Double.parseDouble(leftPoint.getText());
            if (left < -20 || left > 20) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            left = -3;
            log.appendText("Вы ввели некорректное значение X-координаты левой точки. Установлено значение по умолчанию = -3\n");
        }


        try {
            right = Double.parseDouble(rightPoint.getText());
            if (right < -20 || right > 20 || right <= left) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            right = left + 6;
            log.appendText("Вы ввели некорректное значение X-координаты правой точки. Установлено значение по умолчанию = " + right + "\n");
        }


        try {
            yLeft = Double.parseDouble(y0.getText());
            if (yLeft < -40 || yLeft > 40) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            yLeft = 0;
            log.appendText("Вы ввели некорректное значение y0 (значение в левой точке отрезка). Установлено значение по умолчанию = 0\n");
        }

        try {
            eps = Double.parseDouble(accuracy.getText());
            if (eps <= 0.00000005 || eps > 1) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            eps = 0.1;
            log.appendText("Вы ввели некорректное значение точности. Установлено значение по умолчанию = 0.1\n");
        }

        double h = Math.abs(right-left)/12;
        //h /= 100/(eps*10000);
        h = Math.pow( (eps * 90/28), 0.2 );
        if (Math.abs(right-left)/h < 4) h = Math.abs(right-left)/4;
        else if (Math.abs(right-left)/h > 12) h = Math.abs(right-left)/12;
        int n = Integer.parseInt(String.valueOf(Math.round( Math.abs(right-left)/(h) )));
        MilneSolver ms = new MilneSolver(pickedFunction, left, yLeft, h, eps, n);
        ms.solve();
        double[] x = ms.getX();
        double[] y = ms.getY();

        NewtonInterpolator ni = new NewtonInterpolator(x, y, h);
        DoubleFunction<Double> interpolant = ni.interpolate();

        drawLine(interpolant, left, x[n]);
        for (int i=0; i<x.length; i++) {
            drawPoint(x[i], y[i]);
        }
    }

    /**
     * Рисует точку на координатной плоскости
     * @param x - x-координата
     * @param y - y-координата
     */
    private void drawPoint(double x, double y) {
        XYChart.Series<Double, Double> s = new XYChart.Series<Double, Double>();
        XYChart.Data<Double, Double> data = new XYChart.Data<Double, Double>(x, y);
        data.setNode(new Circle(2.0, Color.GREEN));
        s.getData().add(data);
        canvas.getData().add(s);
    }

    /**
     * Рисует график (линию)
     * @param func - функция, график которой рисуем
     * @param left - левая граница области отрисовки
     * @param right - правая граница области отрисовки
     */
    private void drawLine(DoubleFunction<Double> func, Double left, Double right) {
        XYChart.Series<Double, Double> series = new XYChart.Series<Double, Double>();
        double a = left;
        double h = (right - left) / 1000;
        for (double x = a; x <= right; x = x + h) {
            XYChart.Data<Double, Double> data = new XYChart.Data<Double, Double>(x, func.apply(x));
            series.getData().add(data);
        }
        XYChart.Data<Double, Double> data = new XYChart.Data<Double, Double>(right, func.apply(right));
        series.getData().add(data);
        canvas.setCreateSymbols(false);
        canvas.getData().add(series);
    }
}
