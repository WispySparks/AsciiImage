package AsciiImage.Display;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class MouseEvents {

    public static final double scrollMultiplier = 0.03125;

    public static SimpleTransform zoom(ScrollEvent event, Node node) {
        double scale = (event.getDeltaY() >= 0) ? 1.2 : 0.8;
        double x = event.getX();
        double y = event.getY();

        Point2D p0 = node.localToScene(x, y);

        double scaleX = node.getScaleX() * scale;
        double scaleY = node.getScaleY() * scale;

        node.setScaleX(scaleX);
        node.setScaleY(scaleY);

        Point2D p1 = node.localToScene(x, y);

        double deltaX = p1.getX() - p0.getX();
        double deltaY = p1.getY() - p0.getY();

        double posX = node.getTranslateX() - deltaX;
        double posY = node.getTranslateY() - deltaY;

        node.setTranslateX(posX);
        node.setTranslateY(posY);
        
        return new SimpleTransform(new Translate(posX, posY), new Scale(scaleX, scaleY), new Rotate());
    }

    public static Point2D pan(MouseEvent event, Node node, Point2D startPoint) { 
        double x = node.getTranslateX() + event.getX() - startPoint.getX();
        double y = node.getTranslateY() + event.getY() - startPoint.getY();
        node.setTranslateX(x); 
        node.setTranslateY(y);
        return new Point2D(x, y);
    }

}
