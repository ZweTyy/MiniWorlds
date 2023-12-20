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

    enum State {
        IN_DEN_AT_NIGHT,
        NOT_IN_DEN_AT_NIGHT,
        IS_DAY_AND_CARCASS_NEARBY,
        HUNTING,
        ALPHA_MOVING,
        FOLLOWING_ALPHA,
        FLEEING_PREDATOR,
        FIGHTING_ENEMY_WOLF,
        IS_DAY_AND_STILL_IN_DEN,
        OTHER
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
            return;
        }
        State state = determineState(world);

        switch (state) {
            case IS_DAY_AND_STILL_IN_DEN:
                isDayAndStillInDen(world);
                break;
            case IN_DEN_AT_NIGHT:
                inDenAtNight(world);
                break;
            case NOT_IN_DEN_AT_NIGHT:
                notInDenAtNight(world);
                break;
            case IS_DAY_AND_CARCASS_NEARBY:
                isDayAndCarcassNearby(world);
                break;
            case HUNTING:
                hunting(world);
                break;
            case ALPHA_MOVING:
                alphaMoving(world);
                break;
            case FOLLOWING_ALPHA:
                followingAlpha(world);
                break;
            case FLEEING_PREDATOR:
                fleeingPredator(world);
                break;
            case FIGHTING_ENEMY_WOLF:
                fightingEnemyWolf(world);
                break;
            case OTHER:
                other(world);
                break;
        }
    }

    /**
     * Initiates the reproduction process for the wolf, creating a new wolf and
     * adding it to the pack.
     * Adjusts energy levels of the reproducing wolves and adds the new wolf to the
     * world.
     * 
     * @param world The world in which the reproduction occurs.
     */
    private void reproduce(World world) {
        Wolf newWolf = new Wolf(world, world.getSize()); // Assuming world.getSize() provides a size value appropriate
                                                         // for generating a random location.
        newWolf.setPack(this.myPack); // Set the pack for the new wolf
        this.myPack.addMember(newWolf); // Add the new wolf to the pack

        // Optionally, adjust the energy levels of the reproducing wolves
        this.energy -= 10; // Reproduction energy cost
        System.out.println("New wolf added to the pack at location: " + newWolf.getLocation());
        world.setCurrentLocation(newWolf.getLocation());
        world.setTile(newWolf.getLocation(), newWolf);
    }

    /**
     * Hunts for nearby prey within the wolf's hunting range. This involves chasing
     * and attacking the prey.
     * 
     * @param world The world in which the hunting takes place.
     */
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

    /**
     * Finds the nearest bear within the wolf's hunting range.
     * 
     * @param world The world in which the wolf is searching for bears.
     * @return The nearest bear, or null if no bear is found.
     */
    private Bear findNearbyBear(World world) {
        Set<Location> surroundingTiles = world.getSurroundingTiles(this.currentLocation, huntingRange); // Using the
                                                                                                        // wolf's
                                                                                                        // hunting range
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

    /**
     * Flees from a predator to the farthest location within reach.
     * 
     * @param world            The world where the fleeing takes place.
     * @param predatorLocation The location of the predator from which the wolf is
     *                         fleeing.
     */
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

    /**
     * Attacks a bear within range, reducing its health.
     * 
     * @param bear  The bear to attack.
     * @param world The world where the attack occurs.
     */
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

    /**
     * Chases the nearest prey, moving towards it.
     * 
     * @param prey  The prey to chase.
     * @param world The world where the chase occurs.
     */
    private void chasePrey(Animal prey, World world) {
        Location rabbitLocation = prey.getLocation();
        Location bestAdjacentLocation = null;
        double minDistance = Double.MAX_VALUE;

        // Find the closest adjacent empty tile to the rabbit
        Set<Location> adjacentTiles = world.getSurroundingTiles(rabbitLocation, 1);
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

    /**
     * Checks if the wolf is close enough to attack the prey.
     * 
     * @param prey The prey to check.
     * @return boolean indicating whether the wolf is close enough to attack the
     *         prey.
     */
    public boolean isCloseEnoughToAttack(Animal prey) {
        // Check if the wolf is close enough to attack the rabbit
        return distanceTo(prey.getLocation()) <= attackRange;
    }

    /**
     * Attacks the prey, reducing its health.
     * 
     * @param prey The prey to attack.
     */
    public void attackPrey(Animal prey, World world) {
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
        System.out.println("Prey attacked. Health now: " + prey.getHealth());

        // Check if the rabbit is caught (assuming rabbit is dead when health <= 0)
        if (prey.getHealth() <= 0) {
            System.out.println("Prey caught.");
            eatPrey(prey, world);
        } else {
            System.out.println("Prey injured but escaped.");
        }
    }

    private boolean isAdjacentToPrey(Animal prey) {
        Location preyLocation = prey.getLocation();
        return distanceTo(preyLocation) <= 1; // Adjacent if the distance is 1 or less
    }

    private void eatPrey(Animal prey, World world) {
        // Increase energy after eating the prey
        this.energy += prey.getEnergy();
    }

    /**
     * Finds the nearest prey (rabbit or mole) within the wolf's hunting range.
     * 
     * @param world The world in which the wolf is searching for prey.
     * @return The nearest prey, or null if no prey is found.
     */
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

    /**
     * Finds the nearest enemy wolf within a certain range.
     * 
     * @param world The world in which the wolf is searching for enemy wolves.
     * @return The nearest enemy wolf, or null if no enemy wolf is found.
     */
    private Wolf findEnemyWolf(World world) {
        Set<Location> surroundingTiles = world.getSurroundingTiles(currentLocation, 3);
        for (Location loc : surroundingTiles) {
            Object object = world.getTile(loc);
            if (object instanceof Wolf) {
                Wolf otherWolf = (Wolf) object;
                if (!this.myPack.equals(otherWolf.myPack)) {
                    return otherWolf;
                }
            }
        }
        return null; // No enemy wolves found
    }

    /**
     * Engages in a fight with an enemy wolf, where both wolves may lose health.
     * 
     * @param enemyWolf The enemy wolf to fight.
     * @param world     The world where the fight occurs.
     */
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

    /**
     * Determines and executes the actions necessary for the wolf to return to its
     * den, if needed.
     * 
     * @param world The world in which the wolf considers returning to the den.
     */
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

    /**
     * Leaves the den if the wolf is currently in one.
     * 
     * @param world The world where the den is located.
     */
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

    /**
     * Returns if the wolf is currently in a den.
     * 
     * @return boolean indicating if the wolf is in a den.
     */
    public boolean isInDen() {
        return isInDen;
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

    /**
     * Sets the wolf's health and checks if it needs to be removed from the world
     * due to death.
     * 
     * @param health The new health value for the wolf.
     */
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

    private State determineState(World world) {
        if (world.isDay() && isInDen()) {
            return State.IS_DAY_AND_STILL_IN_DEN;
        }
        if (world.isNight() && isInDen()) {
            return State.IN_DEN_AT_NIGHT;
        }
        if (world.isNight() && !isInDen()) {
            return State.NOT_IN_DEN_AT_NIGHT;
        }
        if (world.isDay() && findNearbyCarcass(world) != null) {
            return State.IS_DAY_AND_CARCASS_NEARBY;
        }
        if (findNearbyPrey(world) != null) {
            return State.HUNTING;
        }
        if (isAlpha()) {
            return State.ALPHA_MOVING;
        }
        if (!isAlpha() && isCloseToAlpha(myPack.getAlpha().currentLocation)) {
            return State.FOLLOWING_ALPHA;
        }
        if (findNearbyBear(world) != null) {
            return State.FLEEING_PREDATOR;
        }
        if (findEnemyWolf(world) != null) {
            return State.FIGHTING_ENEMY_WOLF;
        }
        return State.OTHER;
    }

    private void isDayAndStillInDen(World world) {
        leaveDen(world);
    }

    private void inDenAtNight(World world) {
        if (isInDen() && myPack.getPack().size() == 2) {
            reproduce(world);
        }
        recoverHealthAtDen();
        sleep();
    }

    private void notInDenAtNight(World world) {
        if (!isInDen() && world.isNight() || this.health < 30) {
            returnToDenIfNeeded(world);
        }
    }

    private void isDayAndCarcassNearby(World world) {
        Carcass nearbyCarcass = findNearbyCarcass(world);
        if (nearbyCarcass != null && world.contains(nearbyCarcass)) {
            consumeCarcass(nearbyCarcass, world);
        }
    }

    private void hunting(World world) {
        Animal targetPrey = findNearbyPrey(world);
        if (targetPrey != null && world.contains(targetPrey)) {
            chasePrey(targetPrey, world);
            if (isCloseEnoughToAttack(targetPrey)) {
                attackPrey(targetPrey, world);
            }
        }
        consumeNearbyCarcassIfNeeded(world);
    }

    private void alphaMoving(World world) {
        if (isAlpha()) {
            super.move(world);
        }
    }

    private void followingAlpha(World world) {
        if (!isAlpha()) {
            followAlpha(world);
        }
    }

    private void fleeingPredator(World world) {
        Bear nearbyBear = findNearbyBear(world);
        if (nearbyBear != null) {
            if (shouldAttackBear()) {
                attackBear(nearbyBear, world);
            } else {
                fleeFromPredator(world, nearbyBear.getLocation());
            }
        }
    }

    private void fightingEnemyWolf(World world) {
        Wolf enemyWolf = findEnemyWolf(world);
        if (enemyWolf != null && world.contains(enemyWolf)) {
            fight(enemyWolf, world);
        }
        if (!world.contains(this)) {
            return;
        }
    }

    private void other(World world) {

    }

    private void consumeNearbyCarcassIfNeeded(World world) {
        Carcass nearbyCarcass = findNearbyCarcass(world);
        if (nearbyCarcass != null && world.contains(nearbyCarcass)) {
            consumeCarcass(nearbyCarcass, world);
        }
    }

    /**
     * Provides display information for the wolf. Texture is based on its infection
     * status.
     * 
     * @return The display information for the wolf.
     */
    @Override
    public DisplayInformation getInformation() {
        if (this.isInfected) {
            return new DisplayInformation(Color.GREEN, "wolf-fungi", true);
        } else {
            return new DisplayInformation(Color.GREEN, "wolf");
        }
    }
}