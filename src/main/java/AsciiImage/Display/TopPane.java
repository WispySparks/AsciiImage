package AsciiImage.Display;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import AsciiImage.PNG.PNG;
import AsciiImage.PNG.PNGDecoder;
import AsciiImage.PNG.PNGReader;
import AsciiImage.PNG.Pixel;
import AsciiImage.Util.DumpFile;
import AsciiImage.Util.FileUtil;
import AsciiImage.Util.TranslateScale;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;


public class TopPane extends GridPane {

    private final FileChooser chooser = new FileChooser();
    private final Stage stage;
    private final PNGReader reader = new PNGReader();
    private final PNGDecoder decoder = new PNGDecoder();
    private final ExtensionFilter pngFilter = new ExtensionFilter("PNG File", "*.png");
    private AsciiPane ascii;
    private Label errorLabel = new Label();
    private PNG png = new PNG();
    private List<Pixel> pixels = new ArrayList<>();
    private Pane centerPane;
    
    public TopPane(Stage s, Pane center) {
        super();
        stage = s;
        centerPane = center;
        chooser.getExtensionFilters().add(pngFilter);
        setBackground(new Background(new BackgroundFill(Color.rgb(54, 57, 63, 1), null, null)));
        setPadding(new Insets(5, 10, 20, 10));
        setHgap(10);
        setVgap(10);
        setup();
    }

    private void setup() { // Create GUI elements and initialize them
        errorLabel.setTextFill(Color.RED);
        Button selectB = new Button("Select PNG");
        Button convertB = new Button("Convert");
        Button flipB = new Button("Switch");
        Button exportB = new Button("Export");
        CheckBox invertCB = new CheckBox();
        NumberField charField = new NumberField("4");
        Label charLabel = new Label("Pixels per Character");
        charLabel.setTextFill(Color.WHITE);
        Label invertLabel = new Label("Inverted");
        invertLabel.setTextFill(Color.WHITE);
        ComboBox<String> exportChoices = new ComboBox<>();
        exportChoices.getItems().addAll("Text File", "Image");
        exportChoices.getSelectionModel().selectFirst();

        TranslateScale tranform = new TranslateScale();
        ImageCanvas imageCanvas = new ImageCanvas(() -> pixels, tranform);
        imageCanvas.setVisibleWithTransform(false);
        ascii = new AsciiPane(() -> pixels, () -> invertCB.isSelected(), () -> charField.getValue(), tranform);
        ascii.setVisibleWithTransform(false);

        selectB.setOnAction((event) -> { // File Select Button Logic
            File file;
            chooser.setTitle("Select File");
            chooser.getExtensionFilters().set(0, pngFilter);
            if ((file = chooser.showOpenDialog(stage)) != null) {
                Thread t = new Thread(new Task<Void>() {
                    @Override
                    protected Void call() throws IOException {
                        png = decoder.readPNG(file);
                        if (!FileUtil.getFileExtension(file).equals("png")) throw new IOException("Invalid File");
                        if (png.isCorrupted()) throw new IOException("Corrupted");
                        pixels = reader.parseImageData(png).toSingleList();
                        return null;
                    }
                    protected void succeeded() {
                        tranform.reset();
                        errorLabel.setText("");
                        imageCanvas.drawImage(png.height(), png.width());
                        ascii.getCanvas().clearCanvas();
                        imageCanvas.setVisibleWithTransform(true);
                        ascii.setVisibleWithTransform(false);
                    }
                    protected void failed() {
                        if (exceptionProperty().get().getMessage().equals("Corrupted")) {
                            errorLabel.setText("Corrupted PNG");
                            ascii.getCanvas().clearCanvas();
                        } else if (exceptionProperty().get().getMessage().equals("Invalid File")) {
                            errorLabel.setText("Invalid File");
                            ascii.getCanvas().clearCanvas();
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
                    ascii.getCanvas().drawAscii(png.height(), png.width());
                    imageCanvas.setVisibleWithTransform(false);
                    ascii.setVisibleWithTransform(true);
                    errorLabel.setText("");
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
            ascii.setVisibleWithTransform(!ascii.isVisible());
            imageCanvas.setVisibleWithTransform(!imageCanvas.isVisible());
        });
        exportB.setOnAction((event) -> { // Export Button Logic
            switch(exportChoices.getSelectionModel().getSelectedItem()) {
                case "Text File" -> exportTextFile();
                case "Image" -> exportImageFile();
                default -> throw new IllegalArgumentException("Export ComboBox invalid selection");
            }
        });

        centerPane.getChildren().addAll(imageCanvas, ascii); // Add Canvases
        add(selectB, 0, 0); // Add GUI Elements
        add(convertB, 1, 0);
        add(invertLabel, 2, 0);
        add(invertCB, 3, 0);
        add(charLabel, 4, 0);
        add(charField, 5, 0);
        add(flipB, 6, 0);
        add(exportB, 7, 0);
        add(exportChoices, 8, 0);
        add(errorLabel, 9, 0);
        add(centerPane, 0, 1, 10, 1);
    }

    private void exportTextFile() {
        List<String> asciiChars = ascii.getCanvas().getImageCharacters();
        if (asciiChars.size() > 0) {
            chooser.getExtensionFilters().set(0, new ExtensionFilter("Text File", "*.txt"));
            chooser.setTitle("Save File");
            File file = chooser.showSaveDialog(stage);
            if (file == null) return;
            DumpFile dFile = new DumpFile(file);
            for (String s : asciiChars) {
                dFile.write(s);
            }
            dFile.close();
        } else {
            errorLabel.setText("No characters to write");
        }
    }

    private void exportImageFile() {
        if (ascii.getWidth() > 0 && ascii.getHeight() > 0) {
            WritableImage wImg = new WritableImage( (int) ascii.getWidth(), (int) ascii.getHeight());
            wImg = ascii.snapshot(new SnapshotParameters(), wImg);
            chooser.getExtensionFilters().set(0, pngFilter);
            chooser.setTitle("Save File");
            File file = chooser.showSaveDialog(stage);
            if (file != null) {
                BufferedImage bufferedImg = SwingFXUtils.fromFXImage(wImg, null); 
                try {
                    ImageIO.write(bufferedImg, "png", file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            errorLabel.setText("No image to export");
        }
    }

}
