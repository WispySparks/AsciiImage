package AsciiImage;

import AsciiImage.Display.BasePane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application { // todo resize png so that it fits on screen, dump text file(top line is broken), javafx, zoom in?
    public static void main(String[] args) { // todo use tasks for the computing, maybe figure out better performant labels
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(new BasePane(stage), 640, 480);
        stage.setScene(scene);
        stage.setTitle("PNG to ASCII");
        stage.show();
    }

}