import itumulator.simulator.Actor;
import itumulator.world.World;

public class Mole extends Animal implements Actor {

    private static int amountOfMoles = 0;

    public Mole(World world, int size) {
        super(world, size);
        this.MAX_AGE = 9;
        amountOfMoles++;
    }

    

    public void eat(World world) {
         if(getCurrentTime() % 10 == 0 && health <= 80){
            isOnTile(Mole mole);
            this.health =+ 20;
        } else {
            world.remove(this);
        }
    }

    public void breath(World world) {
        if(getCurrentTime() % 5 == 0){
            isOnTile(Mole mole);
        } else {
            world.remove(this);
        }
    }

    public void reproduce(World world, int size) {
        
    }
    
    @Override
    public void act(World world) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'act'");
    }


    
}
