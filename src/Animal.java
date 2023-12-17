import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import itumulator.world.Location;
import itumulator.world.World;

public abstract class Animal extends Entity {
    protected Location currentLocation;
    protected boolean alive = true;
    protected volatile boolean hasReproducedThisTurn = false;
    protected int age = 1;
    protected int energy = 100;
    protected int stepsTaken = 0;
    protected int MAX_AGE;
    List<Class> foods; 
    List<Animal> fears;  
    

    public Animal(World world, int size) {
        super(world, size);
        
    }

    protected synchronized void move(World world) {
        System.out.println(getClass().getSimpleName() + this + " moving from: " + initialLocation);
        if (world.getCurrentLocation() == null || !alive) {
            System.out.println(getClass().getSimpleName() + this + " is dead or has no location.");
            return;
        }
        Set<Location> neighbours = world.getEmptySurroundingTiles(); // Hent alle tomme nabo tiles
        List<Location> validLocations = new ArrayList<>(neighbours); // Lav en liste med alle tomme nabo tiles
        if (validLocations.isEmpty() || energy <= 0) { // Hvis der ikke er nogen tomme nabo tiles skipper vi eller hvis
                                                       // kaninen ikke har energi
            System.out.println(getClass().getSimpleName() + this + " has no energy or no empty neighbouring tiles.");
            this.energy -= 10;
            return;
        }
        if (!validLocations.isEmpty() && energy > 0) { // Hvis der er tomme nabo tiles og kaninen har energi bevæger den
                                                       // sig
            int randomIndex = r.nextInt(validLocations.size());
            Location newLocation = validLocations.get(randomIndex);
            this.initialLocation = newLocation;
            world.move(this, initialLocation);
            world.setCurrentLocation(initialLocation);
            System.out.println(getClass().getSimpleName() + this + " moving to: " + newLocation);

            this.energy -= 7.5;
            this.stepsTaken++;
            System.out.println("age " + this.age);
        }
        updateStats();
        System.out.println("energy " + this.energy );
    }

    public void sleep() {
        this.energy += 20;
        this.stepsTaken++;
    }

    public void updateStats() {
        if (this.stepsTaken % 5 == 0) {
            this.age++;
        }
        if (this.energy <= 0 || this.age >= MAX_AGE) {
            this.alive = false;
            world.delete(this);
        }
    }

     public void eat(World world) {
        
    }

     public void reproduce(World world, int size) {
        
    }

    public boolean setAlive(boolean alive) {
        return this.alive = false;
    }

    public boolean isAlive() {
        return this.alive;
    }

    public int setAge(int age) {
        return this.age = age;
    }

    public int getAge() {
        return this.age;
    }
}
