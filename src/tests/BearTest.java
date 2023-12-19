package tests;

import entities.Bear;
import entities.Berry;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runners.Parameterized.BeforeParam;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BearTest {

    private World world;
    private Bear bear;
    private Location initialLocation;

    @BeforeEach
    public void setUp() {
        // Initialize the world and bear before each test
        int size = 10; // Define the size of the world
        world = new World(size);
        initialLocation = new Location(5, 5); // Set initial bear location
        bear = new Bear(world, size, initialLocation.getX(), initialLocation.getY());
        world.setTile(initialLocation, bear);
    }

    @Test
    public void testBearMovementWithinTerritory() {
        bear.act(world); // The act method includes the move

        Location newLocation = bear.getLocation();
        assertTrue(newLocation.getX() >= 2 && newLocation.getX() <= 8,
                "Bear should move within X bounds of its territory");
        assertTrue(newLocation.getY() >= 2 && newLocation.getY() <= 8,
                "Bear should move within Y bounds of its territory");
    }

    @Test
    public void testBearEatingBerries() {
        Location berryLocation = new Location(5, 6); // Assume this tile is within the bear's territory
        Berry berry = new Berry(world, 10);
        
        world.setTile(berryLocation, berry); // Place the berry in the world
        bear.setHunger(50);
        double initialHunger = bear.getHunger();
        
        bear.eatHerb(world);

        assertFalse(berry.hasBerries(), "Bear should have eaten the berries");
        assertTrue(bear.getHunger() > initialHunger, "Bear's hunger should be satisfied after eating");
    }

    @Test
    public void testBearAging() {
        for (int i = 0; i < 10; i++) {
            bear.act(world); // Simulate several steps to allow aging
        }
        
        assertTrue(bear.getAge() > 1, "Bear should age as it acts");
    }

    @Test
    public void testBearDeath() {
        bear.setHealth(0);
        bear.act(world);

        assertFalse(bear.isAlive(), "Bear should die when its health reaches 0");
    }

    @Test
    public void testBearEnergy() {
        bear.setEnergy(100);
        bear.act(world);

        assertFalse(bear.getEnergy() == 100, "Bear's energy should not stay the same after moving");
    }
}
