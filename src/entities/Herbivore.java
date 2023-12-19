package entities;

import itumulator.world.Location;
import itumulator.world.World;

/**
 * By implementing this interface, an entity is presented as a herbivore.
 *
 */
public interface Herbivore {
    void eatHerb(World world);
}
