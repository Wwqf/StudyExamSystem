package data;

public class StuNewInfo {
    private String name;
    private String score;

    public StuNewInfo() { }

    public StuNewInfo(String name, String score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }


    public int getScoreInt() {
        return Integer.parseInt(score);
    }


    public String getString() {
        return name + "&" + score;
    }
}
