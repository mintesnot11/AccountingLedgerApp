import java.io.File;
import java.io.FileNotFoundException;
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

    //home screen
    static void homescreen() {
        while (true) {
            System.out.println("\n=== Online Store ===");
            System.out.println("D. Add Deposit");
            System.out.println("P. Make Payment (Debit)");
            System.out.println("L. Ledger");
            System.out.println("X. Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "D":
                    addDeposit();
                    break;
                case "P":
                    makePayment();
                    break;
                case "L":
                    ledgerHomescreen();
                    break;
                case "X":
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

        // Write to CSV
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

    static void ledgerHomescreen() {
        while (true) {
            System.out.println("\n=== Ledger Home ===");
            System.out.println("A. Show all Entries");
            System.out.println("D. Deposits");
            System.out.println("P. Payments");
            System.out.println("R. Reports");
            System.out.println("H. Back");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "A":
                    allLedger();
                    break;
                case "D":
                    depositsledger();
                    break;
                case "P":
                    paymentsLedger();
                    break;
                case "R":
                    reportsLedger();
                    break;
                case "H":
                    return; // Exit ledger menu
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    static void allLedger() {
        System.out.println("\n--- All Ledger Entries (Deposits + Payments) ---");

        File file = new File(fileName);

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
                    String date = parts[0];
                    String time = parts[1];
                    String description = parts[2];
                    String vendor = parts[3];
                    double amount = Double.parseDouble(parts[4]);

                    System.out.println("Date       : " + date);
                    System.out.println("Time       : " + time);
                    System.out.println("Description: " + description);
                    System.out.println("Vendor     : " + vendor);
                    System.out.printf("Amount     : $%.2f (%s)\n", amount, amount >= 0 ? "Deposit" : "Payment");
                    System.out.println("---------------------------");

                    hasEntries = true;
                }
            }

            if (!hasEntries) {
                System.out.println("No entries found in the ledger.");
            }

        } catch (FileNotFoundException | NumberFormatException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    static void depositsledger() {
        System.out.println("\n--- Deposits Only ---");

        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("No transactions found. File does not exist.");
            return;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            boolean hasDeposits = false;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|");

                if (parts.length == 5) {
                    double amount = Double.parseDouble(parts[4]);

                    if (amount > 0) { // Only show deposits
                        String date = parts[0];
                        String time = parts[1];
                        String description = parts[2];
                        String vendor = parts[3];

                        System.out.println("Date       : " + date);
                        System.out.println("Time       : " + time);
                        System.out.println("Description: " + description);
                        System.out.println("Vendor     : " + vendor);
                        System.out.printf("Amount     : $%.2f\n", amount);
                        System.out.println("---------------------------");

                        hasDeposits = true;
                    }
                }
            }

            if (!hasDeposits) {
                System.out.println("No deposit entries found.");
            }

        } catch (FileNotFoundException | NumberFormatException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    static void paymentsLedger() {
        System.out.println("\n--- Payments Only ---");

        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("No transactions found. File does not exist.");
            return;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            boolean hasPayments = false;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|");

                if (parts.length == 5) {
                    double amount = Double.parseDouble(parts[4]);

                    if (amount < 0) { // Only show payments
                        String date = parts[0];
                        String time = parts[1];
                        String description = parts[2];
                        String vendor = parts[3];

                        System.out.println("Date       : " + date);
                        System.out.println("Time       : " + time);
                        System.out.println("Description: " + description);
                        System.out.println("Vendor     : " + vendor);
                        System.out.printf("Amount     : $%.2f\n", amount);
                        System.out.println("---------------------------");

                        hasPayments = true;
                    }
                }
            }

            if (!hasPayments) {
                System.out.println("No payment entries found.");
            }

        } catch (FileNotFoundException | NumberFormatException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    static void reportsLedger() {
        while (true) {
            System.out.println("\n==== Reports Menu ====");
            System.out.println("1) Month to Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year to Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back - return to Ledger Menu");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine().trim().toUpperCase();

            switch (input) {
                case "1":
                    filterByDateRange(getStartOfCurrentMonth(), new Date(), "Month to Date");
                    break;
                case "2":
                    filterByDateRange(getStartOfPreviousMonth(), getEndOfPreviousMonth(), "Previous Month");
                    break;
                case "3":
                    filterByDateRange(getStartOfCurrentYear(), new Date(), "Year to Date");
                    break;
                case "4":
                    filterByDateRange(getStartOfPreviousYear(), getEndOfPreviousYear(), "Previous Year");
                    break;
                case "5":
                    System.out.print("Enter vendor name to search: ");
                    String vendor = scanner.nextLine().trim().toLowerCase();
                    searchByVendor(vendor);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    static Date getStartOfCurrentMonth() {
        Date now = new Date();
        return new Date(now.getYear(), now.getMonth(), 1);
    }

    static Date getStartOfPreviousMonth() {
        Date now = new Date();
        int year = now.getYear();
        int month = now.getMonth() - 1;
        if (month < 0) {
            month = 11;
            year -= 1;
        }
        return new Date(year, month, 1);
    }

    static Date getEndOfPreviousMonth() {
        Date start = getStartOfCurrentMonth();
        return new Date(start.getTime() - 24 * 60 * 60 * 1000); // 1 day before current month start
    }

    static Date getStartOfCurrentYear() {
        Date now = new Date();
        return new Date(now.getYear(), 0, 1);
    }

    static Date getStartOfPreviousYear() {
        Date now = new Date();
        return new Date(now.getYear() - 1, 0, 1);
    }

    static Date getEndOfPreviousYear() {
        return new Date(getStartOfCurrentYear().getTime() - 24 * 60 * 60 * 1000);
    }

    static void filterByDateRange(Date start, Date end, String title) {
        System.out.println("\n--- " + title + " ---");

        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("No transactions found.");
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try (Scanner fileScanner = new Scanner(file)) {
            boolean found = false;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|");

                if (parts.length == 5) {
                    Date transactionDate = dateFormat.parse(parts[0]);
                    if (!transactionDate.before(start) && !transactionDate.after(end)) {
                        printTransaction(parts);
                        found = true;
                    }
                }
            }

            if (!found) {
                System.out.println("No entries found in this range.");
            }

        } catch (Exception e) {
            System.out.println("Error filtering: " + e.getMessage());
        }
    }

    static void searchByVendor(String vendorName) {
        System.out.println("\n--- Search by Vendor: " + vendorName + " ---");

        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("No transactions found.");
            return;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            boolean found = false;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|");

                if (parts.length == 5) {
                    if (parts[3].toLowerCase().contains(vendorName)) {
                        printTransaction(parts);
                        found = true;
                    }
                }
            }

            if (!found) {
                System.out.println("No matching vendor entries found.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void printTransaction(String[] parts) {
        System.out.println("Date       : " + parts[0]);
        System.out.println("Time       : " + parts[1]);
        System.out.println("Description: " + parts[2]);
        System.out.println("Vendor     : " + parts[3]);
        double amount = Double.parseDouble(parts[4]);
        System.out.printf("Amount     : $%.2f (%s)\n", amount, amount >= 0 ? "Deposit" : "Payment");
        System.out.println("---------------------------");
    }
}