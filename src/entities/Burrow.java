package entities;

import java.util.List;
import java.util.ArrayList;

import itumulator.world.World;
import itumulator.world.Location;
import itumulator.world.NonBlocking;

import java.util.Random;

import Entity;
import Rabbit;

public class Burrow extends Entity implements NonBlocking {
    private List<Rabbit> rabbits;
    protected static final int MAX_CAPACITY = 3;

    Random r = new Random();

    public Burrow(World world, int size) {
        super(world, size);
        this.rabbits = new ArrayList<>();
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
}
