package View;

import Habitat.Habitat;
import Controller.Controller;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class MyFrame extends JFrame implements KeyListener {
    Habitat habitat;
    Controller controller;
    MyPanel myField;
    JLabel timeLabel;
    int time;

    public MyFrame() {
        habitat = new Habitat(1, 2, 100, 10, this);
        myField = new MyPanel();
        controller = new Controller(myField, habitat, this);
        habitat.confifureController(controller);
        myField.configureController(controller);

        setTitle("Rabbits from -=КВАС=-");
        setPreferredSize(new Dimension(habitat.getWidth(), habitat.getHeight()));
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        myField.setPreferredSize(new Dimension(habitat.getWidth(), habitat.getHeight()));
        add(myField);
        addKeyListener(this);

        timeLabel = new JLabel(" ", SwingConstants.CENTER);
        add(timeLabel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(true);
    }

    public void updateTime(int time) {
        this.time = time;
        timeLabel.setText("Passed time: " + time / 60 + " minutes " + time % 60 + " seconds");
    }


    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_B:
                if (!controller.isBornProcessOn()) {
                    myField = new MyPanel();
                    myField.configureController(controller);
                    controller.startBornProcess();
                }
                break;
            case KeyEvent.VK_E:
                if (controller.isBornProcessOn()) {
                    controller.stopBornProcess();
                    showFinishDialog();
                    controller.refreshRabbitPopulation();
                }
                break;
            case KeyEvent.VK_T:
                timeLabel.setVisible(!timeLabel.isVisible());
                break;
        }

    }


    public void configureController(Controller controller) {
        this.controller = controller;
    }

    private void showFinishDialog() {
        JDialog dialog = new JDialog(this, "RESULTS", true);
        JPanel panel = new JPanel(new GridLayout(5, 1));

        JLabel messageLabel = createLabel("Born process is finished. Here are results: ",
                SwingConstants.CENTER,
                new Font("Times New Roman", Font.BOLD, 20),
                Color.BLACK);

        JLabel simpleRabbitsLabel = createLabel("Ordinary: " + controller.getRabbitSimple(),
                SwingConstants.CENTER,
                new Font("Times New Roman", Font.BOLD, 20),
                Color.BLACK);

        JLabel albinosRabbitsLabel = createLabel("Albinos: " + controller.getRabbitAlbinos(),
                SwingConstants.CENTER,
                new Font("Times New Roman", Font.BOLD, 20),
                Color.BLACK);

        JLabel allRabbitsCount = createLabel("All rabbits: " + controller.getAllRabbitsCount(),
                SwingConstants.CENTER,
                new Font("Times New Roman", Font.BOLD, 20),
                Color.BLACK);

        JLabel timeLabel = createLabel("Passed time: " + time / 60 + " minutes " + time % 60 + " seconds",
                SwingConstants.CENTER,
                new Font("Times New Roman", Font.BOLD, 20),
                Color.BLUE);

        panel.add(messageLabel);
        panel.add(simpleRabbitsLabel);
        panel.add(albinosRabbitsLabel);
        panel.add(allRabbitsCount);
        panel.add(timeLabel);
        dialog.add(panel);

        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setPreferredSize(new Dimension(400,400));
        dialog.setResizable(false);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JLabel createLabel(String text, int fontPosition, Font font, Color fontColor) {
        JLabel label = new JLabel(text, fontPosition);
        label.setFont(font);
        label.setForeground(fontColor);
        return label;
    }

    private final String startButtonName = "Start";

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
