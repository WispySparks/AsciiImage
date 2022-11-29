package AsciiImage.Display;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import AsciiImage.PNG.PNG;
import AsciiImage.PNG.PNGDecoder;
import AsciiImage.PNG.PNGReader;
import AsciiImage.PNG.Pixel;
import AsciiImage.Util.DumpFile;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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
        setBackground(new Background(new BackgroundFill(Color.rgb(54, 57, 63, 1), null, null)));
        setPadding(new Insets(5, 10, 20, 10));
        setHgap(10);
        setVgap(10);
        setup();
    }

    public void setup() {
        Label errorLabel = new Label();
        Button selectB = new Button("Select PNG");
        Button convertB = new Button("Convert");
        Button flipB = new Button("FlipFlop");
        CheckBox fileCB = new CheckBox("Create a text file");
        CheckBox invertCB = new CheckBox("Inverted");
        NumberField charField = new NumberField("4");
        ImageCanvas image = new ImageCanvas(() -> pixels);
        Pane asciiPane = new Pane();
        asciiPane.setStyle("-fx-background-color: white");
        AsciiCanvas asciiCanvas = new AsciiCanvas(() -> pixels, () -> invertCB.isSelected(), () -> charField.getValue());
        asciiPane.getChildren().add(asciiCanvas);
        errorLabel.setTextFill(Color.WHITE);
        fileCB.setTextFill(Color.WHITE);
        invertCB.setTextFill(Color.WHITE);
        image.setVisible(false);
        asciiPane.setVisible(false);
        selectB.setOnAction((event) -> {
            File file;
            if ((file = chooser.showOpenDialog(stage)) != null) {
                Thread t = new Thread(new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        fileName = file.getName();
                        png = decoder.readPNG(file);
                        if (png.isCorrupted()) throw new IOException();
                        pixels = reader.parseImageData(png).toSingleList();
                        return null;
                    }
                    protected void succeeded() {
                        errorLabel.setText("");
                        image.drawImage(png.height(), png.width());
                        image.setVisible(true);
                        asciiPane.setVisible(false);
                    }
                    protected void failed() {
                        if (exceptionProperty().get().getClass() == IOException.class ) {
                            errorLabel.setText("Invalid File or Corrupted PNG");
                            pixels.clear();
                        }
                    }
                });
                t.setDaemon(true);
                t.start();
            }
        });
        convertB.setOnAction((event) -> {
            if (charField.getValue() != 0) {
                DumpFile file = new DumpFile(DumpFile.removeFileExt(fileName), fileCB.isSelected());
                asciiCanvas.drawAscii(file, png.height(), png.width());
                image.setVisible(false);
                asciiPane.setVisible(true);
            } else {
                charField.setText("Must be > 0");
                charField.setOnMouseClicked((clickEvent) -> {
                    charField.setText("");
                    charField.setOnMouseClicked((e)->{});
                });
            }
        });
        flipB.setOnAction((event) -> {
            asciiPane.setVisible(!asciiPane.isVisible());
            image.setVisible(!image.isVisible());
        });
        add(selectB, 0, 0);
        add(convertB, 1, 0);
        add(fileCB, 2, 0);
        add(invertCB, 3, 0);
        add(charField, 4, 0);
        add(errorLabel, 5, 0);
        add(flipB, 6, 0);
        add(image, 0, 1, 10, 1);
        add(asciiPane, 0, 1, 10, 1);
    }

}
