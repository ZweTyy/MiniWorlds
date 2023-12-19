package utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This class is responsible for parsing input data from a file to configure the
 * simulation.
 * It reads entity configuration from a given file and translates it into a
 * format usable by the simulation.
 */

public class InputParser {
    private String filePath;
    private int size;

    /**
     * Constructs an InputParser with a specified file path.
     *
     * @param filePath The path to the file containing the simulation input data.
     */
    public InputParser(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Parses the input file to extract entity configurations.
     * The method reads the file line by line, interpreting each line as an
     * instruction to create certain types of entities,
     * along with their quantities and, if applicable, specific coordinates.
     *
     * @return A map where keys are entity types and values are lists of
     *         configurations (quantity and coordinates).
     */
    public Map<String, List<EntityConfig>> parseInput() {
        final Random r = new Random();
        Map<String, List<EntityConfig>> elementsToAdd = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            if (line != null) {
                this.size = Integer.parseInt(line.trim());
            }

            while ((line = br.readLine()) != null && !line.isEmpty()) {
                String[] parts = line.split(" ");

                // Handle compound entity names like "cordyceps rabbit" or "cordyceps wolf"
                String entityType = parts[0];
                int quantityIndex = 1;
                if (entityType.equals("cordyceps") && parts.length > 2) {
                    entityType += " " + parts[1];
                    quantityIndex = 2; // Adjust the index for quantity if it's a compound name
                }

                // Parse the quantity, which can be a range or a single number
                int minQuantity, maxQuantity;
                if (parts[quantityIndex].contains("-")) {
                    String[] ranges = parts[quantityIndex].split("-");
                    minQuantity = Integer.parseInt(ranges[0]);
                    maxQuantity = ranges.length > 1 ? Integer.parseInt(ranges[1]) : minQuantity;
                } else {
                    minQuantity = maxQuantity = Integer.parseInt(parts[quantityIndex]);
                }
                int quantity = r.nextInt(maxQuantity - minQuantity + 1) + minQuantity;

                // Parse additional info if present
                Integer x = null, y = null;
                String additionalInfo = null;
                if (parts.length > quantityIndex + 1) {
                    String coordinatePart = parts[quantityIndex + 1];
                    // Check if the coordinate part matches the pattern "(number,number)"
                    if (coordinatePart.matches("\\(\\d+,\\d+\\)")) {
                        // Remove parentheses and split by comma
                        coordinatePart = coordinatePart.substring(1, coordinatePart.length() - 1);
                        String[] coordinates = coordinatePart.split(",");
                        if (coordinates.length == 2) {
                            try {
                                x = Integer.parseInt(coordinates[0].trim());
                                y = Integer.parseInt(coordinates[1].trim());
                                System.out.println("Parsed coordinates: " + x + ", " + y);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid format for coordinates: " + coordinatePart);
                            }
                        }
                    } else {
                        System.out.println("Coordinates not provided in the correct format.");
                    }
                }

                EntityConfig config = new EntityConfig(quantity, x, y, additionalInfo);
                List<EntityConfig> configs = elementsToAdd.get(entityType);
                if (configs == null) {
                    configs = new ArrayList<>();
                    elementsToAdd.put(entityType, configs);
                }
                configs.add(config);
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        return elementsToAdd;
    }

    /**
     * Gets the size parameter extracted from the input file.
     * This represents the size of the simulation world.
     *
     * @return The size parameter as an integer.
     */
    public int getSize() {
        return this.size;
    }
}
