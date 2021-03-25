package Rabbit;


import java.util.ArrayList;

class RabbitsStorage {
    private static RabbitsStorage instance;
    private ArrayList<Rabbit> rabbitsList;

    private RabbitsStorage() {
        this.rabbitsList = new ArrayList<>();
    }

    public static RabbitsStorage getInstance() {
        if (instance == null) {
            instance = new RabbitsStorage();
        }
        return instance;
    }

    public ArrayList<Rabbit> getRabbitsList() {
        return rabbitsList;
    }
}
