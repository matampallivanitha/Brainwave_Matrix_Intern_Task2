import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class EHRManager {

    private Scanner scanner = new Scanner(System.in);

    // Method to add a medical record
    public void addRecord() {
        System.out.println("\n--- Add Medical Record ---");

        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        scanner.nextLine(); // consume leftover newline

        if (!ValidationUtil.isValidPatientId(patientId)) {
            System.out.println("Invalid Patient ID. Please try again.");
            return;
        }

        System.out.print("Enter Diagnosis: ");
        String diagnosis = scanner.nextLine();
        if (!ValidationUtil.isValidDiagnosisOrTreatment(diagnosis)) {
            System.out.println("Invalid Diagnosis. Please try again.");
            return;
        }

        System.out.print("Enter Treatment: ");
        String treatment = scanner.nextLine();
        if (!ValidationUtil.isValidDiagnosisOrTreatment(treatment)) {
            System.out.println("Invalid Treatment. Please try again.");
            return;
        }

        System.out.print("Enter Record Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        if (!ValidationUtil.isValidDate(date)) {
            System.out.println("Invalid Date format. Please use YYYY-MM-DD.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO ehr (patient_id, diagnosis, treatment, record_date) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, patientId);
            stmt.setString(2, diagnosis);
            stmt.setString(3, treatment);
            stmt.setString(4, date);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Medical record added successfully!");
            } else {
                System.out.println("Failed to add medical record.");
            }

        } catch (SQLException e) {
            System.out.println("Error while adding medical record: " + e.getMessage());
        }
    }

    // Method to view medical records
    public void viewRecords() {
        System.out.println("\n--- View Medical Records ---");

        System.out.print("Enter Patient ID to view medical records: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();

        if (!ValidationUtil.isValidPatientId(patientId)) {
            System.out.println("Invalid Patient ID. Please try again.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM ehr WHERE patient_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, patientId);

            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No medical records found for Patient ID: " + patientId);
            } else {
                System.out.println("\n--- Medical Records for Patient ID: " + patientId + " ---");
                while (rs.next()) {
                    int recordId = rs.getInt("ehr_id");  // ✅ updated
                    String diagnosis = rs.getString("diagnosis");
                    String treatment = rs.getString("treatment");
                    String recordDate = rs.getString("record_date");

                    System.out.println("Record ID: " + recordId);
                    System.out.println("Diagnosis: " + diagnosis);
                    System.out.println("Treatment: " + treatment);
                    System.out.println("Record Date: " + recordDate);
                    System.out.println("-------------------------------");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while viewing medical records: " + e.getMessage());
        }
    }

    // Method to update an existing medical record
    public void updateRecord() {
        System.out.println("\n--- Update Medical Record ---");

        System.out.print("Enter Record ID to update: ");
        int recordId = scanner.nextInt();
        scanner.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM ehr WHERE ehr_id = ?";  // ✅ updated
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, recordId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No record found for Record ID: " + recordId);
                return;
            }

            rs.next();
            String oldDiagnosis = rs.getString("diagnosis");
            String oldTreatment = rs.getString("treatment");
            String oldRecordDate = rs.getString("record_date");

            System.out.println("Current Diagnosis: " + oldDiagnosis);
            System.out.println("Current Treatment: " + oldTreatment);
            System.out.println("Current Record Date: " + oldRecordDate);

            System.out.print("Enter new Diagnosis (leave blank to keep current): ");
            String newDiagnosis = scanner.nextLine();
            if (newDiagnosis.isEmpty()) newDiagnosis = oldDiagnosis;

            System.out.print("Enter new Treatment (leave blank to keep current): ");
            String newTreatment = scanner.nextLine();
            if (newTreatment.isEmpty()) newTreatment = oldTreatment;

            System.out.print("Enter new Record Date (leave blank to keep current): ");
            String newRecordDate = scanner.nextLine();
            if (newRecordDate.isEmpty()) newRecordDate = oldRecordDate;

            String updateSQL = "UPDATE ehr SET diagnosis = ?, treatment = ?, record_date = ? WHERE ehr_id = ?";  // ✅ updated
            PreparedStatement updateStmt = conn.prepareStatement(updateSQL);
            updateStmt.setString(1, newDiagnosis);
            updateStmt.setString(2, newTreatment);
            updateStmt.setString(3, newRecordDate);
            updateStmt.setInt(4, recordId);

            int rowsUpdated = updateStmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Medical record updated successfully!");
            } else {
                System.out.println("Failed to update medical record.");
            }

        } catch (SQLException e) {
            System.out.println("Error while updating medical record: " + e.getMessage());
        }
    }

    // View medical history
    public void viewMedicalHistory() {
        System.out.println("\n--- View Medical History ---");

        System.out.print("Enter Patient ID to view medical history: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();

        if (!ValidationUtil.isValidPatientId(patientId)) {
            System.out.println("Invalid Patient ID. Please try again.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT diagnosis, treatment, record_date FROM ehr WHERE patient_id = ? ORDER BY record_date";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, patientId);

            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No medical history found for Patient ID: " + patientId);
            } else {
                System.out.println("\n--- Medical History for Patient ID: " + patientId + " ---");
                while (rs.next()) {
                    String diagnosis = rs.getString("diagnosis");
                    String treatment = rs.getString("treatment");
                    String recordDate = rs.getString("record_date");

                    System.out.println("Diagnosis: " + diagnosis);
                    System.out.println("Treatment: " + treatment);
                    System.out.println("Record Date: " + recordDate);
                    System.out.println("-------------------------------");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while viewing medical history: " + e.getMessage());
        }
    }

    // Method to generate a report (e.g., list of all diagnoses within a time range)
    public void generateReport() {
        System.out.println("\n--- Generate Medical Report ---");

        // Get date range for the report
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String startDate = scanner.nextLine();
        if (!ValidationUtil.isValidDate(startDate)) {
            System.out.println("Invalid start date format. Please use YYYY-MM-DD.");
            return;
        }

        System.out.print("Enter end date (YYYY-MM-DD): ");
        String endDate = scanner.nextLine();
        if (!ValidationUtil.isValidDate(endDate)) {
            System.out.println("Invalid end date format. Please use YYYY-MM-DD.");
            return;
        }

        // Fetch and display the report
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT diagnosis, COUNT(*) as diagnosis_count FROM ehr " +
                    "WHERE record_date BETWEEN ? AND ? " +
                    "GROUP BY diagnosis " +
                    "ORDER BY diagnosis_count DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, startDate);
            stmt.setString(2, endDate);

            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No data found for the given date range.");
            } else {
                System.out.println("\n--- Medical Report ---");
                while (rs.next()) {
                    String diagnosis = rs.getString("diagnosis");
                    int diagnosisCount = rs.getInt("diagnosis_count");

                    System.out.println("Diagnosis: " + diagnosis);
                    System.out.println("Diagnosis Count: " + diagnosisCount);
                    System.out.println("-------------------------------");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while generating report: " + e.getMessage());
        }
    }

}
