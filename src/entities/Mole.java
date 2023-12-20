package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import utilities.SimulationManager;

/**
 * Represents a mole within a simulation environment. The mole has behaviors
 * such as going
 * underground, coming above ground, searching for food, and reproducing.
 */
public class Mole extends Animal implements Actor, Prey {

    private static int amountOfMoles = SimulationManager.getMoleCount();
    private boolean underground = false;

    /**
     * Constructs a Mole instance and initializes it within the specified world.
     * The mole is initially above ground and increases the total mole count.
     *
     * @param world The world where the mole exists.
     * @param size  The size of the world.
     */
    public Mole(World world, int size) {
        super(world, size);
        this.MAX_AGE = 8;
        amountOfMoles++;
    }

    /**
     * Checks the mole's hunger level and decides whether to go underground or come
     * above ground.
     * If hunger is below 70, the mole comes above ground. If hunger is 100 or
     * above, the mole goes underground.
     *
     * @param world The world where the mole exists.
     */
    public void checkHunger(World world) {
        if (hunger <= 70) {
            comeAboveGround(world);
        } else if (hunger >= 100) {
            goUnderground(world);
        }
    }

    /**
     * Mole's behavior during sleep. Decreases hunger, and if it's night and the
     * mole is underground,
     * increases energy and health.
     */
    @Override
    public void sleep() {
        this.hunger -= 5;
        if (world.isNight() && underground) {
            energy += 10;
            health += 5;
        }
    }

    /**
     * Checks if the mole is eligible for reproduction based on its age, energy, and
     * whether it has already reproduced.
     *
     * @return true if the mole is eligible for reproduction, false otherwise.
     */
    private boolean isEligibleForReproduction() {
        return this.age >= 3 && this.energy >= 25 && !hasReproducedThisTurn
                && Mole.amountOfMoles < world.getSize() * world.getSize() / 4;
    }

    /**
     * Handles mole reproduction. If eligible, the mole reproduces by creating a new
     * mole at a random location.
     *
     * @param world The world where the mole exists.
     * @param size  The size of the world.
     */
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

    /**
     * Consumes energy after reproduction and sets the reproduction flag for the
     * current turn.
     */
    private void consumeReproductionEnergy() {
        this.energy -= 30;
        this.hasReproducedThisTurn = true;
    }

    /**
     * Defines the mole's behavior in each simulation step. Includes checking for
     * predators, handling hunger,
     * sleeping, moving, and reproducing.
     *
     * @param world The world in which the mole acts.
     */
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

    /**
     * Checks for predators in the neighboring tiles of the mole's current location.
     *
     * @param world The world where the mole exists.
     * @return The location of a detected predator, or null if no predator is found.
     */
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

    public void goUnderground(World world) {
        System.out.println("Mole hunger: " + hunger);
        if (!underground && this.currentLocation != null) {
            System.out.println("Mole going underground at: " + this.currentLocation);
            world.remove(this);
            underground = true;
        }
    }

    public void comeAboveGround(World world) {
        if (underground) {
            Set<Location> emptyTiles = world.getEmptySurroundingTiles(this.initialLocation);
            Location newLocation = emptyTiles.isEmpty() ? this.currentLocation : emptyTiles.iterator().next();
            this.currentLocation = newLocation;
            world.setTile(newLocation, this); // This should make the mole visible in the world view.
            System.out.println("Mole coming above ground at: " + newLocation);
            underground = false;
        }
    }

    /**
     * Method for the mole to search for carcasses as a source of food.
     *
     * @param world The world where the mole is searching for carcasses.
     */
    private void searchForCarcasses(World world) {
        Set<Location> surroundingTiles = world.getSurroundingTiles(this.currentLocation);
        for (Location loc : surroundingTiles) {
            System.out.println("Searching for carcasses at: " + loc);
            Object object = world.getTile(loc);
            if (object instanceof Carcass) {
                Carcass carcass = (Carcass) object;
                consumeCarcass(carcass, world);
                break; // Stop searching after finding and consuming a carcass
            }
        }
    }

    /**
     * Consumes a carcass found in the world, increasing the mole's energy and
     * hunger.
     *
     * @param carcass The carcass to be consumed.
     * @param world   The world where the carcass is located.
     */
    private void consumeCarcass(Carcass carcass, World world) {
        int meatToConsume = Math.min(carcass.getMeatQuantity(), (int) this.hunger);
        carcass.consumeMeat(meatToConsume);
        this.energy += meatToConsume + 15;
        this.hunger += meatToConsume + 15;
        if (carcass.getMeatQuantity() <= 0) {
            world.delete(carcass);
        }
    }

    /**
     * Mole's behavior when fleeing from a predator. The mole moves to a safe
     * neighboring location if possible.
     *
     * @param world            The world where the mole exists.
     * @param predatorLocation The location of the detected predator.
     */
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

    /**
     * Sets the mole's underground status.
     *
     * @param underground The new underground status.
     * @return The updated underground status.
     */
    public boolean setUnderground(boolean underground) {
        return this.underground = underground;
    }
}
