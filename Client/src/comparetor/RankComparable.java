package comparetor;

import data.StuNewInfo;
import data.StudentInformation;

import java.util.Comparator;


public class RankComparable<T> implements Comparator<T> {

    @Override
    public int compare(T t, T t1) {
        StuNewInfo stu01 = (StuNewInfo) t;
        StuNewInfo stu02 = (StuNewInfo) t1;
        if (stu01.getScoreInt() > stu02.getScoreInt()) return 1;
        return -1;
    }

}
