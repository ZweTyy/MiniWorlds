import itumulator.world.World;

public class BerryFactory {
    public static Berry createBerry(World world, int size) {
        return new Berry(world, size);
    }
}
