import itumulator.world.Location;
import itumulator.world.World;

import java.util.Random;

// Klassen bruges til at initialisere en entity
public abstract class Entity {
    protected Location initialLocation;
    protected World world;
    protected static final Random r = new Random();

    public Entity(World world, int size) {
        this.world = world;
        this.initialLocation = generateRandomLocation(size);
        initializeEntity(world, size);
    }

    protected void initializeEntity(World world, int size) {
        this.initialLocation = generateRandomLocation(world.getSize());
    }

    protected Location generateRandomLocation(int size) {
        int x, y;
        Location location;
        do {
            x = r.nextInt(size);
            y = r.nextInt(size);
            location = new Location(x, y);
        } while (!world.isTileEmpty(location));
        return location;
    }

    public void setLocation(Location location) {
        this.initialLocation = location;
    }

    public Location getLocation() {
        return initialLocation;
    }
}
