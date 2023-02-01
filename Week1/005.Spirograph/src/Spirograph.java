import java.awt.*;
import java.awt.geom.*;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import javax.swing.*;

public class Spirograph extends Application {
    private double a = 300;
    private double b = 1;
    private double c = 300;
    private double d = 10;

    Canvas canvas;
    private CheckBox checkBox;
    private Button button;

    private boolean centered = true;
    private TextField v1;
    private TextField v2;
    private TextField v3;
    private TextField v4;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.canvas = new Canvas(1920, 1080);


        VBox mainBox = new VBox();
        HBox topBar = new HBox();
        mainBox.getChildren().add(topBar);
        mainBox.getChildren().add(new Group(canvas));

        topBar.getChildren().add(v1 = new TextField("300"));
        topBar.getChildren().add(v2 = new TextField("1"));
        topBar.getChildren().add(v3 = new TextField("300"));
        topBar.getChildren().add(v4 = new TextField("10"));
        topBar.getChildren().add(button = new Button("Draw"));
        topBar.getChildren().add(checkBox = new CheckBox("Rainbow"));


        button.setOnAction((event) -> draw(new FXGraphics2D(canvas.getGraphicsContext2D())));

        primaryStage.setScene(new Scene(mainBox));
        primaryStage.setTitle("Spirograph");
        primaryStage.show();
    }


    public void draw(FXGraphics2D graphics) {


        a = Double.parseDouble(v1.getText());
        b = Double.parseDouble(v2.getText());
        c = Double.parseDouble(v3.getText());
        d = Double.parseDouble(v4.getText());


        if (centered)
        {

            graphics.translate(this.canvas.getWidth() / 2, this.canvas.getHeight() / 2);
            graphics.scale(0.5, -0.5);
            centered = false;
        }


        double resolution = 0.01;
        float lastx = (float) (a * Math.cos(b * 0) + c * Math.cos(d * 0));
        float lasty = (float) (a * Math.sin(b * 0) + c * Math.sin(d * 0));

        for (double i = 0; i < 10; i += resolution)
        {
            if (checkBox.isSelected())
            {
                graphics.setColor(Color.getHSBColor((float) (i / 10), 1, 1));
            }
            float x = (float) (a * Math.cos(b * i) + c * Math.cos(d * i));
            float y = (float) (a * Math.sin(b * i) + c * Math.sin(d * i));

            graphics.draw(new Line2D.Double(x, y, lastx, lasty));
            lastx = x;
            lasty = y;
        }
        //you can use Double.parseDouble(v1.getText()) to get a double value from the first textfield
        //feel free to add more textfields or other controls if needed, but beware that swing components might clash in naming
    }


    public static void main(String[] args) {
        launch(Spirograph.class);
    }

}
