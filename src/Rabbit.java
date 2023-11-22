import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import java.util.Random;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Rabbit implements Actor {
    private Location place;
    private Random r = new Random(); // Laver en ny random generator

    public Rabbit(World world, int size) {
        initializeRabbit(world, size);
    }

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

    public void initializeRabbit(World world, int size) {
        int x = r.nextInt(size); // Lav et tilfældigt x koordinat
        int y = r.nextInt(size); // Lav et tilfældigt y koordinat
        place = new Location(x, y); // Lav ny lokation med de tilfældige koordinater
        while (!world.isTileEmpty(place)) { // While tile ikke er tom
            x = r.nextInt(size);
            y = r.nextInt(size);
            place = new Location(x, y);
        }
    }

    public Location getPlace() {
        return place;
    }
}
