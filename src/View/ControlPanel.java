package View;

import Controller.Controller;
import Habitat.Habitat;
import Rabbit.Rabbit;
import Rabbit.RabbitsStorage;
import utils.Serialization;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ControlPanel extends JPanel {
    private JPanel buttonsPanel; // panel for
    private JPanel timePanel; // panel for time
    private JPanel albinosPanel;
    private JPanel simplePanel;
    private JPanel lifeTimePanel;
    private JPanel currentInfoPanel;
    private JPanel panelAI;
    private int a = 0;
    private JButton reduceAlbinosRabbits = new JButton("Reduce albinos rabbits");
    private JButton openNetwork = new JButton("Open network");
    private JButton closeNetwork = new JButton("Close network");
    private JButton sendSettings = new JButton ("Send settings");
    private JButton showActiveClients = new JButton("Clients");
    private JPanel serverClientPanel;
    private JButton startButton = new JButton("Start"); // button start
    private JButton stopButton = new JButton("Stop"); // button stop
    private JButton consoleButton = new JButton("Console"); // button console
    private JButton saveButton = new JButton("Save"); // button save
    private JButton loadButton = new JButton("Load"); // button load
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
    private JComboBox<Integer> ordinaryPriorityComboBox;
    private JComboBox<Integer> albinosPriorityComboBox;
    final private Integer[] probabilitiesArray = { 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 };
    final private Integer[] prioritiesArray = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

    private JRadioButton ordinaryAIRadioButtonOn;
    private JRadioButton albinosAIRadioButtonOn;
    private JRadioButton ordinaryAIRadioButtonOff;
    private JRadioButton albinosAIRadioButtonOff;

    Controller controller;


    ControlPanel(int N1, int N2, int P1, int K, int D1, int D2, Vector<Rabbit> rabbitVector) { //исправить тут, если получится
        super();
        setLayout(new GridLayout(7,1));
        setBorder(this, "Control panel");
        configureButtonsPanel();
        configureTimePanel();
        configureSimplePanel(N1, P1);
        configureAlbinosPanel(N2, K);
        configureLifeTimePanel(D1, D2);
        configureInfoPanel();
        serverPanel();
    }
    public void serverPanel() {
        serverClientPanel = new JPanel(new GridBagLayout());
        setBorder(serverClientPanel, "Ordinary rabbits");
        GridBagConstraints c = new GridBagConstraints();
        serverClientPanel.setBackground(Color.decode("#E0FFFF"));

        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 50;
        serverClientPanel.add(openNetwork, c);

        c.gridx = 1;
        c.gridy = 0;
        c.ipadx = 50;
        serverClientPanel.add(closeNetwork, c);

        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 50;
        serverClientPanel.add(sendSettings, c);

        c.gridx = 1;
        c.gridy = 1;
        c.ipadx = 50;
        serverClientPanel.add(showActiveClients, c);

        setBorder(serverClientPanel, "Server");

        serverClientPanel.setVisible(true);
        add(serverClientPanel);

        openNetwork.setEnabled(true);
        closeNetwork.setEnabled(false);

        closeNetwork.addActionListener(listener -> {
            openNetwork.setEnabled(true);
            closeNetwork.setEnabled(false);
        });

        openNetwork.addActionListener(action -> {
            controller.work();
            openNetwork.setEnabled(false);
            closeNetwork.setEnabled(true);
        });

        showActiveClients.addActionListener(action -> {
            controller.showClientsDialog();
        });

        closeNetwork.addActionListener(action -> {
            controller.stopWork();
            openNetwork.setEnabled(true);
            closeNetwork.setEnabled(false);
        });

        sendSettings.addActionListener(action -> {
            controller.sendFile();
        });
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

        //for lab 5
        saveButton.setEnabled(true);
        loadButton.setEnabled(true);
        saveButton.setFocusable(false);
        loadButton.setFocusable(false);

        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 50;
        buttonsPanel.add(startButton, c);

        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 50;
        buttonsPanel.add(stopButton, c);

        c.gridx = 1;
        c.gridy = 0;
        c.ipadx = 50;
        buttonsPanel.add(saveButton, c);

        c.gridx = 1;
        c.gridy = 1;
        c.ipadx = 50;
        buttonsPanel.add(loadButton, c);

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
            consoleButton.setEnabled(true);
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

        saveButton.addActionListener(listener -> {
            Serialization serialization = new Serialization();
            serialization.serialize();
        });

        loadButton.addActionListener(listener -> {
            Serialization serialization = new Serialization();
            serialization.deserialize();
        });


    }
    private void configureTimePanel() {
        timePanel = new JPanel(new GridLayout(2,2));

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

    private void configurePanelAI(){
        panelAI = new JPanel(new GridBagLayout());
        setBorder(panelAI, "AI");
        GridBagConstraints с = new GridBagConstraints();
        albinosAIRadioButtonOn = new JRadioButton("Albinos On", true);
        albinosAIRadioButtonOn.addActionListener(action -> {
            albinosAIRadioButtonOn.setSelected(true);
            albinosAIRadioButtonOff.setSelected(false);
            controller.turnAlbinosAIOn();
        });
        albinosAIRadioButtonOn.setFocusable(false);
        albinosAIRadioButtonOn.setFocusPainted(false);
        с.gridx = 0;
        с.gridy = 0;
        panelAI.add(albinosAIRadioButtonOn, с);

        albinosAIRadioButtonOff = new JRadioButton("Albinos Off", true);
        albinosAIRadioButtonOff.setSelected(false);
        albinosAIRadioButtonOff. addActionListener(action -> {
            albinosAIRadioButtonOff.setSelected(true);
            albinosAIRadioButtonOn.setSelected(false);
            controller.turnAlbinosAIOff();
        });
        albinosAIRadioButtonOff.setFocusable(false);
        albinosAIRadioButtonOff.setFocusPainted(false);
        с.gridx = 1;
        с.gridy = 0;
        panelAI.add(albinosAIRadioButtonOff, с);

        ordinaryAIRadioButtonOn = new JRadioButton("Ordinary On", true);
        ordinaryAIRadioButtonOn.addActionListener(action -> {
            ordinaryAIRadioButtonOn.setSelected(true);
            ordinaryAIRadioButtonOff.setSelected(false);
            controller.turnOrdinaryAIOn();
        });
        ordinaryAIRadioButtonOn.setFocusable(false);
        ordinaryAIRadioButtonOn.setFocusPainted(false);
        с.gridx = 0;
        с.gridy = 1;
        panelAI.add(ordinaryAIRadioButtonOn, с);

        ordinaryAIRadioButtonOff = new JRadioButton("Ordinary Off", true);
        ordinaryAIRadioButtonOff.setSelected(false);
        ordinaryAIRadioButtonOff. addActionListener(action -> {
            ordinaryAIRadioButtonOff.setSelected(true);
            ordinaryAIRadioButtonOn.setSelected(false);
            controller.turnOrdinaryAIOff();
        });
        ordinaryAIRadioButtonOff.setFocusable(false);
        ordinaryAIRadioButtonOff.setFocusPainted(false);
        с.gridx = 1;
        с.gridy = 1;
        panelAI.add(ordinaryAIRadioButtonOff, с);


        JLabel priorityLabelAlbinos = new JLabel("- Priority Albinos");
        с.gridx = 3;
        с.gridy = 0;
        panelAI.add(priorityLabelAlbinos, с);

        albinosPriorityComboBox = new JComboBox<>(prioritiesArray);
        albinosPriorityComboBox.setSelectedIndex(Arrays.asList(prioritiesArray).indexOf(5));
        albinosPriorityComboBox.setFocusable(false);
        albinosPriorityComboBox.addActionListener(actionEvent -> {
            controller.changeAlbinosPriority(albinosPriorityComboBox.getItemAt(albinosPriorityComboBox.getSelectedIndex()));
        });
        с.gridx = 2;
        с.gridy = 0;
        panelAI.add(albinosPriorityComboBox, с);

        JLabel priorityLabelOrdinary = new JLabel("- Priority Ordinary");
        с.gridx = 3;
        с.gridy = 1;
        panelAI.add(priorityLabelOrdinary, с);

        ordinaryPriorityComboBox = new JComboBox<>(prioritiesArray);
        ordinaryPriorityComboBox.setSelectedIndex(Arrays.asList(prioritiesArray).indexOf(5));
        ordinaryPriorityComboBox.setFocusable(false);
        ordinaryPriorityComboBox.addActionListener(actionEvent -> {
            controller.changeOrdinaryPriority(ordinaryPriorityComboBox.getItemAt(ordinaryPriorityComboBox.getSelectedIndex()));
        });
        с.gridx = 2;
        с.gridy = 1;
        panelAI.add(ordinaryPriorityComboBox, с);

        panelAI.setVisible(true);
        panelAI.setFocusable(false);

        add(panelAI);
    }

    private void configureInfoPanel(){
        currentInfoPanel = new JPanel(new GridBagLayout());
        setBorder(currentInfoPanel, "Info panel");
        GridBagConstraints c = new GridBagConstraints();

        currentObjectsButton.setEnabled(false);
        currentObjectsButton.setFocusable(false);
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 50;
        currentInfoPanel.add(currentObjectsButton, c);

        consoleButton.setEnabled(false);
        consoleButton.setFocusable(false);
        JPanel consolePanel = new JPanel(new GridLayout(1, 1));
        JTextArea areaConsole = new JTextArea();
        areaConsole.setEditable(true);
        areaConsole.setLineWrap(true);
        areaConsole.setText("Available command: delete Albinos K. Changes probability to K" + '\n');
        consolePanel.setAutoscrolls(true);
        consolePanel.setPreferredSize(new Dimension(700,400));
        consolePanel.add(areaConsole);
        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 50;
        currentInfoPanel.add(consoleButton, c);

        add(currentInfoPanel);
        currentObjectsButton.addActionListener(listener -> {
            Vector<Rabbit> rabbitVector = RabbitsStorage.getInstance().getRabbitVector();
            String infoMessage = "";
            if (!(RabbitsStorage.getInstance() == null || rabbitVector == null)){
                for (int i = 0; i < rabbitVector.size(); i++) {
                    infoMessage += rabbitVector.get(i).getBirthTime()/100 + " " + rabbitVector.get(i).getDeathTime()/100 + " " + rabbitVector.get(i).getUUID() + "\n";
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

        consoleButton.addActionListener(listener -> {


            if (JOptionPane.showConfirmDialog(null, consolePanel, "Console",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                Component pan = consolePanel.getComponent(0);
                if (pan.getClass().equals(JTextArea.class)) {
                    PipedInputStream input = new PipedInputStream();
                    PipedOutputStream output = new PipedOutputStream();
                    try {
                        output.connect(input);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scanner fieldInput = new Scanner(input);
                    PrintStream fieldOutput = new PrintStream(output);
                    String txt = ((JTextArea) pan).getText();
                    String last = "";
                    for(String temp : txt.split("\n")) {
                        last = temp;
                    }
                    //System.out.println(last);
                    // System.out.println(txt);
                    fieldOutput.println(last);
                    //System.out.println(fieldInput);
                    ((JTextArea) pan).setText("");
                    String line = fieldInput.nextLine();
                    ((JTextArea) pan).setText(txt + '\n');
                    try {
                        if(line.contains("delete Albinos") && line.matches(".*\\d+.*")) {
                            Pattern pattern = Pattern.compile("\\d+");
                            Matcher matcher = pattern.matcher(line);
                            int start = 0;
                            int result = 0;
                            while (matcher.find(start)) {
                                String value = line.substring(matcher.start(), matcher.end());
                                result = Integer.parseInt(value);

                                start = matcher.end();
                            }
                            if(result % 10 == 0) {
                                try {
                                    this.setK(result);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                ((JTextArea) pan).setText(txt + '\n' + "Successfully changed K to " + result + '\n');
                                //System.out.println(result);
                            }
                            else ((JTextArea) pan).setText(txt + '\n' + "Failed. K must be % 10 " + '\n');
                        }
                        else {
                            ((JTextArea) pan).setText(txt + '\n' + "Failed" + '\n');
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } else {
                // no option
            }

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

