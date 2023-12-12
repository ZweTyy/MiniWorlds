import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.World;
import java.awt.Color;

import java.util.Map;
import itumulator.world.Location;

public class Main {

    public static void main(String[] args) {
        InputParser parser = new InputParser("./data/input-filer/t1-1a.txt"); // Opret en ny parser
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
        DisplayInformation bushDisplayInfo = new DisplayInformation(Color.black, "bush-berries");
        DisplayInformation LocationDisplayInfo = new DisplayInformation(Color.black);
        p.setDisplayInformation(Grass.class, grassDisplayInfo);
        p.setDisplayInformation(Rabbit.class, rabbitDisplayInfo);
        p.setDisplayInformation(Burrow.class, burrowDisplayInfo);
        p.setDisplayInformation(Wolf.class, wolfDisplayInfo);
        p.setDisplayInformation(Bear.class, bearDisplayInfo);
        p.setDisplayInformation(Bush.class, bushDisplayInfo);
        p.setDisplayInformation(Location.class, LocationDisplayInfo);

        p.show();
        Rabbit.resetRabbitCount();

        EntityLoader.loadEntities(world, elementsToAdd, size); // Tilføj alle elementer til verdenen

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
}