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

public class Rainbow extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage stage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        stage.setScene(new Scene(mainPane));
        stage.setTitle("Rainbow");
        stage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));

    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
//        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        graphics.translate(1920/2, 1080/2);
        String woord ="Regenboog";
        Font font = new Font ("Arial", Font.PLAIN, 100);
        double angle = (-Math.PI) + (Math.PI/woord.length() /4);


        for (int i = 0; i < woord.length(); i++)
        {
            Shape shape = font.createGlyphVector(graphics.getFontRenderContext(), String.valueOf(woord.charAt(i))).getOutline();
            AffineTransform tx = new AffineTransform();
            tx.translate (300*Math.cos(angle), 300*Math.sin(angle));
            tx.rotate(Math.PI/(woord.length()-1) * i - (0.5 * Math.PI));
            graphics.draw(tx.createTransformedShape(shape));
            graphics.setColor(Color.getHSBColor((float) i/woord.length(),0.5f,1));
            graphics.fill(tx.createTransformedShape(shape));
            graphics.setColor(Color.black);
            angle += Math.PI/(woord.length());

        }

//
//        Shape shape = font.createGlyphVector(graphics.getFontRenderContext(), "regenboog").getOutline();
//        graphics.draw(AffineTransform.getTranslateInstance(100,100).createTransformedShape(shape));

    }

    public AffineTransform getTransform(double rotation, double translation){
        AffineTransform tx = new AffineTransform();
        tx.translate(translation, 100);
        tx.rotate(rotation);
        return tx;
    }

    public Shape getTransformedShape(Shape shape, double rotation, double translation){
        return getTransform(rotation, translation).createTransformedShape(shape);
    }


    public static void main(String[] args)
    {
        launch(Rainbow.class);
    }

}
