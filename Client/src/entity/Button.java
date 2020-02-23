package entity;

import model.BtnUI;
import enums.ButtonState;
import enums.ButtonType;

import javax.swing.*;
import java.awt.*;

public class Button {

    private JButton button;
    private Point location;
    private int width, height;
    private final ButtonType _type;
    private ButtonState _state;

    public Button(ButtonType type, ButtonState state, int width, int height, Point location) {
        this._type = type;
        this._state = state;
        this.width = width;
        this.height = height;
        this.location = location;
        button = new JButton();
        button.setLocation(location.x, location.y);
        button.setSize(width, height);
    }

    public JButton get() {
        return button;
    }

    public ButtonType type() {
        return _type;
    }

    public ButtonState state() {
        return _state;
    }

    public void state(ButtonState _state) {
        this._state = _state;
    }

    public void setUI(BtnUI btnUI) {
        button.setUI(btnUI);
    }
}
