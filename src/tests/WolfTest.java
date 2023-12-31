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
        world = new World(2);
        wolf = new Wolf(world, 2);
        WolfPack pack = new WolfPack(world, 2);
        wolf.joinPack(pack);
        initialLocation = new Location(0, 0);
        Rabbit rabbit = new Rabbit(world, 2);
        world.setCurrentLocation(initialLocation);
        world.setTile(new Location(0, 0), wolf);
        world.setTile(new Location(0, 1), rabbit);

        wolf.act(world);

        assertTrue(wolf.isCloseEnoughToAttack(rabbit), "Wolf should move close enough to attack the rabbit");
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
        world = new World(2);
        int size = 2;
        WolfPack pack = new WolfPack(world, size);
        Wolf newWolf = new Wolf(world, size);
        Wolf newWolf2 = new Wolf(world, size);
        Wolf newWolf3 = new Wolf(world, size);
        newWolf.joinPack(pack);
        newWolf2.joinPack(pack);
        newWolf3.joinPack(pack);
        world.setTile(new Location(0, 1), newWolf);
        world.setTile(new Location(1, 1), newWolf2);
        world.setTile(new Location(1, 0), newWolf3);

        Bear bear = new Bear(world, size, 0, 0);
        world.setTile(new Location(0, 0), bear);

        newWolf.act(world);
        newWolf2.act(world);
        newWolf3.act(world);

        assertTrue(newWolf.isEngagedInFight() || newWolf2.isEngagedInFight() || newWolf3.isEngagedInFight(),
                "Wolf should attack the bear when outnumbering");
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