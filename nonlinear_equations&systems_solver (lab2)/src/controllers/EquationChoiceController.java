package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import data.Equation;
import exceptions.EquationException;
import drawers.PlotDrawer;
import data.SystemOfEquations;
import solvers.HalvingSolver;
import solvers.SimpleIterationsSolver;
import solvers.TangentSolver;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * отвечает зв взаимодействие с GUI
 */
public class EquationChoiceController implements Initializable {
    public Label mainTitle;
    public ComboBox equationChoice;
    public VBox exponential;
    public VBox logarithmic;
    public VBox trigonometric;
    public ComboBox exponentialEquationChoice;
    public ComboBox logarithmicEquationChoice;
    public ComboBox trigonometricEquationChoice;
    public TextField aBorder;
    public TextField bBorder;
    public TextField epsilon;
    public TextArea resultArea;
    public Button submit;
    public Label chooseTitle;
    public ComboBox typeChoice;
    public StackPane system;
    public VBox equation;
    public Button showGraph;
    public ComboBox systemDimensionChoice;
    public VBox system1;
    public VBox system2;
    public VBox system3;
    public HBox aBorderEnter;
    public HBox bBorderEnter;

    Equation equationToSolve;
    SystemOfEquations systemOfEquationsToSolve;

    TangentSolver ts = null;
    HalvingSolver hs = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        typeChoice.getItems().setAll("Уравнение", "Систему уравнений");
        equationChoice.getItems().setAll("Степенное", "Логарифмическое", "Тригонометрическое");

        systemDimensionChoice.getItems().setAll("cos(x-1) + y = 0.5\nx + cos(y) = 3", "sin(x-1) + y = 1.5\nx - sin(y+1) = 1", "tg(x-1) + 3y = 0.5\n4x - cos(y) = 0.6");

        addTextLimiter(aBorder, 16);
        addTextLimiter(bBorder, 16);

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Double eps = null;
                try {
                    eps = Double.parseDouble(epsilon.getText());
                    if (typeChoice.getSelectionModel().isSelected(0)){
                        if (equationToSolve != null) {
                            Double a = null, b = null;
                            try {
                                a = Double.parseDouble(aBorder.getText());
                                if (equationChoice.getSelectionModel().isSelected(1) && a < 0){
                                    throw new EquationException("Левая граница отрезка поиска корней логарифмического уравнения должна быть >= 0");
                                }
                            }catch(NumberFormatException e1) {
                                resultArea.setText("Левая граница отрезка поиска корней должна быть числом");
                            } catch (EquationException equationException) {
                                resultArea.setText(equationException.getMessage());
                            }
                            try {
                                b = Double.parseDouble(bBorder.getText());
                            }catch(NumberFormatException numberFormatException) {
                                resultArea.setText("Правая граница отрезка поиска корней должна быть числом");
                            }

                            if(a != null && b != null){
                                hs = new HalvingSolver(a, b, eps, equationToSolve);
                                ts = new TangentSolver(a, b, eps, equationToSolve);
                                try {
                                    hs.solve();
                                    ts.solve();
                                    showGraph.setVisible(true);
                                    resultArea.setText("Корень, найденный с помощью метода деления пополам:\nx = " + hs.getX() + " +- " + eps + "\nРешение найдено за " + hs.getIterations() + " итераций.\nВремя выполнения = " + hs.getExecTime() + " мкс" );
                                    resultArea.setText(resultArea.getText() + "\nКорень, найденный с помощью метода касательных:\nx = " + ts.getX() + " +- " + eps + "\nРешение найдено за " + ts.getIterations() + " итераций.\nВремя выполнения = " + ts.getExecTime() + " мс" );
                                } catch (EquationException equationException) {
                                    showGraph.setVisible(false);
                                    resultArea.setText(equationException.getMessage());
                                }
                            }
                        }
                    } else {
                        if(systemOfEquationsToSolve != null){
                            SimpleIterationsSolver sis = new SimpleIterationsSolver(systemOfEquationsToSolve, eps);
                            sis.solve();
                            double[] answers = sis.getAnswers();
                            resultArea.clear();
                            resultArea.setText(resultArea.getText() + "x: "  + answers[1] + " +- " + eps + "\n");
                            resultArea.setText(resultArea.getText() + "y: "  + answers[0] + " +- " + eps);
                            showGraph.setVisible(true);
                        }
                    }
                }catch(NumberFormatException numberFormatException) {
                    resultArea.setText("Точность должна быть числом неотрицательным числом <= 1");
                }
            }
        });

        showGraph.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e) {

                PlotDrawer pd = new PlotDrawer();
                if (typeChoice.getSelectionModel().isSelected(0)){
                    pd.addFunction(equationToSolve.getFunc(), true);
                } else {
                    pd.addFunction(systemOfEquationsToSolve.getFuncs().get(0), true);
                    pd.addFunction(systemOfEquationsToSolve.getFuncs().get(1), false);
                }
                pd.plot();

            }
        });

        systemDimensionChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> selected, String oldValue, String newValue) {
                if (oldValue != null){
                    switch (oldValue){
                        case "cos(x-1) + y = 0.5\nx + cos(y) = 3": system1.setVisible(false); break;
                        case "sin(x-1) + y = 1.5\nx - sin(y+1) = 1": system2.setVisible(false);  break;
                        case "tg(x-1) + 3y = 0.5\n4x - cos(y) = 0.6": system3.setVisible(false);  break;
                    }
                }
                if (newValue != null){
                    switch (newValue){
                        case "cos(x-1) + y = 0.5\nx + cos(y) = 3": systemOfEquationsToSolve = new SystemOfEquations(2, (y) -> (3 - Math.cos(y)), (x) -> (0.5 - Math.cos(x-1))); system1.setVisible(true); break;
                        case "sin(x-1) + y = 1.5\nx - sin(y+1) = 1": systemOfEquationsToSolve = new SystemOfEquations(2, (y) -> (1 + Math.sin(y+1)), (x) -> (1.5 - Math.sin(x-1))); system2.setVisible(true);  break;
                        case "tg(x-1) + 3y = 0.5\n4x - cos(y) = 0.6": systemOfEquationsToSolve = new SystemOfEquations(2, (y) -> (0.6 + Math.cos(y))/4, (x) -> (0.5 - Math.tan(x-1))/3); system3.setVisible(true);  break;
                    }
                }
            }
        });

        typeChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> selected, String oldValue, String newValue) {
                if (oldValue != null) {
                    switch(oldValue) {
                        case "Уравнение": equation.setVisible(false); aBorderEnter.setVisible(false); bBorderEnter.setVisible(false); break;
                        case "Систему уравнений": system.setVisible(false); break;
                    }
                }
                if (newValue != null) {
                    switch (newValue) {
                        case "Уравнение": equation.setVisible(true); aBorderEnter.setVisible(true); bBorderEnter.setVisible(true); break;
                        case "Систему уравнений": system.setVisible(true); break;
                    }
                }
            }
        });

        equationChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> selected, String oldValue, String newValue) {
                if (oldValue != null) {
                    switch(oldValue) {
                        case "Степенное": exponential.setVisible(false); break;
                        case "Логарифмическое": logarithmic.setVisible(false); break;
                        case "Тригонометрическое": trigonometric.setVisible(false); break;
                    }
                }
                if (newValue != null) {
                    switch(newValue) {
                        case "Степенное":
                            exponential.setVisible(true);
                            if (exponentialEquationChoice.getItems().isEmpty()) { initializeExponential(); }
                            break;
                        case "Логарифмическое":
                            logarithmic.setVisible(true);
                            if (logarithmicEquationChoice.getItems().isEmpty()) { initializeLogarithmic(); }
                            break;
                        case "Тригонометрическое":
                            trigonometric.setVisible(true);
                            if (trigonometricEquationChoice.getItems().isEmpty()) { initializeTrigonometric(); }
                            break;
                    }
                }
            }
        });
    }

    /**
     * Проинициализировать конкретными уравнениями блок с степенными уравнениями и добавить выбор конкретного уравнения из них
     */
    private void initializeExponential(){

        exponentialEquationChoice.getItems().setAll("9^x - 12*3^x + 27 = 0", "x^2 - 2 = 0", "x^2 + 2 = 0");
        exponentialEquationChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> selected, String oldValue, String newValue) {
                equationToSolve = null;
                if (newValue != null) {
                    switch(newValue) {
                        case "9^x - 12*3^x + 27 = 0": equationToSolve = new Equation(x -> ( Math.pow(9, x) - 12*Math.pow(3, x) + 27 )); break;
                        case "x^2 - 2 = 0": equationToSolve = new Equation(x -> Math.pow(x, 2) - 2); break;
                        case "x^2 + 2 = 0": equationToSolve = new Equation(x -> Math.pow(x, 2) + 2); break;
                    }
                }
            }
        });
    }

    /**
     * Проинициализировать конкретными уравнениями блок с логарифмическими уравнениями и добавить выбор конкретного уравнения из них
     */
    private void initializeLogarithmic(){
        logarithmicEquationChoice.getItems().setAll("log(x) + 2 = 0", "log(x) - 1 = 0", "log(x) = 0");
        logarithmicEquationChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> selected, String oldValue, String newValue) {
                equationToSolve = null;
                if (newValue != null) {
                    switch(newValue) {
                        case "log(x) + 2 = 0": equationToSolve = new Equation(x -> Math.log(x) + 2); break;
                        case "log(x) - 1 = 0": equationToSolve = new Equation(x -> Math.log(x) - 1); break;
                        case "log(x) = 0": equationToSolve = new Equation(Math::log); break;
                    }
                }
            }
        });
    }

    /**
     * Проинициализировать конкретными уравнениями блок с тригонометрическими уравнениями и добавить выбор конкретного уравнения из них
     */
    private void initializeTrigonometric(){
        trigonometricEquationChoice.getItems().setAll("sin(x) = 0", "cos(x) - 1 = 0", "tg(x) = 0");
        trigonometricEquationChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> selected, String oldValue, String newValue) {
                equationToSolve = null;
                if (newValue != null) {
                    switch(newValue) {
                        case "sin(x) = 0": equationToSolve = new Equation(Math::sin); break;
                        case "cos(x) - 1 = 0": equationToSolve = new Equation(x -> Math.cos(x) - 1); break;
                        case "tg(x) = 0": equationToSolve = new Equation(Math::tan); break;
                    }
                }
            }
        });
    }

    /**
     * Добавляет ограничение на количество символов в поле ввода
     * @param tf - поле ввода
     * @param maxLength - максимальное количество символов
     */
    public static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }
}
