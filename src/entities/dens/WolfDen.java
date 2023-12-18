package entities.dens;

import org.junit.jupiter.api.Named;

import entities.Entity;
import entities.Wolf;
import entities.WolfPack;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import java.util.ArrayList;
import java.util.List;

public class WolfDen extends Entity implements NonBlocking {
    private List<Wolf> restingWolves = new ArrayList<>();
    private WolfPack associatedPack;

    public WolfDen(World world, int size, WolfPack pack) {
        super(world, size);
        this.associatedPack = pack;
    }

    public void addWolf(Wolf wolf) {
        restingWolves.add(wolf);
    }

    public void removeWolf(Wolf wolf) {
        restingWolves.remove(wolf);
    }

    public WolfPack getAssociatedPack() {
        return associatedPack;
    }
}
