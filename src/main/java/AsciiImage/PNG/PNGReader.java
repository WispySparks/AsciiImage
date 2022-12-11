package AsciiImage.PNG;

import AsciiImage.PNG.PNG.ColorType;
import AsciiImage.Util.ArrayList2D;
import AsciiImage.Util.PNGUtil;

public class PNGReader {
    
    private PNG png;
    private byte[] data;
    private int lineLength; // amount of bytes in one scanline of the image
    private int bytesPerPixel; // how many bytes each one pixel is

    /** 
     * Read a PNG's data and create a 2D list of pixels for the image by decoding filters<p>
     * x the byte being filtered;
     * a the byte corresponding to x in the pixel immediately before the pixel containing x;
     * b the byte corresponding to x in the previous scanline;
     * c the byte corresponding to b in the pixel immediately before the pixel containing b. <p>
     * c, b, <p>
     * a, x
     * @param image the image to parse into a list of pixels
     * @return pixels of the image
     * @link https://www.w3.org/TR/2003/REC-PNG-20031110/#9Filter-types
     */
    public ArrayList2D<Pixel> parseImageData(PNG image) {
        ArrayList2D<Pixel> pixels = new ArrayList2D<>();
        setup(image);
        for (int i = 0; i < data.length; i += lineLength) { // go through every scan line
            int currentLine = i/lineLength;
            for (int j = 0; j<png.width(); j++) { // go through each byte in that line to form the pixels
                // Pixel X
                int xRed = PNGUtil.toUInt8(data[bytesPerPixel*j+i+1]);
                int xGreen = 0;
                if (png.colorType() != ColorType.GRAYSCALE && png.colorType() != ColorType.PALETTE) { // can only grab green if not grayscale or palette
                    xGreen = PNGUtil.toUInt8(data[bytesPerPixel*j+i+2]);
                }
                int xBlue = 0;
                if (png.colorType() == ColorType.RGB || png.colorType() == ColorType.RGB_ALPHA) { // if rgb or rgba grab blue
                    xBlue = PNGUtil.toUInt8(data[bytesPerPixel*j+i+3]);
                }
                int xAlpha = 0;
                if (png.colorType() == ColorType.RGB_ALPHA) { // if rgba grab alpha 
                    xAlpha = PNGUtil.toUInt8(data[bytesPerPixel*j+i+4]);
                } 
                // Pixel A
                int aRed = 0;
                int aGreen = 0;
                int aBlue = 0;
                int aAlpha = 0;
                // Pixel B
                int bRed = 0;
                int bGreen = 0;
                int bBlue = 0;
                int bAlpha = 0;
                // Pixel C
                int cRed = 0;
                int cGreen = 0;
                int cBlue = 0;
                int cAlpha = 0;

                if (j > 0) { // Find pixel A
                    Pixel prevPixel = pixels.get(currentLine, j-1);
                    aRed = prevPixel.red();
                    aGreen = prevPixel.green();
                    aBlue = prevPixel.blue();
                    aAlpha = prevPixel.alpha();
                }
                if (i > 0) { // Find pixel B
                    Pixel abovePixel = pixels.get(currentLine-1, j);
                    bRed = abovePixel.red();
                    bGreen = abovePixel.green();
                    bBlue = abovePixel.blue();
                    bAlpha = abovePixel.alpha();
                }
                if (i > 0 && j > 0) { // Find pixel C
                    Pixel diagonalPixel = pixels.get(currentLine-1, j-1);
                    cRed = diagonalPixel.red();
                    cGreen = diagonalPixel.green();
                    cBlue = diagonalPixel.blue();
                    cAlpha = diagonalPixel.alpha();
                }
                int[] pixelX = {xRed, xGreen, xBlue, xAlpha}; // RGBA values of pixel X
                int[] pixelA = {aRed, aGreen, aBlue, aAlpha}; // RGBA values of pixel A
                int[] pixelB = {bRed, bGreen, bBlue, bAlpha}; // RGBA values of pixel B
                int[] pixelC = {cRed, cGreen, cBlue, cAlpha}; // RGBA values of pixel C
                pixels.add(currentLine, decodeFilter(data[i], pixelX, pixelA, pixelB, pixelC, j, currentLine));                
            }
        }
        return pixels;
    }

    /**
     * Grab byte data from png and figure out the lineLength of the png
     */
    private void setup(PNG image) {
        this.png = image;
        data = png.imageData();
        bytesPerPixel = switch(png.colorType()) {
            case GRAYSCALE -> 1; // G
            case GRAYSCALE_ALPHA -> 2; // G, A
            case PALETTE -> 1; // Indexed Color
            case RGB -> 3; // R, G, B
            case RGB_ALPHA -> 4; // R, G, B, A
            default -> throw new IllegalArgumentException("Invalid Colortype");
        };
        lineLength = bytesPerPixel * png.width() + 1; // add one for the first byte that tells you the filter
    }

    private Pixel decodeFilter(int filter, int[] pixelX, int[] pixelA, int[] pixelB, int[] pixelC, int x, int y) {
        switch(filter) { // create pixels differently based on filter
            case 0: // Filter Method 0 - None
                return createPixel(pixelX[0], pixelX[1], pixelX[2], pixelX[3], x, y);
            case 1: // Filter Method 1 - Sub
                return createPixel((pixelX[0] + pixelA[0])%256, (pixelX[1] + pixelA[1])%256, (pixelX[2] + pixelA[2])%256, (pixelX[3] + pixelA[3])%256, x, y);
            case 2: // Filter Method 2 - Up
                return createPixel((pixelX[0] + pixelB[0])%256, (pixelX[1] + pixelB[1])%256, (pixelX[2] + pixelB[2])%256, (pixelX[3] + pixelB[3])%256, x, y);
            case 3: // Filter Method 3 - Average
                int R = (int) Math.floor((pixelA[0] + pixelB[0])/2) + pixelX[0];
                int G = (int) Math.floor((pixelA[1] + pixelB[1])/2) + pixelX[1];
                int B = (int) Math.floor((pixelA[2] + pixelB[2])/2) + pixelX[2];
                int A = (int) Math.floor((pixelA[3] + pixelB[3])/2) + pixelX[3];
                return createPixel(R%256, G%256, B%256, A%256, x, y);
            case 4: // Filter Method 4 - Paeth
                int pR = PaethPredictor(pixelA[0], pixelB[0], pixelC[0]) + pixelX[0];
                int pG = PaethPredictor(pixelA[1], pixelB[1], pixelC[1]) + pixelX[1];
                int pB = PaethPredictor(pixelA[2], pixelB[2], pixelC[2]) + pixelX[2];
                int pA = PaethPredictor(pixelA[3], pixelB[3], pixelC[3]) + pixelX[3];
                return createPixel(pR%256, pG%256, pB%256, pA%256, x, y);
            default: throw new IllegalArgumentException("Filter byte doesn't match a filter");
        }
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
        int indexedColor = red; // they represent different things based on the colortype/multiplier
        int gray = red;
        int grayAlpha = green;
        return switch(png.colorType()) {
            case GRAYSCALE -> new Pixel(gray, x, y);
            case GRAYSCALE_ALPHA -> new Pixel(gray, grayAlpha, x, y);
            case PALETTE -> new Pixel(0, 255, 0, 0, 0, indexedColor, x, y);
            case RGB -> new Pixel(red, green, blue, x, y);
            case RGB_ALPHA -> new Pixel(red, green, blue, alpha, x, y);
            default -> throw new IllegalArgumentException("Invalid Colortype");
        };
    }
}
