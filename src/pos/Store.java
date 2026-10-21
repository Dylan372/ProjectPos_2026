package pos;

/**
 * The store aggregates its product catalog and inventory.
 * Indirection: Register obtains specifications through the Store, which
 * delegates to the ProductCatalog.
 */
public class Store {

    private final String name;
    private final String address;
    private final ProductCatalog catalog;
    private final Inventory inventory;

    public Store(String name, String address, ProductCatalog catalog, Inventory inventory) {
        this.name = name;
        this.address = address;
        this.catalog = catalog;
        this.inventory = inventory;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public ProductSpecification getSpecification(int itemID) {
        return catalog.getSpecification(itemID);
    }

    public ProductCatalog getCatalog() {
        return catalog;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getStock(int itemID) {
        return inventory.getStock(itemID);
    }

    public void reduceStock(int itemID, int quantity) {
        inventory.reduce(itemID, quantity);
    }

    public void restock(int itemID, int quantity) {
        inventory.restock(itemID, quantity);
    }
}
