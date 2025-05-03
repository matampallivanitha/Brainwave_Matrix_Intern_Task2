import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class PatientRegistration {

    private Scanner scanner = new Scanner(System.in);

    public void addPatient() {
        System.out.println("\n--- Add New Patient ---");

        // Get patient details
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Age: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Gender (Male/Female/Other): ");
        String gender = scanner.nextLine();

        System.out.print("Enter Phone Number: ");
        String phoneNumber = scanner.nextLine();

        System.out.print("Enter Address: ");
        String address = scanner.nextLine();

        // Validate patient details
        if (!ValidationUtil.validatePatientDetails(name, age, gender, phoneNumber, address)) {
            System.out.println("Invalid input. Patient not added.");
            return; // Stop execution if validation fails
        }

        // If all validations pass, proceed with adding the patient to the database
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO patients (name, age, gender, phone_number, address) VALUES (?, ?, ?, ?, ?)";  // Updated query
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, gender);
            stmt.setString(4, phoneNumber);  // Updated field name
            stmt.setString(5, address);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Patient added successfully!");
            } else {
                System.out.println("Failed to add patient.");
            }

        } catch (SQLException e) {
            System.out.println("Error while adding patient: " + e.getMessage());
        }
    }

    public void viewPatients() {
        System.out.println("\n--- View All Patients ---");

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM patients";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No patients found.");
            } else {
                while (rs.next()) {
                    int id = rs.getInt("patient_id");
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    String gender = rs.getString("gender");
                    String phoneNumber = rs.getString("phone_number");  // Updated field name
                    String address = rs.getString("address");

                    System.out.println("ID: " + id);
                    System.out.println("Name: " + name);
                    System.out.println("Age: " + age);
                    System.out.println("Gender: " + gender);
                    System.out.println("Phone Number: " + phoneNumber);  // Updated field name
                    System.out.println("Address: " + address);
                    System.out.println("------------------------------");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while viewing patients: " + e.getMessage());
        }
    }

    public void updatePatient() {
        System.out.println("\n--- Update Patient Details ---");

        System.out.print("Enter Patient ID to update: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();

        if (!ValidationUtil.isValidPatientId(patientId)) {
            System.out.println("Invalid Patient ID.");
            return;
        }

        System.out.print("Enter new Patient Name: ");
        String name = scanner.nextLine();
        if (!ValidationUtil.isValidName(name)) {
            System.out.println("Invalid name.");
            return;
        }

        System.out.print("Enter new Age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        if (!ValidationUtil.isValidAge(age)) {
            System.out.println("Invalid age.");
            return;
        }

        System.out.print("Enter new Gender (Male/Female/Other): ");
        String gender = scanner.nextLine();
        if (!ValidationUtil.isValidGender(gender)) {
            System.out.println("Invalid gender.");
            return;
        }

        System.out.print("Enter new Phone Number: ");
        String phoneNumber = scanner.nextLine();  // Updated field name
        if (!ValidationUtil.isValidPhoneNumber(phoneNumber)) {
            System.out.println("Invalid phone number.");
            return;
        }

        System.out.print("Enter new Address: ");
        String address = scanner.nextLine();
        if (!ValidationUtil.isValidAddress(address)) {
            System.out.println("Invalid address.");
            return;
        }

        // Update DB
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE patients SET name = ?, age = ?, gender = ?, phone_number = ?, address = ? WHERE patient_id = ?";  // Updated query
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, gender);
            stmt.setString(4, phoneNumber);  // Updated field name
            stmt.setString(5, address);
            stmt.setInt(6, patientId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Patient updated successfully.");
            } else {
                System.out.println("Patient not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating patient: " + e.getMessage());
        }
    }
}
