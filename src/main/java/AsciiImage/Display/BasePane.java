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
import AsciiImage.Util.FileUtil;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
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
    private AsciiCanvas asciiCanvas;
    private PNG png = new PNG();
    private List<Pixel> pixels = new ArrayList<>();
    private File currentFile = new File("");
    
    public BasePane(Stage s) {
        stage = s;
        setBackground(new Background(new BackgroundFill(Color.rgb(54, 57, 63, 1), null, null)));
        setPadding(new Insets(5, 10, 20, 10));
        setHgap(10);
        setVgap(10);
        setup();
    }

    public void setup() {
        Label errorLabel = new Label(); // Create GUI elements and initialize them
        errorLabel.setTextFill(Color.RED);
        Button selectB = new Button("Select PNG");
        Button convertB = new Button("Convert");
        Button flipB = new Button("Switch");
        Button exportB = new Button("Export");
        CheckBox invertCB = new CheckBox("Inverted");
        invertCB.setTextFill(Color.WHITE);
        NumberField charField = new NumberField("4");
        ComboBox<String> exportChoices = new ComboBox<>();
        exportChoices.getItems().addAll("Text File", "Image");
        exportChoices.getSelectionModel().selectFirst();
        ImageCanvas image = new ImageCanvas(() -> pixels);
        image.setVisible(false);
        Pane asciiPane = new Pane();
        asciiPane.setStyle("-fx-background-color: white");
        asciiPane.setVisible(false);
        asciiCanvas = new AsciiCanvas(() -> pixels, () -> invertCB.isSelected(), () -> charField.getValue());
        asciiPane.getChildren().add(asciiCanvas);
        selectB.setOnAction((event) -> { // File Select Button Logic
            File file;
            if ((file = chooser.showOpenDialog(stage)) != null) {
                Thread t = new Thread(new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        png = decoder.readPNG(file);
                        if (!FileUtil.getFileExtension(file).equals("png")) throw new IOException("Invalid File");
                        if (png.isCorrupted()) throw new IOException("Corrupted");
                        currentFile = file;
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
                        if (exceptionProperty().get().getMessage().equals("Corrupted")) {
                            errorLabel.setText("Corrupted PNG");
                            pixels.clear();
                        } else if (exceptionProperty().get().getMessage().equals("Invalid File")) {
                            errorLabel.setText("Invalid File");
                            pixels.clear();
                        }
                    }
                });
                t.setDaemon(true);
                t.start();
            }
        });
        convertB.setOnAction((event) -> { // Convert Button Logic
            if (charField.getValue() != 0) {
                if (pixels.size() > 0) {
                    asciiCanvas.drawAscii(png.height(), png.width());
                    image.setVisible(false);
                    asciiPane.setVisible(true);
                }
            } else {
                charField.setText("Must be > 0");
                charField.setOnMouseClicked((clickEvent) -> {
                    charField.setText("");
                    charField.setOnMouseClicked((e)->{});
                });
            }
        });
        flipB.setOnAction((event) -> { // Switch Button Logic
            asciiPane.setVisible(!asciiPane.isVisible());
            image.setVisible(!image.isVisible());
        });
        exportB.setOnAction((event) -> { // Export Button Logic
            switch(exportChoices.getSelectionModel().getSelectedItem()) {
                case "Text File" -> exportTextFile();
                case "Image" -> exportImageFile();
                default -> throw new IllegalArgumentException("Export ComboBox invalid selection");
            }
        });
        add(selectB, 0, 0); // Add GUI Elements
        add(convertB, 1, 0);
        add(invertCB, 2, 0);
        add(charField, 3, 0);
        add(flipB, 4, 0);
        add(exportB, 5, 0);
        add(exportChoices, 6, 0);
        add(errorLabel, 7, 0);
        add(image, 0, 1, 10, 1);
        add(asciiPane, 0, 1, 10, 1);
    }

    private void exportTextFile() {
        DumpFile dFile = new DumpFile(currentFile);
        List<String> ascii = asciiCanvas.getImageCharacters();
        for (String s : ascii) {
            dFile.write(s);
        }
        dFile.close();
    }

    private void exportImageFile() {

    }

}
