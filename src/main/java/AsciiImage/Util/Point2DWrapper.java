package AsciiImage.Util;

import javafx.geometry.Point2D;

public class Point2DWrapper {
    
    private Point2D point;

    public Point2DWrapper() {
        this(new Point2D(0, 0));
    }

    public Point2DWrapper(Point2D point) {
        this.point = point;
    }

    public Point2D getPoint() {
        return point;
    }

    public void setPoint(Point2D point) {
        this.point = point;
    }
    
}
