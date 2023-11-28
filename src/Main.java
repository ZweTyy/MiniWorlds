import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.World;
import java.awt.Color;
import java.awt.geom.QuadCurve2D;
import java.util.Random;

import org.junit.jupiter.params.shadow.com.univocity.parsers.conversions.IntegerConversion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.Arrays;
import java.util.LinkedHashMap;
import itumulator.world.Location;

public class Main {

    public static void main(String[] args) {
        final Random r = new Random(); // Laver en ny random generator
        int size = 0; // Størrelsen på verdenen
        Map<String, Integer[]> elementsToAdd = new LinkedHashMap<>(); // Vi bruger linkedhashmap for at holde
                                                                      // rækkefølgen af elementer
        try {
            BufferedReader br = new BufferedReader(new FileReader("./data/input-filer/t2-5a.txt")); // Læser input
                                                                                                    // filen
            String line = br.readLine();
            if (line != null) { // Sætter værdien af size til det første tal i filen
                size = Integer.parseInt(line.trim());
            }
            while ((line = br.readLine()) != null && !line.isEmpty()) { // Derefter læser den resten af filen
                String[] parts = line.split(" ");
                String[] ranges = parts[1].split("-");
                String[] coordinates = new String[2];
                Integer[] quantityRange = new Integer[4];
                if (ranges.length == 1) {
                    int quantity = Integer.parseInt(ranges[0]);
                    quantityRange = new Integer[] { quantity, quantity };
                }
                if (ranges.length == 2) {
                    int min = Integer.parseInt(ranges[0]);
                    int max = Integer.parseInt(ranges[1]);
                    quantityRange = new Integer[] { min, r.nextInt(max - min + 1) + min };
                }
                if (parts[0].equals("bear") && parts.length == 3 && parts[2].matches("\\(\\d+,\\d+\\)")) {
                    String coordinatePart = parts[2].substring(1, parts[2].length() - 1); // Fjerner paranteserne
                    coordinates = coordinatePart.split(","); // Splitter koordinaterne op
                    int x = Integer.parseInt(coordinates[0]);
                    int y = Integer.parseInt(coordinates[1]);
                    System.out.println("Parsed bear coordinates: " + x + ", " + y);
                    int quantity = Integer.parseInt(parts[1]);
                    quantityRange = new Integer[] { quantity, x, y };
                }
                elementsToAdd.put(parts[0], quantityRange);
            }
            br.close();
            System.out.println("Parsed elements to add:");
            for (Map.Entry<String, Integer[]> entry : elementsToAdd.entrySet()) {
                System.out.println(entry.getKey() + " => " + Arrays.toString(entry.getValue()));
            }
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
        DisplayInformation bear = new DisplayInformation(Color.black, "bear");
        DisplayInformation Location = new DisplayInformation(Color.black);
        p.setDisplayInformation(Grass.class, grass);
        p.setDisplayInformation(Rabbit.class, rabbit);
        p.setDisplayInformation(Burrow.class, burrow);
        p.setDisplayInformation(Bear.class, bear);
        p.setDisplayInformation(Location.class, Location);

        p.show();
        Rabbit.resetRabbitCount();

        // Vi iterer igennem alle elementer der skal tilføjes
        for (Map.Entry<String, Integer[]> entry : elementsToAdd.entrySet()) {
            String type = entry.getKey();
            Integer[] quantityRange = entry.getValue();
            int quantity = quantityRange[0];
            System.out.println("Creating " + quantity + " of " + type);
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
                    case ("wolf"):
                        break;
                    case ("bear"):
                        Integer x = quantityRange.length > 2 ? quantityRange[1] : null;
                        Integer y = quantityRange.length > 2 ? quantityRange[2] : null;
                        System.out.println("Attempting to create bear with x=" + x + ", y=" + y);
                        createBear(world, size, x, y);
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
        Location location = rabbit.getLocation(); // Tag kaninens lokation

        world.setTile(location, rabbit); // Placerer kaninen på det tilfældige lokation
    }

    public static void createGrass(World world, int size) {
        Grass grass = new Grass(world, size); // Lav græs
        Location location = grass.getLocation(); // Tag græssets lokation

        // Vi tjekker både at tile er tom og at der ikke allerede er græs på lokationen
        if (world.isTileEmpty(location) && !world.containsNonBlocking(location)) {
            world.setTile(location, grass);
        }
    }

    public static void createBurrow(World world, int size) {
        Burrow burrow = new Burrow(world, world.getSize()); // Lav en nyt hul
        Location location = (burrow.getLocation());

        if (world.isTileEmpty(location) && !world.containsNonBlocking(location)) {
            world.setTile(location, burrow);
        }
    }

    public static void createBear(World world, int size, Integer x, Integer y) {
        if (x != null && y != null && x <= size && y <= size) {
            Location location = new Location(x, y);
            Bear bear = new Bear(world, size, x, y);
            world.setTile(location, bear); // Placerer bjørnen på det angivne lokation
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
