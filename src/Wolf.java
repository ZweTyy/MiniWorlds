import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Wolf extends Animal implements Actor, Carnivore {
    private boolean isAlpha;
    private WolfPack myPack;

    public Wolf(World world, int size) {
        super(world, size);
        this.MAX_AGE = 14;
        this.isAlpha = false;
    }

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

    public void seekPackAndHunt(World world) {
        // Check for nearby pack members
        List<Wolf> nearbyWolves = findNearbyPackMembers(world);
        
        // If there are pack members nearby, move towards the average location of those members
        if (!nearbyWolves.isEmpty()) {
            moveTowardsPack(nearbyWolves, world);
        }
        
        // Perform a hunting action if possible
        hunt(world);
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
        Set<Location> neighbours = world.getEmptySurroundingTiles(); // Hent alle tomme nabo tiles
        List<Location> validLocations = new ArrayList<>(neighbours); // Lav en liste med alle tomme nabo tiles
        if (!validLocations.isEmpty()) {
            System.out.println("Moving towards pack member at: " + closestLocation);
            world.move(this, closestLocation);
        }
    }

    private void hunt(World world) {
        // Implement hunting logic, possibly involving other pack members
    }
    public void setPack(WolfPack pack) {
        this.myPack = pack;
    }

    public void joinPack(WolfPack pack) {
        this.myPack = pack;
        pack.addMember(this);
    }

    public void setIsAlpha(boolean isAlpha) {
        this.isAlpha = isAlpha;
    }

    public boolean isAlpha() {
        return isAlpha;
    }
}