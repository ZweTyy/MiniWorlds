package factories;

import java.util.List;
import java.util.ArrayList;

import entities.Carcass;
import itumulator.world.World;

/**
 * This class is a factory for creating Carcass with Fungi objects.
 * It provides a static method to create multiple Carcass with Fungi objects.
 */
public class CarcassFungiFactory {

    /**
     * Creates multiple Carcass with Fungi objects.
     * 
     * @param world The world where the carcass with fungi exist.
     * @param size The size parameter used for each carcass with fungi's initial location.
     * @param quantity The number of carcass with fungi to create.
     * @return A list of created Carcass with Fungi objects.
     */
    public static List<Carcass> createMultipleCarcassesFungi(World world, int size, int quantity) {
        List<Carcass> carcasses = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            Carcass carcass = new Carcass(world, size);
            carcasses.add(carcass);
        }
        return carcasses;
    }
}
