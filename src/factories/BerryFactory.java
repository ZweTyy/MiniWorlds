package factories;

import java.util.ArrayList;
import java.util.List;

import entities.Berry;
import entities.Rabbit;
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
     * @param size  The size parameter used to generate the berry's initial
     *              location.
     * @return A new Berry object.
     */
    public static Berry createBerry(World world, int size) {
        return new Berry(world, size);
    }

    /**
     * Creates multiple Berry objects.
     *
     * @param world           The world where the berries exist.
     * @param size            The size parameter used for each berry's initial
     *                        location.
     * @param numberOfBerries The number of berries to create.
     * @return A list of created Berry objects.
     */
    public static List<Berry> createMultipleBerries(World world, int size, int numberOfBerries) {
        List<Berry> berries = new ArrayList<>();
        for (int i = 0; i < numberOfBerries; i++) {
            berries.add(createBerry(world, size));
        }
        return berries;
    }
}
