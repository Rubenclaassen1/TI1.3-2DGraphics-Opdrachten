import java.awt.*;
import java.awt.geom.*;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Moon extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Moon");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        GeneralPath path = new GeneralPath();
        path.moveTo(130,210);
        path.curveTo(150, 240, 160, 290, 100, 300);
        path.curveTo(195, 315, 200, 240, 130, 210);
//        path.quadTo(200,280, 100, 320);
//        path.quadTo(270,290,110,200);
        graphics.setColor(Color.black);
        graphics.fill(path);
        graphics.setColor(Color.black);
        graphics.draw(path);

    }


    public static void main(String[] args)
    {
        launch(Moon.class);
    }

}
