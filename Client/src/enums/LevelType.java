package enums;

public enum LevelType {
    LEVEL_1(1),
    LEVEL_2(2),
    LEVEL_3(3),
    LEVEL_4(4);

    int _val;
    LevelType(int _val) {
        this._val = _val;
    }

    public int value() {
        return _val;
    }
}
