package Controller;
import Rabbit.Rabbit;
import Rabbit.RabbitAlbinos;
import Rabbit.RabbitSimple;
import Rabbit.AlbinosAI;
import Rabbit.OrdinaryAI;
import Habitat.Habitat;
import View.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;
import Server.TCPConnection;
import Server.TCPConnectionListener;
import utils.Configuration;

import javax.swing.*;

public class Controller implements TCPConnectionListener {
    private MyPanel m;
    private Habitat habitat;
    private MyFrame frame;
    private ControlPanel controlPanel;
    private AlbinosAI albinosAI;
    private OrdinaryAI ordinaryAI;
    private TCPConnection connection;
    private ArrayList<String> clients;

    public Controller(MyPanel myField, Habitat habitat, MyFrame frame, ControlPanel controlPanel) {
        this.m = myField;
        this.habitat = habitat;
        this.frame = frame;
        this.controlPanel = controlPanel;
        this.albinosAI = new AlbinosAI();
        albinosAI.start();
        this.ordinaryAI = new OrdinaryAI();
        ordinaryAI.start();
    }
    public void work() {
        try {
            connection = new TCPConnection(this, TCPConnection.IP_ADDRESS, TCPConnection.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendFile() {
        Configuration.saveConfig();
        connection.sendObject(Configuration.getConfigFile());
    }
    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {

    }

    @Override
    public void onReceiveObject(TCPConnection tcpConnection, Object object) {
        if (object instanceof TCPConnection.BoxUsers) {
            clients = ((TCPConnection.BoxUsers) object).users;
        }
        if (object instanceof File) {
            System.out.println("File");
            System.out.println(((File) object).getName());
            ArrayList<Integer> parameters = new ArrayList<>();

            try(
                    Scanner scanner = new Scanner((File)object)
            ) {
                while (scanner.hasNextLine()) {
                    String string = scanner.nextLine();
                    System.out.println(string);
                    Scanner intScanner = new Scanner(string);
                    parameters.add(intScanner.nextInt());
                }
            } catch (IOException eIO) {
                System.err.println("Error: something went wrong while loading config");
            }
            for (int i : parameters)
                System.out.println(i);
            setN1(parameters.get(0));
            setP1(parameters.get(1));
            setN2(parameters.get(2));
            setK(parameters.get(3));
            setD1(parameters.get(4));
            setD2(parameters.get(5));
        }
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {

    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {

    }
    public void stopWork() { //конец работы
        connection.disconnect();
        clients = null;
    }
    public void showClientsDialog() { //показать клиентов
        if (clients != null) {
            JPanel panel = new JPanel(new GridLayout(1, 1));
            JTextArea area = new JTextArea(6, 25);
            area.setEditable(false);

            for (String client : clients) {
                area.append(client + "\n");
            }
            panel.add(area);
            JOptionPane.showMessageDialog(null, new JScrollPane(panel), "Rabbits", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void toPaint(Vector<Rabbit> rabbits) {
        m.drawRabbits(rabbits);
    }

    public void stopBornProcess() {
        habitat.stopBorn();
    }

    public void startBornProcess() {
        habitat.startBorn();
    }
    public void resumeBornProcess() {
        habitat.resumeBorn();
    }

    public boolean isBornProcessOn() {
        return habitat.isBornProcessOn();
    }

    public int getRabbitSimple() {
        return RabbitSimple.countOfSimple;
    }

    public int getRabbitAlbinos() {
        return RabbitAlbinos.countOfAlbinos;
    }

    public int getAllRabbitsCount() {
        return Rabbit.countOfRabbits;
    }

    public void refreshRabbitPopulation() {
        habitat.refreshRabbitPopulation();
    }

    public void passTime(int time) {
        frame.updateTime(time);
    }

    public void switchTimeRadioGroupState() {
        controlPanel.switchTimeRadioGroupState();
    }

    public void turnTimeLabelOn() {
        frame.turnTimeLabelOn();
    }

    public void turnTimeLabelOff() {
        frame.turnTimeLabelOff();
    }

    public void switchTimeRadioGroupStateOff() {
        controlPanel.switchTimeRadioGroupStateOff();
    }

    public void switchTimeRadioGroupStateOn() {
        controlPanel.switchTimeRadioGroupStateOn();
    }

    public boolean isInfoDialogEnabled() {
        return controlPanel.isInfoDialogEnabled();
    }

    public boolean showInfoDialog() {
        return frame.showFinishDialog();
    }
    public void pauseBornProcess() {
        habitat.pauseBorn();
    }

    public void refreshField() {
        m.refreshField();
    }

    public void setStartButtonState() {
        controlPanel.setStartButtonEnabled();
    }

    public void setStopButtonState() {
        controlPanel.setStopButtonEnabled();
    }

    public void switchInfoRadioButtonState() {
        controlPanel.switchInfoRadioGroupState();
    }

    public void switchDialogRadioButtonState() {
        frame.switchDialogRadioButtonState();
    }

    public void setN1(int N1) {
        Habitat.setN1(N1);
        controlPanel.setN1(N1);
        frame.setN1(N1);
    }

    public void setN2(int N2) {
        Habitat.setN2(N2);
        controlPanel.setN2(N2);
        frame.setN2(N2);
    }

    public void setP1(int P1) {
        Habitat.setP1(P1);
        controlPanel.setP1(P1);
        frame.setP1(P1);
    }

    public void setK(int K) {
        Habitat.setK(K);
        controlPanel.setK(K);
        frame.setK(K);
    }

    public void setD1(int D1) {
        Habitat.setD1(D1);
        controlPanel.setD1(D1);
        frame.setD1(D1);
    }

    public void setD2(int D2) {
        Habitat.setD2(D2);
        controlPanel.setD2(D2);
        frame.setD2(D2);
    }

    public void turnOrdinaryAIOn() {
        if (!ordinaryAI.isAIActive()) {
            try {
                ordinaryAI.startAI();
                ordinaryAI.wait();
            } catch (Exception eInterrupted) {}
        }
    }

    public void turnOrdinaryAIOff() {
        if (ordinaryAI.isAIActive()) {
            ordinaryAI.stopAI();
        }
    }

    public void turnAlbinosAIOff() {
        if (albinosAI.isAIActive()) {
            albinosAI.stopAI();
        }
    }

    public void turnAlbinosAIOn() {
        if (!albinosAI.isAIActive()) {
            try {
                albinosAI.startAI();
                albinosAI.wait();
            } catch (Exception eInterrupted) {}
        }
    }

    public void changeOrdinaryPriority(int priority) {
        ordinaryAI.setAIPriority(priority);
    }

    public void changeAlbinosPriority(int priority) {
        albinosAI.setAIPriority(priority);
    }

}

