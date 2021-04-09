package Controller;
import Rabbit.Rabbit;
import Rabbit.RabbitAlbinos;
import Rabbit.RabbitSimple;
import Habitat.Habitat;
import View.*;
import java.util.Vector;

public class Controller {
    private MyPanel m;
    private Habitat habitat;
    private MyFrame frame;
    private ControlPanel controlPanel;
    public Controller(MyPanel myField, Habitat habitat, MyFrame frame, ControlPanel controlPanel) {
        this.m = myField;
        this.habitat = habitat;
        this.frame = frame;
        this.controlPanel = controlPanel;
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
        habitat.setN1(N1);
        controlPanel.setN1(N1);
        frame.setN1(N1);
    }

    public void setN2(int N2) {
        habitat.setN2(N2);
        controlPanel.setN2(N2);
        frame.setN2(N2);
    }

    public void setP1(int P1) {
        habitat.setP1(P1);
        controlPanel.setP1(P1);
        frame.setP1(P1);
    }

    public void setK(int K) {
        habitat.setK(K);
        controlPanel.setK(K);
        frame.setK(K);
    }

    public void setD1(int D1) {
        habitat.setD1(D1);
        controlPanel.setD1(D1);
        frame.setD1(D1);
    }

    public void setD2(int D2) {
        habitat.setD2(D2);
        controlPanel.setD2(D2);
        frame.setD2(D2);
    }

}

