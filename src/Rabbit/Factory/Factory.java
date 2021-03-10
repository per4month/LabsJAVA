package Rabbit.Factory;

import Rabbit.Rabbit;

public interface Factory {
    Rabbit rabbitBorn(int x, int y, String pathToImg);
}
