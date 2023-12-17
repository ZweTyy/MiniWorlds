import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Wolf extends Animal implements Actor {
    private WolfPack myPack;
    private boolean alpha;

    public Wolf(World world, int size, boolean isAlpha) {
        super(world, size);
        this.MAX_AGE = 14;
        this.alpha = isAlpha;
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


    @Override
    public void eat(World world) {
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
            for (Wolf wolf : myPack.getPack()) {
                if ((world.getSurroundingTiles(wolf.getLocation()) instanceof Bear) && myPack.getPack().size() > 3) {
                    Bear bear = (Bear) world.getSurroundingTiles(wolf.getLocation());
                    world.delete(bear); // if there is a bear delete
                    this.health += 50; // increase health
                    System.out.println("We ate a bear");
                }
            }
        }

    }

    private void goToAlpha(){
        Location alphaLocation = (myPack.getAlpha().getLocation());
        Set<Location> emptyTiles = world.getEmptySurroundingTiles(alphaLocation);
        for(Location emptyTile : emptyTiles){
            this.setLocation(emptyTile);
            break;
        }
    }

    @Override
    protected void move(World world){
        if(alpha){
            super.move(world);
        } else {
            goToAlpha();
        }
    }

}
