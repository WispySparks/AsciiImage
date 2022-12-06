package AsciiImage.Util;

import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class TranslateScale {
    
    private Translate translate;
    private Scale scale;

    public TranslateScale() {
        this(new Translate(0, 0), new Scale(0, 0));
    }

    public TranslateScale(Translate translate, Scale scale) {
        this.translate = translate;
        this.scale = scale;
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

    public void reset() {
        resetTranslate();
        resetScale();
    }
    
}
