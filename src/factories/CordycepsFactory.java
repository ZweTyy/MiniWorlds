package factories;

import itumulator.world.World;
import entities.Rabbit;
import entities.Wolf;
import entities.WolfPack;

import java.util.List;
import java.util.ArrayList;

/**
 * This class is a factory for creating Cordyceps objects.
 * It provides a static method to create multiple Cordyceps objects (rabbits and wolves).
 */
public class CordycepsFactory {
    
    /**
     * Creates new multiple infected Rabbit objects with a random location.
     *
     * @param world The world where the rabbit exists.
     * @param size  The size parameter used to generate the rabbit's initial
     *              location.
     * @return A new Rabbit object.
     */
    public static List<Rabbit> createMultipleCordycepsRabbits(World world, int size, int quantity) {
        List<Rabbit> rabbits = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            Rabbit rabbit = new Rabbit(world, size);
            rabbit.setInfected(true); // Assuming a method to set infection status
            rabbits.add(rabbit);
        }
        return rabbits;
    }
    
    /**
     * Creates new multiple infected Wolf objects with a random location.
     *
     * @param world The world where the wolf exists.
     * @param size  The size parameter used to generate the wolf's initial
     *              location.
     * @return A new Wolf object.
     */
    public static List<Wolf> createMultipleCordycepsWolves(World world, int size, int quantity) {
        List<Wolf> wolves = new ArrayList<>();
        WolfPack wolfPack = new WolfPack(world, size); // One pack for all wolves
        wolfPack.createDen(world, size); // One den for the pack
    
        for (int i = 0; i < quantity; i++) {
            Wolf wolf = new Wolf(world, size);
            wolf.setInfected(true);
            wolf.setPack(wolfPack); // All wolves belong to the same pack
            wolfPack.addMember(wolf); // Add the wolf to the pack
    
            wolves.add(wolf);
        }
        return wolves;
    }
}
