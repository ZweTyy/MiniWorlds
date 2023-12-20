package entities;

import java.awt.Color;
import java.util.List;
import java.util.Set;

import entities.dens.WolfDen;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

/**
 * Represents a wolf in a simulated ecosystem.
 * This class extends Animal and implements Actor and Carnivore interfaces.
 */
public class Wolf extends Animal implements Actor, Carnivore, Prey, DynamicDisplayInformationProvider {
    private boolean isAlpha;
    private WolfPack myPack;
    private int huntingRange = 3;
    private int attackRange = 1;
    private int attackPower = 20;
    private boolean isInDen = false;
    private boolean isInfected;
    private boolean isAlreadyEngagedInFight = false;

    /**
     * Constructs a Wolf with a reference to the world it belongs to and its size.
     *
     * @param world the world in which the wolf exists.
     * @param size  the size of the world, used for generating random locations.
     */

    public Wolf(World world, int size) {
        super(world, size);
        this.MAX_AGE = 14;
        this.isAlpha = false;
    }

    /**
     * Defines the actions the wolf takes in each simulation step.
     * The wolf sleeps during the night and may move and hunt during the day.
     *
     * @param world the world in which the wolf acts.
     */
    @Override
    public void act(World world) {
        if (!world.contains(this)) {
            return; // If the wolf has been removed from the world, do not perform any actions
        }

        if (world.isNight()) {
            if (isInDen() && myPack.getPack().size() == 2) {
                reproduce(world);
            }
            recoverHealthAtDen();
            sleep(); // Assume a sleep method is implemented
        } else {
            leaveDen(world); // Wolves leave the den at the start of the day

            // Alpha wolves move first
            if (isAlpha()) {
                move(world);
            }

            // Non-alpha wolves follow the alpha after it has moved
            followAlpha(world);

            Bear nearbyBear = findNearbyBear(world);
            if (nearbyBear != null) {
                if (shouldAttackBear()) {
                attackBear(nearbyBear, world);
                } else {
                fleeFromPredator(world, nearbyBear.getLocation());
                }
            }

            Wolf enemyWolf = findEnemyWolf(world);
            if (enemyWolf != null && world.contains(enemyWolf)) {
                fight(enemyWolf, world);
                // After a fight, check again if the wolf is still in the world
                if (!world.contains(this)) {
                    return;
                }
            } else {
                Animal targetPrey = findNearbyPrey(world);
                if (targetPrey != null && world.contains(targetPrey)) {
                    chasePrey(targetPrey, world);
                    if (isCloseEnoughToAttack(targetPrey)) {
                        attackPrey(targetPrey, world);
                        // After attacking the rabbit, it might be dead and a carcass created
                    }
                }

                Carcass nearbyCarcass = findNearbyCarcass(world);
                if (nearbyCarcass != null && world.contains(nearbyCarcass)) {
                    consumeCarcass(nearbyCarcass, world);
                }
            }
        }

        // Additional behaviors like returning to the den
        if (world.contains(this)) {
            returnToDenIfNeeded(world);
        }
    }

    private void reproduce(World world) {
        Wolf newWolf = new Wolf(world, world.getSize()); // Assuming world.getSize() provides a size value appropriate for generating a random location.
        newWolf.setPack(this.myPack); // Set the pack for the new wolf
        this.myPack.addMember(newWolf); // Add the new wolf to the pack

        // Optionally, adjust the energy levels of the reproducing wolves
        this.energy -= 10; // Reproduction energy cost
        System.out.println("New wolf added to the pack at location: " + newWolf.getLocation());
        world.setCurrentLocation(newWolf.getLocation());
        world.setTile(newWolf.getLocation(), newWolf);
    }

    @Override
    public void hunt(World world) {
        Animal targetPrey = findNearbyPrey(world);
        if (targetPrey != null) {
            chasePrey(targetPrey, world);
            if (isCloseEnoughToAttack(targetPrey)) {
                attackPrey(targetPrey, world);
            }
        }
    }

    private Bear findNearbyBear(World world) {
        Set<Location> surroundingTiles = world.getSurroundingTiles(this.currentLocation, huntingRange); // Using the wolf's hunting range
        for (Location loc : surroundingTiles) {
            Object object = world.getTile(loc);
            if (object instanceof Bear) {
                return (Bear) object;
            }
        }
        return null;
    }
    
    private boolean shouldAttackBear() {
        return myPack != null && (myPack.getPack().size() >= 3 || isAlreadyEngagedInFight);
    }

    @Override
    public void fleeFromPredator(World world, Location predatorLocation) {
        Set<Location> neighbours = world.getEmptySurroundingTiles(this.currentLocation);
        Location farthestLocation = null;
        double maxDistance = -1;
    
        for (Location loc : neighbours) {
            double distance = distanceTo(loc, predatorLocation);
            if (distance > maxDistance) {
                farthestLocation = loc;
                maxDistance = distance;
            }
        }
    
        if (farthestLocation != null) {
            System.out.println("Wolf fleeing to: " + farthestLocation);
            world.move(this, farthestLocation);
            this.currentLocation = farthestLocation;
        } else {
            System.out.println("Wolf is cornered and cannot move.");
        }
    }

    private void attackBear(Bear bear, World world) {
        double chance = Math.random();
        isAlreadyEngagedInFight = true;
        // Attack logic - reduce bear's health
        if (chance <= 0.5) {
            System.out.println("Attack missed.");
            return; // Attack missed, rabbit escapes
        }
        bear.setHealth(bear.getHealth() - attackPower); // Assuming the bear has setHealth and getHealth methods
        System.out.println("Wolves attacking bear. Bear's health now: " + bear.getHealth());
    }
    
    

    private void chasePrey(Animal prey, World world) {
        Location rabbitLocation = prey.getLocation();
        Location bestAdjacentLocation = null;
        double minDistance = Double.MAX_VALUE;

        // Find the closest adjacent empty tile to the rabbit
        Set<Location> adjacentTiles = world.getSurroundingTiles(rabbitLocation, 1); // Assuming a method to get
                                                                                    // surrounding tiles
        for (Location loc : adjacentTiles) {
            if (world.isTileEmpty(loc)) {
                double distance = distanceTo(loc);
                if (distance < minDistance) {
                    bestAdjacentLocation = loc;
                    minDistance = distance;
                }
            }
        }

        // Move to the closest adjacent empty tile
        if (bestAdjacentLocation != null) {
            System.out.println("Chasing prey, moving to location: " + bestAdjacentLocation);
            this.currentLocation = bestAdjacentLocation;
            world.move(this, bestAdjacentLocation);
        } else {
            System.out.println("No adjacent empty tile to move closer to the rabbit.");
        }
    }

    private boolean isCloseEnoughToAttack(Animal prey) {
        // Check if the wolf is close enough to attack the rabbit
        return distanceTo(prey.getLocation()) <= attackRange; // Define 'attackRange'
    }

    private void attackPrey(Animal prey, World world) {
        // Generate a random number between 0 and 1
        double chance = Math.random();
        if (!isAdjacentToPrey(prey)) {
            System.out.println("Too far to attack the rabbit.");
            return;
        }

        // 50% chance to successfully attack the rabbit
        if (chance <= 0.5) {
            System.out.println("Attack missed.");
            return; // Attack missed, rabbit escapes
        }

        // If the attack is successful
        prey.setHealth(prey.health -= attackPower); // Decrease the rabbit's health by the attack power
        System.out.println("Rabbit attacked. Health now: " + prey.getHealth());

        // Check if the rabbit is caught (assuming rabbit is dead when health <= 0)
        if (prey.getHealth() <= 0) {
            System.out.println("Rabbit caught.");
            eatPrey(prey, world);
        } else {
            System.out.println("Rabbit injured but escaped.");
        }
    }

    private boolean isAdjacentToPrey(Animal prey) {
        Location preyLocation = prey.getLocation();
        return distanceTo(preyLocation) <= 1; // Adjacent if the distance is 1 or less
    }

    private void eatPrey(Animal prey, World world) {
        // Increase energy after eating the rabbit
        this.energy += prey.getEnergy();
    }

    private Animal findNearbyPrey(World world) {
        Set<Location> surroundingTiles = world.getSurroundingTiles(currentLocation, huntingRange); // Define
                                                                                                   // 'huntingRange'
        Animal closestPrey = null;
        double minDistance = Double.MAX_VALUE;

        for (Location loc : surroundingTiles) {
            Object object = world.getTile(loc);
            if (object instanceof Rabbit || object instanceof Mole) {
                Animal animal = (Animal) object;
                double distance = distanceTo(animal.getLocation());
                if (distance < minDistance) {
                    closestPrey = animal;
                    minDistance = distance;
                }
            }
        }
        return closestPrey;
    }

    private Wolf findEnemyWolf(World world) {
        Set<Location> surroundingTiles = world.getSurroundingTiles(currentLocation, 3); // Check within a 3-tile radius
        for (Location loc : surroundingTiles) {
            Object object = world.getTile(loc);
            if (object instanceof Wolf) {
                Wolf otherWolf = (Wolf) object;
                if (!this.myPack.equals(otherWolf.myPack)) {
                    return otherWolf; // Found a wolf from another pack
                }
            }
        }
        return null; // No enemy wolves found
    }

    private void fight(Wolf enemyWolf, World world) {
        System.out.println("Fighting with enemy wolf at " + enemyWolf.currentLocation);

        // Fight logic where the loser loses health
        int damage = 20; // Example damage value, can be adjusted
        if (Math.random() > 0.5) {
            // This wolf wins, enemy wolf loses health
            enemyWolf.setHealth(enemyWolf.getHealth() - damage);
            System.out.println("Enemy wolf injured. Health now: " + enemyWolf.getHealth());
        } else {
            // Enemy wolf wins, this wolf loses health
            this.setHealth(this.getHealth() - damage);
            System.out.println("Injured by enemy wolf. Health now: " + this.getHealth());
        }

        // Check if any wolf's health drops below 0 and remove if necessary
        if (this.getHealth() <= 0 && world.contains(this)) {
            System.out.println("Defeated and died in battle.");
            world.delete(this);
            return;
        }
        if (enemyWolf.getHealth() <= 0 && world.contains(enemyWolf)) {
            System.out.println("Enemy wolf defeated and died.");
            world.delete(enemyWolf);
        }
    }

    private void returnToDenIfNeeded(World world) {
        if ((world.isNight() || this.health < 30) && !isInDen()) {
            System.out.println(this + " is considering returning to den.");
            WolfDen den = myPack.getDen();
            if (den != null) {
                Location denLocation = den.getLocation();
                if (denLocation != null && world.contains(den)) {
                    System.out.println(this + " is returning to den at " + denLocation);
                    den.addWolf(this);
                    world.remove(this); // Remove wolf from the world
                    this.isInDen = true;
                }
            }
        }
    }

    private void leaveDen(World world) {
        // Check if the wolf is currently in a den
        if (myPack.getDen() != null && isInDen) {
            System.out.println(this + " leaving den at: " + myPack.getDen().getLocation());
            myPack.getDen().removeWolf(this);
            isInDen = false;

            // Find an empty neighboring tile to move to
            Set<Location> emptyTiles = world.getEmptySurroundingTiles(myPack.getDen().getLocation());
            if (!emptyTiles.isEmpty()) {
                // Move to the first available empty tile
                Location newLocation = emptyTiles.iterator().next();
                this.currentLocation = newLocation;
                world.setTile(newLocation, this);
                System.out.println(this + " placed at: " + newLocation);
            } else {
                // If no empty tiles are available, stay at the current location (den location)
                this.currentLocation = myPack.getDen().getLocation();
            }
            // Update the wolf's location in the world
            world.setCurrentLocation(this.currentLocation);
            System.out.println(this + " has successfully left the den.");
        }
    }

    private void recoverHealthAtDen() {
        this.health += 5;
    }

    private Carcass findNearbyCarcass(World world) {
        Set<Location> surroundingTiles = world.getSurroundingTiles(currentLocation, huntingRange);
        for (Location loc : surroundingTiles) {
            Object object = world.getTile(loc);
            if (object instanceof Carcass) {
                return (Carcass) object;
            }
        }
        return null;
    }

    private void consumeCarcass(Carcass carcass, World world) {
        if (carcass != null && carcass.getMeatQuantity() > 0) {
            int meatToConsume = Math.min(carcass.getMeatQuantity(), (int) this.hunger);
            carcass.consumeMeat(meatToConsume);
            this.energy += meatToConsume; // Adjust the energy gain logic as per your simulation's rules
            System.out.println("Consumed " + meatToConsume + " meat from carcass. Energy now: " + this.energy);

            if (carcass.getMeatQuantity() <= 0) {
                System.out.println("Carcass fully consumed and decayed.");
            }
        }
    }

    private void followAlpha(World world) {
        if (isAlpha || myPack == null) {
            return; // Alpha doesn't need to follow anyone, and lone wolves don't follow.
        }

        Wolf alpha = myPack.getAlpha();
        if (alpha != null) {
            if (!isCloseToAlpha(alpha.currentLocation)) {
                // Move towards the alpha if not close enough
                System.out.println(this.getClass().getSimpleName() + " following alpha at: " + alpha.currentLocation);
                moveTowards(alpha.currentLocation, world);
            } else {
                // If already close to the alpha, move randomly
                super.move(world);
            }
        }
    }

    private boolean isCloseToAlpha(Location alphaLocation) {
        return distanceTo(alphaLocation) <= 3; // Replace '3' with the desired range
    }

    // Moves the wolf towards a specified location if it's not already there
    private void moveTowards(Location targetLocation, World world) {
        // Ensure that the wolf does not already occupy the target location
        if (this.currentLocation.equals(targetLocation)) {
            return;
        }

        Set<Location> possibleLocations = world.getSurroundingTiles(currentLocation, 1);
        Location bestLocation = null;
        double bestDistance = Double.MAX_VALUE;

        for (Location nextLocation : possibleLocations) {
            if (world.isTileEmpty(nextLocation)) {
                double distance = distanceTo(nextLocation, targetLocation);
                if (distance < bestDistance) {
                    bestLocation = nextLocation;
                    bestDistance = distance;
                }
            }
        }

        // If a move is possible, update the current location to the best location
        if (bestLocation != null) {
            world.move(this, bestLocation);
            this.currentLocation = bestLocation;
        }
    }

    /**
     * Seeks out pack members and performs hunting actions if possible.
     * This method manages the wolf's behavior in finding and moving towards its
     * pack,
     * and then performing hunting actions.
     *
     * @param world the world in which the wolf seeks and hunts.
     */
    public void seekPackAndHunt(World world) {
        // Check for nearby pack members
        List<Wolf> nearbyWolves = findNearbyPackMembers(world);

        // If there are pack members nearby, move towards the average location of those
        // members
        if (!nearbyWolves.isEmpty()) {
            moveTowardsPack(nearbyWolves, world);
        }

        // Perform a hunting action if possible
        hunt(world);
    }

    /**
     * Sets the pack that the wolf belongs to.
     *
     * @param pack the pack that the wolf belongs to.
     */
    public void setPack(WolfPack pack) {
        this.myPack = pack;
    }

    /**
     * Joins the wolf to a specified pack.
     * The wolf is added to the pack's list of members.
     *
     * @param pack the WolfPack that the wolf joins.
     */
    public void joinPack(WolfPack pack) {
        this.myPack = pack;
        pack.addMember(this);
    }

    /**
     * Sets the alpha status of the wolf.
     * If set to true, the wolf is considered the alpha of its pack.
     *
     * @param isAlpha boolean indicating if the wolf is an alpha.
     */
    public void setIsAlpha(boolean isAlpha) {
        this.isAlpha = isAlpha;
    }

    /**
     * Checks if the wolf is the alpha of its pack.
     *
     * @return boolean indicating whether this wolf is the alpha.
     */
    public boolean isAlpha() {
        return isAlpha;
    }

    /**
     * Sets the infection status of the wolf.
     *
     * @param isInfected boolean indicating if the wolf is infected.
     */
    public void setInfected(boolean isInfected) {
        this.isInfected = isInfected;
    }

    /**
     * Returns the infection status of the wolf.
     *
     * @return boolean indicating whether the wolf is infected.
     */
    public boolean isInfected() {
        return isInfected;
    }

    private List<Wolf> findNearbyPackMembers(World world) {
        int searchRange = 3;
        return myPack.getNearbyMembers(this, searchRange);
    }

    private void moveTowardsPack(List<Wolf> nearbyWolves, World world) {
        if (nearbyWolves.isEmpty()) {
            return;
        }

        Location closestLocation = nearbyWolves.get(0).getLocation();
        // Move towards the closest pack member
        if (world.isTileEmpty(closestLocation)) {
            System.out.println("Moving towards pack member at: " + closestLocation);
            world.move(this, closestLocation);
        }
    }

    public boolean isInDen() {
        return isInDen;
    }

    private double distanceTo(Location otherLocation) {
        int dx = this.currentLocation.getX() - otherLocation.getX();
        int dy = this.currentLocation.getY() - otherLocation.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private double distanceTo(Location loc1, Location loc2) {
        int dx = loc1.getX() - loc2.getX();
        int dy = loc1.getY() - loc2.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public void setHealth(int health) {
        this.health = health;
        if (this.health <= 0) {
            this.alive = false;
            createCarcass();
            if (this.isAlpha) {
                System.out.println("Alpha wolf died.");
                myPack.checkAndAssignNewAlpha();
            }
        }
    }

    @Override
    public DisplayInformation getInformation() {
        if (this.isInfected) {
            return new DisplayInformation(Color.GREEN, "wolf-fungi", true);
        } else {
            return new DisplayInformation(Color.GREEN, "wolf");
        }
    }
}