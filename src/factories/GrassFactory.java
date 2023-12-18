package factories;

import entities.Grass;
import itumulator.world.World;

/**
 * This class is a factory for creating Grass objects.
 * 
 */
public class GrassFactory {

    /**
     * Creates a new Grass object with a random location.
     * 
     * @param world The world where the grass exists.
     * @param size The size parameter used to generate the grass's initial location.
     * @return A new Grass object.
     */
    public static Grass createGrass(World world, int size) {
        return new Grass(world, size);
    }
}
