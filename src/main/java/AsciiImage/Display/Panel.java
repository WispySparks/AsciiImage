package main.java.AsciiImage.Display;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import main.java.AsciiImage.PNG.PNG;
import main.java.AsciiImage.PNG.PNGReader;
import main.java.AsciiImage.PNG.Pixel;
import main.java.AsciiImage.Util.ArrayList2D;

public class Panel extends JPanel {

    private PNGReader reader;
    private ArrayList2D<Pixel> pixels;
    
    Panel(PNG png) {
        reader = new PNGReader(png);
        pixels = reader.parseImageData();
    }

    public void paint(Graphics g) { // todo resize png so that it fits on screen
        for (Pixel p : pixels.toSingleList()) {
            // int gray = Math.round((p.R() + p.G() + p.B()) / 3);
            // Color c = new Color(gray, gray, gray);
            Color c = new Color(p.R(), p.G(), p.B(), p.A());
            g.setColor(c);
            g.fillRect(p.X(), p.Y(), 1, 1);
            // if (c.getRed() > 100) {
            //     g.drawString("A", p.X(), p.Y());
            // } else if (c.getRed() > 50) {
            //     g.drawString("*", p.X(), p.Y());
            // } else {
            //     g.drawString(" ", p.X(), p.Y());
            // }
        }
    }

}
