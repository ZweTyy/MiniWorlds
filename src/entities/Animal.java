package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import itumulator.world.Location;
import itumulator.world.World;

public abstract class Animal extends Entity {
    protected Location currentLocation;
    protected boolean alive = true;
    protected double hunger = 100.0;
    protected double energy = 50.0;
    protected int age = 1;
    protected int health = 100;
    protected int stepsTaken = 0;
    protected int MAX_AGE;

    public Animal(World world, int size) {
        super(world, size);
    }

    protected synchronized void move(World world) {
        // System.out.println(getClass().getSimpleName() + this + " moving from: " + initialLocation);
        if (world.getCurrentLocation() == null || !alive) {
            // System.out.println(getClass().getSimpleName() + this + " is dead or has no location.");
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
            // System.out.println(getClass().getSimpleName() + this + " moving to: " + newLocation);

            this.energy -= 2.5;
            this.hunger -= 5.0;
            this.stepsTaken++;
            // System.out.println("age " + this.age);
        }
        updateStats();
        // System.out.println("health " + this.health + " energy " + this.energy + " hunger " + this.hunger);
    }

    public void sleep() {
        this.energy += 10;
        this.hunger -= 1;
        this.stepsTaken++;
    }

    public void updateStats() {
        if (this.stepsTaken % 5 == 0) {
            this.age++;
        }
        if (this.hunger <= 0) {
            this.health -= 10;
        }
        if (this.energy <= 0) {
            this.health -= 5;
        }
        if (this.health <= 0 || this.age >= MAX_AGE) {
            this.alive = false;
            world.delete(this);
        }
    }

    public boolean setAlive(boolean alive) {
        return this.alive = false;
    }

    public boolean isAlive() {
        return this.alive;
    }

    public double setEnergy(int energy) {
        return this.energy = energy;
    }

    public double getEnergy() {
        return this.energy;
    }

    public int setAge(int age) {
        return this.age = age;
    }

    public int getAge() {
        return this.age;
    }
}
