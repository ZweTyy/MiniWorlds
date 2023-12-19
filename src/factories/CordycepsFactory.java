package factories;

import itumulator.world.World;
import entities.Rabbit;
import entities.Wolf;
import entities.WolfPack;

import java.util.List;
import java.util.ArrayList;

public class CordycepsFactory {
    
    public static List<Rabbit> createMultipleCordycepsRabbits(World world, int size, int quantity) {
        List<Rabbit> rabbits = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            Rabbit rabbit = new Rabbit(world, size);
            rabbit.setInfected(true); // Assuming a method to set infection status
            rabbits.add(rabbit);
        }
        return rabbits;
    }
    
    public static List<Wolf> createMultipleCordycepsWolves(World world, int size, int quantity) {
        List<Wolf> wolves = new ArrayList<>();
        WolfPack wolfPack = new WolfPack(world, size); // One pack for all wolves
        wolfPack.createDen(world, size); // One den for the pack
    
        for (int i = 0; i < quantity; i++) {
            Wolf wolf = new Wolf(world, size);
            wolf.setInfected(true);
            wolf.setPack(wolfPack); // All wolves belong to the same pack
            wolfPack.addMember(wolf); // Add the wolf to the pack
    
            wolves.add(wolf);
        }
        return wolves;
    }
}
