import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import java.awt.Color;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Random r = new Random(); // opret en ny random generator
        int size = 3; // størrelsen af vores 'map' (dette er altid kvadratisk)
        int delay = 1000; // forsinkelsen mellem hver skridt af simulationen (i ms) int display_size =
                          // 800; // skærm opløsningen (i px)
        int display_size = 800; // skærm opløsningen (i px)
        Program p = new Program(size, display_size, delay); // opret et nyt program
        World world = p.getWorld(); // hiv verdenen ud, som er der hvor vi skal tilføje ting!
        Person person = new Person(); // opret en ny person
        Location place = new Location(r.nextInt(size), r.nextInt(size)); // opret en ny lokation tilfældigt
        // opret ny tilfældig lokation
        world.setTile(place, person); // sæt personen på lokationen

        DisplayInformation di = new DisplayInformation(Color.red);
        p.setDisplayInformation(Person.class, di);

        p.show(); // viser selve simulationen
        boolean isNight = true; // Declare and initialize the isNight variable
        if (isNight) {
            world.delete(person);
        }
        for (int i = 0; i < 200; i++) {
            p.simulate();
        } // kører 10 runder, altså kaldes 'act' 200 gange for alle placerede aktører
    }
}