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

    public static void pan(MouseEvent event, Node node) {
        System.out.println(event.getX() + " " + event.getY());
        node.setTranslateX(node.getTranslateX());
        node.setTranslateY(node.getTranslateY());
    }

}
