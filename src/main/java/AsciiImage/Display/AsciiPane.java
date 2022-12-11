package AsciiImage.Display;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import AsciiImage.PNG.Pixel;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Translate;

public class AsciiPane extends Pane {

    private Point2D clickPoint;
    private SimpleTransform transform;
    private AsciiCanvas canvas;
    
    public AsciiPane(Supplier<List<Pixel>> supplier, BooleanSupplier inv, IntSupplier size, SimpleTransform ts) {
        super();
        canvas = new AsciiCanvas(supplier, inv, size);
        getChildren().add(canvas);
        transform = ts;
        setStyle("-fx-background-color: white");
        setOnScroll((event) -> {
            SimpleTransform t = MouseEvents.zoom(event, this);
            transform.setScale(t.getScale());
            transform.setTranslate(t.getTranslate());
        });
        setOnMousePressed((event) -> {
            clickPoint = new Point2D(event.getX(), event.getY());
        });
        setOnMouseDragged((event) -> {
            Point2D point = MouseEvents.pan(event, this, clickPoint);
            transform.setTranslate(new Translate(point.getX(), point.getY()));
        });
    }
    
    public void setVisibleWithTransform(boolean visible) {
        setTranslateX(transform.getTranslate().getX());
        setTranslateY(transform.getTranslate().getY());
        setScaleX(transform.getScale().getX());
        setScaleY(transform.getScale().getY());
        setVisible(visible);
    }

    public AsciiCanvas getCanvas() {
        return canvas;
    }

}
