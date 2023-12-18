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
    public Map<String, List<Integer[]>> parseInput() {
        final Random r = new Random(); // Create a new random generator
        Map<String, List<Integer[]>> elementsToAdd = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            if (line != null) { // Set the size value from the first number in the file
                this.size = Integer.parseInt(line.trim());
            }
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                String[] parts = line.split(" ");
                String entityType = parts[0];
                String[] ranges = parts[1].split("-");
                Integer[] quantityRange;

                // Check if a range is specified
                if (ranges.length == 2) {
                    int min = Integer.parseInt(ranges[0]);
                    int max = Integer.parseInt(ranges[1]);
                    int quantity = r.nextInt(max - min + 1) + min; // Generate a random quantity within the range
                    quantityRange = new Integer[] { quantity };
                } else {
                    // Handle a single fixed quantity
                    int quantity = Integer.parseInt(parts[1]);
                    quantityRange = new Integer[] { quantity };
                }

                // Additional entity-specific processing (like coordinates for bears)
                if (entityType.equals("bear") && parts.length == 3 && parts[2].matches("\\(\\d+,\\d+\\)")) {
                    String coordinatePart = parts[2].substring(1, parts[2].length() - 1); // Remove parentheses
                    String[] coordinates = coordinatePart.split(","); // Split coordinates
                    int x = Integer.parseInt(coordinates[0]);
                    int y = Integer.parseInt(coordinates[1]);
                    quantityRange = new Integer[] { quantityRange[0], x, y };
                }

                // Add the entity configuration to the map
                List<Integer[]> quantities = elementsToAdd.get(entityType);
                if (quantities == null) {
                    quantities = new ArrayList<>();
                    elementsToAdd.put(entityType, quantities);
                }
                quantities.add(quantityRange);
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
