package AsciiImage.Display;

import java.util.List;
import java.util.function.Supplier;

import AsciiImage.PNG.Pixel;
import AsciiImage.Util.Point2DWrapper;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class ImageCanvas extends Canvas {
    
    private PixelWriter pw = getGraphicsContext2D().getPixelWriter();
    private Supplier<List<Pixel>> pixels;
    private Point2D clickPoint;
    private Point2DWrapper pos;

    public ImageCanvas(Supplier<List<Pixel>> supplier, Point2DWrapper pos) {
        super();
        pixels = supplier;
        this.pos = pos;
        pos.setPoint(new Point2D(getTranslateX(), getTranslateY()));
        setOnScroll((event) -> {
            MouseEvents.zoom(event, this);
        });
        setOnMousePressed((event) -> {
            clickPoint = new Point2D(event.getX(), event.getY());
        });
        setOnMouseDragged((event) -> {
            pos.setPoint(MouseEvents.pan(event, this, clickPoint));
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

    public void setVisibleWithPos(boolean visible) {
        setTranslateX(pos.getPoint().getX());
        setTranslateY(pos.getPoint().getY());
        setVisible(visible);
    }

}
