import itumulator.display.utility.ImageResourceCache;
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.World;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Map;
import java.util.LinkedHashMap;
import itumulator.world.Location;
import java.net.URL;

public class Main {

    public static void main(String[] args) {

        final Random r = new Random();
        int size = 0; // Størrelsen af verdenen (i tiles) (dette er kvadratisk)
        Map<String, Integer[]> elementsToAdd = new LinkedHashMap<>(); // Vi bruger linkedhashmap for at holde
                                                                      // rækkefølgen af elementer

        try {
            BufferedReader br = new BufferedReader(new FileReader("./data/input-filer/t1-1c.txt")); // Læser input filen
            String line = br.readLine();
            if (line != null) { // Sætter værdien af size til det første tal i filen
                size = Integer.parseInt(line.trim());
            }
            while ((line = br.readLine()) != null) { // Derefter læser den resten af filen
                String[] parts = line.split(" ");
                String[] ranges = parts[1].split("-");
                if (ranges.length == 1) {
                    elementsToAdd.put(parts[0],
                            new Integer[] { Integer.parseInt(ranges[0]), Integer.parseInt(ranges[0]) });
                }
                if (ranges.length == 2) {
                    int min = Integer.parseInt(ranges[0]);
                    int max = Integer.parseInt(ranges[1]);
                    elementsToAdd.put(parts[0], new Integer[] { min, r.nextInt(max - min + 1) + min });
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        int delay = 1000; // Forsinkelsen mellem hver skridt af simulationen (i ms)
        int display_size = 800; // Skærm opløsningen (i px)
        Program p = new Program(size, display_size, delay); // Opret et nyt program
        World world = p.getWorld(); // Hiv verdenen ud, som er der hvor vi skal tilføje ting!

        DisplayInformation grass = new DisplayInformation(Color.green, "grass");
        DisplayInformation rabbit = new DisplayInformation(Color.white, "rabbit-small");
        DisplayInformation Location = new DisplayInformation(Color.black);
        p.setDisplayInformation(Grass.class, grass);
        p.setDisplayInformation(Rabbit.class, rabbit);
        p.setDisplayInformation(Location.class, Location);

        p.show(); // Viser selve simulationen

        // Vi iterer igennem alle elementer der skal tilføjes
        for (Map.Entry<String, Integer[]> entry : elementsToAdd.entrySet()) {
            String type = entry.getKey();
            Integer[] quantityRange = entry.getValue();
            int quantity = quantityRange[1];

            for (int i = 0; i < quantity; i++) {
                switch (type) {
                    case ("rabbit"):
                        createRabbit(world, size);
                        break;
                    case ("grass"):
                        createGrass(world, size);
                        break;
                    default:
                        break;
                }
            }

        }

        // This needs to be implemented further
        // boolean isNight = true;
        // if (isNight) {
        // world.delete(rabbit);
        // }

        // Runs 50 loops of the simulation
        for (int i = 0; i < 50; i++) {
            p.simulate();
        }
    }

    public static void createRabbit(World world, int size) {
        Rabbit rabbit = new Rabbit(world, size); // Lav en ny kanin
        world.setTile(rabbit.getPlace(), rabbit); // Placerer kaninen på det tilfældige lokation
    }

    public static void createGrass(World world, int size) {
        Grass grass = new Grass(world, size); // Lav en nyt græs
        world.setTile(grass.getPlace(), grass); // Placerer græsset på det tilfældige lokation
    }
}
