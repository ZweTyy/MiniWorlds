import itumulator.simulator.Actor;
import itumulator.world.World;

public class Wolf extends Animal implements Actor, Carnivore {

    public Wolf(World world, int size) {
        super(world, size);
        this.MAX_AGE = 14;
    }

    @Override
    public void act(World world) {
        if (world.isNight()) {
            sleep();
        } else {
            move(world);
        }
    }

    @Override
    public void eatMeat(World world) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eatMeat'");
    }
}
