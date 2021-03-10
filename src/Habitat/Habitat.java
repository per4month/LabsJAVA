package Habitat;

import java.awt.*;
import java.util.*;
import java.util.Timer;
import java.util.Random;

import Rabbit.*;

public class Habitat {
    private int N1;
    private int N2;
    private int P1;
    private double K;
    //private Controller controller
    private int WIDTH = 600;
    private int HEIGHT = 600;
    //private MyFrame myframe
    final private String pathToSimple = "src/Resources/Simple.png";
    final private String pathToAlbinos = "src/Resources/Albinos.png";
    private Vector<Rabbit> rabbitVector = new Vector();
    private Timer timer = new Timer();
    private boolean bornProcessOn = false;
    //BornProcess bornProcess = new BornProcess(this);

    public Habitat(int N1, int N2, int P1, double K, MyFrame myframe) {
        this.N1 = N1;
        this.N2 = N2;
        this.P1 = P1;
        this.K = K;
        this.myframe = myframe;
    }

    private Point generatePoint() {
        Random rnd = new Random();
        int x = (int) (Math.random() * (myframe.getWidth() - 99));
        int y = (int) (Math.random() * (myframe.getHeight() - 99));
        return new Point(x, y);
    }



}
