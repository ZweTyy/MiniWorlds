import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import java.util.Random;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Rabbit implements Actor {
    private Location place;
    private boolean alive = true;
    private int age = 1;
    private int hunger = 100;
    private int energy = 50;
    private int stepsTaken = 0;
    private int health = 100;
    private int amountOfRabbits = 0;
    private Random r = new Random(); // Laver en ny random generator

    public Rabbit(World world, int size) {
        initializeRabbit(world, size);
    }

    @Override
    public void act(World world) {
        move(world);
        reproduce(world, age);
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
        amountOfRabbits++;
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
            this.energy -= 5;
            this.hunger -= 5;
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
    }

    public void reproduce(World world, int size) {
        // Reproduction sker kun hvis kaninen er 4 år gammel og har 25 energi og der er
        // mindre end 1/8 kaniner i forhold til størrelsen af verdenen
        if (this.age == 4 && this.energy >= 25 && amountOfRabbits < size * size / 8) {
            // Hent alle nabo tiles
            Set<Location> neighbours = world.getSurroundingTiles(place);

            // Loop igennem alle nabo tiles
            for (Location loc : neighbours) {
                // Hent objektet på lokationen
                Object object = world.getTile(loc);
                // Tjek om objektet er en kanin
                if (object instanceof Rabbit) {
                    // Cast objektet til en kanin og tjek om den er 4 år gammel og har 25 energi
                    Rabbit neighbourRabbit = (Rabbit) object;
                    if (neighbourRabbit.age == 4 && neighbourRabbit.energy >= 25) {
                        // Tjek om der er tomme nabo tiles
                        List<Location> emptyLocations = new ArrayList<>(world.getEmptySurroundingTiles(place));
                        if (!emptyLocations.isEmpty()) {
                            Location newLocation = emptyLocations.get(r.nextInt(emptyLocations.size()));
                            Rabbit newRabbit = new Rabbit(world, size);
                            world.setTile(newLocation, newRabbit);

                            // Sæt energi og sult til 25 for begge kaniner og øg antallet af kaniner
                            this.energy -= 25;
                            neighbourRabbit.energy -= 25;
                            amountOfRabbits++;
                            break;
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
