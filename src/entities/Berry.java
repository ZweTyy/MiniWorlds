package entities;

import itumulator.executable.Program;
import itumulator.simulator.Actor;
import itumulator.world.NonBlocking;
import itumulator.world.World;

/**
 * Represents a berry bush in a simulated ecosystem.
 * This class extends Nature and implements Actor.
 */
public class Berry extends Nature implements Actor {
    private boolean hasBerries = true;
    private int lastEatenDay = -1;

    /**
     * Constructs a Berry bush within the specified world and assigns it a random initial location.
     *
     * @param world The world where the berry bush exists.
     * @param size The size parameter used to generate the berry bush's initial location.
     */
    public Berry(World world, int size) {
        super(world, size);
    }

    /**
     * Defines the actions the berry bush takes in each simulation step.
     * The berry bush may grow berries if it has been a certain number of days since the last time it was eaten.
     *
     * @param world The world in which the berry bush acts.
     */
    @Override
    public void act(World world) {
        int currentDay = world.getCurrentTime() / World.getTotalDayDuration();

        if (!hasBerries && (currentDay > lastEatenDay)) {
            hasBerries = true;
        }
    }

    /**
     * Defines the actions the berry bush takes when it is eaten.
     * The berry bush loses its berries and remembers the day it was eaten.
     */
    public void eaten() {
        if (hasBerries) {
            hasBerries = false;
            lastEatenDay = world.getCurrentTime() / World.getTotalDayDuration();

        }
    }

    /**
     * Returns whether the berry bush has berries.
     *
     * @return true if the berry bush has berries, false otherwise.
     */
    public boolean hasBerries() {
        return hasBerries;
    }

}
