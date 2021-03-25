package View;

import Controller.Controller;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Arrays;
public class ControlPanel extends JPanel {
    private JPanel buttonsPanel; // panel for buttons
    private JPanel timePanel; // panel for time
    private JPanel albinosPanel;
    private JPanel simplePanel;
    private JButton startButton = new JButton("Start"); // button start
    private JButton stopButton = new JButton("Stop"); // button stop
    private JRadioButton timeShowOnButton; // show time
    private JRadioButton timeShowOffButton; //hide time
    private JRadioButton dialogRadioButton;
    private JTextField albinosGenPeriod;
    private JTextField simpleGenPeriod;

    private JComboBox<Integer> simpleProbability;
    private JComboBox<Integer> albinosProbability;
    final private Integer[] probabilitiesArray = { 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 };
    Controller controller;
    ControlPanel(int N1, int N2, int P1, int K) {
        super();
        setLayout(new GridLayout(4,1));
        setBorder(this, "Control panel");
        configureButtonsPanel();
        configureTimePanel();
        configureSimplePanel(N1, P1);
        configureAlbinosPanel(N2, K);
    }
    private void configureButtonsPanel() {
        buttonsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        buttonsPanel.setPreferredSize(new Dimension(200, 100));
        buttonsPanel.setBackground(Color.decode("#E6E6FA"));
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        stopButton.setFocusable(false);
        startButton.setFocusable(false);
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 50;
        buttonsPanel.add(startButton, c);

        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 50;
        buttonsPanel.add(stopButton, c);

        setBorder(buttonsPanel, "Simulation control");
        buttonsPanel.setVisible(true);
        add(buttonsPanel);
        startButton.addActionListener(listener -> {
            try {
                Music.playSound("src/Resources/Music.wav");
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            }
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            controller.startBornProcess();
        });
        stopButton.addActionListener(listener -> {
            Music.stopSound();
            startButton.setEnabled(true);
            stopButton.setEnabled(false);

            if (isInfoDialogEnabled()) {
                controller.stopBornProcess();
                if (!controller.showInfoDialog()) {
                    startButton.setEnabled(false);
                    stopButton.setEnabled(true);
                    controller.resumeBornProcess();
                } else {
                    controller.stopBornProcess();
                }
            } else {
                controller.stopBornProcess();
                controller.refreshRabbitPopulation();
            }
        });
    }
    private void configureTimePanel() {
        timePanel = new JPanel(new GridLayout(3,1));

        setBorder(timePanel, "Simulation info");

        timeShowOnButton = new JRadioButton("Time label on", true);
        timeShowOnButton.addActionListener(action -> {
            controller.turnTimeLabelOn();
        });
        timeShowOnButton.setFocusable(false);
        timeShowOnButton.setFocusPainted(false);

        timeShowOffButton = new JRadioButton("Time label off", false);
        timeShowOffButton.addActionListener(action -> {
            controller.turnTimeLabelOff();
        });
        timeShowOffButton.setFocusable(false);
        timeShowOffButton.setFocusPainted(false);

        dialogRadioButton = new JRadioButton("Show simulation state", true);
        dialogRadioButton.addActionListener(actionEvent -> {
            controller.switchDialogRadioButtonState();
        });
        timeShowOffButton.setFocusable(false);
        timeShowOffButton.setFocusPainted(false);

        ButtonGroup timeRadioButtonsGroup = new ButtonGroup();
        timeRadioButtonsGroup.add(timeShowOnButton);
        timeRadioButtonsGroup.add(timeShowOffButton);

        timePanel.add(timeShowOnButton);
        timePanel.add(timeShowOffButton);
        timePanel.add(dialogRadioButton);

        timePanel.setVisible(true);
        timePanel.setFocusable(false);

        add(timePanel);
    }
    private void configureSimplePanel(int N1, int P1) {
        simplePanel = new JPanel(new GridBagLayout());
        setBorder(simplePanel, "Ordinary rabbits");

        GridBagConstraints с = new GridBagConstraints();
        simpleProbability = new JComboBox<>(probabilitiesArray);
        simpleProbability.setSelectedIndex(Arrays.asList(probabilitiesArray).indexOf(P1));
        simpleProbability.setFocusable(false);
        simpleProbability.addActionListener(actionEvent -> {
            controller.setP1(simpleProbability.getItemAt(simpleProbability.getSelectedIndex()));
        });
        с.gridx = 1;
        с.gridy = 0;
        simplePanel.add(simpleProbability, с);

        simpleGenPeriod = new JTextField();
        simpleGenPeriod.setText(String.valueOf(N1));
        с.gridx = 1;
        с.gridy = 1;
        с.ipadx = 75;

        simpleGenPeriod.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setFocusable(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setFocusable(true);
            }
        });
        simpleGenPeriod.addActionListener(action -> {
            if (!simpleGenPeriod.getText().isEmpty()) {
                try {
                    controller.setN1(Integer.parseInt(simpleGenPeriod.getText()));
                } catch (NumberFormatException a) {
                    simpleGenPeriod.setText(String.valueOf(N1));
                    JOptionPane.showMessageDialog(null,
                            "You have set wrong parameters!",
                            "Warning: wrong parameters",
                            JOptionPane.WARNING_MESSAGE);
                }
            } else {
                simpleGenPeriod.setText(String.valueOf(N1));
                JOptionPane.showMessageDialog(null,
                        "You have set wrong parameters!",
                        "Warning: wrong parameters",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        simplePanel.add(simpleGenPeriod, с);

        JLabel probabilityLabel = new JLabel("Probability (%):");
        с.gridx = 0;
        с.gridy = 0;
        simplePanel.add(probabilityLabel, с);
        JLabel periodLabel = new JLabel("Generation period (in secs): ");
        с.gridx = 0;
        с.gridy = 1;
        simplePanel.add(periodLabel, с);

        simplePanel.setVisible(true);
        simplePanel.setFocusable(false);

        add(simplePanel);
    }

    private void configureAlbinosPanel(int N2, int K) {
        albinosPanel = new JPanel(new GridBagLayout());
        setBorder(albinosPanel, "Albinos rabbits");

        GridBagConstraints с = new GridBagConstraints();
        albinosProbability = new JComboBox<>(probabilitiesArray);
        albinosProbability.setSelectedIndex(Arrays.asList(probabilitiesArray).indexOf(K));
        albinosProbability.setFocusable(false);
        albinosProbability.addActionListener(actionEvent -> {
            controller.setP1(albinosProbability.getItemAt(albinosProbability.getSelectedIndex()));
        });
        с.gridx = 1;
        с.gridy = 0;
        albinosPanel.add(albinosProbability, с);

        albinosGenPeriod = new JTextField();
        albinosGenPeriod.setText(String.valueOf(N2));
        с.gridx = 1;
        с.gridy = 1;
        с.ipadx = 75;

        albinosGenPeriod.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setFocusable(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setFocusable(true);
            }
        });
        albinosGenPeriod.addActionListener(action -> {
            if (!albinosGenPeriod.getText().isEmpty()) {
                try {
                    controller.setN2(Integer.parseInt(albinosGenPeriod.getText()));
                } catch (NumberFormatException a) {
                    albinosGenPeriod.setText(String.valueOf(N2));
                    JOptionPane.showMessageDialog(null,
                            "You have set wrong parameters!",
                            "Warning: wrong parameters",
                            JOptionPane.WARNING_MESSAGE);
                }
            } else {
                albinosGenPeriod.setText(String.valueOf(N2));
                JOptionPane.showMessageDialog(null,
                        "You have set wrong parameters!",
                        "Warning: wrong parameters",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        albinosPanel.add(albinosGenPeriod, с);

        JLabel probabilityLabel = new JLabel("Probability (%):");
        с.gridx = 0;
        с.gridy = 0;
        albinosPanel.add(probabilityLabel, с);
        JLabel periodLabel = new JLabel("Generation period (in secs): ");
        с.gridx = 0;
        с.gridy = 1;
        albinosPanel.add(periodLabel, с);

        albinosPanel.setVisible(true);
        albinosPanel.setFocusable(false);

        add(albinosPanel);
    }


    private void setBorder(JPanel panel, String text) {
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                        text, TitledBorder.RIGHT, TitledBorder.TOP),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }
    public void configureController(Controller controller) {
        this.controller = controller;
    }

    public void setStartButtonEnabled() {
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    public void setStopButtonEnabled() {
        stopButton.setEnabled(true);
        startButton.setEnabled(false);
    }
    public void switchTimeRadioGroupState() {
        boolean previousOnState = timeShowOnButton.isSelected();
        boolean previousOffState = timeShowOffButton.isSelected();
        timeShowOnButton.setSelected(!previousOnState);
        timeShowOffButton.setSelected(!previousOffState);
    }

    public void switchTimeRadioGroupStateOff() {
        timeShowOnButton.setSelected(false);
        timeShowOffButton.setSelected(true);
    }

    public void switchTimeRadioGroupStateOn() {
        timeShowOnButton.setSelected(true);
        timeShowOffButton.setSelected(false);
    }

    public void switchInfoRadioGroupState() {
        dialogRadioButton.setSelected(!dialogRadioButton.isSelected());
    }

    public boolean isInfoDialogEnabled() {
        return dialogRadioButton.isSelected();
    }

    public void setN1(int N1) {
        simpleGenPeriod.setText(String.valueOf(N1));
    }

    public void setN2(int N2) {
        albinosGenPeriod.setText(String.valueOf(N2));
    }

    public void setP1(int P1) {
       simpleProbability.setSelectedIndex(Arrays.asList(probabilitiesArray).indexOf(P1));
    }

    public void setK(int K) {
        albinosProbability.setSelectedIndex(Arrays.asList(probabilitiesArray).indexOf(K));
    }
}

