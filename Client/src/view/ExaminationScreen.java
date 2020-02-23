package view;

import comparetor.ProblemComparable;
import data.*;
import entity.Button;
import enums.ButtonState;
import enums.ButtonType;
import enums.LevelType;
import impl.ReadCallback;
import model.BtnUI;
import model.ChapterLevelModel;
import network.ClientAgent;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class ExaminationScreen extends JFrame {

    /* 控件坐标 */
    int top = 10;
    int bottom = 80;

    /* 屏幕控制 */
    public JFrame screen;
    int scrWidth = 1110;
    int scrHeight = 600;
    Point scrLocation = new Point(150, 50);

    /* 计时器控制 */
    public JLabel timer;
    int timerWidth = 150;
    int timerHeight = 55;
    int timerFontSize = 55;

    /* 用户名标签 */
    public JLabel usernameLabel;
    public static String username;
    int usernameLabelWidth = 300;
    int usernameLabelHeight = 35;
    Point usernameLabelLocation = new Point(10, 2);
    int usernameLabelFontSize = 24;

    /* 章节选择, 难度选择 */
    /* 如果简单难度(级别1)没有通过, 则不能选择第二个难度或第三个难度 */
    public JLabel chapterLabel, levelLabel;
    public JComboBox chapterComboBox, levelComboBox;
    public ChapterSet chapterSet;

    int chapterLevelWidth = 50;
    int chapterLevelHeight = 20;
    int chapterLevelFontSize = 14;
    int chapterLevelComboWidth = 180;
    int chapterLevelComboHeight = 20;
    Point chapterLabelLocation = new Point(usernameLabelLocation.x, usernameLabelLocation.y + usernameLabelHeight + 3);
    Point levelLabelLocation = new Point(chapterLabelLocation.x, chapterLabelLocation.y + chapterLevelHeight + 10);
    Point chapterComboLocation = new Point(chapterLabelLocation.x + chapterLevelWidth, chapterLabelLocation.y + 4);
    Point levelComboLocation = new Point(levelLabelLocation.x + chapterLevelWidth, levelLabelLocation.y + 1);

    /* 排名显示控制 */
    public JList<String> nameRank, timeRank, subjectRank;
    int nameRankWidth = 90;
    int nameRankHeight = scrHeight - bottom - 5 - 30;
    int timeRankWidth = 80;
    int timeRankHeight = scrHeight - bottom - 5 - 30;
    int subjectRankWidth = 60;
    int subjectRankHeight = scrHeight - bottom - 5 - 30;
    Point nameRankLocation = new Point();
    Point timeRankLocation = new Point();
    Point subjectRankLocation = new Point();
    int rankListFontSize = 20;

    int rankY = 80;

    public JLabel rankLabel;

    /* 进度控制 */
    public ProblemDataSet dataSet = new ProblemDataSet(new ProblemComparable<>());
    public int questionCount;
    public ArrayList<Button> progressGroup = new ArrayList<>();

    /* 问题相关控件 */
    public JLabel quesLabel;
    int quesLabelWidth = 560;
    int quesLabelHeight = 180;
    int quesLabelFontSize = 22;
    Point quesLabelLocation = new Point(310, 0);

    public int currentQuesId = 0;
    public int currentChapterId = 5;


    /* 选项相关控件 */
    public ArrayList<JRadioButton> optionGroup = new ArrayList<>();
    public ButtonGroup optionButtonGroup = new ButtonGroup();
    int singleOptionWidth = 500;
    int singleOptionHeight = 30;
    int optionFontSize = 18;

    /* 更新排名 */
    public ClientAgent agent;


    /* 个人信息 */
    public StuNewInfo selfInformation;

    public ExaminationScreen() {
        screen = createJFrame();

        /* 添加用户名 */
        usernameLabel = new JLabel();
        setUsernameLabelParameter(usernameLabel, username);
        screen.add(usernameLabel);
        selfInformation = new StuNewInfo();
        username = String.valueOf(new Random().nextInt(1000));
        selfInformation.setName(username);

        /* 添加章节, 难度选择 */
        chapterLabel = new JLabel();
        levelLabel = new JLabel();
        setChapterLevelLabel(chapterLabel, levelLabel);
        screen.add(chapterLabel);
        screen.add(levelLabel);

        chapterComboBox = new JComboBox();
        levelComboBox = new JComboBox();
        String[] ccSource = new String[]{
                "人工智能之百度AI库应用",
                "人工智能绪论",
                "人工智能Python基础",
                "人工智能Python进阶",
                "人工智能之商业智能",
                "人工智能之机器学习"
        };
        String[] lcSource = new String[]{
                "人脸对比",
                "人脸检测",
                "人脸搜索"
        };
        chapterSet = new ChapterSet();
        setChapterLevelComboBox(chapterComboBox, ccSource, levelComboBox, lcSource);
        screen.add(chapterComboBox);
        screen.add(levelComboBox);

        /* 添加计时器控件 */
        timer = new JLabel();
        setTimerParameter(timer);
        screen.add(timer);
        listenTimer(timer);

        /* 排名控件 */
        nameRank = new JList<>();
        timeRank = new JList<>();
        subjectRank = new JList<>();
        setRankDisplay();
        screen.add(nameRank);
        screen.add(timeRank);
        screen.add(subjectRank);
        listenRank(nameRank, timeRank, subjectRank);

        /* 进度控制 */
        questionCount = dataSet.len();
        createProgressGroup(questionCount);
        for (Button item : progressGroup) {
            screen.add(item.get());
        }

        /* 设置问题参数 */
        quesLabel = new JLabel();
        setQuestionParameter(quesLabel);
        setQuestion(quesLabel, currentQuesId);
        screen.add(quesLabel);

        /* 设置选项参数 */
        optionGroup.add(new JRadioButton());
        optionGroup.add(new JRadioButton());
        optionGroup.add(new JRadioButton());
        optionGroup.add(new JRadioButton());
        setOptionParameter();
        setOption(currentQuesId);
        for (JRadioButton radioButton : optionGroup) {
            screen.add(radioButton);
        }

        /* 更新排名 */
        new Thread() {
            @Override
            public void run() {
                updateRank();
            }
        }.start();

        setJFrameParameter(screen);
    }

    private void updateRank() {
        System.out.println("初始化 agent.");
        try {
            agent = new ClientAgent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        agent.registerReadCallback(new ReadCallback() {
            @Override
            public void message(String msg) {
                System.out.println("rec -> " + msg);
                RankData.set(msg);
            }
        });
    }

    /* 创建窗体 */
    private JFrame createJFrame() {
        return new JFrame();
    }

    /* 设置界面参数 */
    private void setJFrameParameter(JFrame screen) {
        screen.setSize(scrWidth, scrHeight);
        screen.setLocation(scrLocation.x, scrLocation.y);
        screen.setLayout(null);
        screen.setResizable(false);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.setVisible(true);
    }

    /* 设置显示用户名参数 */
    private void setUsernameLabelParameter(JLabel usernameLabel, String username) {
        usernameLabel.setText("组名: " + username);
        Font userFont = new Font("楷体", Font.BOLD, usernameLabelFontSize);
        usernameLabel.setFont(userFont);
        usernameLabel.setSize(usernameLabelWidth, usernameLabelHeight);
        usernameLabel.setLocation(usernameLabelLocation.x, usernameLabelLocation.y);
    }

    private void setChapterLevelLabel(JLabel chapterLabel, JLabel levelLabel) {
        Font font = new Font("楷体", Font.BOLD, chapterLevelFontSize);

        chapterLabel.setText("章节: ");
        chapterLabel.setFont(font);
        chapterLabel.setSize(chapterLevelWidth, chapterLevelHeight);
        chapterLabel.setLocation(chapterLabelLocation.x, chapterLabelLocation.y);

        levelLabel.setText("选节: ");
        levelLabel.setFont(font);
        levelLabel.setSize(chapterLevelWidth, chapterLevelHeight);
        levelLabel.setLocation(levelLabelLocation.x, levelLabelLocation.y);
    }

    private void setChapterLevelComboBox(JComboBox chapterComboBox, String[] chapterSource, JComboBox levelComboBox, String[] levelSource) {
        chapterComboBox.setModel(new ChapterLevelModel(chapterSource));
        levelComboBox.setModel(new ChapterLevelModel(levelSource));
        chapterComboBox.setSize(chapterLevelComboWidth, chapterLevelComboHeight);
        chapterComboBox.setLocation(chapterComboLocation.x, chapterLabelLocation.y);


        levelComboBox.setSize(chapterLevelComboWidth, chapterLevelComboHeight);
        levelComboBox.setLocation(levelComboLocation.x, levelComboLocation.y);
    }

    /* 设置计时器参数 */
    private void setTimerParameter(JLabel timer) {
        Font timeFont = new Font("楷体", Font.BOLD, timerFontSize);
        timer.setFont(timeFont);
        timer.setSize(timerWidth, timerHeight);
        timer.setLocation(scrWidth - 205 + 30, 1);
        setTimeText(timer, 0);
    }

    /* 设置计时器显示 */
    private void setTimeText(JLabel timer, long second) {
        long min = second / 60;
        long sec = second % 60;

        String ms = "";
        if (min < 10) ms = "0" + min;
        else ms = String.valueOf(min);

        String ss = "";
        if (sec < 10) ss = "0" + sec;
        else ss = String.valueOf(sec);

        timer.setText(ms + ":" + ss);
    }

    /* 计时器更新 */
    /**
     * 规则 每题最高分十分, 难度级别为1的题目时间有15秒, 级别2的时间有25秒, 级别3的时间有35秒
     * 在时间耗尽的情况下, 如果答对则给3分, 如果答错只有0分(只有一次机会)
     * 在时间还剩当前难度题目时间的三分之一时, 开始减分, 每过剩下时间的十分之一, 减一分
     */
    // 总时间和每题时间
    public final long[] time = {0, 0};
    // 选择正确后, reset设置为true, 则重新计时
    public boolean reset = true;
    public int score = 0;
    public int currentQuesScore = 10;
    public final long[] levelTime = {10, 15, 18};
    public long currentMaxTime = 0;
    private void listenTimer(final JLabel timer) {
        new Thread(() -> {
            while (true) {

                if (reset) {
                    if (dataSet.getLevel(currentQuesId) == LevelType.LEVEL_1) {
                        time[1] = levelTime[0];
                    } else if (dataSet.getLevel(currentQuesId) == LevelType.LEVEL_2) {
                        time[1] = levelTime[1];
                    } else if (dataSet.getLevel(currentQuesId) == LevelType.LEVEL_3) {
                        time[1] = levelTime[2];
                    }
                    currentQuesScore = 10;
                    currentMaxTime = time[1];
                    reset = false;
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (time[1] < (currentMaxTime / 2)) {
                        if (--currentQuesScore < 0) {
                            currentQuesScore = 0;
                        }
                    }
                    if (time[1] < 0) {
                        time[1] = 0;
                        currentQuesScore = 0;
                    }
                    setTimeText(timer, --time[1] < 0 ? 0 : time[1]);
                    System.out.println("当前分数: " + currentQuesScore);
                }


            }
        }).start();
    }

    /* 设置排名参数 */
    private void setRankDisplay() {
        nameRank.setBackground(new Color(0xEEEEEE));
        timeRank.setBackground(new Color(0xEEEEEE));
        subjectRank.setBackground(new Color(0xEEEEEE));

//        subjectRankLocation.setLocation(scrWidth - subjectRankWidth, rankY + 60);
        timeRankLocation.setLocation(scrWidth - timeRankWidth, rankY + 60);
        nameRankLocation.setLocation(timeRankLocation.x - nameRankWidth, rankY + 60);
        nameRank.setSize(nameRankWidth, nameRankHeight);
        timeRank.setSize(timeRankWidth, timeRankHeight);
        subjectRank.setSize(subjectRankWidth, subjectRankHeight);


        /* TODO 排名字体的设置 */
        int start = nameRankLocation.x;
        int end = scrWidth;
        Font font = new Font("楷体", Font.BOLD, rankListFontSize);
        rankLabel = new JLabel("排名");
        rankLabel.setFont(font);
        rankLabel.setSize(50, 30);
        rankLabel.setLocation(start + (end - start - 50) / 2 - 5, rankY);
        screen.add(rankLabel);

        font = new Font("宋体", Font.BOLD, rankListFontSize - 4);
        JLabel nameLabel = new JLabel("姓名");
        nameLabel.setFont(font);
        nameLabel.setSize(50, 30);
        nameLabel.setLocation(rankLabel.getLocation().x - 55, rankY + 30);
        screen.add(nameLabel);

        JLabel scoreLabel = new JLabel("分数");
        scoreLabel.setFont(font);
        scoreLabel.setSize(50, 30);
        scoreLabel.setLocation(nameLabel.getLocation().x + 80, rankY + 30);
        screen.add(scoreLabel);

        nameRank.setLocation(nameRankLocation.x, nameRankLocation.y);
        timeRank.setLocation(timeRankLocation.x, timeRankLocation.y);
//        subjectRank.setLocation(subjectRankLocation.x, subjectRankLocation.y);

        nameRank.setFont(font);
        timeRank.setFont(font);
//        subjectRank.setFont(font);
    }

    /* 监听排名更新事件 */
    private void listenRank(final JList<String> name, final JList<String> time, final JList<String> subject) {
        new Thread(() -> {

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (RankData.getName() == null) continue;
                System.out.println("+------------ " + RankData.getName().size() + " ------------+");
                name.setListData(RankData.getName());
                time.setListData(RankData.getScore());
//                subject.setListData(RankData.getSubject());
            }

        }).start();
    }

    /* 创建进度控制按钮 */
    private void createProgressGroup(int len) {
        System.out.println("Progress count: " + len);
        /* 上一题按钮 和下一题按钮 */
        final int distance = 5;

        Font buttonFont = new Font("微软雅黑", Font.BOLD, 18);

        Point location = new Point(distance, scrHeight - bottom);
        int lqWidth = 200, ldHeight = 40;
        Button lastQues = new Button(ButtonType.CONTROL, ButtonState.NONE, lqWidth, ldHeight, location);
        lastQues.get().setText("提交");
        BtnUI btnUI = new BtnUI(progressGroup, lastQues, -1, this);
        lastQues.setUI(btnUI);
        lastQues.get().setFont(buttonFont);
        screen.add(lastQues.get());

        location.setLocation(distance, location.y - distance - 40);
        int nqWidth = 200, nqHeight = 40;
        Button nextQues = new Button(ButtonType.CONTROL, ButtonState.NONE, nqWidth, nqHeight, location);
        nextQues.get().setText("下一题");
        btnUI = new BtnUI(progressGroup, nextQues, -2, this);
        nextQues.setUI(btnUI);
        nextQues.get().setFont(buttonFont);
        screen.add(nextQues.get());

        int progressButtonStartX = location.x + (distance * 2) + 200;
        int progressButtonStartY = location.y + 45;

        int blockWidth = 45, blockHeight = 40;
        location.setLocation(progressButtonStartX, location.y);
        Button item = new Button(ButtonType.DISPLAY, ButtonState.SELECT, blockWidth, blockHeight, location);
        btnUI = new BtnUI(progressGroup, item, this);
        item.setUI(btnUI);
        progressGroup.add(item);
        progressButtonStartX -= 5;

        for (int l = 0; l < len - 1; l++) {
            System.out.println("x = " + location.x + ", w = " + scrWidth);
            if (location.x + 45 > (scrWidth - 205)) {
                // 换第二行
                location.setLocation(progressButtonStartX + distance, progressButtonStartY);
            } else {
                location.setLocation(location.x + blockWidth + distance, location.y);
            }
            item = new Button(ButtonType.DISPLAY, ButtonState.NONE, blockWidth, blockHeight, location);
            btnUI = new BtnUI(progressGroup, item, this);
            item.setUI(btnUI);
            progressGroup.add(item);
        }
    }

    /* 设置问题参数 */
    private void setQuestionParameter(JLabel quesLabel) {
        Font font = new Font("宋体", Font.BOLD, quesLabelFontSize);
        quesLabel.setFont(font);
        quesLabel.setForeground(Color.BLACK);
        quesLabel.setSize(quesLabelWidth, quesLabelHeight);
        quesLabel.setLocation(quesLabelLocation.x, quesLabelLocation.y);
    }

    static int lineCount = 1;

    /* 设置问题 */
    public int setQuestion(JLabel quesLabel, final int seId) {

        new Thread(() -> {
            try {
                // TODO 修改选项
                lineCount = labelSetText(quesLabel, (seId + 1) + ". " + dataSet.getProblem(seId));
                setOption(seId);

                quesLabelLocation.y = 40 + (lineCount * 5);

                setQuestionParameter(quesLabel);
                setOptionParameter();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        return lineCount;
    }



    /* JLabel换行 返回行数总数 */
    private int labelSetText(JLabel jLabel, String longString) throws InterruptedException {
        int line = 0;
        StringBuilder builder = new StringBuilder("<html>");

        longString = longString.replaceAll("\n", "<br/>");
        String[] lineData = longString.split("<br/>");
        line = lineData.length;
        System.out.println("line = " + line);

        FontMetrics fontMetrics = jLabel.getFontMetrics(jLabel.getFont());
        for (String src : lineData) {
            int start = 0, len = 0;
            char[] chars = src.toCharArray();
            while (start + len < src.length()) {
                while (true) {
                    len++;
                    if (start + len > src.length()) break;
                    if (fontMetrics.charsWidth(chars, start, len) > jLabel.getWidth()) break;
                }
                line++;
                builder.append(chars, start, len - 1).append("<br/>");
                start = start + len - 1;
                len = 0;
            }
            builder.append(chars, start, src.length() - start);
        }

        builder.append("</html>");
        jLabel.setText(builder.toString());
        return line;
    }

    private void setOptionParameter() {
        Font itemFont = new Font("宋体", Font.BOLD, optionFontSize);
        for (int l = 0; l < optionGroup.size(); l ++) {
            optionGroup.get(l).setVisible(true);
            optionGroup.get(l).setSelected(false);

            optionGroup.get(l).setFont(itemFont);
            optionGroup.get(l).setForeground(Color.BLACK);
            optionGroup.get(l).setSize(singleOptionWidth , singleOptionHeight);
            optionGroup.get(l).setLocation(quesLabelLocation.x, quesLabelLocation.y + quesLabelHeight + ((l + 1) * 30));
            optionButtonGroup.add(optionGroup.get(l));
        }
    }

    /* 设置选项 */
    public void setOption(int seId) {
        new Thread(() -> {
            int size = dataSet.getOptionSize(seId);

            for (int l = 0; l < size; l++) {
                optionGroup.get(l).setText(dataSet.getOption(seId, l));
            }

            for ( ; size < optionGroup.size(); size++) {
                optionGroup.get(size).setVisible(false);
            }
        }).start();
    }

}