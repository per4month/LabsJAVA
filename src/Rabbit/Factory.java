package Rabbit;

import Rabbit.Rabbit;

public interface Factory {
    Rabbit rabbitBorn(int x, int y, String pathToImg, int birthTime, int deathTime);
}
