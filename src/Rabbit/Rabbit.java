package Rabbit;

public abstract class Rabbit implements IBehaviour {
    public static int countOfRabbits = 0;
    private String pathToImg;
    private int x;
    private int y;

    Rabbit(int x, int y, String pathToImg) {
        this.x = x;
        this.y = y;
        this.pathToImg = pathToImg;
        //countOfRabbits++;
    }

    public String getPathToImg() {
        return pathToImg;
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