import java.util.List;

import itumulator.world.World;

public class WolfFactory {
    public static Wolf createWolf(World world, int size, List<Integer[]> details) {
        WolfPack wolfPack = new WolfPack(world, size);
        Integer[] detail = details.get(0); // Get the first Integer[] from the list
        int numberOfWolves = detail[0]; // Get the first element of the Integer[]
        for (int i = 0; i < numberOfWolves; i++) {
            Wolf wolf = new Wolf(world, size);
            wolfPack.addMember(wolf);
            wolf.setPack(wolfPack);
        }
        return wolfPack.getAlpha();
    }
}
