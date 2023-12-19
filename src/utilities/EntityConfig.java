package utilities;

/**
 * Represents the configuration of an entity with specified attributes.
 * This class provides the configuration details like quantity, position (x, y),
 * and additional information about the entity.
 */
public class EntityConfig {

    private int quantity;
    private Integer x;
    private Integer y;
    private String additionalInfo; // e.g., associated entity type

    /**
     * Constructs a new EntityConfig with specified parameters.
     *
     * @param quantity       the quantity of the entity
     * @param x              the X-coordinate of the entity's position
     * @param y              the Y-coordinate of the entity's position
     * @param additionalInfo additional information about the entity, for example,
     *                       its type
     */
    public EntityConfig(int quantity, Integer x, Integer y, String additionalInfo) {
        this.quantity = quantity;
        this.x = x;
        this.y = y;
        this.additionalInfo = additionalInfo;
    }

    /**
     * Gets the quantity of the entity.
     *
     * @return the quantity of the entity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Gets the X-coordinate of the entity's position.
     *
     * @return the X-coordinate of the entity's position, or null if not set
     */
    public Integer getX() {
        return x;
    }

    /**
     * Gets the Y-coordinate of the entity's position.
     *
     * @return the Y-coordinate of the entity's position, or null if not set
     */
    public Integer getY() {
        return y;
    }

    /**
     * Gets additional information about the entity.
     *
     * @return a string containing additional information about the entity
     */
    public String getAdditionalInfo() {
        return additionalInfo;
    }
}
