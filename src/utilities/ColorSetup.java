package utilities;

import java.awt.Color;

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
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;

public class ColorSetup {

    public static void setUpColor(Program p) {
        DisplayInformation grassDisplayInfo = new DisplayInformation(Color.green, "grass");
        DisplayInformation rabbitDisplayInfo = new DisplayInformation(Color.white, "rabbit-small");
        DisplayInformation burrowDisplayInfo = new DisplayInformation(Color.black, "hole-small");
        DisplayInformation wolfDisplayInfo = new DisplayInformation(Color.black, "wolf");
        DisplayInformation bearDisplayInfo = new DisplayInformation(Color.black, "bear");
        DisplayInformation berryDisplayInfo = new DisplayInformation(Color.black, "bush-berries");
        DisplayInformation WolfPackDisplayInfo = new DisplayInformation(Color.black, "wolf");
        DisplayInformation CarcassSmallDisplayInfo = new DisplayInformation(Color.black, "carcass-small");
        DisplayInformation CarcassDisplayInfo = new DisplayInformation(Color.black, "carcass");
        DisplayInformation WolfDenDisplayInfo = new DisplayInformation(Color.black, "hole");
        DisplayInformation LocationDisplayInfo = new DisplayInformation(Color.black);
        p.setDisplayInformation(Grass.class, grassDisplayInfo);
        p.setDisplayInformation(Rabbit.class, rabbitDisplayInfo);
        p.setDisplayInformation(Burrow.class, burrowDisplayInfo);
        p.setDisplayInformation(Wolf.class, wolfDisplayInfo);
        p.setDisplayInformation(Bear.class, bearDisplayInfo);
        p.setDisplayInformation(Berry.class, berryDisplayInfo);
        p.setDisplayInformation(WolfPack.class, WolfPackDisplayInfo);
        p.setDisplayInformation(Carcass.class, CarcassSmallDisplayInfo);
        p.setDisplayInformation(Carcass.class, CarcassDisplayInfo);
        p.setDisplayInformation(WolfDen.class, WolfDenDisplayInfo);
        p.setDisplayInformation(Location.class, LocationDisplayInfo);
        p.setDisplayInformation(Fungus.class, new DisplayInformation(Color.GREEN, "fungi", true));

    }

}