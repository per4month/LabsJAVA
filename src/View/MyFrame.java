package View;

import Habitat.Habitat;
import Controller.Controller;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class MyFrame extends JFrame implements KeyListener {
    final private int N1 = 1;
    final private int N2 = 2;
    final private int P1 = 100;
    final private int K = 50;
    Habitat habitat;
    Controller controller;
    MyPanel myField;
    ControlPanel controlPanel;
    JLabel timeLabel;
    JMenuBar menuBar;
    int time;
    final private int controlPanelSize = 600;
    private JRadioButtonMenuItem timeOnRadioButton;
    private JRadioButtonMenuItem timeOffRadioButton;
    private JRadioButtonMenuItem showDialogRadioButton;

    private JTextField albinosGenPeriodTextField;
    private JTextField ordinaryGenPeriodTextField;

    private JMenuItem startItem;
    private JMenuItem stopItem;
    private JMenuItem quitItem;

    private ButtonGroup albinosButtonGroup;
    private ButtonGroup simpleButtonGroup;

    private JMenu simpleProbability;
    private JMenu albinosProbability;

    @Override
    public void keyTyped(KeyEvent keyEvent) { }

    @Override
    public void keyReleased(KeyEvent keyEvent) { }
    public MyFrame() {
        habitat = new Habitat(N1, N2, P1, K, this);
        myField = new MyPanel();
        controlPanel = new ControlPanel(N1, N2, P1, K);
        controller = new Controller(myField, habitat, this, controlPanel);
        habitat.confifureController(controller);
        myField.configureController(controller);
        controlPanel.configureController(controller);

        setTitle("Rabbits");
        setPreferredSize(new Dimension(habitat.getWidth() + controlPanelSize, habitat.getHeight()));
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();

        configureMenuBar();

        myField.setPreferredSize(new Dimension(habitat.getWidth(), habitat.getHeight()));
        add(myField);
        add(controlPanel, BorderLayout.EAST);

        timeLabel = new JLabel(" ", SwingConstants.CENTER);
        add(timeLabel, BorderLayout.SOUTH);

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                requestFocusInWindow();
            }
        });

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
                try {
                    Music.playSound("src/Resources/Music.wav");
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                }
                if (!controller.isBornProcessOn()) {
                    myField = new MyPanel();
                    myField.configureController(controller);
                    controller.startBornProcess();
                    controller.setStopButtonState();

                }
                break;
            case KeyEvent.VK_E:
                Music.stopSound();
                if (controller.isBornProcessOn()) {
                    if (controller.isInfoDialogEnabled()) {
                        controller.pauseBornProcess();
                        if (showFinishDialog()) {
                            controller.stopBornProcess();
                            controller.setStartButtonState();
                            controller.refreshRabbitPopulation();
                        } else {
                            controller.resumeBornProcess();
                        }
                    } else {
                        controller.setStartButtonState();
                        controller.stopBornProcess();
                        controller.refreshRabbitPopulation();
                    }
                }
                break;
            case KeyEvent.VK_T:
                timeLabel.setVisible(!timeLabel.isVisible());
                boolean previousOnState = timeOnRadioButton.isSelected();
                boolean previousOffState = timeOffRadioButton.isSelected();
                timeOnRadioButton.setSelected(!previousOnState);
                timeOffRadioButton.setSelected(!previousOffState);
                controller.switchTimeRadioGroupState();
                break;
        }
    }


    public void configureController(Controller controller) {
        this.controller = controller;
    }

    public boolean showFinishDialog() {
        JPanel panel = new JPanel(new GridLayout(1, 1));
        JTextArea area = new JTextArea();
        area.setEditable(false);

        area.setText("Born process is finished. Here are results:\n" +
                "Ordinary: " + controller.getRabbitSimple() + "\n" +
                "Albinos: " + controller.getRabbitAlbinos() + "\n" +
                "All rabbits: " + controller.getAllRabbitsCount() + "\n" +
                "Passed time: " + time / 60 + " minutes " + time % 60 + " seconds");

        panel.add(area);

        int res = JOptionPane.showConfirmDialog(null, panel, "Simulation state",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            controller.refreshField();
            return true;
        } else {
            return false;
        }
    }

    private JLabel createLabel(String text, int fontPosition, Font font, Color fontColor) {
        JLabel label = new JLabel(text, fontPosition);
        label.setFont(font);
        label.setForeground(fontColor);
        return label;
    }

    private void configureMenuBar() {
        menuBar = new JMenuBar();

        JMenu simControlMenu = new JMenu("Simulation control");
        startItem = new JMenuItem("Start");
        stopItem = new JMenuItem("Stop");
        quitItem = new JMenuItem("Quit");
        simControlMenu.add(startItem);
        stopItem.setEnabled(false);
        simControlMenu.add(stopItem);
        simControlMenu.addSeparator();
        simControlMenu.add(quitItem);

        startItem.addActionListener(actionEvent -> {
            if (!controller.isBornProcessOn()) {
                startItem.setEnabled(false);

                stopItem.setEnabled(true);
                myField = new MyPanel();
                myField.configureController(controller);
                controller.startBornProcess();
                controller.setStopButtonState();

            }
        });
        stopItem.addActionListener(actionEvent -> {
            if (controller.isInfoDialogEnabled()) {
                controller.pauseBornProcess();
                if (showFinishDialog()) {
                    controller.stopBornProcess();
                    controller.setStartButtonState();
                    controller.refreshRabbitPopulation();
                } else {
                    controller.resumeBornProcess();
                }
            } else {
                controller.stopBornProcess();
            }
        });
        quitItem.addActionListener(actionEvent -> {
            System.exit(0);
        });
        menuBar.add(simControlMenu);

        JMenu infoMenu = new JMenu("Simulation info");
        timeOnRadioButton = new JRadioButtonMenuItem("Time label on", true);
        timeOffRadioButton = new JRadioButtonMenuItem("Time label off");
        showDialogRadioButton = new JRadioButtonMenuItem("Show simulation state", true);

        timeOnRadioButton.addActionListener(actionEvent -> {
            timeLabel.setVisible(true);
            timeOnRadioButton.setSelected(true);
            timeOffRadioButton.setSelected(false);
            controller.switchTimeRadioGroupStateOn();
        });
        timeOffRadioButton.addActionListener(actionEvent -> {
            timeLabel.setVisible(false);
            timeOnRadioButton.setSelected(false);
            timeOffRadioButton.setSelected(true);
            controller.switchTimeRadioGroupStateOff();
        });
        showDialogRadioButton.addActionListener(actionEvent -> {
            controller.switchInfoRadioButtonState();
        });

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(timeOnRadioButton);
        buttonGroup.add(timeOffRadioButton);
        infoMenu.add(timeOnRadioButton);
        infoMenu.add(timeOffRadioButton);
        infoMenu.add(showDialogRadioButton);
        menuBar.add(infoMenu);

        JMenu rabbitsMenu = new JMenu("Rabbits");
        JMenu ordinaryMenu = new JMenu("Ordinary");
        JMenu albinosMenu = new JMenu("Albinos");
        simpleProbability = new JMenu("Probability");
        albinosProbability = new JMenu("Probability");
        JMenu ordinaryTimeBornPeriod = new JMenu("Generation period");
        JMenu albinosTimeBornPeriod = new JMenu("Generation period");

        ordinaryGenPeriodTextField = new JTextField();
        ordinaryGenPeriodTextField.setText(String.valueOf(N1));
        ordinaryGenPeriodTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setFocusable(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setFocusable(true);
            }
        });
        ordinaryGenPeriodTextField.addActionListener(action -> {
            if (!ordinaryGenPeriodTextField.getText().isEmpty()) {
                try {
                    controller.setN1(Integer.parseInt(ordinaryGenPeriodTextField.getText()));
                } catch (NumberFormatException a) {
                    ordinaryGenPeriodTextField.setText(String.valueOf(N1));
                    JOptionPane.showMessageDialog(null,
                            "You have set wrong parameters!",
                            "Warning: wrong parameters",
                            JOptionPane.WARNING_MESSAGE);
                }
            } else {
                ordinaryGenPeriodTextField.setText(String.valueOf(N1));
                JOptionPane.showMessageDialog(null,
                        "You have set wrong parameters!",
                        "Warning: wrong parameters",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        ordinaryTimeBornPeriod.add(ordinaryGenPeriodTextField);

        albinosGenPeriodTextField = new JTextField();
        albinosGenPeriodTextField.setText(String.valueOf(N2));
        albinosGenPeriodTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setFocusable(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setFocusable(true);
            }
        });
        albinosGenPeriodTextField.addActionListener(action -> {
            if (!albinosGenPeriodTextField.getText().isEmpty()) {
                try {
                    controller.setN1(Integer.parseInt(albinosGenPeriodTextField.getText()));
                } catch (NumberFormatException a) {
                    albinosGenPeriodTextField.setText(String.valueOf(N2));
                    JOptionPane.showMessageDialog(null,
                            "You have set wrong parameters!",
                            "Warning: wrong parameters",
                            JOptionPane.WARNING_MESSAGE);
                }
            } else {
                albinosGenPeriodTextField.setText(String.valueOf(N2));
                JOptionPane.showMessageDialog(null,
                        "You have set wrong parameters!",
                        "Warning: wrong parameters",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        albinosTimeBornPeriod.add(albinosGenPeriodTextField);

        ButtonGroup albinosButtonGroup = new ButtonGroup();
        ButtonGroup ordinaryButtonGroup = new ButtonGroup();
        for (int i = 0; i <= 100; i += 10) {
            JCheckBoxMenuItem albinosBoxMenuItem = new JCheckBoxMenuItem(String.valueOf(i));
            JCheckBoxMenuItem ordinaryBoxMenuItem = new JCheckBoxMenuItem(String.valueOf(i));
            if (i == P1) {
                ordinaryBoxMenuItem.setSelected(true);
            }
            if (i == K) {
                albinosBoxMenuItem.setSelected(true);
            }
            albinosBoxMenuItem.addActionListener(actionEvent -> {
                controller.setK(Integer.parseInt(albinosBoxMenuItem.getText()));
            });
            ordinaryBoxMenuItem.addActionListener(actionEvent -> {
                controller.setP1(Integer.parseInt(ordinaryBoxMenuItem.getText()));
            });
            albinosButtonGroup.add(albinosBoxMenuItem);
            ordinaryButtonGroup.add(ordinaryBoxMenuItem);
            albinosProbability.add(albinosBoxMenuItem);
            simpleProbability.add(ordinaryBoxMenuItem);
        }

        ordinaryMenu.add(simpleProbability);
        ordinaryMenu.add(ordinaryTimeBornPeriod);
        albinosMenu.add(albinosProbability);
        albinosMenu.add(albinosTimeBornPeriod);
        rabbitsMenu.add(ordinaryMenu);
        rabbitsMenu.add(albinosMenu);
        menuBar.add(rabbitsMenu);

        setJMenuBar(menuBar);
    }

    public void turnTimeLabelOn() {
        timeLabel.setVisible(true);
        timeOnRadioButton.setSelected(false);
        timeOffRadioButton.setSelected(true);
    }

    public void turnTimeLabelOff() {
        timeLabel.setVisible(false);
        timeOnRadioButton.setSelected(true);
        timeOffRadioButton.setSelected(false);
    }

    public void setStopButtonProcessInMenu() {

        stopItem.setEnabled(false);
        startItem.setEnabled(true);

    }

    public void setStartButtonProcessInMenu() {
        stopItem.setEnabled(true);
        startItem.setEnabled(false);
    }

    public void switchDialogRadioButtonState() {
        showDialogRadioButton.setSelected(!showDialogRadioButton.isSelected());
    }

    public void setN1(int N1) {
        ordinaryGenPeriodTextField.setText(String.valueOf(N1));
    }

    public void setN2(int N2) {
        albinosGenPeriodTextField.setText(String.valueOf(N2));
    }

    public void setP1(int P1) {
        simpleProbability.removeAll();

        simpleButtonGroup = new ButtonGroup();
        for (int i = 0; i <= 100; i += 10) {
            JCheckBoxMenuItem ordinaryBoxMenuItem = new JCheckBoxMenuItem(String.valueOf(i));
            if (i == P1) {
                ordinaryBoxMenuItem.setSelected(true);
            } else {
                ordinaryBoxMenuItem.setSelected(false);
            }
            ordinaryBoxMenuItem.addActionListener(actionEvent -> {
                controller.setP1(Integer.parseInt(ordinaryBoxMenuItem.getText()));
            });
            simpleButtonGroup.add(ordinaryBoxMenuItem);
            simpleProbability.add(ordinaryBoxMenuItem);
        }
    }

    public void setK(int K) {
        albinosProbability.removeAll();

        albinosButtonGroup = new ButtonGroup();
        for (int i = 0; i <= 100; i += 10) {
            JCheckBoxMenuItem albinosBoxMenuItem = new JCheckBoxMenuItem(String.valueOf(i));
            if (i == K) {
                albinosBoxMenuItem.setSelected(true);
            } else {
                albinosBoxMenuItem.setSelected(false);
            }
            albinosBoxMenuItem.addActionListener(actionEvent -> {
                controller.setK(Integer.parseInt(albinosBoxMenuItem.getText()));
            });

            albinosButtonGroup.add(albinosBoxMenuItem);
            albinosProbability.add(albinosBoxMenuItem);
        }

    }
}
