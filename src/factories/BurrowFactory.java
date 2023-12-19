package factories;

import java.util.ArrayList;
import java.util.List;

import entities.Berry;
import entities.dens.Burrow;
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
     * @param size  The size parameter used to generate the burrow's initial
     *              location.
     * @return A new Burrow object.
     */
    public static Burrow createBurrow(World world, int size) {
        return new Burrow(world, size);
    }

    /**
     * Creates multiple Burrow objects.
     *
     * @param world           The world where the burrows exist.
     * @param size            The size parameter used for each burrow's initial
     *                        location.
     * @param numberOfBerries The number of burrows to create.
     * @return A list of created Burrow objects.
     */
    public static List<Burrow> createMultipleBurrows(World world, int size, int numberOfBurrows) {
        List<Burrow> burrows = new ArrayList<>();
        for (int i = 0; i < numberOfBurrows; i++) {
            burrows.add(createBurrow(world, size));
        }
        return burrows;
    }
}