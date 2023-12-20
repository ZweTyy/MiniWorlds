import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.World;
import utilities.ColorSetup;
import utilities.EntityConfig;
import utilities.EntityLoader;
import utilities.InputParser;
import utilities.SimulationManager;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import entities.Bear;
import entities.Berry;
import entities.Carcass;
import entities.Fungus;
import entities.Grass;
import entities.Rabbit;
import entities.Wolf;
import entities.WolfPack;
import entities.dens.Burrow;
import entities.dens.WolfDen;
import itumulator.world.Location;

public class Main {

    public static void main(String[] args) {
        InputParser parser = new InputParser("./data/input-filer/t4-1a.txt"); // Opret en ny parser
        Map<String, List<EntityConfig>> elementsToAdd = parser.parseInput(); // Kør parseren og få en map med elementer
                                                                             // der skal
        // tilføjes
        int size = parser.getSize(); // Hiv størrelsen på verdenen ud
        int delay = 1000; // Forsinkelsen mellem hver skridt af simulationen (i ms)
        int display_size = 720; // Skærm opløsningen (i px)
        Program p = new Program(size, display_size, delay); // Opret et nyt program
        World world = p.getWorld(); // Hiv verdenen ud, som er der hvor vi skal tilføje ting!

        ColorSetup.setUpColor(p);

        p.show();
        SimulationManager.reset();
        EntityLoader.loadEntities(world, elementsToAdd, size); // Tilføj alle elementer til verdenen

        System.out.println("Initial rabbit count: " + SimulationManager.countRabbits(world));
        System.out.println("Initial mole count: " + SimulationManager.countMoles(world));

        // Kører simulationen 40 gange og opdaterer kanin tælleren hver gang
        for (int i = 0; i < 40; i++) {
            p.simulate();
            SimulationManager.incrementStep();
            SimulationManager.updateRabbitCount(world);
            SimulationManager.updateMoleCount(world);
        }
    }
}