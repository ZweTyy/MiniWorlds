package entities;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

/**
 * Represents a carcass in the simulated ecosystem.
 * The carcass is created when an animal dies and can be consumed by carnivorous
 * animals.
 */
public class Carcass extends Entity implements Actor {
    private int meatQuantity;
    private Location location;
    private World world;
    private String animalType;

    /**
     * Constructs a Carcass with a specific amount of meat and its location in the
     * world.
     *
     * @param world        The world in which the carcass exists.
     * @param location     The location of the carcass.
     * @param meatQuantity The amount of meat the carcass contains, based on the
     *                     size of the animal.
     */
    public Carcass(World world, int size, Location location, String animalType) {
        super(world, size);
        this.location = location;
        this.animalType = animalType;
        this.meatQuantity = calculateMeatQuantity();
    }

    /**
     * Gets the amount of meat on the carcass.
     *
     * @return The amount of meat.
     */
    public int getMeatQuantity() {
        return meatQuantity;
    }

    /**
     * Reduces the amount of meat on the carcass when it is consumed.
     *
     * @param amount The amount of meat to be consumed.
     */
    public void consumeMeat(int amount) {
        this.meatQuantity -= amount;
        if (this.meatQuantity <= 0) {
            decay();
        }
    }

    /**
     * Removes the carcass from the world when all meat is consumed or it decays.
     */
    private void decay() {
        world.remove(this.location);
    }

    /**
     * Implements the act method from the Actor interface.
     * The carcass may have actions such as decaying over time.
     *
     * @param world The world in which the carcass acts.
     */
    @Override
    public void act(World world) {
        // Implement any behavior like decaying over time if needed.
    }

    private int calculateMeatQuantity() {
        switch (animalType) {
            case "rabbit":
                return 5;
            case "wolf":
                return 15;
            case "bear":
                return 30;
            default:
                return 0;
        }
    }
}
