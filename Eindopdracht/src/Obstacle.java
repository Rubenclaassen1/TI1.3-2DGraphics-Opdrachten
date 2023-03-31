import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Obstacle {
    private double x;
    private double y;
    private int speed;
    private BufferedImage image;

    public Obstacle(double x, double y, BufferedImage image, int speed){
        this.x = x;
        this.y = y;
        this.image = image;
        this.speed = speed;
    }

    public void draw(Graphics2D graphics){
        AffineTransform tx = new AffineTransform();
        tx.translate(this.x, this.y);
        graphics.drawImage(image,tx,null);
    }

    public void update(double deltaTime){
        x-= deltaTime * speed;
    }

    public double getX(){
        return x;
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }



}
