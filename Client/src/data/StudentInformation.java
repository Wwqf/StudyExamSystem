package data;

import java.util.ArrayList;

public class StudentInformation {
    private String name;
    private String time;
    private String subject;

    public StudentInformation() { }

    public StudentInformation(String name, String time, String subject) {
        this.name = name;
        this.time = time;
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getTimeInt() {
        return Integer.parseInt(time);
    }

    public int getSubjectInt() {
        return Integer.parseInt(subject);
    }

    public void inc() {
        int res = getSubjectInt();
        subject = (res + 1) + "";
    }

    public String getString() {
        return name + "&" + time + "&" + subject;
    }
}
