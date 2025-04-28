import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static final String fileName = "transactions.csv";

    public static void main(String[] args) {
        homescreen();
    }

    static void homescreen() {
        while (true) {
            System.out.println("\n=== Online Store ===");
            System.out.println("1. Add Deposit");
            System.out.println("2. Make Payment (Debit)");
            System.out.println("3. Ledger");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addDeposit();
                    break;
                case "2":
                    makePayment();
                    break;
                case "3":
                    ledger();
                    break;
                case "4":
                    System.out.println("Thanks for visiting!");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    static void addDeposit() {
        System.out.println("\n--- Add Deposit ---");

        System.out.print("Description: ");
        String description = scanner.nextLine().trim();

        System.out.print("Vendor: ");
        String vendor = scanner.nextLine().trim();

        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        // Get current date and time
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());

        // Write to CSV in pipe-separated format
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(date + "|" + time + "|" + description + "|" + vendor + "|" + amount + "\n");
            System.out.println("Deposit saved successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    static void makePayment() {
        System.out.println("\n--- Make Payment ---");

        System.out.print("Description: ");
        String description = scanner.nextLine().trim();

        System.out.print("Vendor: ");
        String vendor = scanner.nextLine().trim();

        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        // Payments are negative
        amount = -Math.abs(amount);

        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());

        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(date + "|" + time + "|" + description + "|" + vendor + "|" + amount + "\n");
            System.out.println("Payment saved successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    static void ledger() {
        // Placeholder
        System.out.println("Ledger - feature not implemented yet.");
    }
}