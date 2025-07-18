import java.io.File;
import java.util.Scanner;

public class Ledger {
    public static void showAll() {
        showEntries(EntryType.ALL);
    }
    public static void showDeposits() {
        showEntries(EntryType.DEPOSIT);
    }
    public static void showPayments() {
        showEntries(EntryType.PAYMENT);
    }

    private enum EntryType { ALL, DEPOSIT, PAYMENT }

    private static void showEntries(EntryType type) {
        System.out.println("\n--- Ledger Entries ---");
        File file = new File(Main.fileName);
        if (!file.exists()) {
            System.out.println("No transactions found. File does not exist.");
            return;
        }
        try (Scanner fileScanner = new Scanner(file)) {
            boolean hasEntries = false;
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    double amount = Double.parseDouble(parts[4]);
                    boolean show = false;
                    switch (type) {
                        case ALL:
                            show = true;
                            break;
                        case DEPOSIT:
                            show = amount > 0;
                            break;
                        case PAYMENT:
                            show = amount < 0;
                            break;
                    }
                    if (show) {
                        String date = parts[0];
                        String time = parts[1];
                        String description = parts[2];
                        String vendor = parts[3];
                        System.out.println("Date       : " + date);
                        System.out.println("Time       : " + time);
                        System.out.println("Description: " + description);
                        System.out.println("Vendor     : " + vendor);
                        System.out.printf("Amount     : $%.2f%s\n", amount, type == EntryType.ALL ? (amount >= 0 ? " (Deposit)" : " (Payment)") : "");
                        System.out.println("---------------------------");
                        hasEntries = true;
                    }
                }
            }
            if (!hasEntries) {
                switch (type) {
                    case ALL:
                        System.out.println("No entries found in the ledger.");
                        break;
                    case DEPOSIT:
                        System.out.println("No deposit entries found.");
                        break;
                    case PAYMENT:
                        System.out.println("No payment entries found.");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
} 