package utilities;

import itumulator.world.World;

public class SimulationManager {
    private static int totalSteps = 0;

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
     * Resets the simulation manager.
     * 
     */
    public static void reset() {
        totalSteps = 0;
    }
}
