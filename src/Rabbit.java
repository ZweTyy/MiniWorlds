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
    private boolean alive = true;
    private boolean hasReproducedThisTurn = false;
    private int age = 1;
    private int hunger = 100;
    private int energy = 50;
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
        hasReproducedThisTurn = false;
        move(world);
        if (!hasReproducedThisTurn) {
            reproduce(world, world.getSize());
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
        if (validLocations.isEmpty() || energy <= 0) { // Hvis der ikke er nogen tomme nabo tiles skipper vi
            world.step();
        }
        if (!validLocations.isEmpty() && energy > 0) { // Hvis der er tomme nabo tiles og kaninen har energi bevæger den
                                                       // sig
            int randomIndex = r.nextInt(validLocations.size());
            Location newLocation = validLocations.get(randomIndex);
            world.move(this, newLocation);
            this.energy -= 0;
            this.hunger -= 0;
            this.stepsTaken++;
        }
        if (stepsTaken == 10) {
            this.age++;
            this.energy = 100;
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
        // Set the static count to the current count
        Rabbit.amountOfRabbits = rabbitCount;
        return rabbitCount;
    }

    public void reproduce(World world, int size) {
        if (this.age == 4 && this.energy >= 25 && amountOfRabbits < size * size / 4 && !hasReproducedThisTurn) {
            List<Location> emptyLocations = new ArrayList<>(world.getEmptySurroundingTiles(place));

            // Find an eligible neighbour
            for (Location loc : world.getSurroundingTiles(place)) {
                Object object = world.getTile(loc);
                if (object instanceof Rabbit) {
                    Rabbit neighbourRabbit = (Rabbit) object;
                    if (neighbourRabbit.age == 4 && neighbourRabbit.energy >= 25
                            && !neighbourRabbit.hasReproducedThisTurn) {
                        if (!emptyLocations.isEmpty()) {
                            Location newLocation = emptyLocations.get(r.nextInt(emptyLocations.size()));
                            Rabbit newRabbit = new Rabbit(world, size);
                            world.setTile(newLocation, newRabbit);

                            this.energy -= 25;
                            neighbourRabbit.energy -= 25;

                            this.hasReproducedThisTurn = true;
                            neighbourRabbit.hasReproducedThisTurn = true;

                            break; // Break after successful reproduction
                        }
                    }
                }
            }
        }
    }

    public void eat(World world) {
        if (world.containsNonBlocking(place)) {
            hunger += 25;
            energy += 50;
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
