package tests;

import entities.Berry;
import itumulator.executable.Program;
import itumulator.world.World;
import utilities.SimulationManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class BerryTest {

    private World world;
    private Berry berry;

    @BeforeEach
    public void setUp() {
        // Initialize the world and berry before each test
        int size = 10; // Define the size of the world
        world = new World(size);
        System.out.println("World instance in setUp: " + world.hashCode());
        berry = new Berry(world, size);
    }

    @AfterEach
    public void tearDown() {
        // Reset the world and berry after each test
        world = null;
        berry = null;
        SimulationManager.reset();
    }

    @Test
    public void testBerryIsInitiallyAvailable() {
        // Test that berries are initially available
        assertTrue(berry.hasBerries(), "Berry bush should initially have berries.");
    }

    @Test
    public void testBerryCanBeEaten() {
        // Test that the berry can be eaten
        berry.eaten();
        assertFalse(berry.hasBerries(), "Berry bush should not have berries after being eaten.");
    }

    @Test // This test is not working
    public void testBerryGrowsBackAfterADay() {
        berry.eaten();
        simulateDays(100); // Simulate 10 days
        berry.act(world);

        assertTrue(berry.hasBerries(), "Berry bush should have berries after a day.");
    }


    @Test
    public void testBerryDoesNotGrowBackSameDay() { // This test works when run alone but not when run with other tests
        // Eat the berry and check that it doesn't grow back on the same day
        berry.eaten();
        berry.act(world);
        
        // Test that the berry does not grow back on the same day
        assertFalse(berry.hasBerries(), "Berry bush should not have berries on the same day it was eaten.");
    }

    private void simulateDays(int days) {
        System.out.println("Starting simulateDays, current world time: " + world.getCurrentTime());
        System.out.println("Simulating days with world instance " + world.hashCode());
        
    
        for (int i = 0; i < days * World.getTotalDayDuration(); i++) {
            world.step();
            SimulationManager.incrementStep();
        }
    
        System.out.println("Ending simulateDays, current world time: " + world.getCurrentTime());
    }
}
