package AsciiImage.Display;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import AsciiImage.PNG.Pixel;
import AsciiImage.Util.DumpFile;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class AsciiCanvas extends Canvas {

    private static final char[] characters = {'@','#','$','0','?',';','+','=',',','.','_',' '};
    private static final char[] charactersInv = {' ','_','.',',','=','+',';','?','0','$','#','@'};
    private Supplier<List<Pixel>> pixels;
    private IntSupplier charSize;
    private BooleanSupplier inverted;
    private int prevY = 0;

    public AsciiCanvas(Supplier<List<Pixel>> supplier, BooleanSupplier inv, IntSupplier size) {
        super();
        pixels = supplier;
        inverted = inv;
        charSize = size;
    }

    public void drawAscii(DumpFile file, double height, double width) {
        int size = charSize.getAsInt();
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        setHeight(height);
        setWidth(width);
        Font font = Font.font("SansSerif", size);
        gc.setFont(font);
        char[] chars = inverted.getAsBoolean() ? charactersInv : characters;
        for (Pixel p : pixels.get()) {
            int gray = Math.round((p.R() + p.G() + p.B()) / 3);
            Color c = Color.grayRgb(gray);
            if (p.X() % size == 0 && p.Y() % size == 0) {
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