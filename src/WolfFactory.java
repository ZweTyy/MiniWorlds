import itumulator.world.World;

public class WolfFactory {
    public static Wolf createWolf(World world, int size, Integer[] details) {
        WolfPack wolfPack = new WolfPack(world, size);
        for (int i = 0; i < details[0]; i++) {
            Wolf wolf = new Wolf(world, size);
            wolfPack.addMember(wolf);
            wolf.setPack(wolfPack);
        }
        return wolfPack.getAlpha();
    }
}
