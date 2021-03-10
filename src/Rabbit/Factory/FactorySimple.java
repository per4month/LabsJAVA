package Rabbit.Factory;
import Rabbit.Rabbit;
import Rabbit.RabbitSimple;


public class FactorySimple implements Factory {
    @Override
    public Rabbit rabbitBorn(int x, int y, String pathToImg) {
        return new RabbitSimple(x,y, pathToImg);
    }
}
