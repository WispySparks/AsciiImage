package main.java.AsciiImage.PNG;

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
        this(gray, 255, 0, 0, 0, 0, X, Y);
    }

    Pixel(int gray, int alpha, int X, int Y) {
        this(gray, alpha, 0, 0, 0, 0, X, Y);
    }
    
    Pixel(int R, int G, int B, int X, int Y) {
        this(0, 255, R, G, B, 0, X, Y);
    }

    Pixel(int R, int G, int B, int A, int X, int Y) {
        this(0, A, R, G, B, 0, X, Y);
    }

}
