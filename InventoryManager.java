import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InventoryManager {

    private Scanner scanner = new Scanner(System.in);

    public void addSupply() {
        System.out.println("\n--- Add New Supply to Inventory ---");

        System.out.print("Enter Supply Name: ");
        String supplyName = scanner.nextLine();

        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Price per Unit: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter Expiration Date (YYYY-MM-DD): ");
        String expirationDate = scanner.nextLine();

        LocalDate expDate;
        try {
            expDate = LocalDate.parse(expirationDate);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please enter in YYYY-MM-DD.");
            return;
        }

        if (expDate.isBefore(LocalDate.now())) {
            System.out.println("Expiration date must be today or later.");
            return;
        }

        // Validate other input (optional if already covered elsewhere)
        if (!ValidationUtil.validateInventoryDetails(supplyName, quantity, price, expirationDate)) {
            System.out.println("Invalid input. Please check the details and try again.");
            return;
        }

        // Insert into database
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO inventory (supply_name, quantity, price, expiration_date) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, supplyName);
            stmt.setInt(2, quantity);
            stmt.setDouble(3, price);
            stmt.setDate(4, java.sql.Date.valueOf(expDate)); // Use parsed LocalDate

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Supply added to inventory successfully!");
            } else {
                System.out.println("Failed to add supply to inventory.");
            }

        } catch (SQLException e) {
            System.out.println("Error while adding supply to inventory: " + e.getMessage());
        }
    }


    public void viewInventory() {
        System.out.println("\n--- View Inventory ---");

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM inventory";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No supplies found in the inventory.");
            } else {
                while (rs.next()) {
                    int inventoryId = rs.getInt("inventory_id");
                    String supplyName = rs.getString("supply_name");
                    int quantity = rs.getInt("quantity");
                    double price = rs.getDouble("price");
                    String expirationDate = rs.getString("expiration_date");

                    System.out.println("Inventory ID: " + inventoryId);
                    System.out.println("Supply Name: " + supplyName);
                    System.out.println("Quantity: " + quantity);
                    System.out.println("Price per Unit: ₹" + price);
                    System.out.println("Expiration Date: " + expirationDate);
                    System.out.println("--------------------------------------");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while viewing inventory: " + e.getMessage());
        }
    }
}
