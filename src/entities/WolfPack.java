package entities;

import java.util.List;

import entities.dens.WolfDen;

import java.util.ArrayList;

import itumulator.world.Location;
import itumulator.world.World;

/**
 * Represents a wolf pack in a simulated ecosystem.
 * Wolf packs can contain multiple wolves, and they are to be led by an alpha
 * wolf.
 * This class provides functionalities for managing the wolf pack members and
 * their interactions.
 */
public class WolfPack extends Entity {
    private List<Wolf> wolfPack;
    private WolfDen den;

    /**
     * Constructs a WolfPack within the specified world and assigns it a random
     * initial location.
     * Initializes the list of wolves in the pack.
     * 
     * @param world The world where the wolf pack exists.
     * @param size  The size parameter used to generate the wolf pack's initial
     *              location.
     */
    public WolfPack(World world, int size) {
        super(world, size);
        this.wolfPack = new ArrayList<>();
    }

    /**
     * Retrieves a list of wolves from the pack that are within a certain range of a
     * specified wolf.
     *
     * @param wolf  The wolf whose nearby pack members are to be found.
     * @param range The range within which to search for other pack members.
     * @return A list of wolves that are within the specified range of the given
     *         wolf.
     */
    public List<Wolf> getNearbyMembers(Wolf wolf, int range) {
        List<Wolf> nearbyWolves = new ArrayList<>();
        Location wolfLocation = wolf.getLocation();

        for (Wolf packMember : wolfPack) {
            if (packMember == wolf) {
                continue;
            }
            Location packMemberLocation = packMember.getLocation();
            if (isWithinRange(wolfLocation, packMemberLocation, range)) {
                nearbyWolves.add(packMember);
            }
        }
        return nearbyWolves;
    }

    /**
     * Determines if two locations are within a specified range of each other.
     *
     * @param loc1  The first location.
     * @param loc2  The second location.
     * @param range The range to check within.
     * @return boolean indicating whether the two locations are within the specified
     *         range of each other.
     */
    private boolean isWithinRange(Location loc1, Location loc2, int range) {
        int dx = Math.abs(loc1.getX() - loc2.getX());
        int dy = Math.abs(loc1.getY() - loc2.getY());
        return dx <= range && dy <= range;
    }

    /**
     * Adds a wolf to the pack.
     * If the wolf is the first wolf in the pack, it is set as the alpha.
     *
     * @param wolf The wolf to add to the pack.
     */
    public void addMember(Wolf wolf) {
        wolfPack.add(wolf);
        if (wolfPack.size() == 1) {
            // If this is the first wolf in the pack, make it the alpha
            System.out.println("First wolf in pack, setting as alpha: " + wolf);
            wolf.setIsAlpha(true);
        }
    }

    /**
     * Handles the removal of a wolf from the pack.
     * If the removed wolf is the alpha, assigns a new alpha from the remaining
     * members.
     * 
     * @param wolf The wolf to be removed.
     */
    public void removeMember(Wolf wolf) {
        wolfPack.remove(wolf);
        if (wolf.isAlpha() && !wolfPack.isEmpty()) {
            // Assign the new alpha
            Wolf newAlpha = wolfPack.get(0); // Just an example, could be based on other criteria
            newAlpha.setIsAlpha(true);
            System.out.println("New alpha assigned: " + newAlpha);
        }
    }

    /**
     * Creates a wolf den for the pack.
     * The den is placed on the world map at a random location.
     * 
     * @param world The world where the den is to be placed.
     * @param size  The size parameter used to generate the den's initial location.
     */
    public void createDen(World world, int size) {
        this.den = new WolfDen(world, size, this);
        // Place the den on the world map at a random location
        Location denLocation = generateRandomLocation(size);
        if (world.isTileEmpty(denLocation)) {
            world.setTile(denLocation, this.den);
        }
    }

    /**
     * Checks if the alpha wolf is alive.
     * If the alpha is dead, assigns a new alpha from the remaining members.
     */
    public void checkAndAssignNewAlpha() {
        if (!wolfPack.isEmpty() && !wolfPack.get(0).isAlive()) { // Assuming isAlive() method checks if health > 0
            wolfPack.remove(0); // Remove the dead alpha
            if (!wolfPack.isEmpty()) {
                wolfPack.get(0).setIsAlpha(true); // Assign the new alpha
                System.out.println("New alpha assigned: " + wolfPack.get(0));
            }
        }
    }

    /**
     * Gets the wolf den of the pack.
     *
     * @return The wolf den of the pack.
     */
    public WolfDen getDen() {
        return den;
    }

    /**
     * Gets the alpha wolf of the pack.
     *
     * @return The alpha wolf of the pack, or null if the pack is empty.
     */
    public Wolf getAlpha() {
        return wolfPack.isEmpty() ? null : wolfPack.get(0);
    }

    /**
     * Gets the list of wolves in the pack.
     *
     * @return The list of wolves that are members of the pack.
     */
    public List<Wolf> getPack() {
        return wolfPack;
    }
}
