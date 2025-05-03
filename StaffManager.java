import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class StaffManager {

    private Scanner scanner = new Scanner(System.in);

    public void addStaff() {
        System.out.println("\n--- Add New Staff ---");

        System.out.print("Enter Staff Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Role: ");
        String role = scanner.nextLine();

        System.out.print("Enter Phone Number: ");
        String phoneNumber = scanner.nextLine();

        System.out.print("Enter Salary: ");
        double salary = scanner.nextDouble();
        scanner.nextLine(); // consume leftover newline

        System.out.print("Enter Hire Date (YYYY-MM-DD): ");
        String hireDate = scanner.nextLine();

        // Validate input
        if (!ValidationUtil.isValidName(name) || !ValidationUtil.isValidRole(role)
                || !ValidationUtil.isValidPhoneNumber(phoneNumber) || !ValidationUtil.isValidSalary(salary)
                || !ValidationUtil.isValidDate(hireDate)) {
            System.out.println("Invalid input data. Please check the details and try again.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO staff (name, role, phone_number, salary, hire_date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, role);
            stmt.setString(3, phoneNumber);
            stmt.setDouble(4, salary);
            stmt.setString(5, hireDate);

            int rowsInserted = stmt.executeUpdate();
            System.out.println(rowsInserted > 0 ? "Staff added successfully!" : "Failed to add staff.");

        } catch (SQLException e) {
            System.out.println("Error while adding staff: " + e.getMessage());
        }
    }

    public void viewStaff() {
        System.out.println("\n--- View Staff ---");

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM staff";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No staff found.");
            } else {
                while (rs.next()) {
                    int staffId = rs.getInt("staff_id");
                    String name = rs.getString("name");
                    String role = rs.getString("role");
                    String phoneNumber = rs.getString("phone_number");
                    double salary = rs.getDouble("salary");
                    String hireDate = rs.getString("hire_date");

                    System.out.println("Staff ID: " + staffId);
                    System.out.println("Name: " + name);
                    System.out.println("Role: " + role);
                    System.out.println("Phone Number: " + phoneNumber);
                    System.out.println("Salary: ₹" + salary);
                    System.out.println("Hire Date: " + hireDate);
                    System.out.println("--------------------------------------");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while viewing staff: " + e.getMessage());
        }
    }

    public void editStaff() {
        System.out.println("\n--- Edit Staff ---");

        System.out.print("Enter Staff ID to edit: ");
        int staffId = scanner.nextInt();
        scanner.nextLine(); // consume leftover newline

        System.out.print("Enter new Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter new Role: ");
        String role = scanner.nextLine();

        System.out.print("Enter new Phone Number (10 digits): ");
        String phoneNumber = scanner.nextLine();

        System.out.print("Enter new Salary: ");
        double salary = scanner.nextDouble();
        scanner.nextLine(); // consume leftover newline

        System.out.print("Enter new Hire Date (YYYY-MM-DD): ");
        String hireDate = scanner.nextLine();

        if (!ValidationUtil.isValidName(name) || !ValidationUtil.isValidRole(role)
                || !ValidationUtil.isValidPhoneNumber(phoneNumber) || !ValidationUtil.isValidSalary(salary)
                || !ValidationUtil.isValidDate(hireDate)) {
            System.out.println("Invalid input data. Please check the details and try again.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE staff SET name = ?, role = ?, phone_number = ?, salary = ?, hire_date = ? WHERE staff_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, role);
            stmt.setString(3, phoneNumber);
            stmt.setDouble(4, salary);
            stmt.setString(5, hireDate);
            stmt.setInt(6, staffId);

            int rowsUpdated = stmt.executeUpdate();
            System.out.println(rowsUpdated > 0 ? "Staff information updated successfully!" : "Failed to update staff information.");

        } catch (SQLException e) {
            System.out.println("Error while updating staff: " + e.getMessage());
        }
    }
}
