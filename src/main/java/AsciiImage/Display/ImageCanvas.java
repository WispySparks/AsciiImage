package AsciiImage.Display;

import java.util.List;
import java.util.function.Supplier;

import AsciiImage.PNG.Pixel;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class ImageCanvas extends Canvas {
    
    private PixelWriter pw = getGraphicsContext2D().getPixelWriter();
    private Supplier<List<Pixel>> pixels;
    private Point2D clickPoint;
    private SimpleTransform transform;

    public ImageCanvas(Supplier<List<Pixel>> supplier, SimpleTransform ts) {
        super();
        pixels = supplier;
        transform = ts;
        transform.setTranslate(new Translate(getTranslateX(), getTranslateY()));
        transform.setScale(new Scale(getScaleX(), getScaleY()));
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

    public void drawImage(double height, double width) {
        getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
        setHeight(height);
        setWidth(width);
        for (Pixel p : pixels.get()) {
            Color c = Color.rgb(p.red(), p.green(), p.blue());
            pw.setColor(p.X(), p.Y(), c);
        }
    }

    public void setVisibleWithTransform(boolean visible) {
        setTranslateX(transform.getTranslate().getX());
        setTranslateY(transform.getTranslate().getY());
        setScaleX(transform.getScale().getX());
        setScaleY(transform.getScale().getY());
        setVisible(visible);
    }

}
