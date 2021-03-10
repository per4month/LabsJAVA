package bornProcess;


import Habitat.Habitat;

import java.util.TimerTask;

public class bornProcess extends TimerTask {
    Habitat h;
    int sec;
    int gameSec;
    int min;

    public bornProcess(Habitat h) {
        this.h = h;
    }

    @Override
    public void run() {
        sec++;
        gameSec++;
        if (sec % 60 == 0) {
            min++;
            sec = 0;
        }
        h.update(gameSec);
    }
}