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
    public Controller(MyPanel myField, Habitat habitat, MyFrame frame) {
        this.m = myField;
        this.habitat = habitat;
        this.frame = frame;
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

}
