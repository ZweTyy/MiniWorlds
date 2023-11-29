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
        eatHerb(world);
    }

    @Override
    public void eatHerb(World world) {
        if (hunger <= 75) {
            System.out.println("Attempting to eat berries");
            try {
                for (Location loc : world.getSurroundingTiles(initialLocation)) {
                    Object object = world.getTile(loc);
                    if (object instanceof Berry) {
                        Berry berry = (Berry) object;
                        berry.eaten();
                        this.hunger += 50;
                        this.energy += 25;
                        System.out.println("I sucessfully ate");
                    }
                    world.step();
                    System.out.println("Nothing to eat");
                    return;
                }

            } catch (IllegalArgumentException iae) {
                // Vi burde ikke aldrig nå herned da vi håndterer exception tidligere
                System.out.println("No entity");
                return;
            }
        }
    }
}
