import itumulator.world.World;

public class BearFactory {
    public static Bear createBear(World world, int size, Integer x, Integer y) {
        return new Bear(world, size, x, y);
    }

    public static Bear createBear(World world, int size) {
        return new Bear(world, size);
    }
}