package AsciiImage.Display;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import AsciiImage.PNG;

public class Panel extends JPanel {

    PNG png;
    
    Panel(PNG png) {
        this.png = png;
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(30, 30, 300, 300);
    }
}
