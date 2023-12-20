package entities;

import itumulator.world.Location;
import itumulator.world.World;

import java.util.Random;

/**
 * Abstract base class for entities in the simulation.
 * This class provides common functionalities for all entities, such as location
 * handling.
 */
public abstract class Entity {
    protected Location initialLocation;
    protected World world;
    protected static final Random r = new Random();

    /**
     * Constructs an Entity within the specified world and assigns it a random
     * initial location.
     *
     * @param world The world where the entity exists.
     * @param size  The size parameter used to generate the entity's initial
     *              location.
     */
    public Entity(World world, int size) {
        this.world = world;
        this.initialLocation = generateRandomLocation(size);
        initializeEntity(world, size);
    }

    /**
     * Generates a random location within the given size constraints.
     * The method ensures that the generated location is empty.
     *
     * @param size The size parameter defining the bounds for the random location.
     * @return A randomly generated, unoccupied location.
     */
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

    /**
     * Sets the entity's location to the given location.
     *
     * @param location The location to set.
     */
    public void setLocation(Location location) {
        this.initialLocation = location;
    }

    /**
     * Returns the entity's location.
     *
     * @return The entity's location.
     */
    public Location getLocation() {
        return initialLocation;
    }

    private void initializeEntity(World world, int size) {
        this.initialLocation = generateRandomLocation(world.getSize());
    }
}
