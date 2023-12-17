import org.junit.Before;
import org.junit.Test;

import itumulator.world.Location;
import itumulator.world.World;

import static org.junit.Assert.*;

public class WolfTest {
    private Wolf wolf;
    private World world;
  
    public void setUp() {
    world = new World(10);
    wolf = new Wolf(world, 10, false);
    }


    @Test
    public void testLocationChange() {
        Location initialLocation = new Location(5, 5);
        wolf.move(world);

        Location newLocation = wolf.getLocation();
        assertNotEquals("Rabbit should move to a new location", initialLocation, newLocation);
    }
   
    @Test
    void testAct() {

    }

    @Test
    void testEat() {

    }

    @Test
    void testJoinPack() {

    }

    @Test
    void testMove() {

    }

    @Test
    void testSetPack() {

    }
}
