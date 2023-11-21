import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import java.awt.Color;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        int persons = 10; // The number of persons to simulate
        Random r = new Random(); // Create a new random generator
        int size = 5; // The size of the world (in tiles) (size x size)
        int delay = 1000; // The delay between each simulation step (in ms)
        int display_size = 800; // skærm opløsningen (i px)
        Program p = new Program(size, display_size, delay); // Create a new program
        World world = p.getWorld(); // Get the world from the program

        DisplayInformation di = new DisplayInformation(Color.red);
        p.setDisplayInformation(Person.class, di);
        p.show(); // Show the simulation

        Person person = null; // Declare the person variable outside the loop

        // Create the persons
        for (int i = 0; i < persons; i++) {
            person = new Person(); // Create a new person
            int x = r.nextInt(size); // Create a random x coordinate
            int y = r.nextInt(size); // Create a random y coordinate
            Location place = new Location(x, y); // Create a new location
            while (!world.isTileEmpty(place)) { // While the tile is not empty
                x = r.nextInt(size);
                y = r.nextInt(size);
                place = new Location(x, y);
            }
            world.setTile(place, person); // Place the person on the location
        }

        boolean isNight = true; // Declare and initialize the isNight variable
        // Runs 50 loops of the simulation
        for (int i = 0; i < 50; i++) {
            p.simulate();
            if (isNight) {
                world.delete(person);
                world.step();
            }
        }
    }
}