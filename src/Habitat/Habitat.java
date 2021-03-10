package Habitat;

import java.awt.*;
import java.util.*;
import java.util.Vector;
import java.util.Random;

import Rabbit.*;
import View.*;
import bornProcess.bornProcess;
import Controller.Controller;

public class Habitat {
    private int N1;
    private int N2;
    private int P1;
    private double K;
    private Controller controller;
    private int WIDTH = 600;
    private int HEIGHT = 600;
    private MyFrame myframe;
    final private String pathToSimple = "src/Resources/Simple.png";
    final private String pathToAlbinos = "src/Resources/Albinos.png";
    private Vector<Rabbit> rabbitVector = new Vector<>();
    private Timer timer = new Timer();
    private boolean bornProcessOn = false;
    bornProcess bornProcess = new bornProcess(this);

    public Habitat(int N1, int N2, int P1, double K, MyFrame myframe) {
        this.N1 = N1;
        this.N2 = N2;
        this.P1 = P1;
        this.K = K;
        this.myframe = myframe;
    }

    private Point generatePoint() {
        int x = (int) (Math.random() * (myframe.getWidth() - 99));
        int y = (int) (Math.random() * (myframe.getHeight() - 99));
        return new Point(x, y);
    }
    boolean isSimpleBorn(int N1, int P1, int time) {
        int prob = (int)(Math.random()*100 +1);
        return prob <= P1 && time % N1 == 0;
    }
    boolean isAlbinosBorn(int N2, double K, int time) {
        return N2 % time == 0 && K < (double)(RabbitAlbinos.countOfAlbinos / Rabbit.countOfRabbits)*100;
    }
    public int getWidth() {
        return WIDTH;
    }
    public int getHeight() {
        return HEIGHT;
    }
    public boolean isBornProcessOn() {
        return bornProcessOn;
    }
    public void update (int time) {
        Factory fact;
        controller.passTime(time);
        if(isAlbinosBorn(N1,K,time)) {
            fact = new FactoryAlbinos();
            Point randomPoint = generatePoint();
            Rabbit newRabbit = fact.rabbitBorn(randomPoint.x, randomPoint.y, pathToAlbinos);
            rabbitVector.add(newRabbit);
            controller.toPaint(rabbitVector);

        }
        if(isSimpleBorn(N1,P1,time)) {
            fact = new FactorySimple();
            Point randomPoint = generatePoint();
            Rabbit newRabbit = fact.rabbitBorn(randomPoint.x, randomPoint.y, pathToSimple);
            rabbitVector.add(newRabbit);
            controller.toPaint(rabbitVector);

        }
    }

    public void startBorn() {
        bornProcessOn = true;
        timer.schedule(bornProcess,0, 1000);
    }

    public void stopBorn() {
        timer.cancel();
        timer.purge();
        timer = new Timer();
        bornProcess = new bornProcess(this);
        rabbitVector  = new Vector();
        bornProcessOn = false;
    }

    public void refreshRabbitPopulation() {
        RabbitSimple.countOfSimple = 0;
        RabbitAlbinos.countOfRabbits = 0;
        Rabbit.countOfRabbits = 0;
    }

    public void confifureController(Controller controller) {
        this.controller = controller;
    }

}
