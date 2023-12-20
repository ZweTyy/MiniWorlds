package tests;

import entities.Wolf;
import entities.WolfPack;
import entities.Rabbit;
import entities.Bear;
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

    @Test
    public void testAlphaAssignmentOnDeath() {
        int size = 10;
        Wolf wolf2 = new Wolf(world, size);
        WolfPack pack = new WolfPack(world, size);
        wolf.joinPack(pack);
        wolf2.joinPack(pack);
        Wolf alphaWolf = pack.getAlpha();
        alphaWolf.setHealth(0); // Simulate alpha wolf's death
        wolf.act(world); // Trigger the pack to respond to the alpha's death

        assertNotEquals(alphaWolf, pack.getAlpha(), "A new alpha should be assigned on the previous alpha's death");
    }

    @Test
    public void testJoinAndLeavePack() {
        int size = 10;
        WolfPack pack = new WolfPack(world, size);
        Wolf newWolf = new Wolf(world, size);
        newWolf.joinPack(pack);
        assertTrue(pack.getPack().contains(newWolf), "Wolf should be part of the pack after joining");

        pack.removeMember(newWolf);
        assertFalse(pack.getPack().contains(newWolf), "Wolf should not be part of the pack after leaving");
    }

    @Test
    public void testWolfDenCreation() {
        int size = 10;
        WolfPack pack = new WolfPack(world, size);
        pack.createDen(world, size);

        assertNotNull(pack.getDen(), "Wolf pack should have a den after creation");
    }

    @Test
    public void testWolfAvoidsFightingWhenOutNumbered() {
        int size = 10;
        WolfPack pack = new WolfPack(world, size);
        Wolf newWolf = new Wolf(world, size);
        wolf.joinPack(pack);
        newWolf.joinPack(pack);
        world.setTile(new Location(5, 4), newWolf);

        Bear bear = new Bear(world, size, 5, 6);
        world.setTile(new Location(5, 6), bear);

        wolf.act(world);
        newWolf.act(world);

        assertFalse(wolf.isEngagedInFight(), "Wolf should not attack the bear when outnumbered");
    }

    @Test
    public void testWolfAttacksWhenOutNumbering() {
        int size = 10;
        WolfPack pack = new WolfPack(world, size);
        Wolf newWolf = new Wolf(world, size);
        Wolf newWolf2 = new Wolf(world, size);
        wolf.joinPack(pack);
        newWolf.joinPack(pack);
        newWolf2.joinPack(pack);
        world.setTile(new Location(5, 4), newWolf);
        world.setTile(new Location(5, 7), newWolf2);

        Bear bear = new Bear(world, size, 5, 6);
        world.setTile(new Location(5, 6), bear);

        wolf.act(world);
        newWolf.act(world);

        assertTrue(newWolf.isEngagedInFight(), "Wolf should attack the bear when outnumbering");
    }

    @Test
    public void testWolfChasesAndAttacksPrey() {
        int size = 2;
        world = new World(size);
        initialLocation = new Location(0, 1);
        wolf = new Wolf(world, size);
        wolf.setHunger(50);
        world.setTile(initialLocation, wolf);
        WolfPack pack = new WolfPack(world, size);
        wolf.joinPack(pack);
        Rabbit rabbit = new Rabbit(world, size);
        Location rabbitLocation = new Location(0, 0);
        world.setTile(rabbitLocation, rabbit);

        // Act
        wolf.act(world);

        // Assert that the rabbit is close enough to get attacked
        assertTrue(wolf.isCloseEnoughToAttack(rabbit), "Wolf should move close enough to attack the rabbit");
    }

}
