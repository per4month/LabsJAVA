package Rabbit;


public class FactorySimple implements Factory {
    @Override
    public Rabbit rabbitBorn(int x, int y, String pathToImg, int birthTime, int deathTime) {
        return new RabbitSimple(x,y, pathToImg, birthTime, deathTime);
    }
}
