import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Renderable {
    private Area shape;
    private Point2D position;
    private Color color;

    public Renderable(Area shape, Point2D position, Color color){
        this.shape = shape;
        this.position = position;
        this.color = color;

    }


//    public int getWidth() {
//        return (int)shape.getWidth();
//    }
//
//    public int getHeight() {
//        return (int)shape.getHeight();
//    }

    public Area getShape() {
        return shape;
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
