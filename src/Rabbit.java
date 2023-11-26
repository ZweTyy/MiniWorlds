import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Rabbit extends Animal implements Actor {
    private Burrow myBurrow;
    private volatile boolean hasReproducedThisTurn = false;
    private volatile boolean hidden;
    private static int amountOfRabbits = 0;

    public Rabbit(World world, int size) {
        super(world, size);
        this.MAX_AGE = 9;
        amountOfRabbits++;
    }

    enum State {
        DAY_AND_HIDDEN,
        NIGHT_WITH_NO_BURROW,
        NIGHT_WITH_BURROW,
        HIDDEN_AT_NIGHT,
        OTHERWISE
    }

    @Override
    public synchronized void act(World world) {
        State state = determineState(world, hidden, myBurrow);

        switch (state) {
            case HIDDEN_AT_NIGHT:
                break;
            case DAY_AND_HIDDEN:
                leaveBurrow(world);
                break;
            case NIGHT_WITH_NO_BURROW:
                findAndEnterBurrow(world);
                break;
            case NIGHT_WITH_BURROW:
                enterBurrow(world, myBurrow);
                break;
            case OTHERWISE:
                performDailyActivities(world);
                break;
        }
    }

    private void performDailyActivities(World world) {
        hasReproducedThisTurn = false;
        move(world); // Assuming move is defined in Animal
        eat(world);
        if (!hasReproducedThisTurn) {
            reproduce(world, world.getSize());
        }
    }

    @Override
    protected void move(World world) {
        // We need to implement flee logic here
        super.move(world);
    }

    public void reproduce(World world, int size) {
        // Vi tjekker om kaninen er 4 år gammel, har nok energi, om der er plads til
        // flere og om den ikke har reproduceret i denne tur
        if (this.age == 4 && this.energy >= 25 && amountOfRabbits < size * size / 4 && !hasReproducedThisTurn) {
            // Find alle tomme nabo tiles
            List<Location> neighbours = new ArrayList<>(world.getEmptySurroundingTiles(currentLocation));
            // Løber igennem alle nabo tiles
            for (Location loc : world.getSurroundingTiles(currentLocation)) {
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

    @Override
    public void eat(World world) {
        if (hunger <= 50) {
            System.out.println("Attempting to eat");
            try {
                if (!(world.containsNonBlocking(this.getLocation()))) {
                    world.step();
                    System.out.println("Nothing to eat");
                    return;
                }
                if ((world.getNonBlocking(this.getLocation()) instanceof Grass)) {
                    Grass grass = (Grass) world.getNonBlocking(this.getLocation());
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

    public static int countRabbits(World world) {
        Map<Object, Location> entities = world.getEntities();
        int rabbitCount = 0;
        for (Object obj : entities.keySet()) {
            if (obj instanceof Rabbit) {
                rabbitCount++;
            }
        }
        Rabbit.amountOfRabbits = rabbitCount;
        return rabbitCount;
    }

    public static void resetRabbitCount() {
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

    public synchronized void findAndEnterBurrow(World world) {
        System.out.println("Finding burrow");
        Burrow availableBurrow = null;

        // Vi henter alle objekterne i verdenen og itererer igennem dem
        Map<Object, Location> entities = world.getEntities();
        for (Object object : entities.keySet()) {
            if (object instanceof Burrow) {
                Burrow burrow = (Burrow) object;
                if (burrow.getCurrentOccupants() < Burrow.MAX_CAPACITY) {
                    System.out.println("Found available burrow");
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
        if (this.currentLocation == null) {
            return;
        }

        System.out.println("Digging burrow");
        // Find alle tomme nabo tiles
        Set<Location> neighbours = world.getSurroundingTiles(this.currentLocation);
        List<Location> validLocations = new ArrayList<>(neighbours);
        // Her tilføjer vi valide lokationer til listen
        for (Location loc : neighbours) {
            if (world.isTileEmpty(loc) || world.getTile(loc) instanceof Grass) {
                validLocations.add(loc);
            }
        }
        if (validLocations.isEmpty()) {
            return;
        }
        // Hvis der er tomme nabo tiles graver kaninen et hul
        if (!validLocations.isEmpty()) {
            int randomIndex = r.nextInt(validLocations.size());
            Location newLocation = validLocations.get(randomIndex);

            Object objectAtLocation = world.getTile(newLocation);
            if (objectAtLocation instanceof Grass) {
                ((Grass) objectAtLocation).die(world);
            }
            this.currentLocation = newLocation;
            this.myBurrow = new Burrow(world, world.getSize());
            world.setTile(currentLocation, this.myBurrow);
            enterBurrow(world, myBurrow);
            this.hidden = true;
        }
    }

    private synchronized void enterBurrow(World world, Burrow burrow) {
        System.out.println("Trying to enter burrow");
        // Vi tjekker om kaninen kan komme ind i hullet
        if (burrow != null && burrow.addRabbit(this)) {
            System.out.println("Rabbit entering burrow at: " + burrow.getLocation());
            world.remove(this); // Fjerne kaninen fra verdenen
            this.hidden = true;
            this.myBurrow = burrow; // Sætte kaninens hul til at være det hul den har fundet
            System.out.println("Rabbit location er: " + currentLocation);
        }
    }

    private synchronized void leaveBurrow(World world) {
        if (this.myBurrow != null && this.hidden) {
            System.out.println("Rabbit leaving burrow at: " + this.myBurrow.getLocation());
            this.myBurrow.removeRabbit(this);

            Set<Location> emptyTiles = world.getEmptySurroundingTiles(this.myBurrow.getLocation());
            if (!emptyTiles.isEmpty()) {
                this.currentLocation = this.myBurrow.getLocation();
                this.hidden = false;
                world.setCurrentLocation(this.currentLocation);
                world.setTile(currentLocation, this);
                System.out.println("Placing rabbit at: " + this.currentLocation);
                System.out.println("Successfully left burrow.");
                System.out.println("Rabbit's location: " + world.getCurrentLocation());
            }
        }
    }

    private State determineState(World world, boolean hidden, Burrow myBurrow) {
        if (!hidden && world.isDay()) {
            return State.OTHERWISE;
        }
        if (hidden && world.isNight()) {
            return State.HIDDEN_AT_NIGHT;
        }
        if (!hidden && world.isNight()) {
            return myBurrow == null ? State.NIGHT_WITH_NO_BURROW : State.NIGHT_WITH_BURROW;
        }
        if (world.isDay()) {
            return State.DAY_AND_HIDDEN;
        }
        return world.isNight() && myBurrow == null ? State.NIGHT_WITH_NO_BURROW : State.NIGHT_WITH_BURROW;
    }

    public boolean isAlive() {
        return alive;
    }

    public int getAge() {
        return age;
    }
}
