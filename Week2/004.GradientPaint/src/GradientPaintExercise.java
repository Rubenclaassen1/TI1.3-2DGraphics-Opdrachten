import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class GradientPaintExercise extends Application {
    private ResizableCanvas canvas;
    private float[] fractions;
    private Color[] colors;


    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("GradientPaint");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        fractions = new float[]{0.0f, 0.3f,0.9f};
        colors = new Color[] {Color.black,Color.blue,Color.red};
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        Area square = new Area(new Rectangle2D.Double(0 , 0, canvas.getWidth(), canvas.getHeight()));
        RadialGradientPaint p = new RadialGradientPaint(new Point2D.Double(canvas.getHeight()/2, canvas.getWidth()/2), 200, new Point2D.Double(200, 500), fractions, colors, MultipleGradientPaint.CycleMethod.REPEAT);

        graphics.setPaint(p);
        graphics.fill(square);


    }




    public static void main(String[] args)
    {
        launch(GradientPaintExercise.class);
    }

}
