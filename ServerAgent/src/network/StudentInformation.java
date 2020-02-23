package network;

import java.util.Comparator;

public class StudentInformation {
    private String name;
    private String time;
    private String subject;

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
}



