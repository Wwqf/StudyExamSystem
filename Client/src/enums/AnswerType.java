package enums;

public enum AnswerType {
    A(1),
    B(2),
    C(3),
    D(4);

    int _val;
    AnswerType(int _val) {
        this._val = _val;
    }

    public int value() {
        return _val;
    }
}
