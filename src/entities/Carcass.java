package entities;

import java.awt.Color;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Carcass extends Entity implements Actor, DynamicDisplayInformationProvider {
    private int meatQuantity;
    private Location location;
    private World world;
    private String animalType;
    private int decayCounter;
    private Fungus fungus;

    /**
     * Constructs a Carcass at a specified location in the world (i.e. where the animal died).
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
     * Constructs a Carcass within the specified world and assigns it a random initial
     * location.
     *
     * @param world      The world where the carcass exists.
     * @param size       The size parameter used to generate the carcass's initial location.
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

    private void onFullyDecayed() {
        // When the carcass is fully decayed, remove it from the world
        world.delete(this);
        // If the fungus has grown to a certain size, make it visible
        if (fungus.getSize() >= Fungus.GROWTH_THRESHOLD && fungus.isVisible()) {
            world.setTile(location, fungus);
            System.out.println("Fungus is now visible at: " + location);
        }
    }

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

    private int calculateInitialDecayCounter() {
        return 10; // Set this to the number of simulation steps after which the carcass decays.
    }

    public void consumeMeat(int amount) {
        this.meatQuantity -= amount;
        if (this.meatQuantity <= 0) {
            decayCounter = 0; // Trigger decay in the next simulation step.
        }
    }

    public int getMeatQuantity() {
        return meatQuantity;
    }

    public Location getLocation() {
        return location;
    }

    public Fungus getFungus() {
        return fungus;
    }

    public boolean hasFungus() {
        return fungus != null;
    }

    public boolean hasMeat() {
        return meatQuantity > 0;
    }

    public void setFungus(Fungus fungus) {
        this.fungus = fungus;
    }

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
