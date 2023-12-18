package factories;

import entities.Bear;
import itumulator.world.World;

/**
 * This class is a factory for creating Bear objects.
 * 
 */
public class BearFactory {

    /**
     * Creates a new Bear object with a specified location.
     * 
     * @param world The world where the bear exists.
     * @param size The size parameter used to generate the bear's initial location.
     * @return A new Bear object.
     */
    public static Bear createBear(World world, int size, Integer x, Integer y) {
        return new Bear(world, size, x, y);
    }

    /**
     * Creates a new Bear object with a random location.
     * 
     * @param world The world where the bear exists.
     * @param size The size parameter used to generate the bear's initial location.
     * @return A new Bear object.
     */
    public static Bear createBear(World world, int size) {
        return new Bear(world, size);
    }
}