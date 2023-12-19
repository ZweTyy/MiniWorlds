package entities;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Mole extends Animal implements Actor {

    private static int amountOfMoles = 0;
    private boolean underground = true;

    public Mole(World world, int size) {
        super(world, size);
        this.MAX_AGE = 6;
        amountOfMoles++;
    }

    public void eat(World world) {
        if (world.getCurrentTime() % 10 == 0 && energy <= 80) {
            world.remove(this);
            this.energy = +20;
        }
    }

    @Override
    public void sleep() {
        if (world.getCurrentTime() % 5 == 0 && world.getCurrentTime() % 4 == 0) {
            return;
        } else {
            super.sleep();
            world.remove(this);
        }
    }

    private boolean isEligibleForReproduction() {
        return this.age >= 1 && this.energy >= 25 && !hasReproducedThisTurn
                && Mole.amountOfMoles < world.getSize();
    }

    public void reproduce(World world, int size) {
        if (!isEligibleForReproduction() || this.currentLocation == null) {
            return;
        }
        Location newLocation = generateRandomLocation(world.getSize());
        if (newLocation == null) {
            return;
        }
        Mole newMole = new Mole(world, size);
        world.setCurrentLocation(newLocation);
        world.setTile(newLocation, newMole);

        consumeReproductionEnergy();

    }

    private void consumeReproductionEnergy() {
        this.energy -= 30;
        this.hasReproducedThisTurn = true;
    }

    @Override
    public void act(World world) {
        hasReproducedThisTurn = false;
        if (hunger <= 75) {
            underground = false;
        }
        if (!underground) {
            super.move(world); // when moles are above earth they are gathering supplies and can be eaten
        }
        eat(world);
        sleep();
        if (!hasReproducedThisTurn) {
            reproduce(world, world.getSize());
        }
    }

}
