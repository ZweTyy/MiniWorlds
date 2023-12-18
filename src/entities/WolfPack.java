package entities;

import java.util.List;
import java.util.ArrayList;

import itumulator.world.Location;
import itumulator.world.World;

/**
 * Represents a wolf pack in a simulated ecosystem.
 * Wolf packs can contain multiple wolves, and they are to be led by an alpha wolf.
 * This class provides functionalities for managing the wolf pack members and their interactions.
 */
public class WolfPack extends Entity {
    private List<Wolf> wolfPack;

    /**
     * Constructs a WolfPack within the specified world and assigns it a random initial location.
     * Initializes the list of wolves in the pack.
     * 
     * @param world The world where the wolf pack exists.
     * @param size The size parameter used to generate the wolf pack's initial location.
     */
    public WolfPack(World world, int size) {
        super(world, size);
        this.wolfPack = new ArrayList<>();
    }

    /**
     * Retrieves a list of wolves from the pack that are within a certain range of a specified wolf.
     *
     * @param wolf The wolf whose nearby pack members are to be found.
     * @param range The range within which to search for other pack members.
     * @return A list of wolves that are within the specified range of the given wolf.
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
     * @param loc1 The first location.
     * @param loc2 The second location.
     * @param range The range to check within.
     * @return boolean indicating whether the two locations are within the specified range of each other.
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
