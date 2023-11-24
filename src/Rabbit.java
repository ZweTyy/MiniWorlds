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
        System.out.println(energy + " " + hunger + " " + health + " " + age + " " + amountOfRabbits);
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
            System.out.println("Eating");
            try {
                if (world.getNonBlocking(this.getPlace()) instanceof Grass) {
                    Grass grass = (Grass) world.getNonBlocking(this.getPlace());
                    grass.die(world);
                    this.hunger += 50;
                    this.energy += 25;
                }
            } catch (Exception e) {
                System.out.println("No grass");
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
        if (availableBurrow != null) {
            enterBurrow(world, availableBurrow);
        }
        if (availableBurrow == null) {
            digBurrow(world);
        }
    }

    private void digBurrow(World world) {
        System.out.println("Digging burrow");
        // Find alle tomme nabo tiles
        Set<Location> neighbours = world.getEmptySurroundingTiles();
        List<Location> validLocations = new ArrayList<>(neighbours);
        // Hvis der ikke er nogen tomme nabo tiles skipper vi
        if (validLocations.isEmpty()) {
            world.step();
        }
        // Hvis der er tomme nabo tiles graver kaninen et hul
        if (!validLocations.isEmpty()) {
            int randomIndex = r.nextInt(validLocations.size());
            Location newLocation = validLocations.get(randomIndex);
            this.place = newLocation;
            this.myBurrow = new Burrow(world, world.getSize());
            world.setTile(place, this.myBurrow);
        }
    }

    private void enterBurrow(World world, Burrow burrow) {
        System.out.println("Trying to enter burrow");
        // Vi tjekker om kaninen kan komme ind i hullet
        if (burrow != null && burrow.addRabbit(this)) {
            System.out.println("Rabbit entering burrow at: " + burrow.getPlace());
            world.remove(this); // Remove the rabbit from the map visually
            this.hidden = true;
            this.myBurrow = burrow; // Assign the burrow to the rabbit
        }
    }

    private void leaveBurrow(World world) {
        if (this.myBurrow != null && this.hidden) {
            System.out.println("Rabbit leaving burrow at: " + this.myBurrow.getPlace());
            this.myBurrow.removeRabbit(this); // Remove from burrow list

            Set<Location> emptyTiles = world.getEmptySurroundingTiles(this.myBurrow.getPlace());
            if (!emptyTiles.isEmpty()) {
                Location newLocation = emptyTiles.iterator().next();
                System.out.println("Rabbit moving to: " + newLocation);

                // Add the rabbit back to the world at the new location
                this.place = newLocation; // Update the rabbit's location
                this.hidden = false; // The rabbit is no longer hidden

                // Use setTile to place the rabbit directly since it's not on the map
                world.setTile(newLocation, this); // This should not throw an exception
                world.setCurrentLocation(this.place);
                System.out.println("Successfully left burrow.");
            } else {
                // Handle the case where no empty tiles are available
                System.out.println("No empty tiles available to leave the burrow.");
                // The rabbit remains hidden and in the burrow for now
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
