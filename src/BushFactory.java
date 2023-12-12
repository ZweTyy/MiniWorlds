import itumulator.world.World;

public class BushFactory {
    public static Bush createBush(World world, int size) {
        return new Bush(world, size);
    }
}
