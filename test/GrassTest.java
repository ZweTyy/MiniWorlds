import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

public class GrassTest {

    @Test
    public void testGrassInitialization() {
        World mockWorld = Mockito.mock(World.class);
        Mockito.when(mockWorld.getSize()).thenReturn(10);
        Mockito.when(mockWorld.isTileEmpty(Mockito.any(Location.class))).thenReturn(true);

        Grass grass = new Grass(mockWorld, 10);
        assertNotNull("Grass should have a location after initialization", grass.getPlace());
    }

    @Test
    public void testGrassDecay() {
        World mockWorld = Mockito.mock(World.class);
        Mockito.when(mockWorld.getSize()).thenReturn(10);
        Mockito.when(mockWorld.isTileEmpty(Mockito.any(Location.class))).thenReturn(true);

        Grass grass = new Grass(mockWorld, 10);
        grass.decay(mockWorld); // You might need to call this multiple times or adjust the random threshold for the test to be effective

        assertFalse("Grass should decay after calling decay", grass.isAlive());
    }

    @Test
    public void testGrassSpread() {
        World mockWorld = Mockito.mock(World.class);
        Mockito.when(mockWorld.getSize()).thenReturn(10);
        Mockito.when(mockWorld.isTileEmpty(Mockito.any(Location.class))).thenReturn(true);
        Mockito.when(mockWorld.getEmptySurroundingTiles(Mockito.any(Location.class))).thenReturn(new HashSet<Location>());

        Grass grass = new Grass(mockWorld, 10);
        grass.spread(mockWorld, 10);

        // Verify that setTile is called when spreading
        Mockito.verify(mockWorld, Mockito.times(1)).setTile(Mockito.any(Location.class), Mockito.any(Grass.class));
    }
}
