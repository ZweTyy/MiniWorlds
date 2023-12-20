package entities;

import java.awt.Color;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

/**
 * This class represents a carcass in the simulation.
 * It provides functionalities for handling the carcass's state, such as
 * decaying and growing fungus.
 */
public class Carcass extends Entity implements Actor, DynamicDisplayInformationProvider {
    private int meatQuantity;
    private Location location;
    private World world;
    private String animalType;
    private int decayCounter;
    private Fungus fungus;

    /**
     * Constructs a Carcass at a specified location in the world (i.e. where the
     * animal died).
     *
     * @param world      The world where the carcass exists.
     * @param size       The size of the world.
     * @param location   The location of the carcass.
     * @param animalType The type of animal that the carcass came from.
     */
    public Carcass(World world, int size, Location location, String animalType) {
        super(world, size);
        this.world = world;
        this.location = location;
        this.animalType = animalType;
        this.meatQuantity = calculateMeatQuantity();
        this.decayCounter = calculateInitialDecayCounter();
        this.fungus = new Fungus(world, size, meatQuantity); // Initialize the fungus when the carcass is created
    }

    /**
     * Constructs a Carcass within the specified world and assigns it a random
     * initial
     * location.
     *
     * @param world      The world where the carcass exists.
     * @param size       The size parameter used to generate the carcass's initial
     *                   location.
     * @param animalType The type of animal that the carcass came from.
     */
    public Carcass(World world, int size) {
        super(world, size);
        this.world = world;
        this.location = initialLocation;
        this.meatQuantity = 10;
        this.decayCounter = 15;
        this.fungus = new Fungus(world, size, meatQuantity); // Initialize the fungus when the carcass is created
    }

    /**
     * Defines the behavior of the carcass in each simulation step.
     * It checks the state of the carcass and updates it accordingly, such as
     * decaying or growing fungus.
     *
     * @param world The world in which the carcass exists.
     */
    @Override
    public void act(World world) {
        if (!world.contains(this)) {
            System.out.println("Carcass is not in the world.");
            return;
        }

        if (meatQuantity > 0) {
            // If there is still meat, decrement the decay counter and grow the fungus
            decayCounter--;
            fungus.grow();
        }

        // Check if the carcass should decay now or if it's already fully decayed.
        if (decayCounter <= 0 || meatQuantity <= 0) {
            onFullyDecayed();
        }
    }

    /**
     * Handles the event when the carcass is fully decayed.
     * Removes the carcass from the world and makes the fungus visible if it has
     * grown to a certain size.
     */
    private void onFullyDecayed() {
        world.delete(this);
        if (fungus.getSize() >= Fungus.GROWTH_THRESHOLD && fungus.isVisible()) {
            world.setTile(location, fungus);
            System.out.println("Fungus is now visible at: " + location);
        }
    }

    /**
     * Calculates the initial quantity of meat based on the type of animal.
     *
     * @return The initial meat quantity of the carcass.
     */
    private int calculateMeatQuantity() {
        switch (animalType) {
            case "rabbit":
                return 10;
            case "wolf":
                return 25;
            case "bear":
                return 50;
            case "mole":
                return 5;
            default:
                return 0;
        }
    }

    /**
     * Calculates the initial decay counter for the carcass.
     *
     * @return The initial decay counter value.
     */
    private int calculateInitialDecayCounter() {
        return 10;
    }

    /**
     * Consumes a specified amount of meat from the carcass and triggers decay if
     * all meat is consumed.
     *
     * @param amount The amount of meat to consume.
     */
    public void consumeMeat(int amount) {
        this.meatQuantity -= amount;
        if (this.meatQuantity <= 0) {
            decayCounter = 0;
        }
    }

    /**
     * Returns the current quantity of meat in the carcass.
     *
     * @return The current meat quantity.
     */
    public int getMeatQuantity() {
        return meatQuantity;
    }

    /**
     * Returns the location of the carcass.
     *
     * @return The current location of the carcass.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Returns the fungus associated with the carcass.
     *
     * @return The fungus growing on the carcass, if any.
     */
    public Fungus getFungus() {
        return fungus;
    }

    /**
     * Checks if the carcass has fungus growing on it.
     *
     * @return True if the carcass has fungus, otherwise false.
     */
    public boolean hasFungus() {
        return fungus != null;
    }

    /**
     * Checks if the carcass still has meat.
     *
     * @return True if the carcass has meat, otherwise false.
     */
    public boolean hasMeat() {
        return meatQuantity > 0;
    }

    /**
     * Sets the fungus associated with the carcass.
     *
     * @param fungus The fungus to be associated with the carcass.
     */
    public void setFungus(Fungus fungus) {
        this.fungus = fungus;
    }

    /**
     * Returns the current decay counter of the carcass.
     *
     * @return The current decay counter.
     */
    public int getDecayCounter() {
        return decayCounter;
    }

    @Override
    public DisplayInformation getInformation() {
        if (meatQuantity > 25) {
            return new DisplayInformation(Color.GREEN, "carcass-small", true);
        } else {
            return new DisplayInformation(Color.GREEN, "carcass");
        }
    }
}
