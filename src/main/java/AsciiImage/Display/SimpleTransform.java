package AsciiImage.Display;

import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class SimpleTransform {
    
    private Translate translate;
    private Scale scale;
    private Rotate rotate;

    public SimpleTransform() {
        this(new Translate(0, 0), new Scale(0, 0), new Rotate(0));
    }

    public SimpleTransform(Translate translate, Scale scale, Rotate rotate) {
        this.translate = translate;
        this.scale = scale;
        this.rotate = rotate;
    }

    public Translate getTranslate() {
        return translate;
    }

    public void setTranslate(Translate translate) {
        this.translate = translate;
    }

    public void resetTranslate() {
        translate = new Translate(0, 0);
    }

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }

    public void resetScale() {
        scale = new Scale(1, 1);
    }

    public Rotate getRotate() {
        return rotate;
    }

    public void setRotate(Rotate rotate) {
        this.rotate = rotate;
    }

    public void resetRotate() {
        rotate = new Rotate(0);
    }

    public void reset() {
        resetTranslate();
        resetScale();
        resetRotate();
    }
    
}
