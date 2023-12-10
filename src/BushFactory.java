import itumulator.world.World;

public class BerryFactory {
    public static Bush createBush(World world, int size) {
        return new Bush(world, size);
    }
}
