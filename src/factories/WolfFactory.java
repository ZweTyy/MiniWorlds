package factories;

import java.util.List;

import WolfPack;
import entities.Wolf;
import itumulator.world.World;

public class WolfFactory {
    public static WolfPack createWolfPack(World world, int size, int numberOfWolves) {
        WolfPack wolfPack = new WolfPack(world, size);
        for (int i = 0; i < numberOfWolves; i++) {
            Wolf wolf = new Wolf(world, size);
            wolfPack.addMember(wolf);
            wolf.setPack(wolfPack);
            if (i == 0) {
                wolf.setIsAlpha(true);
            }
        }
        return wolfPack;
    }
}
