package entities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import entities.dens.Burrow;

import java.util.Random;

import java.util.Set;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

/**
 * Represents a rabbit in a simulated ecosystem.
 * This class extends Animal and implements Actor, Herbivore and DynamicDisplayInformationProviderinterfaces.
 */
public class Rabbit extends Animal implements Actor, Herbivore, DynamicDisplayInformationProvider {
    private Burrow myBurrow;
    private boolean hasReproducedThisTurn = false;
    private boolean hidden;
    private boolean isInfected;
    private static int amountOfRabbits = 0;

    /**
     * Constructs a Rabbit with a reference to the world it belongs to and its size.
     *
     * @param world the world in which the rabbit exists.
     * @param size  the size of the world, used for generating random locations.
     */
    public Rabbit(World world, int size) {
        super(world, size);
        this.MAX_AGE = 9;
        amountOfRabbits++;
    }

    enum State {
        DAY_AND_HIDDEN,
        NIGHT_WITH_NO_BURROW,
        NIGHT_WITH_BURROW,
        HIDDEN_AT_NIGHT,
        OTHERWISE
    }

    /**
     * Defines the actions the rabbit takes in each simulation step.
     * The actions are determined based on the rabbit's state, which depends on time
     * of day and whether it has a burrow.
     *
     * @param world the world in which the rabbit acts.
     */
    @Override
    public synchronized void act(World world) {
        State state = determineState(world, hidden, myBurrow);

        switch (state) {
            case HIDDEN_AT_NIGHT:
                break;
            case DAY_AND_HIDDEN:
                leaveBurrow(world);
                break;
            case NIGHT_WITH_NO_BURROW:
                findAndEnterBurrow(world);
                break;
            case NIGHT_WITH_BURROW:
                enterBurrow(world, myBurrow);
                break;
            case OTHERWISE:
                performDailyActivities(world);
                break;
        }
    }

    /**
     * Moves the rabbit in the world. It calls the move method of the superclass
     * Animal.
     * The rabbit runs away from bears and wolves.
     *
     * @param world the world in which the rabbit moves.
     */
    @Override
    public void move(World world) {
        Location predatorLocation = checkForPredators(world);
        if (predatorLocation != null) {
            // Predator detected, attempt to flee
            fleeFromPredator(world, predatorLocation);
        } else {
            // No predators detected, proceed with normal movement
            super.move(world);
        }
    }

    /**
     * Defines the actions the rabbit takes in each simulation step.
     * The rabbit sleeps during the night and may move, eat, and reproduce during
     * the day.
     *
     * @param world the world in which the rabbit acts.
     */
    public void performDailyActivities(World world) {
        hasReproducedThisTurn = false;
        move(world); // Assuming move is defined in Animal
        eatHerb(world);
        if (!hasReproducedThisTurn) {
            reproduce(world, world.getSize());
        }
    }

    /**
     * Handles the rabbit's reproduction process. It finds a mate and produces a new
     * rabbit if conditions are met.
     *
     * @param world the world in which reproduction occurs.
     * @param size  the size of the world, used to restrict the number of rabbits.
     */
    public void reproduce(World world, int size) {
        if (!isEligibleForReproduction() || this.currentLocation == null) {
            return;
        }
        Rabbit mate = findMate(world, currentLocation);
        if (mate == null) {
            return;
        }
        Location newLocation = generateRandomLocation(world.getSize());
        if (newLocation == null) {
            return;
        }
        Rabbit newRabbit = new Rabbit(world, size);
        System.out.println("Rabbit reproducing at: " + newLocation);
        world.setCurrentLocation(newLocation);
        world.setTile(newLocation, newRabbit);

        consumeReproductionEnergy();
        mate.consumeReproductionEnergy();
    }

    /**
     * Enables the rabbit to eat herb (grass) in its current location to gain
     * energy.
     * This action increases the rabbit's energy and hunger levels.
     *
     * @param world the world in which the rabbit eats.
     */
    @Override
    public void eatHerb(World world) {
        if (hunger <= 75) {
            System.out.println("Attempting to eat");
            try {
                if (!(world.containsNonBlocking(this.getLocation()))) {
                    world.step();
                    System.out.println("Nothing to eat");
                    return;
                }
                if ((world.getNonBlocking(this.getLocation()) instanceof Grass)) {
                    Grass grass = (Grass) world.getNonBlocking(this.getLocation());
                    grass.die(world);
                    this.hunger += 50;
                    this.energy += 25;
                    System.out.println("I sucessfully ate");
                }

            } catch (IllegalArgumentException iae) {
                // Vi burde ikke aldrig nå herned da vi håndterer exception tidligere
                System.out.println("No entity");
                return;
            }
        }
    }

    /**
     * Returns the number of rabbits in the world.
     *
     * @param world the world in which the rabbit count is returned.
     * @return the number of rabbits in the world.
     */
    public static int countRabbits(World world) {
        Map<Object, Location> entities = world.getEntities();
        int rabbitCount = 0;
        for (Object obj : entities.keySet()) {
            if (obj instanceof Rabbit) {
                rabbitCount++;
            }
        }
        Rabbit.amountOfRabbits = rabbitCount;
        return rabbitCount;
    }

    /**
     * Resets the rabbit count to 0.
     */
    public static void resetRabbitCount() {
        Rabbit.amountOfRabbits = 0;
    }

    /**
     * Updates the rabbit count in the world.
     *
     * @param world the world in which the rabbit count is updated.
     */
    public static void updateRabbitCount(World world) {
        int count = 0;
        for (Object obj : world.getEntities().keySet()) {
            if (obj instanceof Rabbit) {
                count++;
            }
        }
        amountOfRabbits = count;
    }

    /**
     * Sets the flag indicating whether the rabbit has reproduced in the current
     * turn.
     *
     * @param hasReproduced boolean flag indicating if the rabbit has reproduced
     *                      this turn.
     */
    public void setHasReproducedThisTurn(boolean hasReproduced) {
        this.hasReproducedThisTurn = hasReproduced;
    }

    private void findAndEnterBurrow(World world) {
        System.out.println("Finding burrow");
        Burrow availableBurrow = null;

        // Vi henter alle objekterne i verdenen og itererer igennem dem
        Map<Object, Location> entities = world.getEntities();
        for (Object object : entities.keySet()) {
            if (object instanceof Burrow) {
                Burrow burrow = (Burrow) object;
                if (burrow.getCurrentOccupants() < Burrow.MAX_CAPACITY) {
                    System.out.println("Found available burrow");
                    availableBurrow = burrow;
                    break;
                }
            }
        }
        // Hvis der er et ledigt hul går kaninen ind i det eller hvis der ikke er et hul
        // så graver den et nyt
        if (availableBurrow != null) {
            enterBurrow(world, availableBurrow);
        }
        if (availableBurrow == null) {
            digBurrow(world);
        }
    }

    private void digBurrow(World world) {
        // Vi sikrer os at kaninens lokation ikke er null
        this.currentLocation = world.getCurrentLocation();
        if (this.currentLocation == null) {
            return;
        }

        System.out.println("Digging burrow");
        // Find alle tomme nabo tiles
        Set<Location> neighbours = world.getSurroundingTiles(this.currentLocation);
        List<Location> validLocations = new ArrayList<>(neighbours);
        // Her tilføjer vi valide lokationer til listen
        for (Location loc : neighbours) {
            if (world.isTileEmpty(loc)
                    || world.getTile(loc) instanceof Grass && !(world.getTile(loc) instanceof NonBlocking)) {
                validLocations.add(loc);
            }
        }
        if (validLocations.isEmpty()) {
            return;
        }
        // Hvis der er tomme nabo tiles graver kaninen et hul
        if (!validLocations.isEmpty()) {
            int randomIndex = r.nextInt(validLocations.size());
            Location newLocation = validLocations.get(randomIndex);

            Object objectAtLocation = world.getTile(newLocation);
            if (objectAtLocation instanceof Grass) {
                ((Grass) objectAtLocation).die(world);
            }
            this.currentLocation = newLocation;
            this.myBurrow = new Burrow(world, world.getSize());
            world.setTile(currentLocation, this.myBurrow);
            enterBurrow(world, myBurrow);
            this.hidden = true;
        }
    }

    private void enterBurrow(World world, Burrow burrow) {
        System.out.println("Trying to enter burrow");
        // Vi tjekker om kaninen kan komme ind i hullet
        if (burrow != null && burrow.addRabbit(this) && energy > 0) {
            System.out.println("Rabbit entering burrow at: " + burrow.getLocation());
            world.remove(this); // Fjerne kaninen fra verdenen
            this.hidden = true;
            this.myBurrow = burrow; // Sætte kaninens hul til at være det hul den har fundet
            System.out.println("Rabbit location er: " + currentLocation);
        }
        this.health -= 25;
    }

    private void leaveBurrow(World world) {
        if (this.myBurrow != null && this.hidden) {
            System.out.println("Rabbit leaving burrow at: " + this.myBurrow.getLocation());
            this.myBurrow.removeRabbit(this);
            this.hidden = false;

            // Find an empty neighboring tile to move to
            Set<Location> emptyTiles = world.getEmptySurroundingTiles(this.myBurrow.getLocation());
            if (!emptyTiles.isEmpty()) {
                // Move to the first available empty tile
                Location newLocation = emptyTiles.iterator().next();
                this.currentLocation = newLocation;
                world.setTile(newLocation, this);
                System.out.println("Rabbit placed at: " + newLocation);
            } else {
                // If no empty tiles are available, stay in the current location
                this.currentLocation = this.myBurrow.getLocation();
            }
            world.setCurrentLocation(this.currentLocation);
            System.out.println("Successfully left burrow.");
            System.out.println("Rabbit's location: " + world.getCurrentLocation());
        }
    }

    private Rabbit findMate(World world, Location location) {
        for (Location loc : world.getSurroundingTiles(location)) {
            Object object = world.getTile(loc);
            if (object instanceof Rabbit) {
                Rabbit potentialMate = (Rabbit) object;
                if (potentialMate.isEligibleForReproduction()) {
                    return potentialMate;
                }
            }
        }
        return null;
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
                return loc; // Return the location of the predator
            }
        }
        return null; // No predators found
    }

    private void fleeFromPredator(World world, Location predatorLocation) {
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
            System.out.println("Rabbit fleeing to: " + newLocation);
            world.move(this, newLocation);
            this.energy -= 5; // Fleeing costs additional energy
        } else {
            // No escape route found, rabbit is cornered and cannot move
        }
    }

    private void consumeReproductionEnergy() {
        this.energy -= 25;
        this.hasReproducedThisTurn = true;
    }

    private boolean isEligibleForReproduction() {
        return this.age == 4 && this.energy >= 25 && !hasReproducedThisTurn
                && Rabbit.amountOfRabbits < world.getSize() * world.getSize() / 4;
    }

    /**
     * Sets the rabbit's infection status.
     *
     * @return boolean indicating whether the rabbit is infected.
     */
    public void setInfected(boolean isInfected) {
        this.isInfected = isInfected;
    }

    /**
     * Returns the rabbit's infection status.
     *
     * @return boolean indicating whether the rabbit is infected.
     */
    public boolean isInfected() {
        return isInfected;
    }

    private State determineState(World world, boolean hidden, Burrow myBurrow) {
        if (!hidden && world.isDay()) {
            return State.OTHERWISE;
        }
        if (hidden && world.isNight()) {
            return State.HIDDEN_AT_NIGHT;
        }
        if (!hidden && world.isNight()) {
            return myBurrow == null ? State.NIGHT_WITH_NO_BURROW : State.NIGHT_WITH_BURROW;
        }
        if (world.isDay()) {
            return State.DAY_AND_HIDDEN;
        }
        return world.isNight() && myBurrow == null ? State.NIGHT_WITH_NO_BURROW : State.NIGHT_WITH_BURROW;
    }

    @Override
    public DisplayInformation getInformation() {
        if (this.isInfected) {
            return new DisplayInformation(Color.GREEN, "rabbit-fungi-small", true);
        } else {
            return new DisplayInformation(Color.GREEN, "rabbit-small");
        }
    }
}
