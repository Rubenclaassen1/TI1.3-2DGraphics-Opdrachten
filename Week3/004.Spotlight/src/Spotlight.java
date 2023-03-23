import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Spotlight extends Application {
    private ResizableCanvas canvas;
    private BufferedImage background;
    private double x = 0;
    private double y;
    private Shape shape;
    private AffineTransform circle = new AffineTransform();




    @Override
    public void start(Stage stage) throws Exception
    {

        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        canvas.setOnMouseDragged(e-> mouseDragged(e));
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now)
            {
                if (last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Spotlight");
        stage.show();
        draw(g2d);
    }

    private void mouseReleased(MouseEvent e) {

    }

    private void mousePressed(MouseEvent e) {

    }


    public void draw(FXGraphics2D graphics)
    {

        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.black);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        graphics.draw(shape);
        graphics.clip(shape);

        AffineTransform tx = new AffineTransform();
        tx.scale(1.5f,1.5f);
        graphics.drawImage(background,tx,null);
        graphics.setClip(null);


    }

    public void init()
    {
        try{
            background = ImageIO.read(getClass().getResource("background.jpg"));
        }catch(Exception e){
            e.printStackTrace();
        }
        shape = new Ellipse2D.Double(10,100,200,200);

    }

    public void update(double deltaTime)
    {

    }

    private void mouseDragged(MouseEvent e){
        x = e.getX() -100;
        y = e.getY() -100;
        System.out.println(x + "," + y);
        shape = new Ellipse2D.Double(x,y,200,200);

    }

    public static void main(String[] args)
    {
        launch(Spotlight.class);
    }

}
