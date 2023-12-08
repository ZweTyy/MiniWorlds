import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.World;
import java.awt.Color;
import java.awt.geom.QuadCurve2D;
import java.util.Random;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.Arrays;
import java.util.LinkedHashMap;
import itumulator.world.Location;

public class Main {

    public static void main(String[] args) {
        InputParser parser = new InputParser("./data/input-filer/t2-1c.txt"); // Opret en ny parser
        Map<String, Integer[]> elementsToAdd = parser.parseInput(); // Kør parseren og få en map med elementer der skal
                                                                    // tilføjes
        int size = parser.getSize(); // Hiv størrelsen på verdenen ud
        int delay = 1000; // Forsinkelsen mellem hver skridt af simulationen (i ms)
        int display_size = 720; // Skærm opløsningen (i px)
        Program p = new Program(size, display_size, delay); // Opret et nyt program
        World world = p.getWorld(); // Hiv verdenen ud, som er der hvor vi skal tilføje ting!

        DisplayInformation grassDisplayInfo = new DisplayInformation(Color.green, "grass");
        DisplayInformation rabbitDisplayInfo = new DisplayInformation(Color.white, "rabbit-small");
        DisplayInformation burrowDisplayInfo = new DisplayInformation(Color.black, "hole");
        DisplayInformation wolfDisplayInfo = new DisplayInformation(Color.black, "wolf");
        DisplayInformation bearDisplayInfo = new DisplayInformation(Color.black, "bear");
        DisplayInformation berryDisplayInfo = new DisplayInformation(Color.black, "bush-berries");
        DisplayInformation LocationDisplayInfo = new DisplayInformation(Color.black);
        p.setDisplayInformation(Grass.class, grassDisplayInfo);
        p.setDisplayInformation(Rabbit.class, rabbitDisplayInfo);
        p.setDisplayInformation(Burrow.class, burrowDisplayInfo);
        p.setDisplayInformation(Wolf.class, wolfDisplayInfo);
        p.setDisplayInformation(Bear.class, bearDisplayInfo);
        p.setDisplayInformation(Berry.class, berryDisplayInfo);
        p.setDisplayInformation(Location.class, LocationDisplayInfo);

        p.show();
        Rabbit.resetRabbitCount();

        // Vi iterer igennem alle elementer der skal tilføjes
        for (Map.Entry<String, Integer[]> entry : elementsToAdd.entrySet()) {
            Location location;
            String type = entry.getKey();
            Integer[] quantityRange = entry.getValue();
            int quantity = quantityRange[0];
            for (int i = 0; i < quantity; i++) {
                switch (type) {
                    case ("rabbit"):
                        Rabbit rabbit = RabbitFactory.createRabbit(world, size);
                        location = rabbit.getLocation();
                        placeEntity(world, rabbit, size);
                        break;
                    case ("grass"):
                        Grass grass = GrassFactory.createGrass(world, size);
                        location = grass.getLocation();
                        placeEntity(world, grass, size);
                        break;
                    case ("burrow"):
                        Burrow burrow = BurrowFactory.createBurrow(world, size);
                        location = burrow.getLocation();
                        placeEntity(world, burrow, size);
                        break;
                    case ("wolf"):
                        Wolf wolf = WolfFactory.createWolf(world, size);
                        location = wolf.getLocation();
                        placeEntity(world, wolf, size);
                        break;
                    case ("bear"):
                        Integer x = quantityRange.length > 2 ? quantityRange[1] : null;
                        Integer y = quantityRange.length > 2 ? quantityRange[2] : null;
                        Bear bear;
                        if (x != null && y != null) {
                            bear = BearFactory.createBear(world, size, x, y);
                        } else {
                            bear = BearFactory.createBear(world, size);
                        }
                        location = bear.getLocation();
                        placeEntity(world, bear, size);
                        break;

                    case ("berry"):
                        Berry berry = BerryFactory.createBerry(world, size);
                        location = berry.getLocation();
                        placeEntity(world, berry, size);
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

    public static void placeEntity(World world, Entity entity, int size) {
        Location location = entity.getLocation();
        if (world.isTileEmpty(location)) {
            world.setTile(location, entity);
        }
    }
}
