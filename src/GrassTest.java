import org.junit.Before;
import org.junit.Test;

import itumulator.world.Location;
import itumulator.world.World;

import static org.junit.Assert.*;

public class GrassTest {

    private Grass grass;
    private World world;

    @Before
    public void setUp() {
        world = new World(10);
        grass = new Grass(world, world.getSize());
    }

    @Test
    public void createGrass() {
        assertNotNull("Grass should not be null", grass);
    }

    @Test
    public void testGrassSpread(){
        world.setTile(new Location(0,0), grass);

        for(int i = 0 ; i < 50 ; i++){
            grass.spread(world, world.getSize());
        }

        Boolean overOneEntity = world.getEntities().size() > 1;

        assertEquals(overOneEntity, true);
    }

    @Test
    public void testGrassDecay(){
        world.setTile(new Location(0,0), grass);

        assertEquals(1, world.getEntities().size());

        grass.die(world);

        assertEquals(0, world.getEntities().size());
    }

}