package entities;

import itumulator.world.Location;
import itumulator.world.World;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.executable.DisplayInformation;

import java.awt.Color;

public class Fungus extends Entity implements Actor, DynamicDisplayInformationProvider {
    private int size;
    private boolean visible;
    private Location location;
    private double lifespan; // Lifespan of the fungus
    private double remainingLife; // Remaining life of the fungus
    private int sporeRange; // The range in which fungus can spread spores
    private double defaultLifespan = 20;
    public static final int GROWTH_THRESHOLD = 5; // Set this to your desired threshold

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

    public Fungus(World world, int size) {
        super(world, size);
        this.location = initialLocation;
        this.size = 0; // Initial size of the fungus
        this.visible = false; // Initially, the fungus is not visible
        this.lifespan = defaultLifespan; // Use the default lifespan
        this.remainingLife = this.lifespan;
        this.sporeRange = 2; // Example range value
    }

    @Override
    public void act(World world) {
        if (!world.contains(this)) {
            return;
        }
        // Decrease remaining life
        if (--remainingLife <= 0) {
            if (!checkAndSpreadToNearbyCarcasses(world)) {
                die(world); // Fungus dies if it cannot spread
            }
        }

        // Additional fungus behavior (e.g., growing, spreading spores)
    }

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

    private void die(World world) {
        world.delete(this); // Remove the fungus from the world
    }

    public void grow() {
        size++;
        if (size >= GROWTH_THRESHOLD) {
            makeVisible();
        }
    }

    public int getSize() {
        return size;
    }

    public double getRemainingLife() {
        return remainingLife;
    }

    public double getLifespan() {
        return lifespan;
    }

    public boolean isVisible() {
        return visible;
    }

    // This method makes the fungus visible and should be called when the fungus reaches a certain size
    public void makeVisible() {
        this.visible = true;
    }

    private boolean isWithinSporeRange(Location targetLocation) {
        int dx = Math.abs(this.location.getX() - targetLocation.getX());
        int dy = Math.abs(this.location.getY() - targetLocation.getY());
        return dx <= sporeRange && dy <= sporeRange;
    }

    @Override
    public DisplayInformation getInformation() {
        if (visible) {
            // If the fungus is visible, return display information with a specific color and possibly an image
            // The imageKey should correspond to an actual image in your resources if images are being used
            // The random_direction is set to true to allow for random orientation of the image
            return new DisplayInformation(Color.GREEN, "fungi", true);
        } else {
            // If the fungus is not visible yet, return display information with a transparent color
            return new DisplayInformation(new Color(0, 0, 0, 0)); // Transparent color, effectively invisible
        }
    }
}
