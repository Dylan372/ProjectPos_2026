package pos;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A catalog of ProductSpecifications, keyed by itemID.
 * Loaded from a text file "id|description|price" (loaded once at start-up).
 */
public class ProductCatalog {

    private final Map<Integer, ProductSpecification> specs = new HashMap<>();

    public void add(ProductSpecification spec) {
        specs.put(spec.getItemID(), spec);
    }

    public ProductSpecification getSpecification(int itemID) {
        return specs.get(itemID);
    }

    public Collection<ProductSpecification> getAll() {
        return specs.values();
    }

    public static ProductCatalog loadFromFile(String path) {
        ProductCatalog catalog = new ProductCatalog();
        try {
            for (String line : Files.readAllLines(Path.of(path), StandardCharsets.UTF_8)) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split("\\|");
                catalog.add(new ProductSpecification(
                        Integer.parseInt(parts[0].trim()),
                        parts[1].trim(),
                        Double.parseDouble(parts[2].trim())));
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot load product catalog from " + path, e);
        }
        return catalog;
    }
}
