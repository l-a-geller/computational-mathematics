package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import math.NewtonInterpolator;
import data.Point;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.DoubleFunction;

public class Controller implements Initializable {
    public ComboBox<String> functionChooser;
    public TextField leftPoint;
    public TextField rightPoint;
    public TextField pointsCount;
    public TextArea log;
    public LineChart<Double, Double> canvas;
    public TableView pointsTable;
    public Button calcY;
    public TextField inputX;
    public TextField outputY;
    public TextArea calcComment;

    ObservableList<String> functionOptions = FXCollections.observableArrayList(
            "sin(x)",
            "1/(1+x^2)",
            "x^2"
    );

    List<DoubleFunction<Double>> functions = Arrays.asList(Math::sin,
            x -> 1/(1+Math.pow(x, 2)),
            x -> Math.pow(x, 2));
    List<Double> points = new ArrayList<Double>();
    List<Double> values = new ArrayList<Double>();

    DoubleFunction<Double> interpolant = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        functionChooser.setItems(functionOptions);
    }

    public void inputSubmit() {
        DoubleFunction<Double> pickedFunction = functions.get(0);
        double left = -5;
        double right = 5;
        int count = 1;

        log.clear();
        pointsTable.getItems().clear();
        pointsTable.getColumns().clear();
        clear();
        values.clear();
        points.clear();

        int functionIndex = functionChooser.getSelectionModel().selectedIndexProperty().get();
        if (functionIndex >= 0 && functionIndex < functions.size()) {
            pickedFunction = functions.get(functionIndex);
        } else {
            log.setText("Вы не выбрали функцию. Установлено значение по умолчанию = sin(x)\n");
        }

        try {
            left = Double.parseDouble(leftPoint.getText());
            if (left < -100 || left > 100) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            left = -5;
            log.appendText("Вы ввели некорректное значение X -координаты левой точки. Установлено значение по умолчанию = -5\n");
        }


        try {
            right = Double.parseDouble(rightPoint.getText());
            if (right < -100 || right > 100 || right <= left) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            right = left + 10;
            log.appendText("Вы ввели некорректное значение X-координаты правой точки. Установлено значение по умолчанию = " + right + "\n");
        }


        try {
            count = Integer.parseInt(pointsCount.getText());
            if (count < 2 || count > 100) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            count = 5;
            log.appendText("Вы ввели некорректное значение количества точек. Установлено значение по умолчанию = 5\n");
        }

        fillTable(pickedFunction, left, right, count);
        drawLine(pickedFunction, left, right, count, Color.RED);
        NewtonInterpolator ni = new NewtonInterpolator(points, values, (right-left)/(count-1));
        interpolant = ni.interpolate();
        drawLine(interpolant, left, right, count, Color.ORANGE);
        log.appendText("\nИнтерполянт найден\n");
        log.appendText("Красный график - исходная функция\n");
        log.appendText("Оранжевый график - интерполянт\n");

    }

    /**
     * Рисует график (линию) и точки (узлы) на ней
     * @param func - функция, график которой рисуем
     * @param left - левая граница области отрисовки
     * @param right - правая граница области отрисовки
     * @param count - количество узлов
     * @param color - цвет графика
     */
    private void drawLine(DoubleFunction<Double> func, Double left, Double right, int count, Color color) {
        XYChart.Series<Double, Double> series = new XYChart.Series<Double, Double>();
        double a = left;
        double b = right;

        for (double x = a; x <= b; x = x + 0.01) {
            XYChart.Data<Double, Double> data = new XYChart.Data<Double, Double>(x, func.apply(x));
            data.setNode(new Circle(2.0, color));
            series.getData().add(data);
        }
        canvas.setCreateSymbols(false);
        canvas.getData().add(series);

        double l = left;
        while (l <= right) {
            XYChart.Series<Double, Double> s = new XYChart.Series<Double, Double>();
            XYChart.Data<Double, Double> data = new XYChart.Data<Double, Double>(l, func.apply(l));
            data.setNode(new Circle(3.0, Color.GREEN));
            s.getData().add(data);
            canvas.getData().add(s);
            l += (right - left) / (count-1);
            l = Math.round(l * 10000.0) / 10000.0;
        }
    }

    /**
     * Заполняет таблицу исходными точками
     * @param func - исходная функция
     * @param left - левая точка
     * @param right - правая точка
     * @param count - количесво точек
     */
    private void fillTable(DoubleFunction<Double> func, double left, double right, int count){
        TableColumn<Point, String> column1 = new TableColumn<>("X");
        column1.setCellValueFactory(new PropertyValueFactory<>("x"));
        column1.setPrefWidth(150.0);

        TableColumn<Point, String> column2 = new TableColumn<>("Y");
        column2.setCellValueFactory(new PropertyValueFactory<>("y"));
        column2.setPrefWidth(150.0);
        
        pointsTable.getColumns().add(column1);
        pointsTable.getColumns().add(column2);

        double l = left;
        while (l <= right) {
            pointsTable.getItems().add(new Point(l, func.apply(l)));
            points.add(l);
            values.add(func.apply(l));
            l += (right - left) / (count-1);
            l = Math.round(l * 10000.0) / 10000.0;
        }
    }

    /**
     * Очистить область построения (удалить все точки и графики)
     */
    private void clear(){
        this.canvas.getData().clear();
    }

    /**
     * Вычисляет значение функции-интерполянта в точке x, если интерпрлянт построен
     */
    public void calcY(){
        try {
            double x = Double.parseDouble(inputX.getText());
            if( interpolant == null ) {
                calcComment.setText("Сначала постройте интерполянт");
            } else {
                outputY.setText(interpolant.apply(x).toString());
                calcComment.setText("Значение получено");
            }
        } catch (NumberFormatException e) {
            calcComment.setText("Введите число");
        }
    }
}
