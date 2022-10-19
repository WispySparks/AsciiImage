package AsciiImage.Display;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import AsciiImage.PNG.PNG;
import AsciiImage.PNG.PNGReader;
import AsciiImage.PNG.Pixel;

public class Panel extends JPanel {

    private PNGReader reader;
    private List<Pixel> pixels;
    
    Panel(PNG png) {
        reader = new PNGReader(png);
        pixels = reader.parseImageData();
    }

    public void paint(Graphics g) { // data has a filter applied at every line, one filter per line LINE,
        for (Pixel p : pixels) {
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

    /*
    private void paintBackground(Graphics g) {
        Color bgColor = null;
        if (!png.backgroundColor().isEmpty()) {
            switch(png.colorType()) {
                case PALETTE -> System.out.println("cry");
                case GRAYSCALE, GRAYSCALE_ALPHA -> new Color(png.backgroundColor().get(0));
                case RGB, RGB_ALPHA -> bgColor = new Color(png.backgroundColor().get(0), png.backgroundColor().get(1), png.backgroundColor().get(2));
            }
            g.setColor(bgColor);
            for (int i = 0; i<png.height(); i++) {
                for (int j = 0; j<png.width(); j++) {
                    g.fillRect(j, i, 1, 1);
                }
            }
        }
    }
    */
}
