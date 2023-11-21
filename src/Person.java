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
        Location l = list.get(0); // Linje 2 og 3 kan erstattes af neighbours.toArray()[0]
        world.move(this, l);
    }

}
