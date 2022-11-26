package AsciiImage.Display;

import java.util.List;
import java.util.function.Supplier;

import AsciiImage.PNG.Pixel;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class ImageCanvas extends Canvas {
    
    private Supplier<List<Pixel>> pixels;

    public ImageCanvas(Supplier<List<Pixel>> supplier) {
        super();
        pixels = supplier;
    }

    public void drawImage(double height, double width) {
        getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
        setHeight(height);
        setWidth(width);
        PixelWriter pw = getGraphicsContext2D().getPixelWriter();
        for (Pixel p : pixels.get()) {
            Color c = Color.rgb(p.R(), p.G(), p.B());
            pw.setColor(p.X(), p.Y(), c);
        }
    }

}
