package Habitat;

import java.awt.*;
import java.util.*;

import Rabbit.*;
import View.*;
import bornProcess.bornProcess;
import Controller.Controller;

public class Habitat {
    private static int N1;
    private static int N2;
    private static int P1;
    //private static double K;
    private static int K;
    private static int D1;
    private static int D2;
    private Controller controller;
    private int WIDTH = 600;
    private int HEIGHT = 600;
    private MyFrame myframe;
    final private String pathToSimple = "src/Resources/Simple.png";
    final private String pathToAlbinos = "src/Resources/Albinos.png";
    private Timer timer = new Timer();
    private static int time = 0;
    //private int pastTime = 0;
    private boolean bornProcessOn = false;
    bornProcess bornProcess = new bornProcess(this);

    public Habitat(int N1, int N2, int P1, int K, MyFrame myframe, int D1, int D2) {
        this.N1 = N1;
        this.N2 = N2;
        this.P1 = P1;
        this.K = K;
        this.D1 = D1;
        this.D2 = D2;
        this.myframe = myframe;
    }

    public Habitat() {
        this.N1 = 0;
        this.N2 = 0;
        this.P1 = 0;
        this.K = 0;
        this.D1 = 0;
        this.D2 = 0;
    }

    private Point generatePoint() {
        int frameWidth = myframe.getWidth(), frameHeight = myframe.getHeight();

        int withBorder = myframe.getWidth() - 500; //change 500 to ContropPanel size
        int x = (int) (Math.random() * (withBorder - 100));
        int y = (int) (Math.random() * (myframe.getHeight() - 150)); //crutch
        return new Point(x, y);
    }

    boolean isSimpleBorn(int N1, int P1, int time) {
        int prob = (int) (Math.random() * 100 + 1);
        return prob <= P1 && time % N1 == 0;
    }

    boolean isAlbinosBorn(int N2, int K, int time) {
        return time % N2 == 0 && RabbitAlbinos.countOfAlbinos < Rabbit.countOfRabbits * ((double)K / 100);
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

    public void update(int time) {
        Factory fact;
        RabbitsStorage storage = RabbitsStorage.getInstance();
        this.time = time;

        controller.passTime(time);

        if (isAlbinosBorn(N2, K, time / 100) && time % 100 == 0) {
            fact = new FactoryAlbinos();
            Point randomPoint = generatePoint();
            Rabbit newRabbit = fact.rabbitBorn(randomPoint.x, randomPoint.y, pathToAlbinos, time, time + D2 * 100);
            storage.getRabbitVector().add(newRabbit);
            storage.getAliveRabbits().add(newRabbit.getUUID());
            storage.getRabbitsBornTime().put(newRabbit.getUUID(), newRabbit.getBirthTime());
        }
        if (isSimpleBorn(N1, P1, time / 100) && time % 100 == 0) {
            fact = new FactorySimple();
            Point randomPoint = generatePoint();
            Rabbit newRabbit = fact.rabbitBorn(randomPoint.x, randomPoint.y, pathToSimple, time, time + D1 * 100);
            storage.getRabbitVector().add(newRabbit);
            storage.getAliveRabbits().add(newRabbit.getUUID());
            storage.getRabbitsBornTime().put(newRabbit.getUUID(), newRabbit.getBirthTime());
        }

        if (time % 100 == 0) storage.removeRabbits(time / 100);
        controller.toPaint(storage.getRabbitVector());
    }

    public void refreshRabbitPopulation() { // не обновляется вектор: не чистится нигде, не могу найти где он толком создаётся, чтоб его почистить (ну и остальные хранилища)
        RabbitSimple.countOfSimple = 0;
        RabbitAlbinos.countOfAlbinos = 0;
        Rabbit.countOfRabbits = 0;
        RabbitsStorage.getInstance().reset();
    }

    public void startBorn() {
        bornProcessOn = true;
        //timer.schedule(bornProcess,0, 1000);
        timer.schedule(bornProcess, 0, 10);
    }

    public void stopBorn() {
        timer.cancel();
        timer.purge();
        timer = new Timer();
        bornProcess = new bornProcess(this);
        RabbitsStorage.getInstance().reset();
        bornProcessOn = false;
    }

    public void pauseBorn() {
        //pastTime = bornProcess.getGameSec();
        timer.cancel();
        timer.purge();
    }

    public void resumeBorn() {
        timer = new Timer();
        bornProcess = new bornProcess(this, time);
        timer.schedule(bornProcess, 0, 100);
    }

    public static void setN1(int N1) {
        Habitat.N1 = N1;
    }

    public static void setN2(int N2) {
        Habitat.N2 = N2;
    }

    public static void setP1(int P1) {
        Habitat.P1 = P1;
    }

    public static void setK(int K) {
        Habitat.K = K;
    }

    public static void setD1(int D1) {
        Habitat.D1 = D1;
    }

    public static void setD2(int D2) {
        Habitat.D2 = D2;
    }

    public static int getTime() {
        return time;
    }

    public static int getD1() {
        return D1;
    }

    public static int getD2() {
        return D2;
    }

    public static int getN1() {
        return N1;
    }

    public static int getN2() {
        return N2;
    }

    public static int getP1() {
        return P1;
    }

    public static int getK(){
        return K;
    }

    public void confifureController(Controller controller) {
        this.controller = controller;
    }

}
