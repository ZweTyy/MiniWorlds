import itumulator.world.World;

public class WolfFactory {
    public static Wolf createWolf(World world, int size) {
        return new Wolf(world, size);
    }
}
