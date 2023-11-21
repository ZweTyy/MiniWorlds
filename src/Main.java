import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import java.awt.Color;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        int objects = 10; // Nummer af objekter der skal genereres
        Random r = new Random(); // Laver en ny random generator
        int size = 0; // Størrelsen af verdenen (i tiles) (dette er kvadratisk)
        List<String> additionalLines = new ArrayList<>(); // Ny arraylist til at gemme de ekstra linjer i filen
        try {
            BufferedReader br = new BufferedReader(new FileReader("./data/input-filer/t1-1a.txt")); // Læser input filen
            String line = br.readLine();
            if (line != null) { // På første linje sætter vi size til at være det første tal i filen
                size = Integer.parseInt(line.split(" ")[0]);
            }
            while ((line = br.readLine()) != null) { // Læser derefter de andre linjer i filen og gemmer dem i en
                                                     // arraylist
                additionalLines.add(line);
            }
            br.close();
        } catch (Exception e) {
            System.out.println("File not found");
        }

        int delay = 1000; // Forsinkelsen mellem hver skridt af simulationen (i ms)
        int display_size = 800; // Skærm opløsningen (i px)
        Program p = new Program(size, display_size, delay); // Opret et nyt program
        World world = p.getWorld(); // Hiv verdenen ud, som er der hvor vi skal tilføje ting!

        DisplayInformation di = new DisplayInformation(Color.red);
        p.setDisplayInformation(Person.class, di);
        p.show(); // Viser selve simulationen

        Person person = null; // Declare the person variable outside the loop
        Grass grass = null;

        // Create the objects
        for (int i = 0; i < objects; i++) {
            person = new Person(); // Lav en ny person
            int x = r.nextInt(size); // Lav et tilfældigt x koordinat
            int y = r.nextInt(size); // Lav et tilfældigt y koordinat
            Location place = new Location(x, y); // Lav ny lokation med de tilfældige koordinater
            while (!world.isTileEmpty(place)) { // While tile ikke er tom
                x = r.nextInt(size);
                y = r.nextInt(size);
                place = new Location(x, y);
            }
            world.setTile(place, person); // Placerer personen på den tilfældige lokation
        }

        boolean isNight = true;
        if (isNight) {
            world.delete(person);
        }
        // Runs 50 loops of the simulation
        for (int i = 0; i < 50; i++) {
            p.simulate();
        }
    }
}