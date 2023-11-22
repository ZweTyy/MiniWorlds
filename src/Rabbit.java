import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Person implements Actor {
    @Override
    public void act(World world) {
        Set<Location> neighbours = world.getEmptySurroundingTiles();
        List<Location> list = new ArrayList<>(neighbours);
        if (!list.isEmpty()) {
            Location l = list.get(0);
            world.move(this, l);
        } else {
            world.step();
        }
    }

}
