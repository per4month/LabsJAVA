package Rabbit;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class AlbinosAI extends BaseAI {
    private int route = 1;

    public synchronized void run() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                update();
            }
        }, 0, 1000);
    }

    void update() {
        if (!isActive) {
            return;
        }

        Vector<Rabbit> rabbitsList = RabbitsStorage.getInstance().getRabbitVector();
        for (int i = 0; i < rabbitsList.size(); i++) {
            if (rabbitsList.get(i) instanceof RabbitAlbinos) {
                RabbitAlbinos rabbit = (RabbitAlbinos) rabbitsList.get(i);
                int rabbitX = rabbit.getX();
                int speed = 100;
                if (rabbitX + speed > 600) {
                    route = -1;
                }
                if (rabbitX - speed < 0) {
                    route = 1;
                }
                rabbit.setX(rabbitX + speed * route);
            }
        }
    }
}
