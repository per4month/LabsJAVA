package Rabbit;

import Rabbit.RabbitsStorage;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class OrdinaryAI extends BaseAI {
    //private int routeX = 1;
    //private int routeY = 1;

    public synchronized void run(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                update();
            }
        }, 0, 10); //возможно потребуется изменить
        Timer changeRouteTimer = new Timer();
        changeRouteTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                changeRoute();
            }
        }, 0, 3000); //возможно потребуется изменить
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
                int speed = 1;
                if (rabbitX + speed > 712) {
                    rabbit.setRoteX(-1);
                }

                if (rabbitX - speed < 0) {
                    rabbit.setRoteX(1);
                }

                if (rabbitY + speed > 424) {
                    rabbit.setRoteY(-1);
                }

                if (rabbitY - speed < 0) {
                    rabbit.setRoteY(1);
                }

                rabbit.setX(rabbitX + speed * rabbit.getRoteX());
                rabbit.setY(rabbitY + speed * rabbit.getRoteY());
            }
        }
    }

    private void changeRoute() {
        int max = 1;
        int min = 1;
        Vector<Rabbit> rabbitsList = RabbitsStorage.getInstance().getRabbitVector();
        for (int i = 0; i < rabbitsList.size(); i++) {
            if (rabbitsList.get(i) instanceof RabbitSimple) {
                RabbitSimple rabbit = (RabbitSimple) rabbitsList.get(i);
                rabbit.setRoteX((int) (Math.random() * ++max) + min);
                rabbit.setRoteY((int) (Math.random() * ++max) + min);
            }
        }
        //routeX = (int) (Math.random() * ++max) + min;
        //routeY = (int) (Math.random() * ++max) + min;
    }
}
