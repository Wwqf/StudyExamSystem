package data;

import comparetor.RankComparable;

import java.util.ArrayList;
import java.util.Vector;

public class RankData {
    /*
     *  name&time~name&time
     */
    private static ArrayList<StuNewInfo> studentSet = new ArrayList<>();

    public static void set(String source) {
        studentSet.clear();
        String[] data = source.split("~");
        String[] name_time;
        for (String datum : data) {
            name_time = datum.split("&");
            StuNewInfo stu = new StuNewInfo(name_time[0], name_time[1]);
            stu.setName(name_time[0]);
            studentSet.add(stu);
        }

//        studentSet.sort(new RankComparable<>());

        for (StuNewInfo stu : studentSet) {
            System.out.println(stu.getName() + ", " + stu.getScore());
        }
    }

    public static Vector<String> getName() {
        if (studentSet.size() == 0) return null;
        Vector<String> result = new Vector<>();
        for (StuNewInfo studentInformation : studentSet) {
            result.add(studentInformation.getName());
        }
        return result;
    }

    public static Vector<String> getScore() {
        if (studentSet.size() == 0) return null;
        Vector<String> result = new Vector<>();
        for (StuNewInfo studentInformation : studentSet) {
            result.add(studentInformation.getScore());
        }
        return result;
    }


}
