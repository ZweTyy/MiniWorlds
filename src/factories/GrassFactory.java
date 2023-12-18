package factories;

import java.util.ArrayList;
import java.util.List;

import entities.Grass;
import entities.Rabbit;
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
     * @param size  The size parameter used to generate the grass's initial
     *              location.
     * @return A new Grass object.
     */
    public static Grass createGrass(World world, int size) {
        return new Grass(world, size);
    }

    /**
     * Creates multiple Grass objects.
     *
     * @param world           The world where the grasses exist.
     * @param size            The size parameter used for each grass' initial
     *                        location.
     * @param numberOfGrasses The number of grass to create.
     * @return A list of created Grass objects.
     */
    public static List<Grass> createMultipleGrasses(World world, int size, int numberOfGrasses) {
        List<Grass> grasses = new ArrayList<>();
        for (int i = 0; i < numberOfGrasses; i++) {
            grasses.add(createGrass(world, size));
        }
        return grasses;
    }
}
