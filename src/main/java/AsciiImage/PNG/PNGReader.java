package main.java.AsciiImage.PNG;

import main.java.AsciiImage.PNG.PNG.ColorType;
import main.java.AsciiImage.Util.ArrayList2D;
import main.java.AsciiImage.Util.PNGUtil;

public class PNGReader {
    
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
        // if (data.length / lineLength == png.height()) System.out.println("HOORAY!"); // something is lining up here
    }

    /** 
     * {@link} https://www.w3.org/TR/PNG/#9Filter-types
     */
    public ArrayList2D<Pixel> parseImageData() {
        ArrayList2D<Pixel> pixels = new ArrayList2D<>();
        int multiplier = switch (png.colorType()) {
            case GRAYSCALE -> 1;
            case GRAYSCALE_ALPHA -> 2;
            case PALETTE -> 1;
            case RGB -> 3;
            case RGB_ALPHA -> 4;
        };
        for (int i = 0; i < data.length; i += lineLength) { // go through every scan line's first byte to see the filter
            int currentLine = i/lineLength;
            for (int j = 0; j<png.width(); j++) {
                int xR = PNGUtil.toUInt8(data[multiplier*j+i+1]);
                int xG = PNGUtil.toUInt8(data[multiplier*j+i+2]);
                int xB = PNGUtil.toUInt8(data[multiplier*j+i+3]);
                int xA = 0;
                if (png.colorType() == ColorType.GRAYSCALE_ALPHA || png.colorType() == ColorType.RGB_ALPHA) {
                    xA = PNGUtil.toUInt8(data[multiplier*j+i+4]);
                } 
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
                switch(data[i]) {
                    case 0:
                        pixels.add(currentLine, createPixel(xR, xG, xB, xA, j, currentLine));
                        break;
                    case 1: 
                        pixels.add(currentLine, createPixel((xR + aR)%256, (xG + aG)%256, (xB + aB)%256, (xA + aA)%256, j, currentLine));
                        break;
                    case 2:
                        pixels.add(currentLine, createPixel((xR + bR)%256, (xG + bG)%256, (xB + bB)%256, (xA + bA)%256, j, currentLine));
                        break;
                    case 3: //  Filt(x) + floor((Recon(a) + Recon(b)) / 2)
                        int R = (int) Math.floor((aR + bR)/2) + xR;
                        int G = (int) Math.floor((aG + bG)/2) + xG;
                        int B = (int) Math.floor((aB + bB)/2) + xB;
                        int A = (int) Math.floor((aA + bA)/2) + xA;
                        pixels.add(currentLine, createPixel(R%256, G%256, B%256, A%256, j, currentLine));
                        break;
                    case 4: // Filt(x) + PaethPredictor(Recon(a), Recon(b), Recon(c))
                        int pR = PaethPredictor(aR, bR, cR) + xR;
                        int pG = PaethPredictor(aG, bG, cG) + xG;
                        int pB = PaethPredictor(aB, bB, cB) + xB;
                        int pA = PaethPredictor(aA, bA, cA) + xA;
                        pixels.add(currentLine, createPixel(pR%256, pG%256, pB%256, pA%256, j, currentLine));
                        break;
                }
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

    private Pixel createPixel(int red, int green, int blue, int alpha, int x, int y) {
        int indexedColor = red;
        int gray = red;
        int grayAlpha = green;
        return switch(png.colorType()) {
            case GRAYSCALE -> new Pixel(gray, x, y);
            case GRAYSCALE_ALPHA -> new Pixel(gray, grayAlpha, x, y);
            case PALETTE -> new Pixel(0, 255, 0, 0, 0, indexedColor, x, y);
            case RGB -> new Pixel(red, green, blue, x, y);
            case RGB_ALPHA -> new Pixel(red, green, blue, alpha, x, y);
        };
    }
}
