import java.util.ArrayList;
import java.util.Arrays;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Wolf extends Animal implements Actor {
    private WolfPack myPack;

    public Wolf(World world, int size) {
        super(world, size);
        this.MAX_AGE = 14;
    }

    public void setPack(WolfPack pack) {
        this.myPack = pack;
    }

    public void joinPack(WolfPack pack) {
        this.myPack = pack;
        pack.addMember(this);
    }

    @Override
    public void act(World world) {
        if (world.isNight()) {
            sleep();
        } else {
            performDailyActivities(world);
        }
    }

    private void performDailyActivities(World world) {
        hasReproducedThisTurn = false;
        move(world); // Assuming move is defined in Animal
        eat(world);
        if (!hasReproducedThisTurn) {
            reproduce(world, world.getSize());
        }
    }

    // @Override
    // public void reproduce(World world, int size) {
    // // Lav om på reproduce metode
    // if (world.isDay()) {
    // Location newLocation = generateRandomLocation(world.getSize());
    // if (newLocation == null) {
    // return;
    // }
    // Rabbit newRabbit = new Rabbit(world, size);
    // world.setCurrentLocation(newLocation);
    // world.setTile(newLocation, newRabbit);
    // }
    // }

    @Override
    public void eat(World world) {
        ArrayList<Wolf> wolfPack = new ArrayList<Wolf>(Arrays.asList(new Wolf(world, 1)));
        // Eat rabbit
        // check surroundings for rabbit
        if (health <= 50) {
            System.out.println("Attempting to eat");
            if ((world.getSurroundingTiles(this.getLocation()) instanceof Rabbit)) {
                Rabbit rabbit = (Rabbit) world.getSurroundingTiles(this.getLocation());
                world.delete(rabbit); // eat babbit if instance of rabbit
                this.health += 50;// increase health
                System.out.println("I sucessfully ate a rabbit");
            }
            // Eat bear if pack over 3
            // check the surroundings of all of the packmembers locations for bears
            for (Wolf mate : wolfPack) {
                if ((world.getSurroundingTiles(mate.getLocation()) instanceof Bear)) {
                    Bear bear = (Bear) world.getSurroundingTiles(mate.getLocation());
                    world.delete(bear); // if there is a bear delete
                    this.health += 50; // increase health
                    System.out.println("We ate a bear");
                }
            }
        }

    }

}
