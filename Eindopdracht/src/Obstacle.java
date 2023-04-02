import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Obstacle {
    private double x;
    private double y;
    private double width;
    private double height;
    private double speed;
    private BufferedImage image;
    private World world;
    private Body body;

    public Obstacle(double width, double height, double y, double speed, World world, BufferedImage image){
        this.world = world;
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.image = image;
        body = new Body();
        body.addFixture(Geometry.createRectangle(this.width, this.height));
        body.getTransform().setTranslation(10,y);
        body.setMass(MassType.INFINITE);

        world.addBody(body);
        x = body.getTransform().getTranslationX();

        this.y = y;
    }

    public void draw(Graphics2D graphics){

    }

    public void update(double deltaTime){
        if (x> -15){
            x-= speed;
            body.getTransform().setTranslation(x,y);

        }
    }

    public double getX(){
        return x;
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }

    public Body getBody(){
        return body;
    }

    public BufferedImage getImage(){
        return image;
    }

    public boolean colliding(Shape playerShape){
        AffineTransform tx = new AffineTransform();
        tx.translate(this.x, this.y);
        Shape obstacleShape = tx.createTransformedShape(new Rectangle2D.Double(0,0, width+.1, height+.1));
        Area obstacleArea = new Area(obstacleShape);
        obstacleArea.intersect(new Area(playerShape));
        if (!obstacleArea.isEmpty()){
            return true;
        }
        return false;
    }

}
