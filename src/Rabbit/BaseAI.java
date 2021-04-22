package Rabbit;

import java.util.Timer;

public abstract class BaseAI extends Thread {
    protected Timer timer;
    protected  boolean isActive = true;

    public synchronized  void startAI() {
        isActive = true;
    }

    public synchronized  void stopAI(){
        isActive = false;
        notify(); //освобождение монитора потока
    }

    public boolean isAIActive() { //активность потока
        return isActive;
    }

    public void setAIPriority(int priority) {
        setPriority(priority);
    }

    public int getAIPriority() {
        return getPriority();
    }
}
