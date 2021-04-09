package Rabbit;

import java.util.HashMap;
import java.util.TreeSet;
import java.util.Vector;

public class RabbitsStorage {
    private static RabbitsStorage instance; //сущность - объект
    //Коллекции
    private Vector<Rabbit> rabbitVector; //для хранения кроликов, переименовала на вектор, потому что у нас не список
    private TreeSet<String> aliveRabbits; //для хранения и поиска уникальных идентификаторов
    private HashMap<String,Integer> rabbitsBornTime; //для хранения времени рождения объектов

    private RabbitsStorage() {
        this.aliveRabbits = new TreeSet<>();
        this.rabbitsBornTime = new HashMap<>();
        this.rabbitVector = new Vector<>();
    }

    public static RabbitsStorage getInstance() {
        if (instance == null) {
            instance = new RabbitsStorage();
        }
        return instance;
    }

    public Vector<Rabbit> getRabbitVector() {
        return rabbitVector;
    }
    public TreeSet<String> getAliveRabbits() {
        return aliveRabbits;
    }
    public HashMap<String, Integer> getRabbitsBornTime() {
        return rabbitsBornTime;
    }
    public void reset(){
        instance.rabbitsBornTime = new HashMap<>();
        instance.aliveRabbits = new TreeSet<>();
        instance.rabbitVector = new Vector<>();
    }
    public void removeRabbits(int gameSec) {
        for (int i = 0; i < rabbitVector.size(); i++) {
            Rabbit rabbit = rabbitVector.get(i);
            if (gameSec == rabbit.getDeathTime()) {
                if (rabbit instanceof RabbitSimple) {
                    RabbitSimple.countOfSimple--;
                }
                else {
                    RabbitAlbinos.countOfAlbinos--;
                }
                Rabbit.countOfRabbits--;
                rabbitVector.remove(i);
                aliveRabbits.remove(rabbit.getUUID());
                rabbitsBornTime.remove(rabbit.getUUID());
            }
        }
    }
}
