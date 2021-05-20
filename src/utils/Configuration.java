package utils;

import Habitat.Habitat;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Configuration {
    private static String configFileName = "config.txt";
    private static File configFile = new File(configFileName);

    public static void saveConfig() {
        try (
                FileWriter writer = new FileWriter(configFileName, false)
        ) {
            String text = Habitat.getN1() + " // Generation time (sec) of ordinary rabbit\n" +
                    Habitat.getP1() + " // Probability (%) to born ordinary rabbit\n" +
                    Habitat.getN2() + " // Generation time (sec) of albinos rabbit\n" +
                    Habitat.getK() + " // Probability (%) to born albinos rabbit\n" +
                    Habitat.getD1() + " // Lifetime of ordinary rabbit\n" +
                    Habitat.getD2() + " // Lifetime period of albinos rabbit\n";
            writer.append(text);
            writer.flush();
        } catch (FileNotFoundException eFileNotFound) {
            System.err.println("Error: file was not found while saving config");
        } catch (IOException eIO) {
            System.err.println("Error: something went wrong with IO while saving config");
        }
    }
    public static File getConfigFile() {
        return configFile;
    }
    public static ArrayList<Integer> loadConfig() {
        ArrayList<Integer> parameters = new ArrayList<>();

        try(
                Scanner scanner = new Scanner(configFile)
        ) {
            while (scanner.hasNextLine()) {
                String string = scanner.nextLine();
                Scanner intScanner = new Scanner(string);
                parameters.add(intScanner.nextInt());
            }
        } catch (IOException eIO) {
            System.err.println("Error: something went wrong while loading config");
        }

        return parameters;
    }
}
