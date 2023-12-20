package tests;

import org.junit.Before;
import org.junit.Test;

import entities.Grass;
import entities.Rabbit;
import itumulator.world.Location;
import itumulator.world.World;
import utilities.SimulationManager;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void testRabbitReproduction() {
        // Set up the world and initial conditions for rabbit reproduction
        world = new World(4);
        rabbit = new Rabbit(world, 4);
        Location initialLocation = new Location(0, 0);
        world.setTile(initialLocation, rabbit);

        Rabbit mateRabbit = new Rabbit(world, 4);
        Location mateLocation = new Location(0, 1);
        world.setCurrentLocation(initialLocation);
        world.setTile(mateLocation, mateRabbit);

        // Set the rabbit's properties to make it eligible for reproduction
        rabbit.setAge(4);
        rabbit.setEnergy(100);
        rabbit.setHasReproducedThisTurn(false); // Ensure the rabbit has not reproduced yet

        mateRabbit.setAge(4);
        mateRabbit.setEnergy(100);
        mateRabbit.setHasReproducedThisTurn(false); // Ensure the rabbit has not reproduced yet
        // Count rabbits before reproduction
        int initialRabbitCount = SimulationManager.countRabbits(world);

        // Perform the act method which should include an attempt to reproduce
        rabbit.act(world);

        // Update the rabbit count after the act
        SimulationManager.updateRabbitCount(world);

        // Assert that the number of rabbits has increased by at least one
        assertTrue(SimulationManager.getRabbitCount() > initialRabbitCount,
                "The rabbit count should increase after reproduction");

        // Additionally, assert that the rabbit has reproduced this turn
        assertTrue(rabbit.getHasReproducedThisTurn(),
                "The rabbit should have the flag set to true after reproducing");
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
