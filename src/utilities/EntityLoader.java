package utilities;

import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entities.Bear;
import entities.BurrowFactory;
import entities.Entity;
import entities.Rabbit;
import entities.Wolf;
import entities.WolfPack;
import factories.GrassFactory;
import factories.RabbitFactory;
import factories.WolfFactory;
import factories.BearFactory;
import factories.BerryFactory;

public class EntityLoader {
    public static void loadEntities(World world, Map<String, List<Integer[]>> entitiesToLoad, int size) {
        // Vi løber igennem alle de entities, som vi skal loade
        for (Map.Entry<String, List<Integer[]>> entry : entitiesToLoad.entrySet()) {
            String entityType = entry.getKey();
            List<Integer[]> entityQuantities = entry.getValue();
            // Den kører for hver entity, som vi skal loade
            for (Integer[] entityQuantity : entityQuantities) {
                // For wolf entities, handle the entire pack creation in one go
                if ("wolf".equals(entityType)) {
                    int numberOfWolves = entityQuantity[0];
                    WolfPack wolfPack = WolfFactory.createWolfPack(world, size, numberOfWolves);
                    for (Wolf wolf : wolfPack.getPack()) {
                        System.out.println("Created wolf: " + wolf);
                        placeEntity(world, wolf);
                    }
                } else {
                    // For all other entities, create and place them one by one
                    Entity entity = createEntity(entityType, world, size, entityQuantities);
                    System.out.println("Created entity: " + entity);
                    if (entity != null) {
                        placeEntity(world, entity);
                    }
                }
            }
        }
    }

    private static Entity createEntity(String entityType, World world, int size, List<Integer[]> details) {
        switch (entityType) {
            case "rabbit":
                return RabbitFactory.createRabbit(world, size);
            case "grass":
                return GrassFactory.createGrass(world, size);
            case "burrow":
                return BurrowFactory.createBurrow(world, size);
            case "wolf":
                int numberOfWolves = details.get(0)[0];
                return WolfFactory.createWolfPack(world, size, numberOfWolves);
            case "bear":
                return createBear(world, size, details);
            case "berry":
                return BerryFactory.createBerry(world, size);
            default:
                System.out.println("Unknown entity type: " + entityType);
                return null;
        }
    }

    private static Bear createBear(World world, int size, List<Integer[]> details) {
        if (details.size() > 2 && details.get(1) != null && details.get(2) != null) {
            // If specific coordinates are provided
            Integer x = details.get(0)[1];
            Integer y = details.get(0)[2];
            return BearFactory.createBear(world, size, x, y);
        } else {
            // If no coordinates provided, let the factory decide
            return BearFactory.createBear(world, size);
        }
    }

    private static void placeEntity(World world, Entity entity) {
        Location location = entity.getLocation();
        if (world.isTileEmpty(location) && !world.containsNonBlocking(location)) {
            world.setTile(location, entity);
        }
    }
}
