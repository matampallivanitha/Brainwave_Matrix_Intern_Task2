import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        PatientRegistration pr = new PatientRegistration();
        AppointmentScheduler as = new AppointmentScheduler();
        EHRManager ehrManager = new EHRManager();
        BillingManager billingManager = new BillingManager();
        InventoryManager inventoryManager = new InventoryManager();
        StaffManager staffManager = new StaffManager();
        DoctorRegistration doctorRegistration = new DoctorRegistration();  // New object for doctor management
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Hospital Management System ---");
            System.out.println("1. Doctor Registration");
            System.out.println("2. Patient Registration");
            System.out.println("3. Appointment Scheduling");
            System.out.println("4. Electronic Health Records (EHR)");
            System.out.println("5. Billing & Invoicing");
            System.out.println("6. Inventory Management");
            System.out.println("7. Staff Management");
            System.out.println("8. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    doctorRegistrationMenu(doctorRegistration);
                    break;
                case 2:
                    patientRegistrationMenu(pr);
                    break;
                case 3:
                    appointmentSchedulingMenu(as);
                    break;
                case 4:
                    ehrMenu(ehrManager);
                    break;
                case 5:
                    billingMenu(billingManager);
                    break;
                case 6:
                    inventoryMenu(inventoryManager);
                    break;
                case 7:
                    staffMenu(staffManager);
                    break;
                case 8:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Try again!");
            }
        }
    }

    // Doctor Registration Menu
    private static void doctorRegistrationMenu(DoctorRegistration doctorRegistration) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Doctor Registration ---");
            System.out.println("1. Add Doctor");
            System.out.println("2. View Doctors");
            System.out.println("3. Update Doctor");
            System.out.println("4. Delete Doctor");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    doctorRegistration.addDoctor();
                    break;
                case 2:
                    doctorRegistration.viewDoctors();
                    break;
                case 3:
                    doctorRegistration.updateDoctor();
                    break;
                case 4:
                    doctorRegistration.deleteDoctor();
                    break;
                case 5:
                    return; // Back to main menu
                default:
                    System.out.println("Invalid choice. Try again!");
            }
        }
    }

    // Patient Registration Module
    private static void patientRegistrationMenu(PatientRegistration pr) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Patient Registration ---");
            System.out.println("1. Add Patient");
            System.out.println("2. View Patients");
            System.out.println("3. Update Patient");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    pr.addPatient();
                    break;
                case 2:
                    pr.viewPatients();
                    break;
                case 3:
                    pr.updatePatient();
                    break;
                case 4:
                    return; // Back to main menu
                default:
                    System.out.println("Invalid choice. Try again!");
            }
        }
    }

    // Appointment Scheduling Menu
    private static void appointmentSchedulingMenu(AppointmentScheduler as) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Appointment Scheduling ---");
            System.out.println("1. Schedule Appointment");
            System.out.println("2. View Appointments");
            System.out.println("3. Cancel Appointment");
            System.out.println("4. Reschedule Appointment");
            System.out.println("5. View Appointment History");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    as.scheduleAppointment();
                    break;
                case 2:
                    as.viewAppointments();
                    break;
                case 3:
                    as.cancelAppointment();
                    break;
                case 4:
                    as.rescheduleAppointment();
                    break;
                case 5:
                    // Ask for the patient ID to view appointment history
                    System.out.print("Enter Patient ID to view history: ");
                    int patientId = sc.nextInt();
                    as.viewAppointmentHistory(patientId);
                    break;
                case 6:
                    return; // Back to main menu
                default:
                    System.out.println("Invalid choice. Try again!");
            }
        }
    }

    private static void ehrMenu(EHRManager ehrManager) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Electronic Health Records (EHR) ---");
            System.out.println("1. Add Medical Record");
            System.out.println("2. View Medical Records");
            System.out.println("3. Update Medical Record");
            System.out.println("4. View Medical History");
            System.out.println("5. Generate Medical Report");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter choice: ");

            // Handle non-integer input
            if (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between 1 and 6.");
                sc.next(); // Consume the invalid input
                continue; // Prompt the user again
            }

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    ehrManager.addRecord();
                    break;
                case 2:
                    ehrManager.viewRecords();
                    break;
                case 3:
                    ehrManager.updateRecord();  // Handle updating medical records
                    break;
                case 4:
                    ehrManager.viewMedicalHistory();  // Handle viewing medical history
                    break;
                case 5:
                    ehrManager.generateReport();  // Handle generating medical report
                    break;
                case 6:
                    return; // Back to main menu
                default:
                    System.out.println("Invalid choice. Please try again!");
            }
        }
    }

   //Billing & Invoicing Menu
    private static void billingMenu(BillingManager billingManager) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Billing & Invoicing ---");
            System.out.println("1. Add Billing Information");
            System.out.println("2. View Billing Information");
            System.out.println("3. Create Invoice");
            System.out.println("4. View Invoice");
            System.out.println("5. View Payment History");
            System.out.println("6. Process Payment");
            System.out.println("7. Back to Main Menu");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume leftover newline

            switch (choice) {
                case 1:
                    billingManager.addBilling(); // Calls addBilling which contains payment status validation
                    break;
                case 2:
                    billingManager.viewBilling();
                    break;
                case 3:
                    billingManager.createInvoice();
                    break;
                case 4:
                    billingManager.viewInvoice();
                    break;
                case 5:
                    billingManager.viewPaymentHistory();
                    break;
                case 6:
                    billingManager.processPayment();
                    break;
                case 7:
                    return; // Back to main menu
                default:
                    System.out.println("Invalid choice. Try again!");
            }
        }
    }

    // Inventory Management Module
    private static void inventoryMenu(InventoryManager inventoryManager) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Inventory Management ---");
            System.out.println("1. Add Supply to Inventory");
            System.out.println("2. View Inventory");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    inventoryManager.addSupply();
                    break;
                case 2:
                    inventoryManager.viewInventory();
                    break;
                case 3:
                    return; // Back to main menu
                default:
                    System.out.println("Invalid choice. Try again!");
            }
        }
    }

    // Staff Management Module
    private static void staffMenu(StaffManager staffManager) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Staff Management ---");
            System.out.println("1. Add Staff");
            System.out.println("2. View Staff");
            System.out.println("3. Edit Staff");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    staffManager.addStaff();
                    break;
                case 2:
                    staffManager.viewStaff();
                    break;
                case 3:
                    staffManager.editStaff();
                    break;
                case 4:
                    return; // Back to main menu
                default:
                    System.out.println("Invalid choice. Try again!");
            }
        }
    }
}
