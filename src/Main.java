import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import java.awt.Color;

public class Main {

    public static void main(String[] args) {
        int size = 3; // størrelsen af vores 'map' (dette er altid kvadratisk)
        int delay = 1000; // forsinkelsen mellem hver skridt af simulationen (i ms) int display_size =
                          // 800; // skærm opløsningen (i px)
        int display_size = 800; // skærm opløsningen (i px)
        Program p = new Program(size, display_size, delay); // opret et nyt program
        World world = p.getWorld(); // hiv verdenen ud, som er der hvor vi skal tilføje ting!

        Person person = new Person(); // opret en ny person
        Location place = new Location(0, 1); // opret en ny lokation
        world.setTile(place, person); // sæt personen på lokationen

        DisplayInformation di = new DisplayInformation(Color.red);
        p.setDisplayInformation(Person.class, di);

        p.show(); // viser selve simulationen
        for (int i = 0; i < 200; i++) {
            p.simulate();
        } // kører 200 runder, altså kaldes 'act' 200 gange for alle placerede aktører
    }
}