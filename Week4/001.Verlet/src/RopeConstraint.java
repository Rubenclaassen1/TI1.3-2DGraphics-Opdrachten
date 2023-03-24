import org.jfree.fx.FXGraphics2D;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class RopeConstraint implements Constraint{
    private Particle a;
    private Particle b;
    private double distance;

    public RopeConstraint(Particle a, Particle b, double distance){
        this.a = a;
        this.b = b;
        this.distance = distance;
    }
    @Override
    public void satisfy() {
        double currentDistance = a.getPosition().distance(b.getPosition());
        Point2D BA = new Point2D.Double(1,0);
        double length = BA.distance(0,0);
        if (currentDistance >= distance) {
            BA = new Point2D.Double(BA.getX()/length, BA.getY()/length);
        }else{
            BA = new Point2D.Double(1,0);
        }
        b.setPosition(new Point2D.Double(b.getPosition().getX(),b.getPosition().getY()));
        a.setPosition(new Point2D.Double(a.getPosition().getX() +BA.getX(), a.getPosition().getY()+BA.getY()));
    }

    @Override
    public void draw(FXGraphics2D g2d) {
        g2d.draw(new Line2D.Double(a.getPosition(),b.getPosition()));
    }
}
