package tests;

import entities.Wolf;
import entities.WolfPack;
import entities.Rabbit;
import entities.Carcass;
import itumulator.world.Location;
import itumulator.world.World;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.After;

public class WolfTest {

    private World world;
    private Wolf wolf;
    private Location initialLocation;
    private int size;

    @BeforeEach
    public void setUp() {
        // Initialize the world and wolf with minimal setup
        int size = 10; // Define the size of the world
        world = new World(size);
        initialLocation = new Location(5, 5); // Set initial wolf location
        wolf = new Wolf(world, size); // Instantiate the Wolf object

        WolfPack pack = new WolfPack(world, size);
        wolf.joinPack(pack);

        world.setTile(initialLocation, wolf);
    }

    @Test
    public void testWolfMovement() {
        wolf.act(world); // The act method includes the move

        Location newLocation = wolf.getLocation();
        assertNotEquals(initialLocation, newLocation, "Wolf should have moved to a new location");
    }

    @Test
    public void testWolfHunting() {
        Rabbit rabbit = new Rabbit(world, 10);
        Location rabbitLocation = new Location(5, 6); // Place the rabbit close to the wolf
        int initialRabbitHealth = rabbit.getHealth();
        world.setCurrentLocation(initialLocation);
        world.setTile(rabbitLocation, rabbit);

        wolf.act(world);

        assertTrue(wolf.isCloseEnoughToAttack(rabbit), "Wolf should move close enough to attack the rabbit");
        assertTrue(rabbit.getHealth() < initialRabbitHealth, "Rabbit's health should decrease after being attacked");
    }

    @Test
    public void testWolfEatingCarcass() {
        int size = 10;
        Carcass carcass = new Carcass(world, size);
        Location carcassLocation = new Location(5, 6);
        world.setDay();
        world.setCurrentLocation(initialLocation);
        world.setTile(carcassLocation, carcass);

        wolf.setHunger(50);
        double initialHunger = wolf.getHunger();

        wolf.act(world);
        System.out.println("Carcass location: " + carcassLocation);
        System.out.println("Wolf location: " + initialLocation);

        assertTrue(world.getTile(carcassLocation) == null,
                "Carcass should be removed from the world after being eaten");
        assertTrue(wolf.getHunger() > initialHunger, "Wolf's hunger should be satisfied after eating the carcass");
    }

    @Test
    public void testWolfAging() {
        world.setCurrentLocation(initialLocation);
        for (int i = 0; i < 10; i++) {
            wolf.act(world); // Simulate several steps to allow aging
        }

        assertTrue(wolf.getAge() > 1, "Wolf should age as it acts");
    }

    @Test
    public void testWolfDeath() {
        wolf.setHealth(0);
        wolf.act(world);

        assertFalse(wolf.isAlive(), "Wolf should die when its health reaches 0");
    }

    @Test
    public void testWolfEnergy() {
        wolf.setEnergy(100);
        world.setDay();
        world.setCurrentLocation(initialLocation);
        wolf.act(world);

        assertFalse(wolf.getEnergy() == 100, "Wolf's energy should not stay the same after moving");
    }
}
