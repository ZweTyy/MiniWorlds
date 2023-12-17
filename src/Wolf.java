import itumulator.simulator.Actor;
import itumulator.world.World;

public class Wolf extends Animal implements Actor, Carnivore {
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
            move(world);
            System.out.println(myPack.getPack().size());
        }
    }
}