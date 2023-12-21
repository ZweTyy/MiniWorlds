package factories;

import java.util.ArrayList;
import java.util.List;

import entities.Mole;
import itumulator.world.World;

/**
 * This class is a factory for creating Mole objects.
 * It provides a static method to create multiple Mole objects.
 */
public class MoleFactory {

    /**
     * Creates a new Mole object with a random location.
     *
     * @param world The world where the mole exists.
     * @param size  The size parameter used to generate the mole's initial
     *              location.
     * @return A new Mole object.
     */
    public static Mole createMole(World world, int size) {
        return new Mole(world, size);
    }

    /**
     * Creates multiple Mole objects.
     *
     * @param world           The world where the moles exist.
     * @param size            The size parameter used for each mole's initial
     *                        location.
     * @param numberOfMoles The number of moles to create.
     * @return A list of created Mole objects.
     */
    public static List<Mole> createMultipleMoles(World world, int size, int numberOfMoles) {
        List<Mole> moles = new ArrayList<>();
        for (int i = 0; i < numberOfMoles; i++) {
            moles.add(createMole(world, size));
        }
        return moles;
    }

}
