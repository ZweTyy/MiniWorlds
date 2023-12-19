package factories;

import java.util.ArrayList;
import java.util.List;

import entities.Fungus;
import itumulator.world.World;

public class FungusFactory {

    public static Fungus createFungus(World world, int size) {
        return new Fungus(world, size);
    }

    public static List<Fungus> createMultipleFungi(World world, int size, int numberOfFungi) {
        List<Fungus> fungi = new ArrayList<>();
        for (int i = 0; i < numberOfFungi; i++) {
            fungi.add(createFungus(world, size));
        }
        return fungi;
    }

}
