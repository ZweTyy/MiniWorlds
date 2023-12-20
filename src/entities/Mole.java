package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Mole extends Animal implements Actor, Prey {

    private static int amountOfMoles = 0;
    private boolean underground = false;

    public Mole(World world, int size) {
        super(world, size);
        this.MAX_AGE = 8;
        amountOfMoles++;
    }

    public void checkHunger(World world) {
        if (hunger <= 70) {
            comeAboveGround(world);
        } else if (hunger >= 100) {
            goUnderground(world);
        }
    }

    @Override
    public void sleep() {
        this.hunger -= 5;
        if (world.isNight() && underground) {
            energy += 10;
            health += 5;
        }
    }

    private boolean isEligibleForReproduction() {
        return this.age >= 4 && this.energy >= 25 && !hasReproducedThisTurn
                && Mole.amountOfMoles < world.getSize() * world.getSize() / 4;
    }

    public void reproduce(World world, int size) {
        if (!isEligibleForReproduction() || this.currentLocation == null) {
            return;
        }
        Location newLocation = generateRandomLocation(world.getSize());
        if (newLocation != null) {
            Mole newMole = new Mole(world, size);
            world.setCurrentLocation(newLocation);
            world.setTile(newLocation, newMole);
            consumeReproductionEnergy();
        }
    }

    private void consumeReproductionEnergy() {
        this.energy -= 30;
        this.hasReproducedThisTurn = true;
    }

    @Override
    public void act(World world) {
        hasReproducedThisTurn = false;
        Location predatorLocation = checkForPredators(world);
        if (predatorLocation != null) {
            // Predator detected, attempt to flee
            fleeFromPredator(world, predatorLocation);
        }
        checkHunger(world);
        sleep();
        System.out.println("Mole hunger: " + hunger);
        if (!underground) {
            searchForCarcasses(world); // Search for carcasses when above ground
            super.move(world); // Moles gather supplies above ground and can be eaten
            if (!hasReproducedThisTurn) {
                reproduce(world, world.getSize());
            }
        }
    }

    private Location checkForPredators(World world) {
        this.currentLocation = world.getCurrentLocation();
        if (this.currentLocation == null) {
            return null;
        }
        Set<Location> neighbours = world.getSurroundingTiles(this.currentLocation);
        for (Location loc : neighbours) {
            Object object = world.getTile(loc);
            if (object instanceof Wolf || object instanceof Bear) {
                System.out.println("Predator detected at: " + loc);
                return loc; // Return the location of the predator
            }
        }
        return null; // No predators found
    }

    // Method for the mole to go underground
    private void goUnderground(World world) {
        System.out.println("Mole hunger: " + hunger);
        if (!underground && this.currentLocation != null) {
            System.out.println("Mole going underground at: " + this.currentLocation);
            world.remove(this);
            underground = true;
        }
    }

    // Method for the mole to come above ground
    private void comeAboveGround(World world) {
        if (underground) {
            Set<Location> emptyTiles = world.getEmptySurroundingTiles(this.initialLocation);
            Location newLocation = emptyTiles.isEmpty() ? this.currentLocation : emptyTiles.iterator().next();
            this.currentLocation = newLocation;
            world.setTile(newLocation, this); // This should make the mole visible in the world view.
            System.out.println("Mole coming above ground at: " + newLocation);
            underground = false;
        }
    }

    // Method for the mole to search for carcasses
    private void searchForCarcasses(World world) {
        Set<Location> surroundingTiles = world.getSurroundingTiles(this.currentLocation);
        for (Location loc : surroundingTiles) {
            Object object = world.getTile(loc);
            if (object instanceof Carcass) {
                Carcass carcass = (Carcass) object;
                consumeCarcass(carcass, world);
                break; // Stop searching after finding and consuming a carcass
            }
        }
    }

    // Method for the mole to consume a carcass
    private void consumeCarcass(Carcass carcass, World world) {
        int meatToConsume = Math.min(carcass.getMeatQuantity(), (int) this.hunger);
        carcass.consumeMeat(meatToConsume);
        this.energy += meatToConsume + 15;
    }

    @Override
    public void fleeFromPredator(World world, Location predatorLocation) {
        Set<Location> neighbours = world.getEmptySurroundingTiles(this.currentLocation);
        List<Location> escapeRoutes = new ArrayList<>();

        for (Location loc : neighbours) {
            if (!world.getSurroundingTiles(loc).contains(predatorLocation)) {
                escapeRoutes.add(loc);
            }
        }

        if (!escapeRoutes.isEmpty()) {
            // Randomly select an escape route from the available safe locations
            int randomIndex = new Random().nextInt(escapeRoutes.size());
            Location newLocation = escapeRoutes.get(randomIndex);

            // Use the world's move method to move the rabbit
            System.out.println("Mole fleeing to: " + newLocation);
            world.move(this, newLocation);
            this.energy -= 5; // Fleeing costs additional energy
        } else {
            System.out.println("Mole is cornered and cannot move.");
            // No escape route found, Mole is cornered and cannot move
        }
    }

    // Add getters and setters for mole count as needed.
}
