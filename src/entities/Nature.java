package entities;

import itumulator.world.World;

/**
 * Represents the nature in a simulated ecosystem.
 * This class extends Entity.
 */
public class Nature extends Entity {
    /**
     * Constructs a Nature entity within the specified world and assigns it a random initial location.
     *
     * @param world The world where the nature exists.
     * @param size The size parameter used to generate the nature's initial location.
     */
    public Nature(World world, int size) {
        super(world, size);
    }
}