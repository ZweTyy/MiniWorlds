package factories;

import java.util.ArrayList;
import java.util.List;

import entities.Carcass;
import itumulator.world.World;

public class CarcassFactory {

    public static Carcass createCarcass(World world, int size) {
        return new Carcass(world, size);
    }

    public static List<Carcass> createMultipleCarcasses(World world, int size, int numberOfFungi) {
        List<Carcass> Carcasses = new ArrayList<>();
        for (int i = 0; i < numberOfFungi; i++) {
            Carcasses.add(createCarcass(world, size));
        }
        return Carcasses;
    }

}
