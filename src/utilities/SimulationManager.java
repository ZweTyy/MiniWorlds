package utilities;

import java.util.Map;

import entities.Mole;
import entities.Rabbit;
import itumulator.world.Location;
import itumulator.world.World;

public class SimulationManager {
    private static int totalSteps = 0;
    private static int rabbitCount = 0;
    private static int moleCount = 0;

    /**
     * Increments the total number of steps every time a simulation step is taken.
     * 
     */
    public static void incrementStep() {
        totalSteps++;
    }

    /**
     * Gets the current day of the simulation.
     * 
     * @return The current day of the simulation.
     */
    public static int getCurrentDay() {
        return totalSteps / World.getTotalDayDuration();
    }

    /**
     * Counts the number of moles in the world without updating the internal count.
     *
     * @param world the world in which the moles are counted.
     * @return the number of moles in the world.
     */
    public static int countRabbits(World world) {
        Map<Object, Location> entities = world.getEntities();
        int rabbitCount = 0;
        for (Object obj : entities.keySet()) {
            if (obj instanceof Rabbit) {
                rabbitCount++;
            }
        }
        return rabbitCount;
    }

    /**
     * Updates the rabbit count based on the entities in the world.
     *
     * @param world the world in which the rabbits are counted.
     */
    public static void updateRabbitCount(World world) {
        rabbitCount = 0;
        for (Object obj : world.getEntities().keySet()) {
            if (obj instanceof Rabbit) {
                rabbitCount++;
            }
        }
    }

    /**
     * Gets the current rabbit count.
     *
     * @return the number of rabbits in the world.
     */
    public static int getRabbitCount() {
        return rabbitCount;
    }

    /**
     * Counts the number of moles in the world without updating the internal count.
     *
     * @param world the world in which the moles are counted.
     * @return the number of moles in the world.
     */
    public static int countMoles(World world) {
        Map<Object, Location> entities = world.getEntities();
        int moleCount = 0;
        for (Object obj : entities.keySet()) {
            if (obj instanceof Mole) {
                moleCount++;
            }
        }
        return moleCount;
    }

    /**
     * Counts the number of moles in the world and updates the internal mole count.
     *
     * @param world the world in which the moles are counted.
     */
    public static void updateMoleCount(World world) {
        moleCount = countMoles(world);
    }

    /**
     * Gets the current mole count.
     *
     * @return the number of moles in the world.
     */
    public static int getMoleCount() {
        return moleCount;
    }

    /**
     * Resets the simulation manager.
     * 
     */
    public static void reset() {
        totalSteps = 0;
        rabbitCount = 0;
    }
}
