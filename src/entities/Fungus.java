package entities;

import itumulator.world.Location;
import itumulator.world.World;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.executable.DisplayInformation;

import java.awt.Color;

public class Fungus extends Entity implements DynamicDisplayInformationProvider {
    private int size;
    private boolean visible;
    private Location location;
    static final int SOME_GROWTH_THRESHOLD = 5; // Set this to your desired threshold

    public Fungus(World world, int size) {
        super(world, size);
        this.location = initialLocation;
        this.size = 0; // Initial size of the fungus
        this.visible = false; // Initially, the fungus is not visible
    }

    public void grow() {
        size++;
        if (size >= SOME_GROWTH_THRESHOLD) {
            makeVisible();
        }
    }

    public int getSize() {
        return size;
    }

    public boolean isVisible() {
        return visible;
    }

    // This method makes the fungus visible and should be called when the fungus reaches a certain size
    public void makeVisible() {
        this.visible = true;
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
