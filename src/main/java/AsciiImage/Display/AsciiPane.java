package AsciiImage.Display;

import java.util.List;
import java.util.function.Supplier;

import AsciiImage.PNG.Pixel;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class AsciiPane extends Pane {

    private static final String[] characters = {"@","#","$","0","?",";","+","=",",",".","_"," "};
    private static final String[] charactersInv = {" ","_",".",",","=","+",";","?","0","$","#","@"};
    private Supplier<List<Pixel>> pixels;
    private int charSize = 4;
    private boolean inverted = false;

    public AsciiPane(Supplier<List<Pixel>> supplier) {
        pixels = supplier;
    }

    public void drawAscii() {
        getChildren().clear();
        String[] chars = inverted ? charactersInv : characters;
        // DumpFile file = new DumpFile();
        for (Pixel p : pixels.get()) {
            int gray = Math.round((p.R() + p.G() + p.B()) / 3);
            Color c = Color.grayRgb(gray);
            if (p.X() % charSize == 0 && p.Y() % charSize == 0) {
                String character = chars[(int) Math.round((double) (c.getRed()*255)*((chars.length-1)/255.0))];
                Label label = new Label(character);
                label.setFont(new Font("SansSerif", charSize));
                label.relocate(p.X(), p.Y());
                getChildren().add(label);

                // boolean nl = (prevY < p.Y());
                // file.write(character, nl);
                // prevY = p.Y();
            }
        }
    }

}