package entities;

import itumulator.world.Location;
import itumulator.world.World;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.executable.DisplayInformation;

import java.awt.Color;

/**
 * Represents a fungus in a simulation world, typically growing on carcasses.
 * The fungus has behaviors such as growing, spreading, and a lifecycle that
 * ends when it dies.
 */
public class Fungus extends Entity implements Actor, DynamicDisplayInformationProvider {
    private int size;
    private boolean visible;
    private Location location;
    private double lifespan;
    private double remainingLife;
    private int sporeRange;
    private double defaultLifespan = 20;
    public static final int GROWTH_THRESHOLD = 5;

    /**
     * Constructs a Fungus instance based on the size of a carcass.
     *
     * @param world       The world where the fungus exists.
     * @param size        The size of the world.
     * @param carcassSize The size of the carcass on which the fungus is growing.
     */
    public Fungus(World world, int size, int carcassSize) {
        super(world, size);
        this.location = initialLocation;
        this.size = 0; // Initial size of the fungus
        this.visible = false; // Initially, the fungus is not visible
        this.lifespan = carcassSize;
        this.lifespan = carcassSize * 1.5; // Lifespan based on carcass size
        this.remainingLife = this.lifespan;
        this.sporeRange = 2; // Example range value
    }

    /**
     * Constructs a Fungus instance with a default lifespan.
     *
     * @param world The world where the fungus exists.
     * @param size  The size of the world.
     */
    public Fungus(World world, int size) {
        super(world, size);
        this.location = initialLocation;
        this.size = 0; // Initial size of the fungus
        this.visible = false; // Initially, the fungus is not visible
        this.lifespan = defaultLifespan; // Use the default lifespan
        this.remainingLife = this.lifespan;
        this.sporeRange = 2; // Example range value
    }

    /**
     * Defines the behavior of the fungus in each simulation step.
     * This includes decrementing its remaining life and attempting to spread if its
     * life ends.
     *
     * @param world The world in which the fungus acts.
     */
    @Override
    public void act(World world) {
        if (!world.contains(this)) {
            return;
        }
        if (--remainingLife <= 0) {
            if (!checkAndSpreadToNearbyCarcasses(world)) {
                die(world); // Fungus dies if it cannot spread
            }
        }
    }

    /**
     * Checks for nearby carcasses to spread to. Spreads to the first carcass found
     * within its spore range.
     *
     * @param world The world where the fungus attempts to spread.
     * @return True if the fungus successfully spreads, false otherwise.
     */
    private boolean checkAndSpreadToNearbyCarcasses(World world) {
        // Check adjacent tiles for carcasses
        for (Location loc : world.getSurroundingTiles(location, sporeRange)) {
            Object object = world.getTile(loc);
            if (object instanceof Carcass) {
                spreadTo((Carcass) object); // Spread to this carcass
                return true; // Fungus has successfully spread
            }
        }
        return false; // No nearby carcasses to spread to
    }

    /**
     * Spreads the fungus to a target carcass.
     *
     * @param targetCarcass The carcass to which the fungus spreads.
     */
    private void spreadTo(Carcass targetCarcass) {
        if (targetCarcass != null && targetCarcass.getFungus() == null) {
            // Check if the target carcass is within the spore range
            if (isWithinSporeRange(targetCarcass.getLocation())) {
                // Create a new fungus on the target carcass
                Fungus newFungus = new Fungus(world, size);
                targetCarcass.setFungus(newFungus);
            }
        }
    }

    /**
     * Handles the death of the fungus by removing it from the world.
     *
     * @param world The world where the fungus dies.
     */
    private void die(World world) {
        world.delete(this); // Remove the fungus from the world
    }

    /**
     * Increases the size of the fungus and makes it visible if it reaches a certain
     * size.
     */
    public void grow() {
        size++;
        if (size >= GROWTH_THRESHOLD) {
            makeVisible();
        }
    }

    /**
     * Returns the current size of the fungus.
     *
     * @return The size of the fungus.
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the remaining life of the fungus.
     *
     * @return The remaining life of the fungus.
     */
    public double getRemainingLife() {
        return remainingLife;
    }

    /**
     * Returns the total lifespan of the fungus.
     *
     * @return The lifespan of the fungus.
     */
    public double getLifespan() {
        return lifespan;
    }

    /**
     * Checks if the fungus is currently visible.
     *
     * @return True if the fungus is visible, false otherwise.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Makes the fungus visible. This method is called when the fungus
     * reaches a certain size.
     */
    public void makeVisible() {
        this.visible = true;
    }

    /**
     * Checks if a target location is within the spore spreading range of the
     * fungus.
     *
     * @param targetLocation The location to check.
     * @return True if the target location is within range, false otherwise.
     */
    private boolean isWithinSporeRange(Location targetLocation) {
        int dx = Math.abs(this.location.getX() - targetLocation.getX());
        int dy = Math.abs(this.location.getY() - targetLocation.getY());
        return dx <= sporeRange && dy <= sporeRange;
    }

    /**
     * Provides display information for the fungus. The display changes based on
     * whether the fungus is visible or not.
     *
     * @return DisplayInformation object containing details about how the fungus
     *         should be displayed.
     */
    @Override
    public DisplayInformation getInformation() {
        if (visible) {
            return new DisplayInformation(Color.GREEN, "fungi", true);
        } else {
            return new DisplayInformation(new Color(0, 0, 0, 0)); // Transparent color, effectively invisible
        }
    }
}
