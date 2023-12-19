package factories;

import entities.Bear;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a factory for creating Bear objects.
 */
public class BearFactory {

    /**
     * Creates a new Bear object with a specified location.
     *
     * @param world The world where the bear exists.
     * @param size  The size parameter used to generate the bear's initial location.
     * @param x     The x-coordinate for the bear's location.
     * @param y     The y-coordinate for the bear's location.
     * @return A new Bear object.
     */
    public static Bear createBear(World world, int size, Integer x, Integer y) {
        return new Bear(world, size, x, y);
    }

    /**
     * Creates a new Bear object with a random location.
     *
     * @param world The world where the bear exists.
     * @param size  The size parameter used to generate the bear's initial location.
     * @return A new Bear object.
     */
    public static Bear createBear(World world, int size) {
        return new Bear(world, size);
    }

    /**
     * Creates a Bear object based on provided details.
     * If specific coordinates are provided, a Bear is created at that location.
     * Otherwise, a Bear with a random location is created.
     *
     * @param world   The world where the bear exists.
     * @param size    The size parameter for bear creation.
     * @param details Array containing bear creation details.
     * @return A new Bear object.
     */
    public static Bear createBear(World world, int size, Integer[] details) {
        if (details.length > 2 && details[1] != null && details[2] != null) {
            int x = details[1];
            int y = details[2];
            return createBear(world, size, x, y);
        } else {
            return createBear(world, size);
        }
    }

    /**
     * Creates multiple Bear objects, each with a random location.
     *
     * @param world         The world where the bears exist.
     * @param size          The size parameter used for each bear's initial
     *                      location.
     * @param numberOfBears The number of bears to create.
     * @return A list of created Bear objects.
     */
    public static List<Bear> createMultipleBears(World world, int size, int numberOfBears) {
        List<Bear> bears = new ArrayList<>();
        for (int i = 0; i < numberOfBears; i++) {
            bears.add(createBear(world, size));
        }
        return bears;
    }
}
