package tests;

import entities.Mole;
import entities.Wolf;
import entities.Carcass;
import itumulator.world.Location;
import itumulator.world.World;
import utilities.SimulationManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MoleTest {

    private World world;
    private Mole mole;
    private Location initialLocation;
    private int size;

    @BeforeEach
    public void setUp() {
        size = 10;
        world = new World(size);
        initialLocation = new Location(5, 5);
        mole = new Mole(world, size);
        world.setTile(initialLocation, mole);
    }

    @Test
    public void testMoleMovementAboveGround() {
        world.remove(mole);
        world.setCurrentLocation(initialLocation);
        mole.setUnderground(true);
        mole.comeAboveGround(world);
        mole.act(world);

        Location newLocation = mole.getLocation();
        assertNotEquals(initialLocation, newLocation, "Mole should have moved to a new location above ground");
    }

    @Test
    public void testMoleEatingCarcass() {
        Carcass carcass = new Carcass(world, size);
        Location carcassLocation = new Location(5, 6);
        world.setTile(carcassLocation, carcass);
        mole.setHunger(50);

        mole.comeAboveGround(world);
        mole.act(world);

        assertNull(world.getTile(carcassLocation),
                "Carcass should be removed from the world after being eaten by mole");
        assertTrue(mole.getHunger() < 50, "Mole's hunger should be reduced after eating the carcass");
    }

    @Test
    public void testMoleGoesUndergroundWhenFull() {
        mole.setHunger(100);
        mole.goUnderground(world);
        assertNull(world.getTile(initialLocation),
                "Mole should be removed from the world view when it goes underground");
    }

    @Test
    public void testMoleComesAboveGroundWhenHungry() {
        mole.setHunger(70);
        mole.act(world);
        assertEquals(initialLocation, mole.getLocation(), "Mole should come above ground when hungry");
    }

    @Test
    public void testMoleFleeingFromPredator() {
        // Setup a predator close to the mole
        Location predatorLocation = new Location(5, 4);
        mole.setHunger(70); // Mole is above ground due to hunger
        world.setTile(predatorLocation, new Wolf(world, size));

        mole.act(world); // Mole should attempt to flee from the predator

        Location moleLocationAfterAct = mole.getLocation();
        assertNotEquals(predatorLocation, moleLocationAfterAct, "Mole should have moved away from the predator");
    }

    @Test
    public void testMoleReproduction() {
        mole.setEnergy(30);
        mole.setAge(3);
        world.setCurrentLocation(initialLocation);
        int initialMoleCount = SimulationManager.getMoleCount();
        mole.act(world);
        SimulationManager.updateMoleCount(world);

        assertTrue(SimulationManager.getMoleCount() > initialMoleCount,
                "There should be more moles after reproduction");
    }
}
