import java.util.Random;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

public class Grass implements NonBlocking {
    private boolean alive;
    private Location location;
    Random rand = new Random();

    public Grass(World world) {
        this.alive = true;
    }

    // Method to simulate grass decay
    public void decay() {
        // Grass has a chance to die
        if (rand.nextDouble() < 0.1) { // 10% chance to decay
            this.alive = false;
        }
    }

    // Method to simulate grass spreading
    public void spread(World world) {
        // Spread to a random neighbouring empty tile
        if (this.alive && rand.nextDouble() < 0.2) { // 20% chance to spread
            Set<Location> neighbours = world.getEmptySurroundingTiles();
            List<Location> list = new ArrayList<>(neighbours);
            Location newLocation = list.get(0); // Determine a new location to spread
            world.setTile(newLocation, newLocation);
        }
    }

    // Check if grass is alive
    public boolean isAlive() {
        return alive;
    }

    // Get the location of the grass
    public Location getLocation() {
        return location;
    }
}
