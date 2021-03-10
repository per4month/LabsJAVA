package Rabbit;


public class FactorySimple implements Factory {
    @Override
    public Rabbit rabbitBorn(int x, int y, String pathToImg) {
        return new RabbitSimple(x,y, pathToImg);
    }
}
