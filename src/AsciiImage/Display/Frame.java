package AsciiImage.Display;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import AsciiImage.PNG;

public class Frame extends JFrame {
    
    public Frame(PNG png) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(png.width(), png.height());
        setTitle("PNG Display");
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
