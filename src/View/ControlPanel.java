package View;

import Controller.Controller;
import Habitat.Habitat;
import Rabbit.Rabbit;
import Rabbit.RabbitsStorage;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

public class ControlPanel extends JPanel {
    private JPanel buttonsPanel; // panel for buttons
    private JPanel timePanel; // panel for time
    private JPanel albinosPanel;
    private JPanel simplePanel;
    private JPanel lifeTimePanel;
    private JPanel currentInfoPanel;

    private JButton startButton = new JButton("Start"); // button start
    private JButton stopButton = new JButton("Stop"); // button stop
    private JButton currentObjectsButton = new JButton("Current objects"); // button objects

    private JRadioButton timeShowOnButton; // show time
    private JRadioButton timeShowOffButton; //hide time
    private JRadioButton dialogRadioButton;

    private JTextField albinosGenPeriod;
    private JTextField simpleGenPeriod;
    private JTextField simpleLifeTime;
    private JTextField albinosLifeTime;

    private JComboBox<Integer> simpleProbability;
    private JComboBox<Integer> albinosProbability;
    final private Integer[] probabilitiesArray = { 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 };

    Controller controller;


    ControlPanel(int N1, int N2, int P1, int K, int D1, int D2, Vector<Rabbit> rabbitVector) { //исправить тут, если получится
        super();
        setLayout(new GridLayout(6,1));
        setBorder(this, "Control panel");
        configureButtonsPanel();
        configureTimePanel();
        configureSimplePanel(N1, P1);
        configureAlbinosPanel(N2, K);
        configureLifeTimePanel(D1, D2);
        configureInfoPanel();
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
            currentObjectsButton.setEnabled(true);
            controller.startBornProcess();
        });
        stopButton.addActionListener(listener -> {
            Music.stopSound();
            startButton.setEnabled(true);
            stopButton.setEnabled(false);

            if (isInfoDialogEnabled()) {
                controller.pauseBornProcess();
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
            controller.setK(albinosProbability.getItemAt(albinosProbability.getSelectedIndex()));// гавно
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

        JLabel probabilityLabel = new JLabel("Critical part (%):");
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

   private void configureLifeTimePanel(int D1, int D2){
        lifeTimePanel = new JPanel(new GridBagLayout());
        setBorder(lifeTimePanel, "Life time");
        GridBagConstraints с = new GridBagConstraints();

       simpleLifeTime = new JTextField();
       simpleLifeTime.setText(String.valueOf(D1));
       с.gridx = 1;
       с.gridy = 0;
       с.ipadx = 75;

       simpleLifeTime.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               setFocusable(true);
           }

           @Override
           public void mouseEntered(MouseEvent e) {
               setFocusable(true);
           }
       });
       simpleLifeTime.addActionListener(action -> {
           if (!simpleLifeTime.getText().isEmpty()) {
               try {
                   controller.setD1(Integer.parseInt(simpleLifeTime.getText())); // как тут ошибка, если я прописала функцию эту???
               } catch (NumberFormatException a) {
                   simpleLifeTime.setText(String.valueOf(D1));
                   JOptionPane.showMessageDialog(null,
                           "You have set wrong parameters!",
                           "Warning: wrong parameters",
                           JOptionPane.WARNING_MESSAGE);
               }
           } else {
               simpleLifeTime.setText(String.valueOf(D1));
               JOptionPane.showMessageDialog(null,
                       "You have set wrong parameters!",
                       "Warning: wrong parameters",
                       JOptionPane.WARNING_MESSAGE);
           }
       });
       lifeTimePanel.add(simpleLifeTime, с);

       albinosLifeTime = new JTextField();
       albinosLifeTime.setText(String.valueOf(D1));
       с.gridx = 1;
       с.gridy = 1;
       с.ipadx = 75;

       albinosLifeTime.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               setFocusable(true);
           }

           @Override
           public void mouseEntered(MouseEvent e) {
               setFocusable(true);
           }
       });
       albinosLifeTime.addActionListener(action -> {
           if (!albinosLifeTime.getText().isEmpty()) {
               try {
                   controller.setD2(Integer.parseInt(albinosLifeTime.getText()));
               } catch (NumberFormatException a) {
                   albinosLifeTime.setText(String.valueOf(D2));
                   JOptionPane.showMessageDialog(null,
                           "You have set wrong parameters!",
                           "Warning: wrong parameters",
                           JOptionPane.WARNING_MESSAGE);
               }
           } else {
               albinosLifeTime.setText(String.valueOf(D2));
               JOptionPane.showMessageDialog(null,
                       "You have set wrong parameters!",
                       "Warning: wrong parameters",
                       JOptionPane.WARNING_MESSAGE);
           }
       });
       lifeTimePanel.add(albinosLifeTime, с);


       JLabel ordinaryLifeTimeLabel = new JLabel("Ordinary life time:");
       с.gridx = 0;
       с.gridy = 0;
       lifeTimePanel.add(ordinaryLifeTimeLabel, с);
       JLabel albinosLifeTimeLabel = new JLabel("Albinos life time:");
       с.gridx = 0;
       с.gridy = 1;
       lifeTimePanel.add(albinosLifeTimeLabel, с);

       lifeTimePanel.setVisible(true);
       lifeTimePanel.setFocusable(false);


       add(lifeTimePanel);
    }

    private void configureInfoPanel(){
        currentInfoPanel = new JPanel(new GridBagLayout());
        setBorder(currentInfoPanel, "Info panel");
        GridBagConstraints c = new GridBagConstraints();
        currentObjectsButton.setEnabled(false);
        currentObjectsButton.setFocusable(false);
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 75;
        currentInfoPanel.add(currentObjectsButton);
        add(currentInfoPanel);
        currentObjectsButton.addActionListener(listener -> {
            Vector<Rabbit> rabbitVector = RabbitsStorage.getInstance().getRabbitVector();
            String infoMessage = "";
            if (!(RabbitsStorage.getInstance() == null || rabbitVector == null)){
                for (int i = 0; i < rabbitVector.size(); i++) {
                    infoMessage += rabbitVector.get(i).getBirthTime() + " " + rabbitVector.get(i).getDeathTime() + " " + rabbitVector.get(i).getUUID() + "\n";
                }
            }
            final String fInfoMessage = infoMessage;
            JPanel panel = new JPanel(new GridLayout(1, 1));
            JTextArea area = new JTextArea();
            area.setEditable(false);
            area.setText(fInfoMessage);
            panel.add(area);

            int res = JOptionPane.showConfirmDialog(null, panel, "Current rabbits info",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);

        });
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

    public void setD1(int D1){ simpleLifeTime.setText(String.valueOf(D1)); }

    public void setD2(int D2){ albinosLifeTime.setText(String.valueOf(D2)); }

    public void setP1(int P1) {
       simpleProbability.setSelectedIndex(Arrays.asList(probabilitiesArray).indexOf(P1));
    }

    public void setK(int K) {
        albinosProbability.setSelectedIndex(Arrays.asList(probabilitiesArray).indexOf(K));
    }
}

