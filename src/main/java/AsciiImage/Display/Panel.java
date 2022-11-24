package main.java.AsciiImage.Display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.plaf.FontUIResource;

import main.java.AsciiImage.PNG.PNG;
import main.java.AsciiImage.PNG.PNGReader;
import main.java.AsciiImage.PNG.Pixel;
import main.java.AsciiImage.Util.ArrayList2D;

public class Panel extends JPanel {

    private final String[] characters = {"@","#","$","0","?",";","+","=",",",".","_"," "};
    private final String[] charactersInv = {" ","_",".",",","=","+",";","?","0","$","#","@"};
    private final int charSize = 4;
    private PNGReader reader;
    private ArrayList2D<Pixel> pixels;
    public boolean inverted = false;
    public boolean ascii = true;
    
    Panel(PNG png) {
        reader = new PNGReader(png);
        pixels = reader.parseImageData();
    }

    public void paint(Graphics g) {
        String[] chars = inverted ? charactersInv : characters;
        g.setFont(new FontUIResource(Font.SANS_SERIF, Font.PLAIN, charSize));
        for (Pixel p : pixels.toSingleList()) {
            if (ascii) {
                int gray = Math.round((p.R() + p.G() + p.B()) / 3);
                Color c = new Color(gray, gray, gray);
                if (p.X() % charSize == 0 && p.Y() % charSize == 0) {
                    g.drawString(chars[(int) Math.round((double) c.getRed()*((chars.length-1)/255.0))], p.X(), p.Y());
                }
            } else {
                Color c = new Color(p.R(), p.G(), p.B(), p.A());
                g.setColor(c);
                g.fillRect(p.X(), p.Y(), 1, 1);
            }
        }
    }

}
