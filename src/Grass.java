import java.util.Random;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

public class Grass implements NonBlocking {
    private boolean alive;
    private Location place;
    Random r = new Random();

    public Grass(World world, int size) {
        initializeGrass(world, size);
        this.alive = true;
    }

    public void initializeGrass(World world, int size) {
        int x = r.nextInt(size); // Lav et tilfældigt x koordinat
        int y = r.nextInt(size); // Lav et tilfældigt y koordinat
        place = new Location(x, y); // Lav ny lokation med de tilfældige koordinater
        while (!world.isTileEmpty(place)) { // While tile ikke er tom
            x = r.nextInt(size);
            y = r.nextInt(size);
            place = new Location(x, y);
        }
    }

    // Method to simulate grass decay
    public void decay() {
        // Grass has a chance to die
        if (r.nextDouble() < 0.1) { // 10% chance to decay
            this.alive = false;
        }
    }

    // Method to simulate grass spreading
    public void spread(World world) {
        // Spread to a random neighbouring empty tile
        if (this.alive && r.nextDouble() < 0.2) { // 20% chance to spread
            Set<Location> neighbours = world.getEmptySurroundingTiles();
            List<Location> list = new ArrayList<>(neighbours);
            Location newLocation = list.get(0); // Determine a new location to spread
            world.setTile(newLocation, newLocation);
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public Location getPlace() {
        return place;
    }
}
