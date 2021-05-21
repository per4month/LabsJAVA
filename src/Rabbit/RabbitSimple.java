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
            image = ImageIO.read(new File("src/Resources/Simple.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    RabbitSimple(int x, int y, String pathToImg, int birthTime, int deathTime) {
        super(x, y, pathToImg, birthTime, deathTime);
        countOfSimple++;
        countOfRabbits++;

    }
    public RabbitSimple(int x, int y, String pathToImg, int birthTime, int deathTime, String _uuid, int _routeX, int _routeY) {
        super(x, y, pathToImg, birthTime, deathTime,_uuid, _routeX, _routeY);
        countOfSimple++;
        countOfRabbits++;
    }
}
