package factories;

import java.util.ArrayList;
import java.util.List;

import entities.Fungus;
import itumulator.world.World;

/**
 * This class is a factory for creating Fungus objects.
 * It provides a static method to create multiple Fungus objects.
 */
public class FungusFactory {

    /**
     * Creates a new Fungus object with a random location.
     *
     * @param world The world where the fungus exists.
     * @param size  The size parameter used to generate the fungus's initial
     *              location.
     * @return A new Fungus object.
     */
    public static Fungus createFungus(World world, int size) {
        return new Fungus(world, size);
    }

    /**
     * Creates multiple Fungus objects.
     *
     * @param world           The world where the fungi exist.
     * @param size            The size parameter used for each fungus's initial
     *                        location.
     * @param numberOfFungi The number of fungi to create.
     * @return A list of created Fungus objects.
     */
    public static List<Fungus> createMultipleFungi(World world, int size, int numberOfFungi) {
        List<Fungus> fungi = new ArrayList<>();
        for (int i = 0; i < numberOfFungi; i++) {
            fungi.add(createFungus(world, size));
        }
        return fungi;
    }

}
