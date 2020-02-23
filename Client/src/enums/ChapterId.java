package enums;

public enum ChapterId {
    CHAPTER_1("人工智能绪论"),
    CHAPTER_2("人工智能Python基础"),
    CHAPTER_3("人工智能Python进阶"),
    CHAPTER_4("人工智能之商业智能"),
    CHAPTER_5("人工智能之百度AI库应用"),
    CHAPTER_6( "人工智能之机器学习");

    String _val;
    ChapterId(String _val) {
        this._val = _val;
    }

    public String value() {
        return _val;
    }
}
