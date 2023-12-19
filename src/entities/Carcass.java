package entities;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Carcass extends Entity implements Actor {
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
        this.fungus = new Fungus(world, size); // Initialize the fungus when the carcass is created
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
        this.decayCounter = 10;
        this.fungus = new Fungus(world, size); // Initialize the fungus when the carcass is created
    }

    @Override
    public void act(World world) {
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
        if (fungus.getSize() >= Fungus.SOME_GROWTH_THRESHOLD && !fungus.isVisible()) {
            fungus.makeVisible();
        }
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

    private int calculateInitialDecayCounter() {
        return 10; // Set this to the number of simulation steps after which the carcass decays.
    }

    public void consumeMeat(int amount) {
        this.meatQuantity -= amount;
        if (this.meatQuantity <= 0) {
            decayCounter = 0; // Trigger decay in the next simulation step.
        }
    }

    // Accessor methods as needed for meatQuantity, location, and fungus.
    public int getMeatQuantity() {
        return meatQuantity;
    }

    public Location getLocation() {
        return location;
    }

    public Fungus getFungus() {
        return fungus;
    }
}
