package entities;

import itumulator.world.World;

/**
 * By implementing this interface, an entity is presented as a carnivore.
 *
 */
public interface Carnivore {
    void hunt(World world);
}
