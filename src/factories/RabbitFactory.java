package factories;

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
     * @param size The size parameter used to generate the rabbit's initial location.
     * @return A new Rabbit object.
     */
    public static Rabbit createRabbit(World world, int size) {
        return new Rabbit(world, size);
    }
}
