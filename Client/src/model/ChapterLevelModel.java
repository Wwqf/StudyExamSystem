package model;

import javax.swing.*;

public class ChapterLevelModel extends DefaultComboBoxModel {
    public ChapterLevelModel(String[] source) {
        for (String s : source) {
            addElement(s);
        }
    }
}
