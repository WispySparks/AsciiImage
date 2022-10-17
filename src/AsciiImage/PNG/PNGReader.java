package AsciiImage.PNG;

import java.util.ArrayList;
import java.util.List;

import AsciiImage.Util.PNGUtil;

public class PNGReader {
    
    private PNGUtil util = new PNGUtil();
    private PNG png;
    private byte[] data;
    private int lineLength;

    public PNGReader(PNG png) {
        if (png.isCorrupted()) return;
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
        if (data.length / lineLength == png.height()) System.out.println("HOORAY!"); // something is lining up here
    }

    public List<Pixel> parseImageData() {
        List<Pixel> pixels = new ArrayList<>();
        for (int i = 0; i < lineLength*(png.height()-4); i += lineLength) { // go through every scan line's first byte to see the filter
            switch(data[i]) { // todo ^ number isnt good
                case 0: 
                for (int j = 1; j <= lineLength; j++) { // todo why is alpha byte first ????
                    pixels.add(new Pixel(   util.toUInt8(data[4*j+i+1]), util.toUInt8(data[4*j+i+2]), 
                    util.toUInt8(data[4*j+i+3]), util.toUInt8(data[4*j+i]), 
                    j, i/lineLength));
                } break;
                case 1:
                case 2:
                case 3:
                case 4:
            }
        }
        return pixels;
    }

}
