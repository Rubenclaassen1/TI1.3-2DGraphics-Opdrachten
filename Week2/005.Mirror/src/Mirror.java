import java.awt.*;
import java.awt.geom.*;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Mirror extends Application {
    Canvas canvas;
    private Area square;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        this.canvas = new Canvas(1900, 1000);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("Graph");
        primaryStage.show();
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.translate(canvas.getWidth()/2, canvas.getHeight()/2);
        graphics.scale(1,-1);

        graphics.setColor(Color.red);
        graphics.drawLine(-1000,0,1000,0);
        graphics.setColor(Color.green);
        graphics.drawLine(0,-1000,0,1000);
        graphics.setColor(Color.black);

        line(graphics);
        square(graphics);
        graphics.draw(getTransformedShape());

//        graphics.setTransform(new AffineTransform());
//        graphics.setBackground(Color.white);
//        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
    }

    public void line (FXGraphics2D graphics){
        double resolution = 0.1;
        double scale = 100.0;
        double lastY  = Math.pow(-10, 3);

        for (double x = -10; x<10; x+=resolution){
            float y = (float) (2.5 * x);
            graphics.draw(new Line2D.Double(x * scale,y*scale,(x-resolution)*scale,lastY*scale));
            lastY = y;
        }
    }

    public void square(FXGraphics2D graphics){
        this.square = new Area (new Rectangle2D.Double(200, 100,100,100));
        graphics.draw(square);
    }

    public AffineTransform getTransform(){
        double m00 = (2/(1+(2.5*2.5)))-1;
        double m10 = (2*2.5)/(1+(2.5*2.5));
        double m01 = (2*2.5)/(1+(2.5*2.5));
        double m11 = ((2*(2.5*2.5)/(1 + (2.5*2.5))))-1;



        AffineTransform tx = new AffineTransform();
        tx.concatenate(new AffineTransform (m00,m10,m01,m11,0,0));
        return tx;
    }

    public Shape getTransformedShape(){

        return getTransform().createTransformedShape(square);
    }


    public static void main(String[] args)
    {
        launch(Mirror.class);
    }

}
