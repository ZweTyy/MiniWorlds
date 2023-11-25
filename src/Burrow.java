import java.util.List;
import java.util.ArrayList;

import itumulator.world.World;
import itumulator.world.Location;
import itumulator.world.NonBlocking;

import java.util.Random;

public class Burrow implements NonBlocking {
    private List<Rabbit> rabbits;
    private Location place;
    protected static final int MAX_CAPACITY = 3;

    Random r = new Random();

    public Burrow(World world, int size) {
        initializeBurrow(world, size);
        this.rabbits = new ArrayList<>();
    }

    public void initializeBurrow(World world, int size) {
        int x = r.nextInt(size); // Lav et tilfældigt x koordinat
        int y = r.nextInt(size); // Lav et tilfældigt y koordinat
        place = new Location(x, y); // Lav ny lokation med de tilfældige koordinater
        while (!world.isTileEmpty(place)) { // While tile ikke er tom
            x = r.nextInt(size);
            y = r.nextInt(size);
            place = new Location(x, y);
        }
    }

    public synchronized boolean addRabbit(Rabbit rabbit) {
        if (rabbits.size() < MAX_CAPACITY) {
            rabbits.add(rabbit);
            return true;
        }
        return false;
    }

    public synchronized void removeRabbit(Rabbit rabbit) {
        rabbits.remove(rabbit);
    }

    public int getCurrentOccupants() {
        return rabbits.size();
    }

    public Location getPlace() {
        return place;
    }
}
