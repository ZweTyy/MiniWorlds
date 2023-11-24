import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.World;
import java.awt.Color;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.LinkedHashMap;
import itumulator.world.Location;

public class Main {

    public static void main(String[] args) {
        final Random r = new Random(); // Laver en ny random generator
        int size = 0; // Størrelsen på verdenen
        Map<String, Integer[]> elementsToAdd = new LinkedHashMap<>(); // Vi bruger linkedhashmap for at holde
                                                                      // rækkefølgen af elementer
        try {
            BufferedReader br = new BufferedReader(new FileReader("./data/input-filer/t1-1a.txt")); // Læser input
                                                                                                    // filen
            String line = br.readLine();
            if (line != null) { // Sætter værdien af size til det første tal i filen
                size = Integer.parseInt(line.trim());
            }
            while ((line = br.readLine()) != null && !line.isEmpty()) { // Derefter læser den resten af filen
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
        int delay = 500; // Forsinkelsen mellem hver skridt af simulationen (i ms)
        int display_size = 720; // Skærm opløsningen (i px)
        Program p = new Program(size, display_size, delay); // Opret et nyt program
        World world = p.getWorld(); // Hiv verdenen ud, som er der hvor vi skal tilføje ting!

        DisplayInformation grass = new DisplayInformation(Color.green, "grass");
        DisplayInformation rabbit = new DisplayInformation(Color.white, "rabbit-small");
        DisplayInformation burrow = new DisplayInformation(Color.black, "hole");
        DisplayInformation Location = new DisplayInformation(Color.black);
        p.setDisplayInformation(Grass.class, grass);
        p.setDisplayInformation(Rabbit.class, rabbit);
        p.setDisplayInformation(Burrow.class, burrow);
        p.setDisplayInformation(Location.class, Location);

        p.show();
        Rabbit.resetRabbitCount();
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
                    case ("burrow"):
                        createBurrow(world, size);
                        break;
                    default:
                        break;
                }
            }

        }
        int initialRabbitCount = countRabbits(world);
        System.out.println("Initial rabbit count: " + initialRabbitCount);

        // Kører simulationen 100 gange og opdaterer kanin tælleren hver gang
        for (int i = 0; i < 150; i++) {
            p.simulate();
            Rabbit.updateRabbitCount(world);
        }
    }

    public static void createRabbit(World world, int size) {
        Rabbit rabbit = new Rabbit(world, size); // Lav en ny kanin
        Location location = rabbit.getPlace(); // Tag kaninens lokation

        world.setTile(location, rabbit); // Placerer kaninen på det tilfældige lokation
    }

    public static void createGrass(World world, int size) {
        Grass grass = new Grass(world, size); // Lav græs
        Location location = grass.getPlace(); // Tag græssets lokation

        // Vi tjekker både at tile er tom og at der ikke allerede er græs på lokationen
        if (world.isTileEmpty(location) && !world.containsNonBlocking(location)) {
            world.setTile(location, grass);
        }
    }

    public static void createBurrow(World world, int size) {
        Burrow burrow = new Burrow(world, world.getSize()); // Lav en nyt hul
        Location location = (burrow.getPlace());

        if (world.isTileEmpty(location) && !world.containsNonBlocking(location)) {
            world.setTile(location, burrow);
        }
    }

    public static int countRabbits(World world) {
        int rabbitCount = 0;
        Map<Object, Location> entities = world.getEntities();
        for (Object obj : entities.keySet()) {
            if (obj instanceof Rabbit) {
                rabbitCount++;
            }
        }
        return rabbitCount;
    }

}
