package AsciiImage.PNG;

import AsciiImage.PNG.PNG.ColorType;
import AsciiImage.Util.ArrayList2D;
import AsciiImage.Util.PNGUtil;

public class PNGReader {
    
    private PNG png;
    private byte[] data;
    private int lineLength; // amount of bytes in one scanline of the image

    /**
     * Grab byte data from png and figure out the lineLength of the png
     */
    private void setup(PNG image) {
        this.png = image;
        data = png.imageData();
        int bytesPerPixel = switch(png.colorType()) {
            case GRAYSCALE -> 1; // G
            case GRAYSCALE_ALPHA -> 2; // G, A
            case PALETTE -> 1; // Indexed Color
            case RGB -> 3; // R, G, B
            case RGB_ALPHA -> 4; // R, G, B, A
        };
        lineLength = bytesPerPixel * png.width() + 1; // add one for the first byte that tells you the filter
    }

    /** 
     * x the byte being filtered;
     * a the byte corresponding to x in the pixel immediately before the pixel containing x;
     * b the byte corresponding to x in the previous scanline;
     * c the byte corresponding to b in the pixel immediately before the pixel containing b.
     * c, b,
     * a, x
     * @param image the image to parse into a list of pixels
     * @return pixels of the image
     * @link https://www.w3.org/TR/2003/REC-PNG-20031110/#9Filter-types
     */
    public ArrayList2D<Pixel> parseImageData(PNG image) {
        setup(image);
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
            for (int j = 0; j<png.width(); j++) { // go through each byte in that line to form the pixels
                int xR = PNGUtil.toUInt8(data[multiplier*j+i+1]); // Find pixel/byte x
                int xG = PNGUtil.toUInt8(data[multiplier*j+i+2]);
                int xB = PNGUtil.toUInt8(data[multiplier*j+i+3]);
                int xA = 0;
                if (png.colorType() == ColorType.GRAYSCALE_ALPHA || png.colorType() == ColorType.RGB_ALPHA) { // If alpha colortype grab alpha byte
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

                if (j > 0) { // Find pixel/byte a
                    Pixel prevP = pixels.get(currentLine, j-1);
                    aR = prevP.red();
                    aG = prevP.green();
                    aB = prevP.blue();
                    aA = prevP.alpha();
                }
                if (i > 0) { // Find pixel/byte b
                    Pixel aboveP = pixels.get(currentLine-1, j);
                    bR = aboveP.red();
                    bG = aboveP.green();
                    bB = aboveP.blue();
                    bA = aboveP.alpha();
                }
                if (i > 0 && j > 0) { // Find pixel/byte c
                    Pixel diagonalP = pixels.get(currentLine-1, j-1);
                    cR = diagonalP.red();
                    cG = diagonalP.green();
                    cB = diagonalP.blue();
                    cA = diagonalP.alpha();
                }
                switch(data[i]) { // Create pixels differently based on filter
                    case 0: // Filter Method 0 - None
                        pixels.add(currentLine, createPixel(xR, xG, xB, xA, j, currentLine));
                        break;
                    case 1: // Filter Method 1 - Sub
                        pixels.add(currentLine, createPixel((xR + aR)%256, (xG + aG)%256, (xB + aB)%256, (xA + aA)%256, j, currentLine));
                        break;
                    case 2: // Filter Method 2 - Up
                        pixels.add(currentLine, createPixel((xR + bR)%256, (xG + bG)%256, (xB + bB)%256, (xA + bA)%256, j, currentLine));
                        break;
                    case 3: // Filter Method 3 - Average
                        int R = (int) Math.floor((aR + bR)/2) + xR;
                        int G = (int) Math.floor((aG + bG)/2) + xG;
                        int B = (int) Math.floor((aB + bB)/2) + xB;
                        int A = (int) Math.floor((aA + bA)/2) + xA;
                        pixels.add(currentLine, createPixel(R%256, G%256, B%256, A%256, j, currentLine));
                        break;
                    case 4: // Filter Method 4 - Paeth
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

    /**
     * @param a the byte corresponding to x in the pixel immediately before the pixel containing x (or the byte immediately before x, when the bit depth is less than 8);
     * @param b the byte corresponding to x in the previous scanline;
     * @param c the byte corresponding to b in the pixel immediately before the pixel containing b (or the byte immediately before b, when the bit depth is less than 8).
     * @return the byte being filtered;
     * @link https://www.w3.org/TR/2003/REC-PNG-20031110/#9Filter-type-4-Paeth
     */
    private int PaethPredictor(int a, int b, int c) { // Paeth Function to find a pixel using 3 adjacent pixels
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
