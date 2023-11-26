import org.junit.Before;
import org.junit.Test;

import itumulator.world.Location;
import itumulator.world.World;

import static org.junit.Assert.*;

public class RabbitTest {

    private Rabbit rabbit;
    private World world;

    @Before
    public void setUp() {
        world = new World(10); // Assuming the World constructor takes the size as a parameter
        rabbit = new Rabbit(world, 10);
    }

    @Test
    public void testMove() {
        Location initialLocation = rabbit.getLocation();
        rabbit.move(world);

        // Assuming move changes rabbit's location
        Location newLocation = rabbit.getLocation();
        assertNotEquals("Rabbit should move to a new location", initialLocation, newLocation);
    }

    @Test
    public void testReproduce() {
        // Setup rabbit with necessary conditions for reproduction
        rabbit.setAge(4); // Assuming there's a setter for age
        rabbit.setEnergy(30); // Assuming there's a setter for energy

        int initialRabbitCount = Rabbit.countRabbits(world);
        rabbit.reproduce(world, 10);

        // Assuming that reproduce adds a new rabbit to the world
        int newRabbitCount = Rabbit.countRabbits(world);
        assertTrue("Rabbit count should increase after reproduction", newRabbitCount > initialRabbitCount);
    }

    @Test
    public void testEatHerb() {
        // Setup conditions for eating (e.g., place a Grass object at rabbit's location)
        Location rabbitLocation = rabbit.getLocation();
        Grass grass = new Grass(world, 10);
        world.setTile(rabbitLocation, grass); // Assuming method to place grass in the world

        double initialEnergy = rabbit.getEnergy(); // Assuming getter for energy
        rabbit.eatHerb(world);

        assertTrue("Rabbit's energy should increase after eating", rabbit.getEnergy() > initialEnergy);
    }

}
