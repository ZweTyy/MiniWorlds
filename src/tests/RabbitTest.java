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
        rabbit.setHunger(50);
        Location rabbitLocation = rabbit.getLocation();
        Grass grass = new Grass(world, 1);
        world.setTile(rabbitLocation, grass);
        world.setTile(rabbitLocation, rabbit);

        double initialEnergy = rabbit.getEnergy();
        rabbit.eatHerb(world);

        assertTrue("Rabbit's energy should increase after eating", rabbit.getEnergy() > initialEnergy);
    }

    @Test
    public void testMoveWhenFullySurrounded() {
        world = new World(3);
        Location centerLocation = new Location(1, 1); // Center of the 3x3 grid
        rabbit = new Rabbit(world, 3);
        rabbit.setLocation(centerLocation);
        world.setTile(centerLocation, rabbit); // Place the rabbit at the center

        // Surround the rabbit with other rabbits on all sides, including diagonals
        world.setTile(new Location(1, 0), new Rabbit(world, 3)); // North
        world.setTile(new Location(1, 2), new Rabbit(world, 3)); // South
        world.setTile(new Location(2, 1), new Rabbit(world, 3)); // East
        world.setTile(new Location(0, 1), new Rabbit(world, 3)); // West

        // Diagonal positions
        world.setTile(new Location(0, 0), new Rabbit(world, 3)); // Northwest
        world.setTile(new Location(2, 0), new Rabbit(world, 3)); // Northeast
        world.setTile(new Location(0, 2), new Rabbit(world, 3)); // Southwest
        world.setTile(new Location(2, 2), new Rabbit(world, 3)); // Southeast

        // Call the move method
        rabbit.move(world);

        // Assert that the rabbit's location remains unchanged
        assertEquals(centerLocation, rabbit.getLocation());
    }

}
