package View;
import Rabbit.*;
import javax.swing.*;
import java.awt.*;
import java.util.Vector;
import Controller.Controller;

public class MyPanel extends JPanel {
    private Vector<Rabbit> rabbits = new Vector();
    Controller controller;

    public MyPanel() {
    super();
    }
    public void drawRabbits(Vector<Rabbit> rabbits) {
        this.rabbits = rabbits;
        repaint();
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for(int i =0; i < rabbits.size(); i++) {
            Rabbit rabbit = rabbits.get((i));
            if(rabbit instanceof RabbitSimple) {
                g.drawImage(RabbitSimple.image, rabbit.getX(), rabbit.getY(), null);
            }
            else {
                g.drawImage(RabbitAlbinos.image, rabbit.getX(), rabbit.getY(), null);
            }
        }
    }
    public void configureController(Controller controller) {
        this.controller = controller;
    }

}
