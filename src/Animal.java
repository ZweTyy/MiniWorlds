import itumulator.world.World;

public abstract class Animal extends Entity {
    protected boolean alive = true;
    protected int age = 1;
    protected double hunger = 100.0;
    protected double energy = 50.0;
    protected int health = 100;

    public Animal(World world, int size) {
        super(world, size);
    }

    // Animal-specific methods like move, eat, etc.
}
