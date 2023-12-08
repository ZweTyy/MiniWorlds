import itumulator.world.World;

public class BurrowFactory {
    public static Burrow createBurrow(World world, int size) {
        return new Burrow(world, size);
    }
}