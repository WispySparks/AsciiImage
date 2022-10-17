package AsciiImage.PNG;

public record Pixel(

    int gray, 
    int A,
    int R,
    int G,
    int B,
    int indexedColor,
    int X,
    int Y

) {

    Pixel(int gray, int X, int Y) {
        this(gray, -1, -1, -1, -1, -1, X, Y);
    }

    Pixel(int gray, int alpha, int X, int Y) {
        this(gray, alpha, -1, -1, -1, -1, X, Y);
    }
    
    Pixel(int R, int G, int B, int X, int Y) {
        this(-1, -1, R, G, B, -1, X, Y);
    }

    Pixel(int R, int G, int B, int A, int X, int Y) {
        this(-1, A, R, G, B, -1, X, Y);
    }

}
