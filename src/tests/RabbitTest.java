package tests;
import org.junit.Before;
import org.junit.Test;

import entities.Grass;
import entities.Rabbit;
import itumulator.world.Location;
import itumulator.world.World;

import static org.junit.Assert.*;

public class RabbitTest {

    private Rabbit rabbit;
    private World world;


    @Test
    public void createRabbit() {
        world = new World(10);
        rabbit = new Rabbit(world, 10);
        assertNotNull("Rabbit should not be null", rabbit);
    }

    @Test
    public void rabbitLocationShouldChange() {
        world = new World(10);
        rabbit = new Rabbit(world, 10);
        Location initialLocation = new Location(5, 5);
        rabbit.move(world);

        Location newLocation = rabbit.getLocation();
        assertNotEquals("Rabbit should move to a new location", initialLocation, newLocation);
    }

    @Test
    public void testReproduce() {
        // Clear surrounding tiles
        for (int x = 4; x <= 6; x++) {
            for (int y = 4; y <= 6; y++) {
                if (x == 5 && y == 5)
                    continue; // Skip the tile of the main rabbit
                Location emptyLocation = new Location(x, y);
                world.setTile(emptyLocation, null); // Ensure these tiles are empty
            }
        }

        // Setup the mating rabbit
        Rabbit matingRabbit = new Rabbit(world, 10);
        matingRabbit.setAge(4);
        matingRabbit.setEnergy(100);
        matingRabbit.setHasReproducedThisTurn(false);
        Location matingRabbitLocation = new Location(5, 4);
        world.setTile(matingRabbitLocation, matingRabbit);

        // Setup the main rabbit
        rabbit.setAge(4);
        rabbit.setEnergy(100);
        rabbit.setHasReproducedThisTurn(false);
        Location rabbitLocation = new Location(5, 5);
        world.setTile(rabbitLocation, rabbit);

        int initialRabbitCount = Rabbit.countRabbits(world);
        rabbit.reproduce(world, world.getSize());

        int newRabbitCount = Rabbit.countRabbits(world);
        assertTrue("Rabbit count should increase after reproduction", newRabbitCount > initialRabbitCount);
    }

    @Test
    public void testEatHerb() {
        // Setup conditions for eating (e.g., place a Grass object at rabbit's location)
        world = new World(1);
        rabbit = new Rabbit(world, 1);
        Location rabbitLocation = rabbit.getLocation();
        Grass grass = new Grass(world, 1);
        world.setTile(rabbitLocation, grass); // Assuming method to place grass in the world

        double initialEnergy = rabbit.getEnergy();
        rabbit.eatHerb(world);

        assertTrue("Rabbit's energy should increase after eating", rabbit.getEnergy() > initialEnergy);
    }

}