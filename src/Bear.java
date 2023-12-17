import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apiguardian.api.API;

public class Bear extends Animal implements Actor {
    private Location initialTerritoryLocation;
    private boolean isHungry = false;
    private final int TERRITORY_RADIUS = 3;

    public Bear(World world, int size, int x, int y) {
        super(world, size);
        this.MAX_AGE = 20;
        this.initialTerritoryLocation = new Location(x, y);
        this.setLocation(initialTerritoryLocation);
    }

    public Bear(World world, int size) {
        super(world, size);
        this.MAX_AGE = 20;
        initialTerritoryLocation = generateRandomLocation(size);
    }

    @Override
    public void act(World world) {
        move(world);
        eat(world);
    }

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

            this.energy -= 5;
            this.stepsTaken++;
            System.out.println("age " + this.age);
        }
        super.updateStats();
        System.out.println("energy " + this.energy);
    }

    private boolean isWithinTerritory(Location loc) {
        int dx = Math.abs(loc.getX() - initialTerritoryLocation.getX());
        int dy = Math.abs(loc.getY() - initialTerritoryLocation.getY());
        return dx <= TERRITORY_RADIUS && dy <= TERRITORY_RADIUS;
    }

    @Override
    public void eat(World world) {
        if (energy <= 60) {
            System.out.println("Attempting to eat");
            try {
                for (Location loc : world.getSurroundingTiles(initialLocation)) {
                    Object object = world.getTile(loc);
                    if (object instanceof Bush) {
                        Bush bush = (Bush) object;
                        bush.eaten();
                        this.energy += 40;
                        System.out.println("I sucessfully ate berries");
                    }
                    if (object instanceof Mole) {
                        Mole mole = (Mole) object;
                        world.delete(mole);
                        this.energy += 30;
                        System.out.println("I sucessfully a mole");
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
}
