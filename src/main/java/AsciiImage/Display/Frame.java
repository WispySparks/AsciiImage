package main.java.AsciiImage.Display;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import main.java.AsciiImage.PNG.PNG;

public class Frame extends JFrame {
    
    public Frame(PNG png) {
        if (!png.isCorrupted()) {
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setSize(png.width(), png.height());
            setTitle("PNG Display");
            setResizable(false);
            setLocationRelativeTo(null);
            setVisible(true);
            add(new Panel(png));
        }
    }

}
