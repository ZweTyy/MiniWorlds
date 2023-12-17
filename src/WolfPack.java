import java.util.List;
import java.util.ArrayList;

import itumulator.world.Location;
import itumulator.world.World;

public class WolfPack extends Entity {

    // Need an instance variable list with all wolves in pack
    private List<Wolf> wolfPack;

    public WolfPack(World world, int size) {
        super(world, size);
        this.wolfPack = new ArrayList<>();
    }

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

    private boolean isWithinRange(Location loc1, Location loc2, int range) {
        int dx = Math.abs(loc1.getX() - loc2.getX());
        int dy = Math.abs(loc1.getY() - loc2.getY());
        return dx <= range && dy <= range;
    }

    public void addMember(Wolf wolf) {
        wolfPack.add(wolf);
        if (wolfPack.size() == 1) {
            // If this is the first wolf in the pack, make it the alpha
            System.out.println("First wolf in pack, setting as alpha: " + wolf);
            wolf.setIsAlpha(true);
        }
    }

    public Wolf getAlpha() {
        return wolfPack.isEmpty() ? null : wolfPack.get(0);
    }

    public List<Wolf> getPack() {
        return wolfPack;
    }
}
