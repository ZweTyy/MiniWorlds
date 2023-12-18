package entities;

import java.util.List;
import java.util.ArrayList;

import itumulator.world.World;
import itumulator.world.Location;
import itumulator.world.NonBlocking;

import java.util.Random;

/**
 * Represents a burrow in a simulated ecosystem.
 * Burrows can house rabbits, and they can be occupied by a maximum of three rabbits.
 * Implements NonBlocking interface.
 */
public class Burrow extends Entity implements NonBlocking {
    private List<Rabbit> rabbits;
    protected static final int MAX_CAPACITY = 3;

    Random r = new Random();

    /**
     * Constructs a Burrow within the specified world and assigns it a random initial location.
     *
     * @param world The world where the burrow exists.
     * @param size The size parameter used to generate the burrow's initial location.
     */
    public Burrow(World world, int size) {
        super(world, size);
        this.rabbits = new ArrayList<>();
    }

    /**
     * Defines the actions the burrow takes in each simulation step.
     * The burrow may add a rabbit to its list of rabbits.
     *
     * @param world The world in which the burrow acts.
     */
    public boolean addRabbit(Rabbit rabbit) {
        if (rabbits.size() < MAX_CAPACITY) {
            rabbits.add(rabbit);
            return true;
        }
        return false;
    }

    /**
     * Defines the actions the burrow takes when a rabbit leaves.
     * The burrow removes the rabbit from its list of rabbits.
     *
     * @param rabbit The rabbit that is leaving the burrow.
     */
    public void removeRabbit(Rabbit rabbit) {
        rabbits.remove(rabbit);
    }

    /**
     * Returns the number of rabbits currently in the burrow.
     *
     * @return The number of rabbits currently in the burrow.
     */
    public int getCurrentOccupants() {
        return rabbits.size();
    }
}
