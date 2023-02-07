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

public class YingYang extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Ying Yang");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        GeneralPath path = new GeneralPath();

        Area circle = new Area(new Ellipse2D.Double(100,100,350,350));
        Area smallBlack = new Area(new Ellipse2D.Double(250, 175,50, 50));
        Area smallWhite = new Area(new Ellipse2D.Double(250, 350,50, 50));
        path.moveTo(275, 100);
        path.curveTo(360, 110, 400,260,275, 275);
        path.curveTo(150, 290, 190, 440, 275, 450);
        path.curveTo(510, 450, 510, 100, 275,100);
        graphics.setColor(Color.black);
        graphics.draw(circle);
        graphics.fill(path);
        graphics.draw(path);
        graphics.fill(smallBlack);
        graphics.draw(smallBlack);
        graphics.setColor(Color.white);
        graphics.fill(smallWhite);
        graphics.draw(smallWhite);
    }


    public static void main(String[] args)
    {
        launch(YingYang.class);
    }

}
