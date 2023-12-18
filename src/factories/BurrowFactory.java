package factories;

import entities.Burrow;
import itumulator.world.World;

/**
 * This class is a factory for creating Burrow objects.
 * 
 */
public class BurrowFactory {

    /**
     * Creates a new Burrow object.
     * 
     * @param world The world where the burrow exists.
     * @param size The size parameter used to generate the burrow's initial location.
     * @return A new Burrow object.
     */
    public static Burrow createBurrow(World world, int size) {
        return new Burrow(world, size);
    }
}