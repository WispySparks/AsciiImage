package AsciiImage.Display;

import javafx.scene.control.IndexRange;
import javafx.scene.control.TextField;

public class NumberField extends TextField {

    protected final static String numberRegex = "^[0-9]*$";

    @Override
    public void replaceSelection(String replacement) {
        if (replacement.matches(numberRegex)) {
            super.replaceSelection(replacement);
        }
    }

    @Override
    public void replaceText(IndexRange range, String text) {
        if (text.matches(numberRegex)) {
            super.replaceText(range, text);
        }
    }

    @Override
    public void replaceText(int start, int end, String text) {
        if (text.matches(numberRegex)) {
            super.replaceText(start, end, text);
        }
    }

    public int getValue() {
        return Integer.parseInt(getText());
    }
    
}
