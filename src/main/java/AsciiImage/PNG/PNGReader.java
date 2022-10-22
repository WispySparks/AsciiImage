package main.java.AsciiImage.PNG;

import main.java.AsciiImage.Util.ArrayList2D;
import main.java.AsciiImage.Util.PNGUtil;

public class PNGReader { // todo read other color types than rgb alpha also gamma/color, looks like we can ignore bKGD
    
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

    public ArrayList2D<Pixel> parseImageData() {
        return switch(png.colorType()) {
            case GRAYSCALE -> parseRGBAData();
            case GRAYSCALE_ALPHA -> parseRGBAData();
            case PALETTE -> parseRGBAData();
            case RGB -> parseRGBAData();
            case RGB_ALPHA -> parseRGBAData();
        };
    }

    /** 
     * {@link} https://www.w3.org/TR/PNG/#9Filter-types
     */
    public ArrayList2D<Pixel> parseRGBAData() {
        ArrayList2D<Pixel> pixels = new ArrayList2D<>();
        for (int i = 0; i < data.length; i += lineLength) { // go through every scan line's first byte to see the filter
            int currentLine = i/lineLength;
            switch(data[i]) {
                case 0:
                    for (int j = 0; j < png.width(); j++) { // data has +1 offset for the first byte being filter byte
                        Pixel p = new Pixel(util.toUInt8(data[4*j+i+1]), util.toUInt8(data[4*j+i+2]), 
                        util.toUInt8(data[4*j+i+3]), util.toUInt8(data[4*j+i+4]), j, currentLine);
                        pixels.add(currentLine, p);
                    } break;
                case 1: 
                    for (int j = 0; j < png.width(); j++) { // data has +1 offset for the first byte being filter byte
                        int xR = util.toUInt8(data[4*j+i+1]);
                        int xG = util.toUInt8(data[4*j+i+2]);
                        int xB = util.toUInt8(data[4*j+i+3]);
                        int xA = util.toUInt8(data[4*j+i+4]);

                        int aR = 0;
                        int aG = 0;
                        int aB = 0;
                        int aA = 0;

                        if (j > 0) {
                            Pixel prevP = pixels.get(currentLine, j-1);
                            aR = prevP.R();
                            aG = prevP.G();
                            aB = prevP.B();
                            aA = prevP.A();
                        }
                        pixels.add(currentLine, new Pixel((xR + aR)%256, (xG + aG)%256, (xB + aB)%256, (xA + aA)%256, j, currentLine));
                    } break;
                case 2:
                    for (int j = 0; j < png.width(); j++) { // data has +1 offset for the first byte being filter byte
                        int xR = util.toUInt8(data[4*j+i+1]);
                        int xG = util.toUInt8(data[4*j+i+2]);
                        int xB = util.toUInt8(data[4*j+i+3]);
                        int xA = util.toUInt8(data[4*j+i+4]);

                        int bR = 0;
                        int bG = 0;
                        int bB = 0;
                        int bA = 0;

                        if (i > 0) {
                            Pixel aboveP = pixels.get(currentLine-1, j);
                            bR = aboveP.R();
                            bG = aboveP.G();
                            bB = aboveP.B();
                            bA = aboveP.A();
                        }
                        pixels.add(currentLine, new Pixel((xR + bR)%256, (xG + bG)%256, (xB + bB)%256, (xA + bA)%256, j, currentLine));
                    } break;
                case 3: //  Filt(x) + floor((Recon(a) + Recon(b)) / 2)
                    for (int j = 0; j < png.width(); j++) { // data has +1 offset for the first byte being filter byte
                        int xR = util.toUInt8(data[4*j+i+1]);
                        int xG = util.toUInt8(data[4*j+i+2]);
                        int xB = util.toUInt8(data[4*j+i+3]);
                        int xA = util.toUInt8(data[4*j+i+4]);

                        int aR = 0;
                        int aG = 0;
                        int aB = 0;
                        int aA = 0;

                        int bR = 0;
                        int bG = 0;
                        int bB = 0;
                        int bA = 0;

                        if (j > 0) {
                            Pixel prevP = pixels.get(currentLine, j-1);
                            aR = prevP.R();
                            aG = prevP.G();
                            aB = prevP.B();
                            aA = prevP.A();
                        }
                        if (i > 0) {
                            Pixel aboveP = pixels.get(currentLine-1, j);
                            bR = aboveP.R();
                            bG = aboveP.G();
                            bB = aboveP.B();
                            bA = aboveP.A();
                        }
                        int R = (int) Math.floor((aR + bR)/2) + xR;
                        int G = (int) Math.floor((aG + bG)/2) + xG;
                        int B = (int) Math.floor((aB + bB)/2) + xB;
                        int A = (int) Math.floor((aA + bA)/2) + xA;
                        pixels.add(currentLine, new Pixel(R%256, G%256, B%256, A%256, j, currentLine));
                    } break;
                case 4: // Filt(x) + PaethPredictor(Recon(a), Recon(b), Recon(c))
                    for (int j = 0; j < png.width(); j++) {
                        int xR = util.toUInt8(data[4*j+i+1]);
                        int xG = util.toUInt8(data[4*j+i+2]);
                        int xB = util.toUInt8(data[4*j+i+3]);
                        int xA = util.toUInt8(data[4*j+i+4]);

                        int aR = 0;
                        int aG = 0;
                        int aB = 0;
                        int aA = 0;

                        int bR = 0;
                        int bG = 0;
                        int bB = 0;
                        int bA = 0;

                        int cR = 0;
                        int cG = 0;
                        int cB = 0;
                        int cA = 0;

                        if (j > 0) {
                            Pixel prevP = pixels.get(currentLine, j-1);
                            aR = prevP.R();
                            aG = prevP.G();
                            aB = prevP.B();
                            aA = prevP.A();
                        }
                        if (i > 0) {
                            Pixel aboveP = pixels.get(currentLine-1, j);
                            bR = aboveP.R();
                            bG = aboveP.G();
                            bB = aboveP.B();
                            bA = aboveP.A();
                        }
                        if (i > 0 && j > 0) {
                            Pixel diagonalP = pixels.get(currentLine-1, j-1);
                            cR = diagonalP.R();
                            cG = diagonalP.G();
                            cB = diagonalP.B();
                            cA = diagonalP.A();
                        }
                        int R = PaethPredictor(aR, bR, cR) + xR;
                        int G = PaethPredictor(aG, bG, cG) + xG;
                        int B = PaethPredictor(aB, bB, cB) + xB;
                        int A = PaethPredictor(aA, bA, cA) + xA;
                        pixels.add(currentLine, new Pixel(R%256, G%256, B%256, A%256, j, currentLine));
                    } break;
            }
        }
        return pixels;
    }

    private int PaethPredictor(int a, int b, int c) {
        int p = a + b - c;
        int pa = Math.abs(p - a);
        int pb = Math.abs(p - b);
        int pc = Math.abs(p - c);
        int Pr;
        if (pa <= pb && pa <= pc) {
            Pr = a;
        }
        else if (pb <= pc) {
            Pr = b;
        }
        else {
            Pr = c;
        }
        return Pr;
    }

}
