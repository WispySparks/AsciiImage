package AsciiImage.PNG;

import AsciiImage.PNG.PNG.ColorType;
import AsciiImage.Util.ArrayList2D;
import AsciiImage.Util.PNGUtil;

public class PNGReader {
    
    private PNG png;
    private byte[] data;
    private int lineLength; // amount of bytes in one scanline of the image
    private int multiplier; // how many bytes each one pixel is

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
            default -> throw new IllegalArgumentException("Invalid Colortype");
        };
        lineLength = bytesPerPixel * png.width() + 1; // add one for the first byte that tells you the filter
        multiplier = switch (png.colorType()) {
            case GRAYSCALE -> 1;
            case GRAYSCALE_ALPHA -> 2;
            case PALETTE -> 1;
            case RGB -> 3;
            case RGB_ALPHA -> 4;
            default -> throw new IllegalArgumentException("Invalid Colortype");
        };
    }

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
        for (int i = 0; i < data.length; i += lineLength) { // go through every scan line's first byte to see the filter
            int currentLine = i/lineLength;
            for (int j = 0; j<png.width(); j++) { // go through each byte in that line to form the pixels
                // Pixel X
                int xR = PNGUtil.toUInt8(data[multiplier*j+i+1]);
                int xG = PNGUtil.toUInt8(data[multiplier*j+i+2]);
                int xB = PNGUtil.toUInt8(data[multiplier*j+i+3]);
                int xA = 0;
                if (png.colorType() == ColorType.GRAYSCALE_ALPHA || png.colorType() == ColorType.RGB_ALPHA) { // If alpha colortype grab alpha byte
                    xA = PNGUtil.toUInt8(data[multiplier*j+i+4]);
                } 
                // Pixel A
                int aR = 0;
                int aG = 0;
                int aB = 0;
                int aA = 0;
                // Pixel B
                int bR = 0;
                int bG = 0;
                int bB = 0;
                int bA = 0;
                // Pixel C
                int cR = 0;
                int cG = 0;
                int cB = 0;
                int cA = 0;

                if (j > 0) { // Find pixel A
                    Pixel prevPixel = pixels.get(currentLine, j-1);
                    aR = prevPixel.red();
                    aG = prevPixel.green();
                    aB = prevPixel.blue();
                    aA = prevPixel.alpha();
                }
                if (i > 0) { // Find pixel B
                    Pixel abovePixel = pixels.get(currentLine-1, j);
                    bR = abovePixel.red();
                    bG = abovePixel.green();
                    bB = abovePixel.blue();
                    bA = abovePixel.alpha();
                }
                if (i > 0 && j > 0) { // Find pixel C
                    Pixel diagonalPixel = pixels.get(currentLine-1, j-1);
                    cR = diagonalPixel.red();
                    cG = diagonalPixel.green();
                    cB = diagonalPixel.blue();
                    cA = diagonalPixel.alpha();
                }
                int[] pixelX = {xR, xG, xB, xA}; // RGBA values of pixel X
                int[] pixelA = {aR, aG, aB, aA}; // RGBA values of pixel A
                int[] pixelB = {bR, bG, bB, bA}; // RGBA values of pixel B
                int[] pixelC = {cR, cG, cB, cA}; // RGBA values of pixel C
                pixels.add(currentLine, decodeFilter(data[i], pixelX, pixelA, pixelB, pixelC, j, currentLine));                
            }
        }
        return pixels;
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
        int indexedColor = red;
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
