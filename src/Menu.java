import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    public static void display(String title, LinkedHashMap<String, MenuOption> options) {
        Scanner scanner = Main.scanner;
        while (true) {
            System.out.println("\n=== " + title + " ===");
            for (Map.Entry<String, MenuOption> entry : options.entrySet()) {
                System.out.println(entry.getKey() + ". " + entry.getValue().description);
            }
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim().toUpperCase();
            MenuOption option = options.get(choice);
            if (option != null) {
                if (option.action.run()) {
                    break;
                }
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }

    public static class MenuOption {
        public final String description;
        public final MenuAction action;
        public MenuOption(String description, MenuAction action) {
            this.description = description;
            this.action = action;
        }
    }

    //explain
    @FunctionalInterface
    public interface MenuAction {
        // Return true to exit the menu, false to stay
        boolean run();
    }
} 