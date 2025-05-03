import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class DoctorRegistration {

    private Scanner scanner = new Scanner(System.in);

    public void addDoctor() {
        System.out.println("\n--- Add New Doctor ---");

        // Get doctor details
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Qualifications: ");
        String qualifications = scanner.nextLine();

        System.out.print("Enter Specialization (Cardiologist, Neurologist, etc.): ");
        String specialization = scanner.nextLine();

        System.out.print("Enter Gender (Male/Female/Other): ");
        String gender = scanner.nextLine();

        System.out.print("Enter Phone Number (10 digits): ");
        String phoneNumber = scanner.nextLine();

        System.out.print("Enter Hire Date (yyyy-mm-dd): ");
        String hireDate = scanner.nextLine();

        // Validate doctor details
        if (!ValidationUtil.validateDoctorRegistration(name, qualifications, specialization, gender, phoneNumber, hireDate)) {
            System.out.println("Invalid input. Doctor not added.");
            return; // Stop execution if validation fails
        }

        // If all validations pass, proceed with adding the doctor to the database
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO doctors (name, qualifications, specialization, phone_number, gender, hire_date, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, qualifications);
            stmt.setString(3, specialization);
            stmt.setString(4, phoneNumber);
            stmt.setString(5, gender);
            stmt.setString(6, hireDate);
            stmt.setString(7, "Active"); // Default status

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Doctor added successfully!");
            } else {
                System.out.println("Failed to add doctor.");
            }

        } catch (SQLException e) {
            System.out.println("Error while adding doctor: " + e.getMessage());
        }
    }

    public void viewDoctors() {
        System.out.println("\n--- View All Doctors ---");

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM doctors";
            PreparedStatement stmt = conn.prepareStatement(sql);
            var rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No doctors found.");
            } else {
                while (rs.next()) {
                    int id = rs.getInt("doctor_id");
                    String name = rs.getString("name");
                    String qualifications = rs.getString("qualifications");
                    String specialization = rs.getString("specialization");
                    String phoneNumber = rs.getString("phone_number");
                    String gender = rs.getString("gender");
                    String hireDate = rs.getString("hire_date");
                    String status = rs.getString("status");

                    System.out.println("ID: " + id);
                    System.out.println("Name: " + name);
                    System.out.println("Qualifications: " + qualifications);
                    System.out.println("Specialization: " + specialization);
                    System.out.println("Phone: " + phoneNumber);
                    System.out.println("Gender: " + gender);
                    System.out.println("Hire Date: " + hireDate);
                    System.out.println("Status: " + status);
                    System.out.println("------------------------------");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while viewing doctors: " + e.getMessage());
        }
    }

    public void updateDoctor() {
        System.out.println("\n--- Update Doctor Details ---");

        System.out.print("Enter Doctor ID to update: ");
        int doctorId = scanner.nextInt();
        scanner.nextLine();

        if (!ValidationUtil.isValidDoctorId(doctorId)) {
            System.out.println("Invalid Doctor ID.");
            return;
        }

        System.out.print("Enter new Name: ");
        String name = scanner.nextLine();
        if (!ValidationUtil.isValidName(name)) {
            System.out.println("Invalid name.");
            return;
        }

        System.out.print("Enter new Qualifications: ");
        String qualifications = scanner.nextLine();
        if (!ValidationUtil.isValidQualifications(qualifications)) {
            System.out.println("Invalid qualifications.");
            return;
        }

        System.out.print("Enter new Specialization: ");
        String specialization = scanner.nextLine();
        if (!ValidationUtil.isValidSpecialization(specialization)) {
            System.out.println("Invalid specialization.");
            return;
        }

        System.out.print("Enter new Phone Number: ");
        String phoneNumber = scanner.nextLine();
        if (!ValidationUtil.isValidPhoneNumber(phoneNumber)) {
            System.out.println("Invalid phone number.");
            return;
        }

        System.out.print("Enter new Gender: ");
        String gender = scanner.nextLine();
        if (!ValidationUtil.isValidGender(gender)) {
            System.out.println("Invalid gender.");
            return;
        }

        System.out.print("Enter new Hire Date (yyyy-mm-dd): ");
        String hireDate = scanner.nextLine();

        // Update DB
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE doctors SET name = ?, qualifications = ?, specialization = ?, phone_number = ?, " +
                    "gender = ?, hire_date = ? WHERE doctor_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, qualifications);
            stmt.setString(3, specialization);
            stmt.setString(4, phoneNumber);
            stmt.setString(5, gender);
            stmt.setString(6, hireDate);
            stmt.setInt(7, doctorId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Doctor updated successfully.");
            } else {
                System.out.println("Doctor not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating doctor: " + e.getMessage());
        }
    }

    public void deleteDoctor() {
        System.out.println("\n--- Delete Doctor ---");
        System.out.print("Enter Doctor ID to delete: ");
        int doctorId = scanner.nextInt();
        scanner.nextLine();

        if (!ValidationUtil.isValidDoctorId(doctorId)) {
            System.out.println("Invalid Doctor ID.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM doctors WHERE doctor_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, doctorId);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Doctor deleted successfully.");
            } else {
                System.out.println("Doctor not found or deletion failed.");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting doctor: " + e.getMessage());
        }
    }
}
