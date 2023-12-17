import java.util.List;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Wolf extends Animal implements Actor, Carnivore {
    private WolfPack myPack;

    public Wolf(World world, int size) {
        super(world, size);
        this.MAX_AGE = 14;
    }

    @Override
    public void act(World world) {
        if (world.isNight()) {
            sleep();
        } else {
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
        world.move(this, closestLocation);
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
}