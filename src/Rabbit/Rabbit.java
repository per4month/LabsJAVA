package Rabbit;

public abstract class Rabbit {
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
}
