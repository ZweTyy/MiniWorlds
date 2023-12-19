package factories;

import java.util.ArrayList;
import java.util.List;

import entities.Mole;
import itumulator.world.World;

public class MoleFactory {

    public static Mole createMole(World world, int size) {
        return new Mole(world, size);
    }

    public static List<Mole> createMultipleMoles(World world, int size, int numberOfMoles) {
        List<Mole> moles = new ArrayList<>();
        for (int i = 0; i < numberOfMoles; i++) {
            moles.add(createMole(world, size));
        }
        return moles;
    }

}
