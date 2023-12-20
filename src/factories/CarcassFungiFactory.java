package factories;

import java.util.List;
import java.util.ArrayList;

import entities.Carcass;
import itumulator.world.World;

public class CarcassFungiFactory {

    public static List<Carcass> createMultipleCarcassesFungi(World world, int size, int quantity) {
        List<Carcass> carcasses = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            Carcass carcass = new Carcass(world, size);
            carcasses.add(carcass);
        }
        return carcasses;
    }
}
