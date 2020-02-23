package data;

import comparetor.RankComparable;

import java.util.ArrayList;
import java.util.Vector;

public class RankData_old {
    /*
     *  name&time~name&time
     */
    private static ArrayList<StudentInformation> studentSet = new ArrayList<>();

    public static void set(String source) {
        studentSet.clear();
//        studentSet.add(new StudentInformation("姓名", "时间", "题数"));
//        studentSet.add(new StudentInformation("", "", ""));
        String[] data = source.split("~");
        String[] name_time;
        for (String datum : data) {
            name_time = datum.split("&");
            StudentInformation stu = new StudentInformation();
            stu.setName(name_time[0]);
            stu.setTime(name_time[1]);
            stu.setSubject(name_time[2]);
            studentSet.add(stu);
        }

        studentSet.sort(new RankComparable<>());

        for (StudentInformation stu : studentSet) {
            System.out.println(stu.getName() + ", " + stu.getTime() + ", " + stu.getSubject());
        }
    }

    public static Vector<String> getName() {
        if (studentSet.size() == 0) return null;
        Vector<String> result = new Vector<>();
        for (StudentInformation studentInformation : studentSet) {
            result.add(studentInformation.getName());
        }
        return result;
    }

    public static Vector<String> getTime() {
        if (studentSet.size() == 0) return null;
        Vector<String> result = new Vector<>();
        for (StudentInformation studentInformation : studentSet) {
            result.add(studentInformation.getTime());
        }
        return result;
    }

    public static Vector<String> getSubject() {
        if (studentSet.size() == 0) return null;
        Vector<String> result = new Vector<>();
        for (StudentInformation studentInformation : studentSet) {
            result.add(studentInformation.getSubject());
        }
        return result;
    }

}
