package AsciiImage;

import AsciiImage.Display.BasePane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application { // todo resize png so that it fits on screen, text file top line, zoom in?
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(new BasePane(stage), 640, 480);
        stage.setScene(scene);
        stage.setTitle("PNG to ASCII");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("../AsciiIcon.png")));
        stage.setMaximized(true);
        stage.show();
    }

}