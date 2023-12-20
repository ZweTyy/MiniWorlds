package entities;

import itumulator.world.Location;
import itumulator.world.World;

/**
 * By implementing this interface, an entity is presented as a prey.
 */
public interface Prey {
    void fleeFromPredator(World world, Location predatorLocation);
}
