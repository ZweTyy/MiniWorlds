package factories;

import entities.Grass;
import itumulator.world.World;

public class GrassFactory {
    public static Grass createGrass(World world, int size) {
        return new Grass(world, size);
    }
}
