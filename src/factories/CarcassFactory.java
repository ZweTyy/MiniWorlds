package factories;

import java.util.ArrayList;
import java.util.List;

import entities.Carcass;
import itumulator.world.World;

/**
 * This is a factory for creating Carcass objects.
 * It provides a static method to create multiple Carcass objects.
 */
public class CarcassFactory {

    /**
     * Creates a new Carcass object with a random location.
     *
     * @param world The world where the carcass exists.
     * @param size  The size parameter used to generate the carcass's initial
     *              location.
     * @return A new Carcass object.
     */
    public static Carcass createCarcass(World world, int size) {
        return new Carcass(world, size);
    }

    /**
     * Creates multiple Carcass objects.
     *
     * @param world           The world where the carcasses exist.
     * @param size            The size parameter used for each carcass's initial
     *                        location.
     * @param numberOfFungi The number of fungi to create.
     * @return A list of created Carcass objects.
     */
    public static List<Carcass> createMultipleCarcasses(World world, int size, int numberOfFungi) {
        List<Carcass> Carcasses = new ArrayList<>();
        for (int i = 0; i < numberOfFungi; i++) {
            Carcasses.add(createCarcass(world, size));
        }
        return Carcasses;
    }

}
