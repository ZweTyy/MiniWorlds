import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.Random;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Rabbit implements Actor {
    private Location place;
    private Burrow myBurrow;
    private boolean alive = true;
    private boolean hasReproducedThisTurn = false;
    private boolean hidden;
    private int age = 1;
    private double hunger = 100.0;
    private double energy = 50.0;
    private int stepsTaken = 0;
    private int health = 100;
    private static int amountOfRabbits = 0;
    private Random r = new Random(); // Laver en ny random generator

    public Rabbit(World world, int size) {
        initializeRabbit(world, size);
        Rabbit.amountOfRabbits++;
    }

    @Override
    public void act(World world) {
        // Hvis det er dag og kaninen er i et hul så forlader den hullet
        if (world.isDay() && hidden) {
            leaveBurrow(world);
        }
        if (!hidden) {
            // Hvis det er nat og kaninen ikke er i et hul så leder den efter et hul
            if (world.isNight() && myBurrow == null) {
                findAndEnterBurrow(world);
            } else {
                hasReproducedThisTurn = false;
                move(world);
                eat(world);
                if (!hasReproducedThisTurn) {
                    reproduce(world, world.getSize());
                }
            }
        }
    }

    public void initializeRabbit(World world, int size) {
        int x = r.nextInt(size); // Lav et tilfældigt x koordinat
        int y = r.nextInt(size); // Lav et tilfældigt y koordinat
        place = new Location(x, y); // Lav ny lokation med de tilfældige koordinater
        while (!world.isTileEmpty(place)) { // While tile ikke er tom
            x = r.nextInt(size);
            y = r.nextInt(size);
            place = new Location(x, y);
        }
    }

    public void move(World world) {
        System.out.println("Rabbit moving from: " + this.place);
        Set<Location> neighbours = world.getEmptySurroundingTiles(); // Hent alle tomme nabo tiles
        List<Location> validLocations = new ArrayList<>(neighbours); // Lav en liste med alle tomme nabo tiles
        if (validLocations.isEmpty() || energy <= 0) { // Hvis der ikke er nogen tomme nabo tiles skipper vi eller hvis
                                                       // kaninen ikke har energi
            world.step();
        }
        if (!validLocations.isEmpty() && energy > 0) { // Hvis der er tomme nabo tiles og kaninen har energi bevæger den
                                                       // sig
            int randomIndex = r.nextInt(validLocations.size());
            Location newLocation = validLocations.get(randomIndex);
            this.place = newLocation;
            world.move(this, newLocation);
            this.energy -= 2.5;
            this.hunger -= 5.0;
            this.stepsTaken++;
            System.out.println("to: " + this.place);
        }
        if (stepsTaken == 10) {
            this.age++;
            this.energy += 50;
            this.stepsTaken = 0;
        }
        if (hunger <= 0) {
            this.health -= 25;
        }
        if (health <= 0 || age >= 9) {
            this.alive = false;
        }
        if (!alive) {
            world.delete(this);
        }
        System.out.println("Energy: " + energy + " " + "Hunger: " + hunger + " " + "HP: " + health + " " + "Age: " + age
                + " " + amountOfRabbits);
        System.out.println("Rabbit's current location: " + this.place);
    }

    public static int countRabbits(World world) {
        Map<Object, Location> entities = world.getEntities();
        int rabbitCount = 0;
        for (Object obj : entities.keySet()) {
            if (obj instanceof Rabbit) {
                rabbitCount++;
            }
        }
        // Sætter den statiske variabel til at være lig med antallet af kaniner
        Rabbit.amountOfRabbits = rabbitCount;
        return rabbitCount;
    }

    public void reproduce(World world, int size) {
        // Vi tjekker om kaninen er 4 år gammel, har nok energi, om der er plads til
        // flere og om den ikke har reproduceret i denne tur
        if (this.age == 4 && this.energy >= 25 && amountOfRabbits < size * size / 4 && !hasReproducedThisTurn) {
            // Find alle tomme nabo tiles
            List<Location> neighbours = new ArrayList<>(world.getEmptySurroundingTiles(place));
            // Løber igennem alle nabo tiles
            for (Location loc : world.getSurroundingTiles(place)) {
                // Laver et objekt af det tile vi er på
                Object object = world.getTile(loc);
                // Hvis objektet ikke er en kanin så skipper vi
                if (!(object instanceof Rabbit)) {
                    break;
                }
                if (object instanceof Rabbit) {
                    // Cast objektet til en kanin
                    Rabbit neighbourRabbit = (Rabbit) object;
                    if (neighbourRabbit.age == 4 && neighbourRabbit.energy >= 25
                            && !neighbourRabbit.hasReproducedThisTurn) {
                        if (!neighbours.isEmpty()) {
                            Location newLocation = neighbours.get(r.nextInt(neighbours.size()));
                            Rabbit newRabbit = new Rabbit(world, size);
                            world.setTile(newLocation, newRabbit);

                            this.energy -= 25;
                            neighbourRabbit.energy -= 25;

                            // Sætter hasReproducedThisTurn til true for begge kaniner
                            this.hasReproducedThisTurn = true;
                            neighbourRabbit.hasReproducedThisTurn = true;

                            break;
                        }
                    }
                }
            }
        }
    }

    public void eat(World world) {
        if (hunger <= 50) {
            System.out.println("Attempting to eat");
            try {
                if (!(world.containsNonBlocking(this.getPlace()))) {
                    world.step();
                    System.out.println("Nothing to eat");
                    return;
                }
                if ((world.getNonBlocking(this.getPlace()) instanceof Grass)) {
                    Grass grass = (Grass) world.getNonBlocking(this.getPlace());
                    grass.die(world);
                    this.hunger += 50;
                    this.energy += 25;
                    System.out.println("I sucessfully ate");
                }
            } catch (IllegalArgumentException iae) {
                // Vi burde ikke aldrig nå herned da vi håndterer exception tidligere
                System.out.println("No entity");
                return;
            }
        }
    }

    public static synchronized void resetRabbitCount() {
        Rabbit.amountOfRabbits = 0;
    }

    public static void updateRabbitCount(World world) {
        int count = 0;
        for (Object obj : world.getEntities().keySet()) {
            if (obj instanceof Rabbit) {
                count++;
            }
        }
        amountOfRabbits = count;
    }

    public void findAndEnterBurrow(World world) {
        System.out.println("Finding burrow");
        Burrow availableBurrow = null;

        // Vi henter alle objekterne i verdenen og itererer igennem dem
        Map<Object, Location> entities = world.getEntities();
        for (Object object : entities.keySet()) {
            if (object instanceof Burrow) {
                Burrow burrow = (Burrow) object;
                if (burrow.getCurrentOccupants() < Burrow.MAX_CAPACITY) {
                    availableBurrow = burrow;
                    break;
                }
            }
        }
        // Hvis der er et ledigt hul går kaninen ind i det eller hvis der ikke er et hul
        // så graver den et nyt
        if (availableBurrow != null) {
            enterBurrow(world, availableBurrow);
        }
        if (availableBurrow == null) {
            digBurrow(world);
        }
    }

    private void digBurrow(World world) {
        // Vi sikrer os at kaninens lokation ikke er null
        if (this.place == null) {
            return;
        }

        System.out.println("Digging burrow");
        // Find alle tomme nabo tiles
        Set<Location> neighbours = world.getSurroundingTiles(this.place);
        List<Location> validLocations = new ArrayList<>(neighbours);
        // Her tilføjer vi valide lokationer til listen
        for (Location loc : neighbours) {
            if (world.isTileEmpty(loc) || world.getTile(loc) instanceof Grass) {
                validLocations.add(loc);
            }
        }
        if (validLocations.isEmpty()) {
            world.step();
        }
        // Hvis der er tomme nabo tiles graver kaninen et hul
        if (!validLocations.isEmpty()) {
            int randomIndex = r.nextInt(validLocations.size());
            Location newLocation = validLocations.get(randomIndex);

            Object objectAtLocation = world.getTile(newLocation);
            if (objectAtLocation instanceof Grass) {
                ((Grass) objectAtLocation).die(world); // Kill the grass
            }
            this.place = newLocation;
            this.myBurrow = new Burrow(world, world.getSize());
            world.setTile(place, this.myBurrow);
            enterBurrow(world, myBurrow);
            this.hidden = true;
        }
    }

    private void enterBurrow(World world, Burrow burrow) {
        System.out.println("Trying to enter burrow");
        // Vi tjekker om kaninen kan komme ind i hullet
        if (burrow != null && burrow.addRabbit(this)) {
            System.out.println("Rabbit entering burrow at: " + burrow.getPlace());
            world.remove(this); // Fjerne kaninen fra verdenen
            this.hidden = true;
            this.myBurrow = burrow; // Sætte kaninens hul til at være det hul den har fundet
        }
    }

    private void leaveBurrow(World world) {
        // Vi tjekker om kaninen er i et hul og om den er skjult
        if (this.myBurrow != null && this.hidden) {
            System.out.println("Rabbit leaving burrow at: " + this.myBurrow.getPlace());
            this.myBurrow.removeRabbit(this); // Vi fjerner kaninen fra hullet

            Set<Location> emptyTiles = world.getEmptySurroundingTiles(this.myBurrow.getPlace());
            if (!emptyTiles.isEmpty()) {
                // Tilføj kaninen til verdenen igen
                this.place = this.myBurrow.getPlace(); // Opdater kaninens lokation
                this.hidden = false; // Kaninen er ikke længere skjult

                // Vi placerer kaninen på den nye lokation
                world.setCurrentLocation(this.place);
                world.setTile(place, this);
                System.out.println("Placing rabbit at: " + this.place);
                System.out.println("Successfully left burrow.");
                System.out.println("Rabbit's location: " + this.place);
            }
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public int getAge() {
        return age;
    }

    public Location getPlace() {
        return place;
    }

}
