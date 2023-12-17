import java.util.List;
import java.util.ArrayList;

import itumulator.world.Location;
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

    
}