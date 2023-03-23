import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.sound.sampled.Line;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Screensaver extends Application {
    private ResizableCanvas canvas;
    private ArrayList<Point> points = new ArrayList<>();

    private ArrayList<Line2D> previousLines = new ArrayList<>();
    private Point previouspoint;

    @Override
    public void start(Stage stage) throws Exception
    {

        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
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
        stage.setTitle("Screensaver");
        stage.show();
        draw(g2d);
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.black);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        Point firstpoint = points.get(0);
        graphics.setColor(Color.magenta);


        for (Point point : points) {
            if (points.indexOf(point)>0){
                Line2D line = new Line2D.Double(previouspoint.getPoint().getX(),previouspoint.getPoint().getY(),point.getPoint().getX(),point.getPoint().getY());
//                graphics.draw(line);
                previousLines.add(line);
            }
            if (points.indexOf(point) == 3){
                Line2D line = new Line2D.Double(firstpoint.getPoint().getX(),firstpoint.getPoint().getY(),point.getPoint().getX(),point.getPoint().getY());
//                graphics.draw(line);
                previousLines.add(line);
            }
            previouspoint = point;
        }
        if (previousLines.size()>250){
            for (int i = 0; i < 4; i++) {
                previousLines.remove(i);
            }
        }

        for (Line2D line : previousLines) {
            graphics.draw(line);
        }
    }

    public void init()
    {
        double xRichting = 2;
        double yRichting = 2;
        Random r = new Random();
        for (int i = 0; i < 4; i++) {
            if (i>1){
                yRichting = -2;
            }
            xRichting = -xRichting;
            points.add(new Point(new Point2D.Double(r.nextInt(1920),r.nextInt(1080)), xRichting,yRichting));
        }
    }

    public void update(double deltaTime)
    {

        for (Point point : points) {
            if (point.getPoint().getX() <= 0 || point.getPoint().getX()>= canvas.getWidth()){
                point.setxRichting(-point.getxRichting());
            }else if (point.getPoint().getY()<= 0 || point.getPoint().getY()>=canvas.getHeight()){
                point.setyRichting(-point.getyRichting());
            }
            point.setPoint(new Point2D.Double(point.getPoint().getX()+point.getxRichting(),point.getPoint().getY() + point.getyRichting()));
        }


    }

    public static void main(String[] args)
    {
        launch(Screensaver.class);
    }

}
