package AsciiImage.Display;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import AsciiImage.PNG.PNG;
import AsciiImage.PNG.PNGDecoder;
import AsciiImage.PNG.PNGReader;
import AsciiImage.PNG.Pixel;
import AsciiImage.Util.DumpFile;
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
        ImageCanvas image = new ImageCanvas(() -> pixels);
        AsciiPane ascii = new AsciiPane(() -> pixels);
        image.setVisible(false);
        ascii.setVisible(false);
        b.setOnAction((event) -> {
            File file;
            if ((file = chooser.showOpenDialog(stage)) != null) {
                fileName = file.getName();
                PNG png = decoder.readPNG(file);
                pixels = reader.parseImageData(png).toSingleList();
                image.drawImage(png.height(), png.width());
                ascii.setVisible(false);
                image.setVisible(true);
            }
        });
        b2.setOnAction((event) -> {
            DumpFile file = new DumpFile(DumpFile.removeFileExt(fileName));
            ascii.drawAscii(file, cb.isSelected());
            image.setVisible(false);
            ascii.setVisible(true);
        });
        add(b, 0, 0);
        add(b2, 1, 0);
        add(cb, 2, 0);
        add(image, 3, 0);
        add(ascii, 3, 0);
    }

}
