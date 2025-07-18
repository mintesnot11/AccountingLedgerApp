import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.LinkedHashMap;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static final String fileName = "transactions.csv";

    public static void main(String[] args) {
        homescreen();
    }

    //home screen
    static void homescreen() {
        LinkedHashMap<String, Menu.MenuOption> options = new LinkedHashMap<>();
        options.put("D", new Menu.MenuOption("Add Deposit", () -> { addDeposit(); return false; }));
        options.put("P", new Menu.MenuOption("Make Payment (Debit)", () -> { makePayment(); return false; }));
        options.put("L", new Menu.MenuOption("Ledger", () -> { ledgerHomescreen(); return false; }));
        options.put("X", new Menu.MenuOption("Exit", () -> { System.out.println("Thanks for visiting!"); return true; }));
        Menu.display("Online Store", options);
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
        LinkedHashMap<String, Menu.MenuOption> options = new LinkedHashMap<>();
        options.put("A", new Menu.MenuOption("Show all Entries", () -> { Ledger.showAll(); return false; }));
        options.put("D", new Menu.MenuOption("Deposits", () -> { Ledger.showDeposits(); return false; }));
        options.put("P", new Menu.MenuOption("Payments", () -> { Ledger.showPayments(); return false; }));
        options.put("R", new Menu.MenuOption("Reports", () -> { reportsLedger(); return false; }));
        options.put("H", new Menu.MenuOption("Back", () -> true));
        Menu.display("Ledger Home", options);
    }

    static void reportsLedger() {
        LinkedHashMap<String, Menu.MenuOption> options = new LinkedHashMap<>();
        options.put("1", new Menu.MenuOption("Month to Date", () -> { filterByDateRange(getStartOfCurrentMonth(), new Date(), "Month to Date"); return false; }));
        options.put("2", new Menu.MenuOption("Previous Month", () -> { filterByDateRange(getStartOfPreviousMonth(), getEndOfPreviousMonth(), "Previous Month"); return false; }));
        options.put("3", new Menu.MenuOption("Year to Date", () -> { filterByDateRange(getStartOfCurrentYear(), new Date(), "Year to Date"); return false; }));
        options.put("4", new Menu.MenuOption("Previous Year", () -> { filterByDateRange(getStartOfPreviousYear(), getEndOfPreviousYear(), "Previous Year"); return false; }));
        options.put("5", new Menu.MenuOption("Search by Vendor", () -> { System.out.print("Enter vendor name to search: "); String vendor = scanner.nextLine().trim().toLowerCase(); searchByVendor(vendor); return false; }));
        options.put("0", new Menu.MenuOption("Back - return to Ledger Menu", () -> true));
        Menu.display("Reports Menu", options);
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
        return new Date(start.getTime() - 24 * 60 * 60 * 1000); // 1 day before current month starts
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