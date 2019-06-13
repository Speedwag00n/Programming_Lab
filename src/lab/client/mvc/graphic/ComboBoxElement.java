package lab.client.mvc.graphic;

import javafx.beans.NamedArg;

public class ComboBoxElement {

    private String value;
    private String text;

    public ComboBoxElement(@NamedArg("value") String value, @NamedArg("text") String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return text;
    }
}
