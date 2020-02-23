package entity;

import enums.AnswerType;
import enums.LevelType;

import java.util.ArrayList;

public class SingleElection {
    private String problem;
    private ArrayList<String> option;
    private AnswerType answerType;
    private LevelType level;

    public SingleElection(String problem, ArrayList<String> option) {
        this.problem = problem;
        this.option = option;
    }

    public SingleElection(String problem, ArrayList<String> option, AnswerType answerType) {
        this.problem = problem;
        this.option = option;
        this.answerType = answerType;
    }


    public SingleElection(String problem, ArrayList<String> option, AnswerType answerType, LevelType level) {
        this.problem = problem;
        this.option = option;
        this.answerType = answerType;
        this.level = level;
    }

    public String getProblem() {
        return problem;
    }

    public ArrayList<String> getOption() {
        return option;
    }

    public String getSingleOption(int opId) {
        return option.get(opId);
    }

    public AnswerType getAnswer() {
        return answerType;
    }

    public LevelType getLevel() {
        return level;
    }
}
