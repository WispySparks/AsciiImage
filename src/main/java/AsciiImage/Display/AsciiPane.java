package AsciiImage.Display;

import AsciiImage.Util.Point2DWrapper;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

public class AsciiPane extends Pane {

    private Point2D clickPoint;
    private Point2DWrapper pos;
    
    public AsciiPane(AsciiCanvas canvas, Point2DWrapper pos) {
        super(canvas);
        this.pos = pos;
        setStyle("-fx-background-color: white");
        setOnScroll((event) -> MouseEvents.zoom(event, this));
        setOnMousePressed((event) -> {
            clickPoint = new Point2D(event.getX(), event.getY());
        });
        setOnMouseDragged((event) -> {
            pos.setPoint(MouseEvents.pan(event, this, clickPoint));
        });
    }
    
    public void setVisibleWithPos(boolean visible) {
        setTranslateX(pos.getPoint().getX());
        setTranslateY(pos.getPoint().getY());
        setVisible(visible);
    }

}
