package factories;

import itumulator.world.World;
import entities.Rabbit;
import entities.Wolf;

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
        for (int i = 0; i < quantity; i++) {
            Wolf wolf = new Wolf(world, size);
            wolf.setInfected(true); // Assuming a method to set infection status
            wolves.add(wolf);
        }
        return wolves;
    }
}
