package Rabbit;

import Rabbit.RabbitAlbinos;
import Rabbit.RabbitSimple;
import Rabbit.Rabbit;

import java.util.HashMap;
import java.util.TreeSet;
import java.util.Vector;

public class RabbitsStorage {
    private static RabbitsStorage instance; //сущность - объект
    //Коллекции
    private Vector<Rabbit> rabbitsList; //для хранения кроликов
    private TreeSet<String> aliveRabbits; //для хранения и поиска уникальных идентификаторов
    private HashMap<String,Integer> rabbitsBornTime; //для хранения времени рония объектов

    private RabbitsStorage() {
        this.aliveRabbits = new TreeSet<>();
        this.rabbitsBornTime = new HashMap<>();
        this.rabbitsList = new Vector<>();
    }

    public static RabbitsStorage getInstance() {
        if (instance == null) {
            instance = new RabbitsStorage();
        }
        return instance;
    }

    public Vector<Rabbit> getRabbitsList() {
        return rabbitsList;
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
        instance.rabbitsList = new Vector<>();
    }
    public void removeRabbits(int gameSec) {
        for (int i = 0; i < rabbitsList.size(); i++) {
            Rabbit rabbit = rabbitsList.get(i);
            if (gameSec == rabbit.getDeathTime()) {
                if (rabbit instanceof RabbitSimple) {
                    RabbitSimple.countOfSimple--;
                }
                else {
                    RabbitAlbinos.countOfAlbinos--;
                }
                Rabbit.countOfRabbits--;
                rabbitsList.remove(rabbit);
                aliveRabbits.remove(rabbit.getUUID());
                rabbitsBornTime.remove(rabbit.getUUID());
            }
        }
    }
}
