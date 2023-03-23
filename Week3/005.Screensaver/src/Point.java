import java.awt.geom.Point2D;

public class Point {
    private Point2D point;

    private double xRichting;
    private double yRichting;

    public Point(Point2D point, double xRichting, double yRichting){
        this.point = point;
        this.xRichting = xRichting;
        this.yRichting = yRichting;
    }

    public Point2D getPoint() {
        return point;
    }

    public double getxRichting() {
        return xRichting;
    }

    public double getyRichting() {
        return yRichting;
    }

    public void setPoint(Point2D point) {
        this.point = point;
    }

    public void setxRichting(double xRichting) {
        this.xRichting = xRichting;
    }

    public void setyRichting(double yRichting) {
        this.yRichting = yRichting;
    }
}
