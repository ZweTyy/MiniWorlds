package factories;

import java.util.ArrayList;
import java.util.List;

import entities.Rabbit;
import itumulator.world.World;

/**
 * This class is a factory for creating Rabbit objects.
 * 
 */
public class RabbitFactory {

    /**
     * Creates a new Rabbit object with a random location.
     * 
     * @param world The world where the rabbit exists.
     * @param size  The size parameter used to generate the rabbit's initial
     *              location.
     * @return A new Rabbit object.
     */
    public static Rabbit createRabbit(World world, int size) {
        return new Rabbit(world, size);
    }

    /**
     * Creates multiple Rabbit objects.
     *
     * @param world           The world where the rabbits exist.
     * @param size            The size parameter used for each rabbit's initial
     *                        location.
     * @param numberOfRabbits The number of rabbits to create.
     * @return A list of created Rabbit objects.
     */
    public static List<Rabbit> createMultipleRabbits(World world, int size, int numberOfRabbits) {
        List<Rabbit> rabbits = new ArrayList<>();
        for (int i = 0; i < numberOfRabbits; i++) {
            rabbits.add(createRabbit(world, size));
        }
        return rabbits;
    }
}
