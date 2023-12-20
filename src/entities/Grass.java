package entities;

import java.util.Random;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import itumulator.simulator.Actor;

/**
 * Represents grass in a simulated ecosystem.
 * Grass can spread to nearby locations and has a chance to decay over time.
 * Implements Actor and NonBlocking interfaces.
 */
public class Grass extends Nature implements Actor, NonBlocking {
    private boolean alive;
    Random r = new Random();

    /**
     * Constructs a Grass object with a reference to the world it belongs to and its size.
     *
     * @param world the world in which the grass exists.
     * @param size the size of the world, used for generating random locations.
     */
    public Grass(World world, int size) {
        super(world, size);
        this.alive = true;
    }

    /**
     * Defines the actions the grass takes in each simulation step.
     * The grass has a chance to decay and spread to nearby locations.
     *
     * @param world the world in which the grass acts.
     */
    @Override
    public void act(World world) {
        decay(world);
        spread(world, world.getSize());
    }

    /**
     * Defines the actions the grass takes when it dies.
     * The grass is deleted from the world.
     *
     * @param world the world in which the grass dies.
     */
    public void die(World world) {
        this.alive = false;
        world.delete(this);
    }

    /**
     * Returns whether the grass is alive or not.
     *
     * @return true if the grass is alive, false otherwise.
     */
    public boolean isAlive() {
        return alive;
    }
    
    /**
     * Defines the actions the grass takes when it decays.
     * The grass has a chance to die.
     *
     * @param world the world in which the grass decays.
     */
    public void decay(World world) {
        // Græs har en chance for at dø tilfældigt
        if (this.alive && r.nextDouble() < 0.025) { // 2.5% chance for at dø
            die(world);
        }
    }

    /**
     * Defines the actions the grass takes when it spreads.
     * The grass has a chance to spread to nearby locations.
     *
     * @param world the world in which the grass spreads.
     * @param size the size of the world, used for generating random locations.
     */
    public void spread(World world, int size) {
        if (this.alive && r.nextDouble() < 0.1) { // 10% chance for at sprede sig
            Set<Location> neighbours = world.getEmptySurroundingTiles(super.getLocation());
            List<Location> validLocations = new ArrayList<>();

            // Her tilføjer vi valide lokationer til listen
            for (Location loc : neighbours) {
                if (world.isTileEmpty(loc) && !world.containsNonBlocking(loc)) {
                    validLocations.add(loc);
                }
            }
            // Hvis der er valid locations, så vælg en tilfældigt og lav nyt græs
            if (!validLocations.isEmpty()) {
                int randomIndex = r.nextInt(validLocations.size());
                Location newLocation = validLocations.get(randomIndex);

                // Tjek igen hvis der er græs på lokationen og sæt græs hvis der ikke er
                if (world.isTileEmpty(newLocation) && !world.containsNonBlocking(newLocation)) {
                    Grass newGrass = new Grass(world, size);
                    world.setTile(newLocation, newGrass);
                }
            }
        }
    }
}
