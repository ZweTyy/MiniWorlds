package entities;

import itumulator.world.World;

/**
 * By implementing this interface, an entity is presented as a carnivore.
 *
 */
public interface Predator {
    void hunt(World world);
}
