package AsciiImage.Display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.plaf.FontUIResource;

import AsciiImage.PNG.PNG;
import AsciiImage.PNG.PNGReader;
import AsciiImage.PNG.Pixel;

public class Panel extends JPanel {

    private final String[] characters = {"@","#","$","0","?",";","+","=",",",".","_"," "};
    private final String[] charactersInv = {" ","_",".",",","=","+",";","?","0","$","#","@"};
    private final int charSize = 4;
    private PNGReader reader;
    private List<Pixel> pixels;
    public boolean inverted = false;
    public boolean ascii = true;
    // private int prevY = 0;
    
    Panel(PNG png) {
        reader = new PNGReader(png);
        pixels = reader.parseImageData().toSingleList();
    }

    public void paint(Graphics g) {
        String[] chars = inverted ? charactersInv : characters;
        g.setFont(new FontUIResource(Font.SANS_SERIF, Font.PLAIN, charSize));
        // DumpFile file = new DumpFile();

        for (Pixel p : pixels) {
            if (ascii) {
                int gray = Math.round((p.R() + p.G() + p.B()) / 3);
                Color c = new Color(gray, gray, gray);
                if (p.X() % charSize == 0 && p.Y() % charSize == 0) {
                    String character = chars[(int) Math.round((double) c.getRed()*((chars.length-1)/255.0))];
                    g.drawString(character, p.X(), p.Y());
                    // boolean nl = (prevY < p.Y());
                    // file.write(character, nl);
                    // prevY = p.Y();
                }
            } else {
                Color c = new Color(p.R(), p.G(), p.B(), p.A());
                g.setColor(c);
                g.fillRect(p.X(), p.Y(), 1, 1);
            }
        }
        // file.close();
    }
    
}
