import itumulator.executable.Program;
import itumulator.simulator.Actor;
import itumulator.world.NonBlocking;
import itumulator.world.World;

public class Bush extends Nature implements Actor {
    private boolean hasBerries = true;
    private int lastEatenDay = -1;

    public Bush(World world, int size) {
        super(world, size);
    }

    @Override
    public void act(World world) {
        int currentDay = world.getCurrentTime() / World.getTotalDayDuration();

        if (!hasBerries && (currentDay > lastEatenDay)) {
            hasBerries = true;
        }
    }

    public void eaten() {
        if (hasBerries) {
            hasBerries = false;
            lastEatenDay = world.getCurrentTime() / World.getTotalDayDuration();

        }
    }

    public boolean hasBerries() {
        return hasBerries;
    }

    public void grow(World world, int size) {
        // Berries skal gro tilbage
    }

    public void decay(World world) {
        // Buske skal dø
    }
}
