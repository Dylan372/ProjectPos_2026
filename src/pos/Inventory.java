package pos;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Stock levels per itemID, persisted to a text file "id|quantity".
 * Updated after each sale (reduce) and each return (restock).
 */
public class Inventory {

    private final Map<Integer, Integer> stock = new HashMap<>();

    public int getStock(int itemID) {
        return stock.getOrDefault(itemID, 0);
    }

    public void reduce(int itemID, int quantity) {
        int current = getStock(itemID);
        if (current < quantity) {
            throw new IllegalStateException(
                    "Insufficient stock for item " + itemID + ": " + current + " < " + quantity);
        }
        stock.put(itemID, current - quantity);
    }

    public void restock(int itemID, int quantity) {
        stock.put(itemID, getStock(itemID) + quantity);
    }

    public void save(String path) {
        StringBuilder sb = new StringBuilder();
        stock.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> sb.append(e.getKey()).append('|').append(e.getValue()).append('\n'));
        try {
            Files.writeString(Path.of(path), sb.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Cannot save inventory to " + path, e);
        }
    }

    public static Inventory loadFromFile(String path) {
        Inventory inventory = new Inventory();
        try {
            for (String line : Files.readAllLines(Path.of(path), StandardCharsets.UTF_8)) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split("\\|");
                inventory.stock.put(Integer.parseInt(parts[0].trim()),
                                    Integer.parseInt(parts[1].trim()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot load inventory from " + path, e);
        }
        return inventory;
    }
}
