package Rabbit;

public class FactoryAlbinos implements Factory {
    @Override
    public Rabbit rabbitBorn(int x, int y, String pathToImg, int birthTime, int deathTime) {
        return new RabbitAlbinos(x, y, pathToImg, birthTime, deathTime);
    }
}
