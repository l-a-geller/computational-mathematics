package drawers;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.function.DoubleFunction;

/**
 * обновлено в следующих лабах
 */
public class PlotDrawer {

    Group group;

    public PlotDrawer() {
        group = new Group();
        group.getChildren().addAll(
                new Line( 0, 300.0, 800.0, 300.0),
                new Line(400.0, 0, 400.0, 600.0)
        );
    }

    public void addFunction(DoubleFunction doubleFunction, boolean horizontal){
        if (horizontal){
            for(int i=-400; i<400; i++){
                group.getChildren().add(
                        new Line(i+400, -Math.floor((Double) doubleFunction.apply(i)) + 300, i+1+400, -Math.floor((Double) doubleFunction.apply(i+1)) + 300)
                );
            }
        }else{
            for(int i=-400; i<400; i++){
                group.getChildren().add(
                        new Line(Math.floor((Double) doubleFunction.apply(i)) + 400, i+300, Math.floor((Double) doubleFunction.apply(i+1)) + 400, i+1+300)
                );
            }
        }
    }

    public void plot(){
        Stage stage = new Stage();
        stage.setScene(new Scene(group, 800, 600));
        stage.show();
    }
}
