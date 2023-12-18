package utilities;

import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entities.Bear;
import entities.Berry;
import entities.Burrow;
import entities.Entity;
import entities.Rabbit;
import entities.Grass;
import entities.Wolf;
import entities.WolfPack;
import factories.GrassFactory;
import factories.RabbitFactory;
import factories.WolfFactory;
import factories.BearFactory;
import factories.BerryFactory;
import factories.BurrowFactory;

/**
 * This class provides utilities for loading various entities into the
 * simulation world.
 * It uses different factories to create entities and places them in the world
 * based on given configurations.
 */
public class EntityLoader {

    /**
     * Loads entities into the world based on a map specifying the types and
     * quantities of entities to load.
     *
     * @param world          The world where entities are to be loaded.
     * @param entitiesToLoad A map with entity types as keys and a list of
     *                       EntityConfig objects as values.
     * @param size           The size parameter used for entity creation.
     */
    public static void loadEntities(World world, Map<String, List<EntityConfig>> entitiesToLoad, int size) {
        for (Map.Entry<String, List<EntityConfig>> entry : entitiesToLoad.entrySet()) {
            String entityType = entry.getKey();
            List<EntityConfig> entityConfigs = entry.getValue();

            for (EntityConfig config : entityConfigs) {
                createAndPlaceEntities(world, entityType, size, config);
            }
        }
    }

    /**
     * Creates and places entities in the world based on the given entity type,
     * size, and quantity.
     *
     * @param world          The world where entities are to be placed.
     * @param entityType     The type of entity to create.
     * @param size           The size parameter used for entity creation.
     * @param entityQuantity The quantity of entities to create.
     */
    private static void createAndPlaceEntities(World world, String entityType, int size, EntityConfig config) {
        switch (entityType) {
            case "rabbit":
                List<Rabbit> rabbits = RabbitFactory.createMultipleRabbits(world, size, config.getQuantity());
                for (Rabbit rabbit : rabbits) {
                    placeEntity(world, rabbit);
                }
                break;
            case "wolf":
                WolfPack wolfPack = WolfFactory.createWolfPack(world, size, config.getQuantity());
                for (Wolf wolf : wolfPack.getPack()) {
                    placeEntity(world, wolf);
                }
                break;
            case "bear":
                if (config.getX() != null && config.getY() != null) {
                    Bear bear = BearFactory.createBear(world, size, config.getX(), config.getY());
                    placeEntity(world, bear);
                } else {
                    List<Bear> bears = BearFactory.createMultipleBears(world, size, config.getQuantity());
                    for (Bear bear : bears) {
                        placeEntity(world, bear);
                    }
                }
                break;
            case "grass":
                List<Grass> grasses = GrassFactory.createMultipleGrasses(world, size, config.getQuantity());
                for (Grass grass : grasses) {
                    placeEntity(world, grass);
                }
                break;
            case "berry":
                List<Berry> berries = BerryFactory.createMultipleBerries(world, size, config.getQuantity());
                for (Berry berry : berries) {
                    placeEntity(world, berry);
                }
                break;
            case "burrow":
                List<Burrow> burrows = BurrowFactory.createMultipleBurrows(world, size, config.getQuantity());
                for (Burrow burrow : burrows) {
                    placeEntity(world, burrow);
                }
                break;
            // Add cases for other entities like grass, berries, burrows
            default:
                System.out.println("Unknown entity type: " + entityType);
                break;
        }
    }

    /**
     * Places an entity in the world at its designated location.
     *
     * @param world  The world where the entity is to be placed.
     * @param entity The entity to place in the world.
     */
    private static void placeEntity(World world, Entity entity) {
        Location location = entity.getLocation();
        if (world.isTileEmpty(location) && !world.containsNonBlocking(location)) {
            world.setTile(location, entity);
        }
    }
}
