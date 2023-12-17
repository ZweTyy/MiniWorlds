package factories;

import entities.Rabbit;
import itumulator.world.World;

public class RabbitFactory {
    public static Rabbit createRabbit(World world, int size) {
        return new Rabbit(world, size);
    }
}
