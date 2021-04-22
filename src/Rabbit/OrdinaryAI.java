package Rabbit;

import Rabbit.RabbitsStorage;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class OrdinaryAI extends BaseAI {
    private int routeX = 1;
    private int routeY = 1;

    public synchronized void run(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                update();
            }
        }, 0, 1000);
        Timer changeRouteTimer = new Timer();
        changeRouteTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                changeRoute();
            }
        }, 0, 3000);
    }

    void update() {
        if (!isActive) {
            return;
        }

        Vector<Rabbit> rabbitsList = RabbitsStorage.getInstance().getRabbitVector();
        for (int i = 0; i < rabbitsList.size(); i++) {
            if (rabbitsList.get(i) instanceof RabbitSimple) {
                RabbitSimple rabbit = (RabbitSimple) rabbitsList.get(i);
                int rabbitX = rabbit.getX();
                int rabbitY = rabbit.getY();
                int speed = 30;
                if (rabbitX + speed > 600) {
                    routeX = -1;
                }
                if (rabbitX - speed < 0) {
                    routeX = 1;
                }
                if (rabbitY + speed > 600) {
                    routeY = -1;
                }
                if (rabbitY - speed < 0) {
                    routeY = 1;
                }
                rabbit.setX(rabbitX + speed * routeX);
                rabbit.setY(rabbitY + speed * routeY);
            }
        }
    }

    private void changeRoute() {
        int max = 1;
        int min = 1;
        routeX = (int) (Math.random() * ++max) + min;
        routeY = (int) (Math.random() * ++max) + min;
    }
}
