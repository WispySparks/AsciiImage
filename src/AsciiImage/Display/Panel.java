package AsciiImage.Display;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import AsciiImage.PNG;
import AsciiImage.Util.PNGUtil;

public class Panel extends JPanel {

    private PNGUtil util = new PNGUtil();
    private PNG png;
    private byte[] data;
    private int lineLength;
    
    Panel(PNG png) {
        this.png = png;
        data = png.imageData();
        int bytesPerPixel = switch(png.colorType()) {
            case GRAYSCALE -> 1; // G
            case GRAYSCALE_ALPHA -> 2; // G, A
            case PALETTE -> 1; // Indexed Color
            case RGB -> 3; // R, G, B
            case RGB_ALPHA -> 4; // R, G, B, A
        };
        lineLength = bytesPerPixel * png.width() + 1;
        if (data.length / lineLength == png.height()) System.out.println("HOORAY!");
    }

    public void paint(Graphics g) { // data has a filter applied at every line, one filter per line LINE,
        paintBackground(g);
        for (int i = 0; i < lineLength*379; i += lineLength) { // go through every scan line's first byte to see the filter
            switch(data[i]) { 
                case 0:
                System.out.println(i);
                    for (int j = 1; j <= lineLength; j++) { // todo fix how it grabs colors
                        g.setColor(new Color(util.toUInt8(data[4*j+i]), util.toUInt8(data[4*j+i+1]), 
                        util.toUInt8(data[4*j+i+2]), util.toUInt8(data[4*j+i+3])));
                        g.fillRect(j, i/lineLength, 1, 1);
                    } break;
                case 1: 
                case 2:
                case 3:
                case 4:
            }
        } 
    }

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
}
