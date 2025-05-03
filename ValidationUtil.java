import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ValidationUtil {

    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Name cannot be empty.");
            return false;
        }
        if (name.length() > 100) {
            System.out.println("Name cannot exceed 100 characters.");
            return false;
        }
        if (!name.matches("[A-Za-z ]+")) {
            System.out.println("Invalid name. Numbers and special characters are not allowed.");
            return false;
        }
        return true;
    }

    public static boolean isValidAge(int age) {
        if (age <= 0 || age > 120) {
            System.out.println("Invalid age. Please enter a valid age between 1 and 120.");
            return false;
        }
        return true;
    }

    public static boolean isValidGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) {
            System.out.println("Gender cannot be empty.");
            return false;
        }
        if (!gender.equalsIgnoreCase("Male") &&
                !gender.equalsIgnoreCase("Female") &&
                !gender.equalsIgnoreCase("Other")) {
            System.out.println("Invalid gender. Must be Male, Female, or Other.");
            return false;
        }
        return true;
    }

    // Validate Phone Number (must start with 6-9 and be exactly 10 digits)
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            System.out.println("Phone number cannot be empty.");
            return false;
        }
        if (!phoneNumber.matches("^[6-9]\\d{9}$")) {
            System.out.println("Invalid phone number. It must start with 6-9 and be exactly 10 digits.");
            return false;
        }
        return true;
    }

    public static boolean isValidAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            System.out.println("Address cannot be empty.");
            return false;
        }
        if (address.length() > 255) {
            System.out.println("Address cannot exceed 255 characters.");
            return false;
        }
        return true;
    }

    public static boolean isValidDoctorName(String doctorName) {
        if (doctorName == null || doctorName.trim().isEmpty()) {
            System.out.println("Doctor name cannot be empty.");
            return false;
        }
        if (doctorName.length() > 100) {
            System.out.println("Doctor name cannot exceed 100 characters.");
            return false;
        }
        if (!doctorName.matches("[A-Za-z ]+")) {
            System.out.println("Invalid doctor name. Numbers and special characters are not allowed.");
            return false;
        }
        return true;
    }

    public static boolean isValidQualifications(String qualifications) {
        if (qualifications == null || qualifications.trim().isEmpty()) {
            System.out.println("Qualifications cannot be empty.");
            return false;
        }
        if (qualifications.length() > 255) {
            System.out.println("Qualifications cannot exceed 255 characters.");
            return false;
        }
        return true;
    }

    public static boolean isValidSpecialization(String specialization) {
        if (specialization == null || specialization.trim().isEmpty()) {
            System.out.println("Specialization cannot be empty.");
            return false;
        }
        if (specialization.length() > 100) {
            System.out.println("Specialization cannot exceed 100 characters.");
            return false;
        }
        return true;
    }

    public static boolean isValidDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            System.out.println("Date cannot be empty.");
            return false;
        }
        try {
            java.sql.Date.valueOf(date);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Use yyyy-mm-dd.");
            return false;
        }
        return true;
    }

    public static boolean isValidTime(String time) {
        if (time == null || time.trim().isEmpty()) {
            System.out.println("Time cannot be empty.");
            return false;
        }
        if (!time.matches("^([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$")) {
            System.out.println("Invalid time format. Use HH:MM:SS.");
            return false;
        }
        return true;
    }

    public static boolean isValidPatientId(int patientId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM patients WHERE patient_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true; // Patient ID exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Patient ID does not exist.");
        return false;
    }

    public static boolean isValidDoctorId(int doctorId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM doctors WHERE doctor_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true; // Doctor ID exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Doctor ID does not exist.");
        return false;
    }

    public static boolean isValidRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            System.out.println("Role cannot be empty.");
            return false;
        }
        if (!role.matches("[A-Za-z ]+")) {
            System.out.println("Invalid role. Only alphabets and spaces are allowed.");
            return false;
        }
        return true;
    }

    public static boolean isValidSalary(double salary) {
        if (salary <= 0) {
            System.out.println("Salary must be greater than 0.");
            return false;
        }
        return true;
    }

    public static boolean validatePatientDetails(String name, int age, String gender, String contact, String address) {
        return isValidName(name) &&
                isValidAge(age) &&
                isValidGender(gender) &&
                isValidPhoneNumber(contact) &&
                isValidAddress(address);
    }

    public static boolean validateDoctorRegistration(String name, String qualifications, String specialization, String gender, String phoneNumber, String hireDate) {
        return isValidDoctorName(name) &&
                isValidQualifications(qualifications) &&
                isValidSpecialization(specialization) &&
                isValidGender(gender) &&
                isValidPhoneNumber(phoneNumber) &&
                isValidDate(hireDate);
    }

    public static boolean isValidAppointmentStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            System.out.println("Status cannot be empty.");
            return false;
        }
        if (!status.equalsIgnoreCase("Scheduled") &&
                !status.equalsIgnoreCase("Completed") &&
                !status.equalsIgnoreCase("Cancelled")) {
            System.out.println("Invalid status. Must be 'Scheduled', 'Completed', 'Cancelled' ");
            return false;
        }
        return true;
    }

    public static boolean validateAppointmentDetails(int patientId, String doctorName, String appointmentDate, String appointmentTime, String status) {
        return isValidPatientId(patientId) &&
                isValidDoctorName(doctorName) &&
                isValidDate(appointmentDate) &&
                isValidTime(appointmentTime) &&
                isValidAppointmentStatus(status);
    }

    public static boolean isValidPaymentStatus(String status) {
        return status.equalsIgnoreCase("Paid") || status.equalsIgnoreCase("Unpaid");
    }


    public static boolean validateInventoryDetails(String supplyName, int quantity, double price, String expirationDate) {
        if (supplyName == null || supplyName.trim().isEmpty()) {
            System.out.println("Supply Name cannot be empty.");
            return false;
        }
        if (quantity <= 0) {
            System.out.println("Quantity must be a positive number.");
            return false;
        }
        if (price <= 0) {
            System.out.println("Price must be greater than 0.");
            return false;
        }
        if (!isValidDate(expirationDate)) {
            System.out.println("Invalid expiration date format. Please use YYYY-MM-DD.");
            return false;
        }
        return true;
    }

    public static boolean isValidDiagnosisOrTreatment(String input) {
        // Check if the input is not null or empty
        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        // Optionally, check the length of the input (e.g., between 5 and 500 characters)
        if (input.length() < 5 || input.length() > 500) {
            return false;
        }

        // Optionally, add regex to ensure the input does not contain invalid characters (e.g., special characters)
        // This example checks for basic alphanumeric characters and spaces.
        if (!input.matches("[a-zA-Z0-9\\s,.;!?-]+")) {
            return false;
        }

        return true;
    }
}
