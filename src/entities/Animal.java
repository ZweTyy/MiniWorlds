package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import itumulator.world.Location;
import itumulator.world.World;

/**
 * Abstract base class for animals in the simulation.
 * Provides common functionalities for animal entities, such as movement,
 * sleeping, and stat updates.
 */
public abstract class Animal extends Entity {
    protected Location currentLocation = initialLocation;
    protected boolean alive = true;
    protected boolean hasReproducedThisTurn = false;
    protected double hunger = 100.0;
    protected double energy = 50.0;
    protected int age = 1;
    protected int health = 100;
    protected int stepsTaken = 0;
    protected int MAX_AGE;

    /**
     * Constructs an Animal within the specified world and assigns it a random
     * initial location.
     *
     * @param world The world where the animal exists.
     * @param size  The size parameter used to generate the animal's initial
     *              location.
     */
    public Animal(World world, int size) {
        super(world, size);
    }

    /**
     * Moves the animal to a new location within the world.
     * The movement is based on the surrounding empty tiles and the animal's energy.
     *
     * @param world The world where the animal moves.
     */
    public synchronized void move(World world) {
        if (this.health <= 0 || this.age >= MAX_AGE) {
            this.alive = false;
            createCarcass();
            return; // Stop further actions as the animal is dead
        }
        if (world.getCurrentLocation() == null || !alive) {
            // System.out.println(getClass().getSimpleName() + this + " is dead or has no
            // location.");
            return;
        }
        Set<Location> neighbours = world.getEmptySurroundingTiles(); // Hent alle tomme nabo tiles
        List<Location> validLocations = new ArrayList<>(neighbours); // Lav en liste med alle tomme nabo tiles
        if (validLocations.isEmpty() || energy <= 0) { // Hvis der ikke er nogen tomme nabo tiles skipper vi eller hvis
                                                       // kaninen ikke har energi
            System.out.println(getClass().getSimpleName() + this + " has no energy or no empty neighbouring tiles.");
            this.health -= 10;
            return;
        }
        if (!validLocations.isEmpty() && energy > 0) { // Hvis der er tomme nabo tiles og kaninen har energi bevæger den
                                                       // sig
            int randomIndex = r.nextInt(validLocations.size());
            Location newLocation = validLocations.get(randomIndex);
            this.initialLocation = newLocation;
            world.move(this, initialLocation);
            world.setCurrentLocation(initialLocation);
            this.energy -= 2.5;
            this.hunger -= 5.0;
            this.stepsTaken++;
        }
        updateStats();
    }

    /**
     * Causes the animal to sleep, increasing its energy and modifying hunger.
     * This method simulates the resting state of the animal.
     */
    public void sleep() {
        this.energy += 15;
        this.stepsTaken++;
        if (this.health <= 100) {
            this.health += 5;
        }
    }

    /**
     * Updates the animal's stats based on its current state.
     * This includes aging, energy and hunger management, and checking for death
     * conditions.
     */
    public void updateStats() {
        if (this.stepsTaken % 5 == 0) {
            this.age++;
        }
        if (this.hunger <= 0) {
            this.health -= 2;
            this.hunger = 0;
        }
        if (this.energy <= 0) {
            this.health -= 5;
        }
    }

    /**
     * Sets the alive status of the animal.
     *
     * @param alive The alive status to set for the animal.
     * @return The updated alive status.
     */
    public boolean setAlive(boolean alive) {
        return this.alive = false;
    }

    /**
     * Checks if the animal is alive.
     *
     * @return boolean indicating whether the animal is alive.
     */
    public boolean isAlive() {
        return this.alive;
    }

    /**
     * Sets the energy level of the animal.
     *
     * @param energy The energy level to set for the animal.
     * @return The updated energy level.
     */
    public double setEnergy(int energy) {
        return this.energy = energy;
    }

    /**
     * Returns the animal's energy level.
     *
     * @return The animal's energy level.
     */
    public double getEnergy() {
        return this.energy;
    }

    /**
     * Sets the hunger level of the animal.
     *
     * @param hunger The hunger level to set for the animal.
     * @return The updated hunger level.
     */
    public int setAge(int age) {
        return this.age = age;
    }

    /**
     * Gets the current age of the animal.
     *
     * @return The current age of the animal.
     */
    public int getAge() {
        return this.age;
    }

    /**
     * Sets the hunger level of the animal.
     *
     * @param hunger The hunger level to set for the animal.
     * @return The updated hunger level.
     */
    public double setHunger(int hunger) {
        return this.hunger = hunger;
    }

    /**
     * Gets the current hunger level of the animal.
     *
     * @return The current hunger level of the animal.
     */
    public double getHunger() {
        return this.hunger;
    }

    /**
     * Sets the health level of the animal.
     *
     * @param health The health level to set for the animal.
     */
    public void setHealth(int health) {
        this.health = health;
        if (this.health <= 0) {
            this.alive = false;
            createCarcass();
        }
    }

    /**
     * Gets the current health level of the animal.
     *
     * @return The current health level of the animal.
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Helper method to create a carcass when an animal dies.
     * The carcass is created at the animal's current location.
     * 
     */
    protected void createCarcass() {
        Location loc = world.getLocation(this);
        world.delete(this);
        if (loc != null && world.isTileEmpty(loc)) {
            System.out.println("Creating carcass at: " + loc);
            String animalType = this.getClass().getSimpleName().toLowerCase();
            Carcass carcass = new Carcass(world, world.getSize(), loc, animalType);
            world.setTile(loc, carcass);
        }
    }
}
