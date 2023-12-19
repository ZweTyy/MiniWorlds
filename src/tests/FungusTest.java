package tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import entities.Carcass;
import entities.Fungus;
import itumulator.world.Location;
import itumulator.world.World;

public class FungusTest {
    
    private World world;
    private Fungus fungus;
    private Location location;
    private int size;
    private int carcassSize;

    @Before
    public void setUp() {
        world = new World(10); // assuming World constructor takes size of the world
        size = 10;
        carcassSize = 10;
        location = new Location(0, 0);
        fungus = new Fungus(world, size, carcassSize);
    }

    @Test
    public void testFungusCreation() {
        assertNotNull("Fungus should be created", fungus);
        assertEquals("Initial size should be 0", 0, fungus.getSize());
        assertFalse("Fungus should not be visible initially", fungus.isVisible());
        assertEquals("Lifespan should be set based on carcass size", carcassSize * 1.5, fungus.getRemainingLife(), 0.0);
    }

    @Test
    public void testFungusGrowth() {
        fungus.grow();
        assertEquals("Fungus size should increase after growth", 1, fungus.getSize());
    }

    @Test
    public void testFungusVisibility() {
        for (int i = 0; i < Fungus.GROWTH_THRESHOLD; i++) {
            fungus.grow();
        }
        assertTrue("Fungus should be visible after reaching growth threshold", fungus.isVisible());
    }

    @Test
    public void testFungusDeath() {
        world.setTile(location, fungus);
        for (int i = 0; i < fungus.getLifespan(); i++) {
            fungus.act(world); // assuming act method is supposed to be called each simulation step
        }
        assertTrue("Fungus should die when lifespan is over", world.isTileEmpty(fungus.getLocation()));
    }

    @Test
    public void testFungusDoesNotBecomeVisiblePrematurely() {
        for (int i = 0; i < Fungus.GROWTH_THRESHOLD - 1; i++) {
            fungus.grow();
        }
        assertFalse("Fungus should not be visible before reaching growth threshold", fungus.isVisible());
    }

    @Test
    public void testFungusDeathWithoutSpread() {
        // Ensure there are no carcasses to spread to

        // Act until fungus's lifespan ends
        for (int i = 0; i < fungus.getLifespan(); i++) {
            fungus.act(world);
        }

        // Confirm fungus has died
        assertTrue("Fungus should die when lifespan is over without spreading", world.isTileEmpty(fungus.getLocation()));
    }

    @Test
    public void testFungusSpreadingSpores() {
        // Set up a carcass next to the fungus
        Carcass carcass = new Carcass(world, size, new Location(0, 1), "rabbit");
        world.setTile(new Location(0, 1), carcass);

        // Trigger spreading
        fungus.act(world);

        // Confirm a new fungus has been created on the carcass
        assertNotNull("New fungus should be created on nearby carcass", carcass.getFungus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFungusWithZeroCarcassSize() {
        new Fungus(world, size, 0); // This should throw an exception
    }
}
