import java.awt.*;
import java.awt.geom.*;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class Rainbow extends Application {
    Canvas canvas;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.canvas = new Canvas(1920, 1080);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("Rainbow");
        primaryStage.show();
    }


    public void draw(FXGraphics2D graphics) {
        graphics.translate(this.canvas.getWidth()/2, this.canvas.getHeight()/2);
        graphics.scale(1,-1);

        double resolution = Math.PI/2500;
        double radiusBinnen = 200;
        double radiusBuiten = 300;


        for (double hoek = 0; hoek < Math.PI; hoek+= resolution) {



            float x1 = (float) (radiusBinnen * Math.cos(hoek));
            float y1 = (float) (radiusBinnen * Math.sin(hoek));
            float x2 = (float) (radiusBuiten * Math.cos(hoek));
            float y2 = (float) (radiusBuiten * Math.sin(hoek));
            graphics.setColor(Color.getHSBColor((float)(hoek/Math.PI),1,1));
            graphics.draw(new Line2D.Double(x1,y1,x2,y2));
        }
    }



    public static void main(String[] args) {
        launch(Rainbow.class);
    }

}
