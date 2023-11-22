import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import java.awt.Color;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class Main {

    public static void main(String[] args) {

        int objects = 10; // Nummer af objekter der skal genereres
        Random r = new Random(); // Laver en ny random generator
        int size = 0; // Størrelsen af verdenen (i tiles) (dette er kvadratisk)
        Map<String, Integer[]> elementsToAdd = new LinkedHashMap<>(); // To maintain insertion order

        try {
            BufferedReader br = new BufferedReader(new FileReader("./data/input-filer/t1-2a.txt")); // Læser input filen
            String line = br.readLine();
            if (line != null) { // On the first line, set size to be the first number in the file
                size = Integer.parseInt(line.trim());
            }
            while ((line = br.readLine()) != null) { // Read then the other lines in the file and store them in a map
                String[] parts = line.split(" ");
                if (parts.length == 2) { // If there is a range min-max
                    String[] range = parts[1].split("-");
                    int min = Integer.parseInt(range[0]);
                    int max = Integer.parseInt(range[1]);
                    elementsToAdd.put(parts[0], new Integer[] { min, r.nextInt(max - min + 1) + min });
                } else if (parts.length == 1) { // If there is an exact amount
                    elementsToAdd.put(parts[0],
                            new Integer[] { Integer.parseInt(parts[1]), Integer.parseInt(parts[1]) });
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        // Now, 'size' holds the size of the world, and 'elementsToAdd' contains the
        // types and quantities
        // You can iterate over 'elementsToAdd' to add elements to your world
        for (Map.Entry<String, Integer[]> entry : elementsToAdd.entrySet()) {
            String type = entry.getKey();
            Integer[] quantityRange = entry.getValue();
            int quantity = quantityRange[1]; // This holds either the exact number or a random number within the range

            // Add 'quantity' of 'type' to the world
            // Your code to add elements to the world goes here
        }

        int delay = 1000; // Forsinkelsen mellem hver skridt af simulationen (i ms)
        int display_size = 800; // Skærm opløsningen (i px)
        Program p = new Program(size, display_size, delay); // Opret et nyt program
        World world = p.getWorld(); // Hiv verdenen ud, som er der hvor vi skal tilføje ting!

        DisplayInformation di = new DisplayInformation(Color.red);
        p.setDisplayInformation(Rabbit.class, di);
        p.show(); // Viser selve simulationen

        Rabbit rabbit = null;
        Grass grass = null;

        // Create the objects
        for (int i = 0; i < objects; i++) {
            rabbit = new Rabbit(); // Lav en ny kanin
            int x = r.nextInt(size); // Lav et tilfældigt x koordinat
            int y = r.nextInt(size); // Lav et tilfældigt y koordinat
            Location place = new Location(x, y); // Lav ny lokation med de tilfældige koordinater
            while (!world.isTileEmpty(place)) { // While tile ikke er tom
                x = r.nextInt(size);
                y = r.nextInt(size);
                place = new Location(x, y);
            }
            world.setTile(place, rabbit); // Placerer kaninen på den tilfældige lokation
        }

        // This needs to be implemented further
        boolean isNight = true;
        if (isNight) {
            world.delete(rabbit);
        }
        // Runs 50 loops of the simulation
        for (int i = 0; i < 50; i++) {
            p.simulate();
        }
    }
}