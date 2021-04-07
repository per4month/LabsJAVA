package Rabbit;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class RabbitAlbinos extends Rabbit {
    public static int countOfAlbinos = 0;
    static public Image image;

    static {
        try {
            image = ImageIO.read(new File("src/Resources/Albinos.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    RabbitAlbinos(int x, int y, String pathToImg, int birthTime, int deathTime) {
        super(x, y, pathToImg, birthTime, deathTime);
        countOfAlbinos++;
        countOfRabbits++;
    }

}