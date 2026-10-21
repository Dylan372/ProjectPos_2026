package pos;

/** Central location of the data file paths (relative to the project root). */
public final class PathsUtil {

    public static final String DATA_DIR = "data";

    private PathsUtil() {
    }

    public static String productsPath() {
        return DATA_DIR + "/products.txt";
    }

    public static String inventoryPath() {
        return DATA_DIR + "/inventory.txt";
    }
}
