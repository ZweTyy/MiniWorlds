import itumulator.world.World;

public class WolfFactory {
    public static Wolf createWolf(World world, int size, Integer[] details) {
        WolfPack wolfPack = new WolfPack(world, size);
        for (int i = 0; i < details[0]; i++) {
            Wolf wolf;
            if (i == 0){
                wolf = new Wolf(world, size, true);
            } else{
                wolf = new Wolf(world, size,false);
            }
            wolfPack.addMember(wolf);
            wolf.setPack(wolfPack);
        }
        return wolfPack.getAlpha();
    }
}
