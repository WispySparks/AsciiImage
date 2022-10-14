package AsciiImage.Display;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import AsciiImage.PNG;

public class Panel extends JPanel {

    private PNG png;
    
    Panel(PNG png) {
        this.png = png;
    }

    public void paint(Graphics g) {
        System.out.println(png.colorType());
        Color bgColor = new Color(png.backgroundColor().get(0), png.backgroundColor().get(1), png.backgroundColor().get(2));
        System.out.println(bgColor);
        g.setColor(bgColor);
        for (int i = 0; i<png.height(); i++) {
            for (int j = 0; j<png.width(); j++) {
                g.fillRect(j, i, 1, 1);
            }
        }
    }
}
