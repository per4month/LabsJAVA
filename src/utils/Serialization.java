package utils;

import Rabbit.RabbitsStorage;
import Habitat.Habitat;
import Rabbit.Rabbit;

import java.io.*;
import java.util.Vector;

public class Serialization {
    private File file = new File("rabbitsData");

    public Serialization() {
    }

    public void serialize(){
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ) {
            Vector<Rabbit> rabbitsVector = RabbitsStorage.getInstance().getRabbitVector();
            objectOutputStream.writeObject(rabbitsVector);
        } catch (FileNotFoundException eFileNotFound) {
            System.err.println("Error: file serialized.dat not found while serializing.");
        } catch (IOException eIO) {
            System.err.println("Error: IOException while serializing");
        } catch (Exception ex) {
            System.err.println("Error: something happened while serializing");
        }
    }

    public void deserialize() {
        Habitat habitat = new Habitat();
        try (
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)
        ) {
            RabbitsStorage.getInstance().reset(); //обнуляем счетчики кроликов

            Vector<Rabbit> a = (Vector<Rabbit>)objectInputStream.readObject();
            RabbitsStorage.getInstance().setAllRabbits(a);
            Vector<Rabbit> rabbitsList = RabbitsStorage.getInstance().getRabbitVector();

            if (!rabbitsList.isEmpty()) {
                for (int i = 0; i < rabbitsList.size(); i++) {
                    Rabbit rabbit = rabbitsList.get(i);
                    int deathTime = rabbit.getDeathTime() - rabbit.getBirthTime();
                    rabbit.setBirthTime(habitat.getTime());
                    rabbit.setDeathTime(rabbit.getBirthTime() + deathTime);
                }
            }
        } catch (FileNotFoundException eFileNotFound) {
            System.err.println("Error: file serialized.dat not found while serializing.");
        } catch (IOException eIO) {
            System.err.println("Error: IOException while serializing");
            eIO.printStackTrace();
        } catch (Exception ex) {
            System.err.println("Error: something happened while serializing");
        }
    }

}
