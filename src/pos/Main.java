package pos;

/**
 * Entry point of the PoS System (ProjectPos_2026).
 *
 * Layered architecture:
 *   ConsoleUI (presentation) -> Register (application controller)
 *   -> Sale / Return / Store / ProductCatalog / Inventory (domain + data)
 *
 * Usage:
 *   java pos.Main          start the interactive console
 *   java pos.Main --demo   run the scripted demonstration
 */
public class Main {

    public static void main(String[] args) {
        Store store = new Store(
                "Supermart No.1",
                "Nanjing, Jiangsu, China",
                ProductCatalog.loadFromFile(PathsUtil.productsPath()),
                Inventory.loadFromFile(PathsUtil.inventoryPath()));
        Register register = new Register(store);
        ConsoleUI ui = new ConsoleUI(store, register);

        if (args.length > 0 && args[0].equals("--demo")) {
            // scripted end-to-end scenario for the demo video
            ui.setScript(
                    // Process Sale: 2x Coca-Cola, 1x Instant Noodles, pay 100
                    "1001", "2", "1004", "1", "0", "100",
                    // Handle Returns: 1x Coca-Cola
                    "1001", "1", "0");
            ui.runDemo();
        } else {
            ui.run();
        }
    }
}
