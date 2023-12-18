package factories;

import java.util.List;

import entities.Wolf;
import entities.WolfPack;
import itumulator.world.World;

/**
 * This class is a factory for creating WolfPack objects.
 * It provides a static method to create a wolf pack with a specified number of wolves.
 */
public class WolfFactory {

    /**
     * Creates a WolfPack in the given world, populating it with a specified number of wolves.
     * The first wolf created is designated as the alpha wolf of the pack.
     *
     * @param world The world in which the wolf pack and wolves will exist.
     * @param size The size parameter for the creation of wolves and the wolf pack.
     * @param numberOfWolves The number of wolves to create and add to the wolf pack.
     * @return A newly created WolfPack populated with the specified number of wolves.
     */
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
