package AsciiImage.Display;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import AsciiImage.PNG.Pixel;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class AsciiCanvas extends Canvas {

    public static final char[] characters = {'@','#','$','0','?',';','+','=',',','.','_',' '};
    public static final char[] charactersInv = {' ','_','.',',','=','+',';','?','0','$','#','@'};
    private final GraphicsContext gc = getGraphicsContext2D();
    private List<String> currentChars = new ArrayList<>();
    private Supplier<List<Pixel>> pixelSupplier;
    private IntSupplier charSize;
    private BooleanSupplier inverted;

    AsciiCanvas(Supplier<List<Pixel>> supplier, BooleanSupplier inv, IntSupplier size) {
        super();
        pixelSupplier = supplier;
        inverted = inv;
        charSize = size;
    }

    public void drawAscii(double height, double width) {
        clearCanvas();
        setHeight(height);
        setWidth(width);
        int size = charSize.getAsInt();
        Font font = Font.font("SansSerif", size);
        gc.setFont(font);
        char[] chars = inverted.getAsBoolean() ? charactersInv : characters;
        List<Pixel> pixels = pixelSupplier.get();
        for (int i = 0; i < pixels.size(); i++) {
            Pixel p = pixels.get(i);
            int gray = Math.round((p.red() + p.green() + p.blue()) / 3);
            Color c = Color.grayRgb(gray);
            if (p.X() % size == 0 && p.Y() % size == 0) {
                String character = String.valueOf(chars[(int) Math.round((double) (c.getRed()*255)*((chars.length-1)/255.0))]);
                if (!character.equals(" ")) gc.fillText(character, p.X(), p.Y());
                currentChars.add(character);
                if (i+size < pixels.size() && p.Y() < pixels.get(i+size).Y()) {
                    currentChars.add(System.lineSeparator());
                }
            }
        }
    }

    public List<String> getImageCharacters() {
        return currentChars;
    }

    public void clearCanvas() {
        currentChars.clear();
        gc.clearRect(0, 0, getWidth(), getHeight());
        setHeight(0);
        setWidth(0);
    }

}