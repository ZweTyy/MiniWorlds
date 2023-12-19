package entities;

import itumulator.world.World;

/**
 * By implementing this interface, an entity is presented as a carnivore.
 *
 */
public interface Carnivore {
    public int attackPower = 0;

    void hunt(World world);
}
