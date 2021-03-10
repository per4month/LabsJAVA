package Rabbit;

import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class RabbitSimple extends Rabbit {
    public static int countOfSimple = 0;
    static public Image image;

    static {
        try {
            image = ImageIO.read(new File("src/resources/Simple.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    RabbitSimple(int x, int y, String pathToImg) {
        super(x, y, pathToImg);
        countOfSimple++;
        countOfRabbits++;

    }
}
