package AsciiImage.Display;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import AsciiImage.PNG.PNG;
import AsciiImage.PNG.PNGDecoder;
import AsciiImage.PNG.PNGReader;
import AsciiImage.PNG.Pixel;
import AsciiImage.Util.DumpFile;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class BasePane extends GridPane {

    private final FileChooser chooser = new FileChooser();
    private final Stage stage;
    private final PNGReader reader = new PNGReader();
    private final PNGDecoder decoder = new PNGDecoder();
    private PNG png = new PNG();
    private List<Pixel> pixels = new ArrayList<>();
    private String fileName = "";
    
    public BasePane(Stage s) {
        stage = s;
        setup();
    }

    public void setup() {
        Button b = new Button("Select PNG");
        Button b2 = new Button("Convert");
        CheckBox cb = new CheckBox("Create a text file");
        CheckBox cb2 = new CheckBox("Inverted");
        ImageCanvas image = new ImageCanvas(() -> pixels);
        AsciiPane ascii = new AsciiPane(() -> pixels, () -> cb2.isSelected());
        image.setVisible(false);
        ascii.setVisible(false);
        b.setOnAction((event) -> {
            File file;
            if ((file = chooser.showOpenDialog(stage)) != null) {
                fileName = file.getName();
                png = decoder.readPNG(file);
                pixels = reader.parseImageData(png).toSingleList();
                image.drawImage(png.height(), png.width());
                ascii.setVisible(false);
                image.setVisible(true);
            }
        });
        b2.setOnAction((event) -> {
            new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    return null;
                }
            };
            DumpFile file = new DumpFile(DumpFile.removeFileExt(fileName), cb.isSelected());
            ascii.drawAscii(file, png.height(), png.width());
            image.setVisible(false);
            ascii.setVisible(true);
        });
        add(b, 0, 0);
        add(b2, 1, 0);
        add(cb, 2, 0);
        add(cb2, 3, 0);
        add(image, 4, 0);
        add(ascii, 4, 0);
    }

}
