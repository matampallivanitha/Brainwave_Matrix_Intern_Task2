import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class AppointmentScheduler {

    private Scanner scanner = new Scanner(System.in);

    // Schedule a new appointment
    public void scheduleAppointment() {
        System.out.println("\n--- Schedule New Appointment ---");

        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter Doctor Name: ");
        String doctorName = scanner.nextLine();

        System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        // Validate date format and check if it's today or later
        LocalDate appointmentDate;
        try {
            appointmentDate = LocalDate.parse(date);
            if (appointmentDate.isBefore(LocalDate.now())) {
                System.out.println("Appointment date cannot be in the past.");
                return;
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            return;
        }

        System.out.print("Enter Appointment Time (HH:MM:SS): ");
        String time = scanner.nextLine();

        System.out.print("Enter Status (Scheduled/Completed/Cancelled): ");
        String status = scanner.nextLine();

        if (!ValidationUtil.validateAppointmentDetails(patientId, doctorName, date, time, status)) {
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO appointments (patient_id, doctor_name, appointment_date, appointment_time, status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, patientId);
            stmt.setString(2, doctorName);
            stmt.setString(3, date);
            stmt.setString(4, time);
            stmt.setString(5, status);

            int rowsInserted = stmt.executeUpdate();
            System.out.println(rowsInserted > 0 ? "Appointment scheduled successfully!" : "Failed to schedule appointment.");

        } catch (SQLException e) {
            System.out.println("Error while scheduling appointment: " + e.getMessage());
        }
    }

    public void viewAppointments() {
        System.out.println("\n--- View All Appointments ---");

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT a.*, p.name AS patient_name FROM appointments a JOIN patients p ON a.patient_id = p.patient_id";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No appointments found.");
            } else {
                while (rs.next()) {
                    System.out.printf("Appointment ID: %d\nPatient Name: %s\nDoctor Name: %s\nDate: %s\nTime: %s\nStatus: %s\n",
                            rs.getInt("appointment_id"), rs.getString("patient_name"), rs.getString("doctor_name"),
                            rs.getString("appointment_date"), rs.getString("appointment_time"), rs.getString("status"));
                    System.out.println("--------------------------------------");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while fetching appointments: " + e.getMessage());
        }
    }

    public void cancelAppointment() {
        System.out.println("\n--- Cancel Appointment ---");

        System.out.print("Enter Appointment ID: ");
        int appointmentId = scanner.nextInt();
        scanner.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE appointments SET status = 'Cancelled' WHERE appointment_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, appointmentId);

            int rowsUpdated = stmt.executeUpdate();
            System.out.println(rowsUpdated > 0 ? "Appointment cancelled successfully." : "Appointment not found.");

        } catch (SQLException e) {
            System.out.println("Error while cancelling appointment: " + e.getMessage());
        }
    }

    public void rescheduleAppointment() {
        System.out.println("\n--- Reschedule Appointment ---");

        System.out.print("Enter Appointment ID: ");
        int appointmentId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter New Appointment Date (YYYY-MM-DD): ");
        String newDate = scanner.nextLine();

        try {
            LocalDate parsedDate = LocalDate.parse(newDate);
            if (parsedDate.isBefore(LocalDate.now())) {
                System.out.println("New appointment date cannot be in the past.");
                return;
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Use YYYY-MM-DD.");
            return;
        }

        System.out.print("Enter New Appointment Time (HH:MM:SS): ");
        String newTime = scanner.nextLine();

        if (!ValidationUtil.isValidDate(newDate) || !ValidationUtil.isValidTime(newTime)) {
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE appointments SET appointment_date = ?, appointment_time = ? WHERE appointment_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newDate);
            stmt.setString(2, newTime);
            stmt.setInt(3, appointmentId);

            int rowsUpdated = stmt.executeUpdate();
            System.out.println(rowsUpdated > 0 ? "Appointment rescheduled successfully." : "Appointment not found.");

        } catch (SQLException e) {
            System.out.println("Error while rescheduling appointment: " + e.getMessage());
        }
    }

    public void viewAppointmentHistory(int patientId) {
        System.out.println("\n--- View Appointment History ---");

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT a.*, p.name AS patient_name FROM appointments a JOIN patients p ON a.patient_id = p.patient_id " +
                    "WHERE a.patient_id = ? AND (a.status = 'Completed' OR a.status = 'Cancelled') ORDER BY a.appointment_date DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No past appointments found.");
            } else {
                while (rs.next()) {
                    System.out.printf("Appointment ID: %d\nPatient Name: %s\nDoctor Name: %s\nDate: %s\nTime: %s\nStatus: %s\n",
                            rs.getInt("appointment_id"), rs.getString("patient_name"), rs.getString("doctor_name"),
                            rs.getString("appointment_date"), rs.getString("appointment_time"), rs.getString("status"));
                    System.out.println("--------------------------------------");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while fetching appointment history: " + e.getMessage());
        }
    }
}
