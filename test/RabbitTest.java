import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

public class RabbitTest {

    @Test
    public void testRabbitInitialization() {
        World mockWorld = Mockito.mock(World.class);
        Mockito.when(mockWorld.getSize()).thenReturn(10);
        Mockito.when(mockWorld.isTileEmpty(Mockito.any(Location.class))).thenReturn(true);

        Rabbit rabbit = new Rabbit(mockWorld, 10);
        assertNotNull("Rabbit should have a location after initialization", rabbit.getPlace());
    }

    @Test
    public void testRabbitMovement() {
        World mockWorld = Mockito.mock(World.class);
        Mockito.when(mockWorld.getSize()).thenReturn(10);
        Mockito.when(mockWorld.isTileEmpty(Mockito.any(Location.class))).thenReturn(true);
        Set<Location> mockNeighbours = Mockito.mock(Set.class);
        Mockito.when(mockWorld.getEmptySurroundingTiles()).thenReturn(mockNeighbours);
        Mockito.when(mockNeighbours.isEmpty()).thenReturn(false);

        Rabbit rabbit = new Rabbit(mockWorld, 10);
        rabbit.act(mockWorld);

        // Verify that move is called when the rabbit acts
        Mockito.verify(mockWorld, Mockito.times(1)).move(Mockito.any(Rabbit.class), Mockito.any(Location.class));
    }
}
