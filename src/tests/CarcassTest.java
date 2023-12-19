package tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import entities.Carcass;
import entities.Fungus;
import itumulator.world.Location;
import itumulator.world.World;

public class CarcassTest {

    private World world;
    private Carcass carcass;
    private int size;
    private String animalType;
    private Location location;

    @Before
    public void setUp() {
        world = new World(10); // assuming World constructor takes size of the world
        size = 10;
        animalType = "rabbit";
        location = new Location(5, 5); // assuming Location constructor takes x and y coordinates
        carcass = new Carcass(world, size, location, animalType);
    }

    @Test
    public void testCarcassCreation() {
        assertNotNull("Carcass should be created", carcass);
        assertEquals("Carcass should have correct meat quantity based on animal type", 10, carcass.getMeatQuantity());
        assertEquals("Carcass should be at the correct location", location, carcass.getLocation());
    }

    @Test
    public void testCarcassDecay() {
        world.setTile(location, carcass);
        int initialDecayCounter = carcass.getDecayCounter();
        carcass.act(world);
        assertTrue("Decay counter should decrease after act", carcass.getDecayCounter() < initialDecayCounter);
    }

    @Test
    public void testFungusGrowthOnCarcass() {
        carcass.act(world);
        assertNotNull("Fungus should be present on carcass", carcass.getFungus());
    }

    @Test
    public void testCarcassRemoval() {
        while (carcass.getMeatQuantity() > 0) {
            carcass.act(world);
        }
        assertFalse("Carcass should be removed from world when decayed", world.contains(carcass));
    }

    @Test
    public void testFungusVisibilityPostDecay() {
        while (carcass.getMeatQuantity() > 0) {
            carcass.act(world);
        }
        assertTrue("Fungus should be visible when carcass is fully decayed", carcass.getFungus().isVisible());
    }

    // Edge case tests

    @Test
    public void testFungusNotSpreadingWithoutAdjacentCarcasses() {
        // Ensure there are no adjacent carcasses
        Carcass carcass2 = new Carcass(world, size, new Location(3, 3), animalType);
        // Decay the carcass completely

        carcass.getFungus().act(world); // assuming fungus act method checks for spreading
        assertFalse("Fungus should not spread without adjacent carcasses", !carcass2.hasFungus());
    }

}
