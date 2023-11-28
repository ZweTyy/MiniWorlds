import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Bear extends Animal implements Actor, Herbivore, Carnivore {
    Location initialTerritoryLocation;

    public Bear(World world, int size, int x, int y) {
        super(world, size);
        this.MAX_AGE = 20;
        this.initialTerritoryLocation = new Location(x, y);
        this.setLocation(initialTerritoryLocation);
    }

    public Bear(World world, int size) {
        super(world, size);
        this.MAX_AGE = 20;
        initialTerritoryLocation = generateRandomLocation(size);
    }

    @Override
    public void act(World world) {
        move(world);
    }

    @Override
    public void eatHerb(World world) {

    }
}
