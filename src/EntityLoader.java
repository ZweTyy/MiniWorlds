import itumulator.world.Location;
import itumulator.world.World;

import java.util.Map;

public class EntityLoader {
    public static void loadEntities(World world, Map<String, Integer[]> entitiesToLoad, int size) {
        // Vi løber igennem alle de entities, som vi skal loade
        for (Map.Entry<String, Integer[]> entry : entitiesToLoad.entrySet()) {
            String entityType = entry.getKey();
            Integer[] entityQuantity = entry.getValue();
            // Den kører for hver entity, som vi skal loade
            for (int i = 0; i < entityQuantity[0]; i++) {
                Entity entity = createEntity(entityType, world, size, entityQuantity);
                if (entity != null) {
                    placeEntity(world, entity);
                }
            }
        }
    }

    private static Entity createEntity(String entityType, World world, int size, Integer[] details) {
        switch (entityType) {
            case "rabbit":
                return RabbitFactory.createRabbit(world, size);
            case "grass":
                return GrassFactory.createGrass(world, size);
            case "burrow":
                return BurrowFactory.createBurrow(world, size);
            case "wolf":
                return WolfFactory.createWolf(world, size, details);
            case "bear":
                return createBear(world, size, details);
            case "berry":
                return BerryFactory.createBerry(world, size);
            default:
                System.out.println("Unknown entity type: " + entityType);
                return null;
        }
    }

    private static Bear createBear(World world, int size, Integer[] details) {
        if (details.length > 2 && details[1] != null && details[2] != null) {
            // If specific coordinates are provided
            return BearFactory.createBear(world, size, details[1], details[2]);
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
