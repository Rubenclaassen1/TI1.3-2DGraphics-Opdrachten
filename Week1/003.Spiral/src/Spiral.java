import java.awt.*;
import java.awt.geom.*;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class Spiral extends Application {
    Canvas canvas;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.canvas = new Canvas(1900, 1000);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("Spiral");
        primaryStage.show();
    }
    
    
    public void draw(FXGraphics2D graphics) {
        graphics.translate(this.canvas.getWidth()/2, this.canvas.getHeight()/2);
        graphics.scale(1,-1);

        graphics.setColor(Color.red);
        graphics.drawLine(-1000,0,1000,0);
        graphics.setColor(Color.green);
        graphics.drawLine(0,-1000,0,1000);
        graphics.setColor(Color.black);

        double resolution = 0.1;
        double n = 5;
        double lastX = n* 0 * Math.cos(0);
        double lastY = n* 0 * Math.sin(0);

        for(double i = 0; i<1000; i+=resolution){
            float y = (float)(n*i*Math.sin(i));
            float x = (float)(n*i*Math.cos(i));
            graphics.draw(new Line2D.Double(x,y,lastX,lastY));
            lastX = x;
            lastY = y;
        }

    }
    
    
    
    public static void main(String[] args) {
        launch(Spiral.class);
    }

}
