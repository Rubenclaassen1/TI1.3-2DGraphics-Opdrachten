import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import java.awt.geom.Line2D;

public class House extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas canvas = new Canvas(1900, 1000);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));

        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("House");
        primaryStage.show();
    }


    public void draw(FXGraphics2D graphics) {
        frame(graphics);
        door(graphics);
        window(graphics, 200, 550);
        window(graphics, 350, 700);
        window(graphics, 350, 550);
    }

    public void frame(FXGraphics2D graphics) {
        graphics.draw(new Line2D.Double(150, 900, 150, 500));
        graphics.draw(new Line2D.Double(150, 500, 500, 500));
        graphics.draw(new Line2D.Double(500, 500, 500, 900));
        graphics.draw(new Line2D.Double(500, 900, 150, 900));
        graphics.draw(new Line2D.Double(150, 500, 325, 300));
        graphics.draw(new Line2D.Double(325, 300, 500, 500));
    }

    public void door(FXGraphics2D graphics) {
        graphics.draw(new Line2D.Double(200, 900, 200, 700));
        graphics.draw(new Line2D.Double(200, 700, 300, 700));
        graphics.draw(new Line2D.Double(300, 700, 300, 900));
    }

    public void window(FXGraphics2D graphics, int xStart, int yStart) {
        graphics.draw(new Line2D.Double(xStart, yStart, xStart + 100, yStart));
        graphics.draw(new Line2D.Double(xStart + 100, yStart, xStart + 100, yStart + 100));
        graphics.draw(new Line2D.Double(xStart + 100, yStart + 100, xStart, yStart + 100));
        graphics.draw(new Line2D.Double(xStart, yStart + 100, xStart, yStart));
        graphics.draw(new Line2D.Double(xStart, yStart, xStart + 100, yStart + 100));
        graphics.draw(new Line2D.Double(xStart, yStart + 100, xStart + 100, yStart));

    }


    public static void main(String[] args) {
        launch(House.class);
    }

}
