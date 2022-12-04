package AsciiImage.Display;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class MouseEvents {

    public static final double scrollMultiplier = 0.03125;

    public static void zoom(ScrollEvent event, Node node) {
        double scale = (event.getDeltaY() >= 0) ? 1.2 : 0.8;
        double x = event.getX();
        double y = event.getY();

        Point2D p0 = node.localToScene(x, y);

        node.setScaleX(node.getScaleX() * scale);
        node.setScaleY(node.getScaleY() * scale);

        Point2D p1 = node.localToScene(x, y);

        double deltaX = p1.getX() - p0.getX();
        double deltaY = p1.getY() - p0.getY();

        node.setTranslateX(node.getTranslateX() - deltaX);
        node.setTranslateY(node.getTranslateY() - deltaY);   
    }

    public static Point2D pan(MouseEvent event, Node node, Point2D startPoint) { 
        double x = node.getTranslateX() + event.getX() - startPoint.getX();
        double y = node.getTranslateY() + event.getY() - startPoint.getY();
        node.setTranslateX(x); 
        node.setTranslateY(y);
        return new Point2D(x, y);
    }

}
