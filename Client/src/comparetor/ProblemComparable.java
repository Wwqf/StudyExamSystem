package comparetor;

import entity.SingleElection;

import java.util.Comparator;

public class ProblemComparable<T> implements Comparator<T> {

    @Override
    public int compare(T t, T t1) {
        SingleElection se1 = (SingleElection) t;
        SingleElection se2 = (SingleElection) t1;
        if (se1.getLevel().value() < se1.getLevel().value()) return 1;
        else return -1;
    }

}
