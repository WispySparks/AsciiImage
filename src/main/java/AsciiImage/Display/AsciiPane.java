package AsciiImage.Display;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import AsciiImage.PNG.Pixel;
import AsciiImage.Util.DumpFile;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class AsciiPane extends Canvas {

    private static final char[] characters = {'@','#','$','0','?',';','+','=',',','.','_',' '};
    private static final char[] charactersInv = {' ','_','.',',','=','+',';','?','0','$','#','@'};
    private Supplier<List<Pixel>> pixels;
    private int charSize = 4;
    private BooleanSupplier inverted;
    private int prevY = 0;

    public AsciiPane(Supplier<List<Pixel>> supplier, BooleanSupplier inv) {
        super();
        pixels = supplier;
        inverted = inv;
    }

    public void drawAscii(DumpFile file, double height, double width) {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        setHeight(height);
        setWidth(width);
        Font font = Font.font("SansSerif", charSize);
        gc.setFont(font);
        char[] chars = inverted.getAsBoolean() ? charactersInv : characters;
        for (Pixel p : pixels.get()) {
            int gray = Math.round((p.R() + p.G() + p.B()) / 3);
            Color c = Color.grayRgb(gray);
            if (p.X() % charSize == 0 && p.Y() % charSize == 0) {
                String character = String.valueOf(chars[(int) Math.round((double) (c.getRed()*255)*((chars.length-1)/255.0))]);
                gc.strokeText(character, p.X(), p.Y());
                if (file.shouldCreate()) {
                    boolean nl = (prevY < p.Y());
                    file.write(character, nl);
                    prevY = p.Y();
                }
            }
        }
        file.close();
    }

}