package entities;

import itumulator.world.Location;
import itumulator.world.World;

public interface Prey {
    void fleeFromPredator(World world, Location predatorLocation);
}
