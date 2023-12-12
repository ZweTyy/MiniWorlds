import java.util.List;
import java.util.ArrayList;

import itumulator.world.World;

public class WolfPack {

    // Need an instance variable list with all wolves in pack
    private List<Wolf> wolfPack;

    public WolfPack(World world, int size) {
        wolfPack = new ArrayList<>();
    }

    public void addMember(Wolf wolf) {
        wolfPack.add(wolf);
    }

    public Wolf getAlpha() {
        return wolfPack.isEmpty() ? null : wolfPack.get(0);
    }

    public List<Wolf> getPack() {
        return wolfPack;
    }

    // Creat right number of wolves
    // Add pack perameter to contructer and factory
    // Add these wolves to instance variable list
    // Creat method in wolfpack named getPack(), needs to return instance variable
    // list
}

// Change how entity creation happens when multiple wolves are placed
// call wolfpack factory instead of wolf factory

// in entityloader change wolf to wolfpack and add details so arguments

// Add way to place consecutive things close to each other
// or make method that makes pack move toward one wolf

// compare locations of first wolfs movement, assign that a direction that the
// other wolves move in
// reset location direction so first wolf moves random next step