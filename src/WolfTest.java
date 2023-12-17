import org.junit.Before;
import org.junit.Test;

import itumulator.world.Location;
import itumulator.world.World;

import static org.junit.Assert.*;

public class WolfTest {
    private Wolf wolf;
    private World world;


    @Test
    void testMove() {
        world = new World(10);
        wolf = new Wolf(world, 10, true);
        
        Location initialLocationOfAlpha = new Location(5, 5);
        wolf.move(world);

        Location newLocationOfAlpha = wolf.getLocation();
        assertNotEquals("Alpha should move to a new location", initialLocationOfAlpha, newLocationOfAlpha);
        
        wolf = new Wolf(world, 10, false);
        Location initialLocation = new Location(8, 9);
        wolf.move(world);

        Location newLocation = wolf.getLocation();
        assertNotEquals("!Alpha should move to a new location close to alpha", initialLocation, newLocation);

    }

    @Test

    void testAct() {

    }

    @Test
    void testEat() {
        world = new World(10);
        wolf = new Wolf(world, 10, true);
        Location wolfLocation = new Location(5, 3);
        wolf.setEnergy(50);

        Mole mole= new Mole(world, 10);
        Location moleLocation = new Location(5, 4);

        double initialEnergy = wolf.getEnergy();
        wolf.eat(world);

        assertTrue("Rabbit's energy should increase after eating", wolf.getEnergy() > initialEnergy);

    }

    @Test
    void testJoinPack() {

    }


    @Test
    void testSetPack() {

    }
}
