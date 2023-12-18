package utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This class is responsible for parsing input data from a file to configure the simulation.
 * It reads entity configuration from a given file and translates it into a format usable by the simulation.
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
     * The method reads the file line by line, interpreting each line as an instruction to create certain types of entities,
     * along with their quantities and, if applicable, specific coordinates.
     *
     * @return A map where keys are entity types and values are lists of configurations (quantity and coordinates).
     */
    public Map<String, List<Integer[]>> parseInput() {
        final Random r = new Random(); // Laver en ny random generator
        Map<String, List<Integer[]>> elementsToAdd = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            if (line != null) { // Sætter værdien af size til det første tal i filen
                this.size = Integer.parseInt(line.trim());
            }
            while ((line = br.readLine()) != null && !line.isEmpty()) { // Derefter læser den resten af filen
                String[] parts = line.split(" ");
                String[] ranges = parts[1].split("-");
                String[] coordinates = new String[2];
                Integer[] quantityRange = new Integer[4];
                if (ranges.length == 1) {
                    int quantity = Integer.parseInt(ranges[0]);
                    quantityRange = new Integer[] { quantity, quantity };
                }
                if (ranges.length == 2) {
                    int min = Integer.parseInt(ranges[0]);
                    int max = Integer.parseInt(ranges[1]);
                    quantityRange = new Integer[] { min, r.nextInt(max - min + 1) + min };
                }
                if (parts[0].equals("bear") && parts.length == 3 && parts[2].matches("\\(\\d+,\\d+\\)")) {
                    String coordinatePart = parts[2].substring(1, parts[2].length() - 1); // Fjerner paranteserne
                    coordinates = coordinatePart.split(","); // Splitter koordinaterne op
                    int x = Integer.parseInt(coordinates[0]);
                    int y = Integer.parseInt(coordinates[1]);
                    int quantity = Integer.parseInt(parts[1]);
                    quantityRange = new Integer[] { quantity, x, y };
                }
                if (parts[0].equals("wolf") && parts.length >= 2) {
                    int quantity = Integer.parseInt(parts[1]);
                    quantityRange = new Integer[] { quantity };
                }
                List<Integer[]> quantities = elementsToAdd.get(parts[0]);
                if (quantities == null) {
                    quantities = new ArrayList<>();
                    elementsToAdd.put(parts[0], quantities);
                }
                quantities.add(quantityRange);
            }
            br.close();
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
