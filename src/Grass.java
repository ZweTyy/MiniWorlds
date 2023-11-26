import java.util.Random;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import itumulator.simulator.Actor;

public class Grass implements Actor, NonBlocking {
    private boolean alive;
    private Location place;
    Random r = new Random();

    public Grass(World world, int size) {
        initializeGrass(world, size);
        this.alive = true;
    }

    @Override
    public void act(World world) {
        decay(world);
        spread(world, world.getSize());
    }

    public void initializeGrass(World world, int size) {
        int x = r.nextInt(size); // Lav et tilfældigt x koordinat
        int y = r.nextInt(size); // Lav et tilfældigt y koordinat
        place = new Location(x, y); // Lav ny lokation med de tilfældige koordinater
        while (!world.isTileEmpty(place)) { // While tile ikke er tom
            x = r.nextInt(size);
            y = r.nextInt(size);
            place = new Location(x, y);
        }
    }

    public void decay(World world) {
        // Græs har en chance for at dø tilfældigt
        if (this.alive && r.nextDouble() < 0.025) { // 2.5% chance for at dø
            this.alive = false;
        }
        if (!this.alive) {
            world.delete(this);
        }
    }

    public void spread(World world, int size) {
        if (this.alive && r.nextDouble() < 0.1) { // 10% chance for at sprede sig
            Set<Location> neighbours = world.getEmptySurroundingTiles(place);
            List<Location> validLocations = new ArrayList<>();

            // Her tilføjer vi valide lokationer til listen
            for (Location loc : neighbours) {
                if (world.isTileEmpty(loc) && !world.containsNonBlocking(loc)) {
                    validLocations.add(loc);
                }
            }
            // Hvis der er valid locations, så vælg en tilfældigt og lav nyt græs
            if (!validLocations.isEmpty()) {
                int randomIndex = r.nextInt(validLocations.size());
                Location newLocation = validLocations.get(randomIndex);

                // Tjek igen hvis der er græs på lokationen og sæt græs hvis der ikke er
                if (world.isTileEmpty(newLocation) && !world.containsNonBlocking(newLocation)) {
                    Grass newGrass = new Grass(world, size);
                    world.setTile(newLocation, newGrass);
                }
            }
        }
    }

    public void die(World world) {
        this.alive = false;
        world.delete(this);
    }

    public boolean isAlive() {
        return alive;
    }

    public Location getPlace() {
        return place;
    }
}
