import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class BillingManager {

    private Scanner scanner = new Scanner(System.in);

    public void addBilling() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Add Billing Information ---");

        System.out.print("Enter Patient ID: ");
        int patientId = sc.nextInt();
        sc.nextLine(); // consume newline

        System.out.print("Enter Total Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine(); // consume newline

        System.out.print("Enter Billing Date (YYYY-MM-DD): ");
        String billingDate = sc.nextLine();

        System.out.print("Enter Payment Status (Paid/Unpaid): ");
        String paymentStatus = sc.nextLine();

        if (!ValidationUtil.isValidPaymentStatus(paymentStatus)) {
            System.out.println("Invalid payment status. Please enter 'Paid' or 'Unpaid'.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO billing (patient_id, amount, billing_date, status) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, patientId);
            stmt.setDouble(2, amount);
            stmt.setString(3, billingDate);
            stmt.setString(4, paymentStatus);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Billing information added successfully.");
            } else {
                System.out.println("Failed to add billing information.");
            }
        } catch (SQLException e) {
            System.out.println("Error while adding billing information: " + e.getMessage());
        }
    }

    public void viewBilling() {
        System.out.println("\n--- View Billing Information ---");
        System.out.print("Enter Patient ID to view billing: ");
        int patientId = scanner.nextInt();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT b.*, p.name AS patient_name FROM billing b " +
                    "JOIN patients p ON b.patient_id = p.patient_id WHERE b.patient_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No billing information found for the specified Patient ID.");
            } else {
                while (rs.next()) {
                    int billId = rs.getInt("bill_id");
                    String patientName = rs.getString("patient_name");
                    double amount = rs.getDouble("amount");
                    String billingDate = rs.getString("billing_date");
                    String status = rs.getString("status");

                    System.out.println("Bill ID: " + billId);
                    System.out.println("Patient Name: " + patientName);
                    System.out.println("Amount: ₹" + amount);
                    System.out.println("Billing Date: " + billingDate);
                    System.out.println("Status: " + status);
                    System.out.println("--------------------------------------");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while viewing billing information: " + e.getMessage());
        }
    }

    public void createInvoice() {
        System.out.println("\n--- Create Invoice ---");
        System.out.print("Enter Bill ID to generate invoice: ");
        int billId = scanner.nextInt();
        scanner.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT b.*, p.name AS patient_name FROM billing b " +
                    "JOIN patients p ON b.patient_id = p.patient_id WHERE b.bill_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, billId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No billing information found for the specified Bill ID.");
            } else {
                while (rs.next()) {
                    String patientName = rs.getString("patient_name");
                    double amount = rs.getDouble("amount");
                    String billingDate = rs.getString("billing_date");
                    String status = rs.getString("status");

                    System.out.println("----- Invoice -----");
                    System.out.println("Bill ID: " + billId);
                    System.out.println("Patient Name: " + patientName);
                    System.out.println("Amount: ₹" + amount);
                    System.out.println("Billing Date: " + billingDate);
                    System.out.println("Status: " + status);
                    System.out.println("-------------------");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while creating invoice: " + e.getMessage());
        }
    }

    public void viewInvoice() {
        System.out.println("\n--- View Invoice ---");
        System.out.print("Enter Bill ID to view invoice: ");
        int billId = scanner.nextInt();
        scanner.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT b.*, p.name AS patient_name FROM billing b " +
                    "JOIN patients p ON b.patient_id = p.patient_id WHERE b.bill_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, billId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No invoice found for the specified Bill ID.");
            } else {
                while (rs.next()) {
                    String patientName = rs.getString("patient_name");
                    double amount = rs.getDouble("amount");
                    String billingDate = rs.getString("billing_date");
                    String status = rs.getString("status");

                    System.out.println("----- Invoice Details -----");
                    System.out.println("Bill ID: " + billId);
                    System.out.println("Patient Name: " + patientName);
                    System.out.println("Amount: ₹" + amount);
                    System.out.println("Billing Date: " + billingDate);
                    System.out.println("Status: " + status);
                    System.out.println("---------------------------");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while viewing invoice: " + e.getMessage());
        }
    }

    public void viewPaymentHistory() {
        System.out.println("\n--- Payment History ---");
        System.out.print("Enter Patient ID to view payment history: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT b.bill_id, b.amount, b.billing_date, b.status, p.payment_date, p.amount AS payment_amount, p.payment_method " +
                    "FROM billing b " +
                    "JOIN payments p ON b.bill_id = p.bill_id " +
                    "WHERE b.patient_id = ? ORDER BY p.payment_date DESC";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No payment history found for the specified Patient ID.");
            } else {
                while (rs.next()) {
                    int billId = rs.getInt("bill_id");
                    double amount = rs.getDouble("amount");
                    String billingDate = rs.getString("billing_date");
                    String status = rs.getString("status");
                    String paymentDate = rs.getString("payment_date");
                    double paymentAmount = rs.getDouble("payment_amount");
                    String paymentMethod = rs.getString("payment_method");

                    System.out.println("----- Payment History -----");
                    System.out.println("Bill ID: " + billId);
                    System.out.println("Amount: ₹" + amount);
                    System.out.println("Billing Date: " + billingDate);
                    System.out.println("Status: " + status);
                    System.out.println("Payment Date: " + paymentDate);
                    System.out.println("Payment Amount: ₹" + paymentAmount);
                    System.out.println("Payment Method: " + paymentMethod);  // Display payment method
                    System.out.println("---------------------------");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while viewing payment history: " + e.getMessage());
        }
    }

    public void processPayment() {
        System.out.println("\n--- Payment Processing ---");

        System.out.print("Enter Bill ID to process payment: ");
        int billId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter payment amount: ₹");
        double paymentAmount = scanner.nextDouble();
        scanner.nextLine();

        // Prompt user for payment method
        System.out.print("Enter payment method (Cash, Credit Card, Debit Card): ");
        String paymentMethod = scanner.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT amount, status FROM billing WHERE bill_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, billId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No billing information found for the specified Bill ID.");
            } else {
                rs.next();
                double totalAmount = rs.getDouble("amount");
                String status = rs.getString("status");

                if (status.equalsIgnoreCase("Paid")) {
                    System.out.println("This bill is already marked as Paid.");
                } else if (paymentAmount > totalAmount) {
                    System.out.println("Payment amount cannot exceed the total bill amount.");
                } else {
                    // Update billing status to Paid
                    String updateSql = "UPDATE billing SET status = 'Paid' WHERE bill_id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                    updateStmt.setInt(1, billId);
                    int rowsUpdated = updateStmt.executeUpdate();

                    if (rowsUpdated > 0) {
                        System.out.println("Bill status updated to Paid.");

                        // Insert payment record with the payment method
                        String paymentSql = "INSERT INTO payments (bill_id, payment_date, amount, payment_method, status) VALUES (?, CURRENT_DATE, ?, ?, 'Paid')";
                        PreparedStatement paymentStmt = conn.prepareStatement(paymentSql);
                        paymentStmt.setInt(1, billId);
                        paymentStmt.setDouble(2, paymentAmount);
                        paymentStmt.setString(3, paymentMethod);
                        paymentStmt.executeUpdate();

                        System.out.println("Payment processed successfully and payment history updated.");
                    } else {
                        System.out.println("Error processing payment.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while processing payment: " + e.getMessage());
        }
    }

}