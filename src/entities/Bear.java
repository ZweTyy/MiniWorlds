package entities;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Represents a bear in a simulated ecosystem.
 * Bears can move within a defined territory, and they can eat herbs (like berries).
 * Implements Actor, Herbivore, and Carnivore interfaces.
 */
public class Bear extends Animal implements Actor, Herbivore, Carnivore {
    private Location initialTerritoryLocation;
    private boolean isHungry = false;
    private final int TERRITORY_RADIUS = 3;

    /**
     * Constructs a Bear at a specified location in the world.
     *
     * @param world The world where the bear exists.
     * @param size The size of the world.
     * @param x The x-coordinate of the bear's initial territory location.
     * @param y The y-coordinate of the bear's initial territory location.
     */
    public Bear(World world, int size, int x, int y) {
        super(world, size);
        this.MAX_AGE = 20;
        this.initialTerritoryLocation = new Location(x, y);
        this.setLocation(initialTerritoryLocation);
    }

    /**
     * Constructs a Bear within the specified world and assigns it a random initial location.
     *
     * @param world The world where the bear exists.
     * @param size The size parameter used to generate the bear's initial location.
     */
    public Bear(World world, int size) {
        super(world, size);
        this.MAX_AGE = 20;
        initialTerritoryLocation = generateRandomLocation(size);
    }

    /**
     * Defines the actions the bear takes in each simulation step.
     * The bear moves within its territory and may eat herbs.
     *
     * @param world The world in which the bear acts.
     */
    @Override
    public void act(World world) {
        move(world);
        eatHerb(world);
    }

    /**
     * Moves the bear to a new location within its territory.
     * The movement is based on the surrounding empty tiles within the bear's territory.
     *
     * @param world The world where the bear moves.
     */
    @Override
    public void move(World world) {
        Set<Location> neighbours = world.getEmptySurroundingTiles(initialLocation);
        List<Location> validLocations = new ArrayList<>();

        for (Location loc : neighbours) {
            if (isWithinTerritory(loc)) {
                validLocations.add(loc);
            }
        }

        if (!validLocations.isEmpty() && energy > 0) { // Hvis der er tomme nabo tiles og bjørnen har energi bevæger den
                                                       // sig
            int randomIndex = r.nextInt(validLocations.size());
            Location newLocation = validLocations.get(randomIndex);
            this.initialLocation = newLocation;
            world.move(this, initialLocation);
            world.setCurrentLocation(initialLocation);
            System.out.println(getClass().getSimpleName() + this + " moving to: " + newLocation);

            this.energy -= 2.5;
            this.hunger -= 5.0;
            this.stepsTaken++;
            System.out.println("age " + this.age);
        }
        super.updateStats();
        System.out.println("health " + this.health + " energy " + this.energy + " hunger " + this.hunger);
    }

    /**
     * Checks if a location is within the bear's defined territory.
     *
     * @param loc The location to check.
     * @return boolean indicating whether the location is within the bear's territory.
     */
    private boolean isWithinTerritory(Location loc) {
        int dx = Math.abs(loc.getX() - initialTerritoryLocation.getX());
        int dy = Math.abs(loc.getY() - initialTerritoryLocation.getY());
        return dx <= TERRITORY_RADIUS && dy <= TERRITORY_RADIUS;
    }

    /**
     * Enables the bear to eat berries (herbs) in its current location to gain energy.
     * This action increases the bear's energy and hunger levels.
     *
     * @param world The world in which the bear eats.
     */
    @Override
    public void eatHerb(World world) {
        if (hunger <= 75) {
            System.out.println("Attempting to eat berries");
            try {
                for (Location loc : world.getSurroundingTiles(initialLocation)) {
                    Object object = world.getTile(loc);
                    if (object instanceof Berry) {
                        Berry berry = (Berry) object;
                        berry.eaten();
                        this.hunger += 50;
                        this.energy += 25;
                        System.out.println("I sucessfully ate");
                    }
                    world.step();
                    System.out.println("Nothing to eat");
                    return;
                }

            } catch (IllegalArgumentException iae) {
                // Vi burde ikke aldrig nå herned da vi håndterer exception tidligere
                System.out.println("No entity");
                return;
            }
        }
    }

    // @Override
    // public void eatMeat(World world) {

    // }
}
