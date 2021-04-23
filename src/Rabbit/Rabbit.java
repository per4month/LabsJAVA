package Rabbit;

import java.util.UUID;

public abstract class Rabbit implements IBehaviour {
    public static int countOfRabbits = 0;
    private String pathToImg;
    private int birthTime;
    private int deathTime;
    private String uuid;
    private int x;
    private int y;
    private int routeX;
    private int routeY;


    Rabbit(int x, int y, String pathToImg, int birthTime, int deathTime) {
        this.x = x;
        this.y = y;
        this.pathToImg = pathToImg;
        this.birthTime = birthTime;
        this.deathTime = deathTime;
        this.uuid = UUID.randomUUID().toString();
        this.routeX = 1;
        this.routeY = 1;
        //countOfRabbits++;
    }

    public String getPathToImg() {
        return pathToImg;
    }

    public int getBirthTime() {
        return birthTime;
    }

    public int getDeathTime() {
        return deathTime;
    }

    public String getUUID() {
        return uuid;
    }

    public int getRoteX() {
        return routeX;
    }

    public int getRoteY() {
        return routeY;
    }

    public void setRoteX(int rote) {
        routeX = rote;
    }

    public void setRoteY(int rote) {
        routeY = rote;
    }

    @Override
    public void move(int x, int y) {

    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }
}