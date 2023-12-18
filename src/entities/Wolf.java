package entities;

import java.util.List;
import java.util.Set;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

/**
 * Represents a wolf in a simulated ecosystem.
 * This class extends Animal and implements Actor and Carnivore interfaces.
 */
public class Wolf extends Animal implements Actor, Carnivore {
    private boolean isAlpha;
    private WolfPack myPack;

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
        if (world.isNight()) {
            sleep();
        } else {
            if (isAlpha()) {
                move(world);
            }
            seekPackAndHunt(world);
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

    private void hunt(World world) {
        // Implement hunting logic, possibly involving other pack members
    }

    @Override
    public void eatMeat(World world) {
        // Wolves are carnivores and do not eat herbs
    }
}