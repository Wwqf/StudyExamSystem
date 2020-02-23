package model;

import entity.Button;
import enums.ButtonState;
import enums.ButtonType;
import view.ExaminationScreen;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * UI只有两种颜色, 做过的题目为 #82C256 没有做过的题目为 #E2E2E2
 * 用户点击时, 只能点击做过的, 而不能点击没做过的
 */

public class BtnUI extends BasicButtonUI implements MouseListener {

    private Color color = ButtonState.NONE.value();
    private long start;


    @Override
    public void paint(Graphics graphics, JComponent jComponent) {
        AbstractButton button = (AbstractButton) jComponent;
        ButtonModel model = button.getModel();
        if (button.isContentAreaFilled()) {
            int h = jComponent.getHeight(), w = jComponent.getWidth();
            Graphics2D g2d = (Graphics2D) graphics.create();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0, 0, w - 1, h - 1, 3, 3);
            g2d.clip(r2d);

            if (model.isEnabled()) {

                if (self.state() == ButtonState.NONE) {
                    color = ButtonState.NONE.value();
                } else if (self.state() == ButtonState.ERROR) {
                    color = ButtonState.ERROR.value();
                } else if (self.state() == ButtonState.CORRECT) {
                    color = ButtonState.CORRECT.value();
                } else if (self.state() == ButtonState.SELECT) {
                    color = ButtonState.SELECT.value();
                }

                if (model.isPressed()) {
                    // 控制触发一次
                    if (start + 50 < System.currentTimeMillis()) {
                        clickLogic();
                    }
                }
                g2d.setColor(color);
                g2d.fillRect(0, 0, w, h);
            }

            g2d.dispose();
        }
        start = System.currentTimeMillis();
        super.paint(graphics, jComponent);
    }

    private void clickLogic() {
        // 触发所有按钮状态
        if (btnId == -1) {
            color = ButtonState.CORRECT.value();
            JOptionPane.showConfirmDialog(examinationScreen, "提交成功!");
            // TODO 状态已修改为提交
        } else if (btnId == -2) {
            if (examinationScreen.currentQuesId < examinationScreen.dataSet.len() - 1) {

                // 选中状态
                Enumeration<AbstractButton> model = examinationScreen.optionButtonGroup.getElements();
                while (model.hasMoreElements()) {
                    AbstractButton modelButton = model.nextElement();
                    if (modelButton.isSelected()) {
                        if (isChoiceCorrect(modelButton)) {
                            examinationScreen.progressGroup.get(examinationScreen.currentQuesId).state(ButtonState.CORRECT);
                            examinationScreen.currentQuesId++;
                            examinationScreen.progressGroup.get(examinationScreen.currentQuesId).state(ButtonState.SELECT);
                            // 选择正确后, 重置选择状态
                            examinationScreen.optionButtonGroup.clearSelection();
                            // TODO 如果正确, 则上传当前状态, 获取姓名, 时间, 题目数量
                            upload();
                            // reset
                            examinationScreen.reset = true;

                        } else {
                            examinationScreen.progressGroup.get(examinationScreen.currentQuesId).state(ButtonState.ERROR);

                            if (--examinationScreen.currentQuesScore < 0) {
                                examinationScreen.currentQuesScore = 0;
                            }
                        }
                    }
                }
            }
        }

        // 重新绘制
        for (entity.Button btn : group) {
            if (btn.type() == ButtonType.CONTROL) continue;
            btn.get().repaint();
        }

        // 重新设置问题及选项
        examinationScreen.setQuestion(examinationScreen.quesLabel, examinationScreen.currentQuesId);
        examinationScreen.setOption(examinationScreen.currentQuesId);

    }

    int subject = 0;
    private void upload() {
        if (examinationScreen.agent == null) {
            System.out.println("ExaminationScreen agent is null.");
            return ;
        }
        SocketChannel sc = examinationScreen.agent.getClient();
        if (sc == null) return ;

        try {
            if (examinationScreen.time[1] <= 0) {
                examinationScreen.selfInformation.setScore(String.valueOf(5));
                System.out.println("time is 0");
            } else {
                examinationScreen.selfInformation.setScore(String.valueOf(examinationScreen.currentQuesScore));
                System.out.println("time is has");
            }

//            examinationScreen.selfInformation.setSubject(String.valueOf(++subject));
            System.out.println("will send message -> " + examinationScreen.selfInformation.getString());
            examinationScreen.agent.registerClientWriteEvent();
            examinationScreen.agent.writeMessage(examinationScreen.selfInformation.getString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean isChoiceCorrect(AbstractButton modelButton) {
        for (int l = 0; l < examinationScreen.optionGroup.size(); l++) {
            if (!examinationScreen.optionGroup.get(l).isVisible()) continue;
            // 判断是哪个选择
            if (modelButton.getText().equals(examinationScreen.optionGroup.get(l).getText())) {
                // 判断是否选择正确
                if (examinationScreen.dataSet.getAnswer(examinationScreen.currentQuesId).value() - 1 == l) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void paintButtonPressed(Graphics graphics, AbstractButton abstractButton) {
        super.paintButtonPressed(graphics, abstractButton);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }

    public void installUI(JComponent c) {
        super.installUI(c);
        c.addMouseListener(this);
    }

    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
        c.removeMouseListener(this);
    }

    @Override
    protected void installDefaults(AbstractButton abstractButton) {
        super.installDefaults(abstractButton);
        abstractButton.setBorderPainted(false);
        abstractButton.setFocusPainted(false);
        abstractButton.setPreferredSize(new Dimension(70, 22));
    }

    ArrayList<entity.Button> group;
    entity.Button self;
    int btnId;
    ExaminationScreen examinationScreen;


    public BtnUI(ArrayList<entity.Button> group, entity.Button self) {
        this.group = group;
        this.self = self;
    }

    public BtnUI(ArrayList<entity.Button> group, entity.Button self, ExaminationScreen examinationScreen) {
        this.group = group;
        this.self = self;
        this.examinationScreen = examinationScreen;
    }

    public BtnUI(ArrayList<entity.Button> group, entity.Button self, int btnId) {
        this.group = group;
        this.self = self;
        this.btnId = btnId;
    }

    public BtnUI(ArrayList<entity.Button> group, Button self, int btnId, ExaminationScreen examinationScreen) {
        this.group = group;
        this.self = self;
        this.btnId = btnId;
        this.examinationScreen = examinationScreen;
    }
}
