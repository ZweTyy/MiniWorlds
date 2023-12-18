package factories;

import entities.Berry;
import itumulator.world.World;

/**
 * This class is a factory for creating Berry objects.
 * 
 */
public class BerryFactory {

    /**
     * Creates a new Berry object with a random location.
     * 
     * @param world The world where the berry exists.
     * @param size The size parameter used to generate the berry's initial location.
     * @return A new Berry object.
     */
    public static Berry createBerry(World world, int size) {
        return new Berry(world, size);
    }
}
