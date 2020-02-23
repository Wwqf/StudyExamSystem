package enums;

import java.awt.*;

public enum ButtonState {

    NONE("#E2E2E2"),
    ERROR("#FF0000"),
    CORRECT("#82C256"),
    SELECT("#0FAF0");

    private String _val;
    ButtonState(String _val) {
        this._val =  _val;
    }

    public Color value() {
        return Color.decode(_val);
    }
}


