CREATE DATABASE HOSPITALMANAGEMENTSYSTEM;
USE HOSPITALMANAGEMENTSYSTEM;

CREATE TABLE patients (
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT CHECK (age >= 0 AND age <= 120),
    gender VARCHAR(10) CHECK (gender IN ('Male', 'Female', 'Other')),
    phone_number VARCHAR(20) CHECK (phone_number REGEXP '^[6789]\\d{9}$'),
    address TEXT
);
select * from patients;

CREATE TABLE staff (
    staff_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(100) NOT NULL,
    phone_number VARCHAR(15) CHECK (phone_number REGEXP '^[6789]\\d{9}$'),
    salary DECIMAL(10, 2) CHECK (salary > 0),
    hire_date DATE NOT NULL
);

CREATE TABLE appointments (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT,
    doctor_name VARCHAR(100) NOT NULL,
    appointment_date DATE,
    appointment_time TIME NOT NULL,
    status VARCHAR(20) CHECK (status IN ('Scheduled', 'Completed', 'Cancelled')),
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
);


CREATE TABLE ehr (
    ehr_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT,
    diagnosis TEXT NOT NULL,
    treatment TEXT NOT NULL,
    record_date DATE NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
);

CREATE TABLE billing (
    bill_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT,
    amount DECIMAL(10,2) CHECK (amount > 0),
    billing_date DATE NOT NULL,
    status VARCHAR(20) CHECK (status IN ('Paid', 'Unpaid')),
    invoice_number CHAR(36) DEFAULT (UUID()) UNIQUE,  -- Automatically generates a UUID
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
);

CREATE TABLE inventory (
    inventory_id INT AUTO_INCREMENT PRIMARY KEY,
    supply_name VARCHAR(255) NOT NULL,
    quantity INT CHECK (quantity > 0),
    price DECIMAL(10, 2) CHECK (price > 0),
    expiration_date DATE
);

CREATE TABLE doctors (
    doctor_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    qualifications VARCHAR(255) NOT NULL,
    specialization VARCHAR(100) NOT NULL,  -- Removed CHECK constraint to allow any specialization
    phone_number VARCHAR(15) UNIQUE CHECK (phone_number REGEXP '^[6789]\\d{9}$'),
    gender VARCHAR(10) CHECK (gender IN ('Male', 'Female', 'Other')),
    hire_date DATE NOT NULL,
    status VARCHAR(20) CHECK (status IN ('Active', 'Inactive')) DEFAULT 'Active'
);

select * from doctors;

CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    bill_id INT,
    payment_date DATE NOT NULL,
    amount DECIMAL(10,2) CHECK (amount > 0),
    payment_method VARCHAR(50),  -- e.g., Cash, Credit Card, etc.
    status VARCHAR(20) CHECK (status IN ('Paid', 'Failed', 'Pending')),
    FOREIGN KEY (bill_id) REFERENCES billing(bill_id)  -- Linking to the billing table
);








