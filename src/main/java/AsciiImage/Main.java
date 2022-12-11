package AsciiImage;

import java.io.File;

import AsciiImage.Display.TopPane;
import AsciiImage.PNG.PNG;
import AsciiImage.PNG.PNGDecoder;
import AsciiImage.PNG.PNGReader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application { // todo 2nd line of pixels has incorrect Y value with PNGReader, gray color, spazzing thing when panning
    public static void main(String[] args) {
        PNGReader reader = new PNGReader();
        PNGDecoder decoder = new PNGDecoder();
        PNG png = decoder.readPNG(new File("C:\\Users\\wispy\\Downloads\\square.png"));
        System.out.println(reader.parseImageData(png).toSingleList());
        throw new RuntimeException("stop");
        // launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Pane centerPane = new Pane();
        centerPane.setBackground(new Background(new BackgroundFill(Color.rgb(54, 57, 63, 1), null, null)));
        Scene scene = new Scene(new BorderPane(centerPane, new TopPane(stage, centerPane), null, null, null), 640, 480);
        stage.setScene(scene);
        stage.setTitle("PNG to ASCII");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("../AsciiIcon.png")));
        stage.setMaximized(true);
        stage.show();
    }

}