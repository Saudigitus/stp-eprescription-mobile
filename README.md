# E-Prescription Mobile App

E-Prescription is a mobile application designed to digitalize and streamline the process of validating and dispensing patient prescriptions at pharmacies. The app allows pharmacists to securely access prescription data, verify its authenticity, and dispense medicines according to the doctor’s recommendations.

---

## 🔐 Authentication

To access the app, pharmacists must log in using their **DHIS2 instance credentials**:

* **DHIS2 URL instance**
* **Username**
* **Password**

A successful login is required before any prescription can be scanned or processed.

---

## 📱 Key Features

### 1. Scan Prescription Codes

* Prescriptions are scanned **directly at the pharmacy**.
* The system validates the scanned code online.
* If the code is valid, the app retrieves and displays the patient's **basic information**, including:

  * Full name
  * Contact information
  * Identification details (if applicable)

---

### 2. View Prescribed Medicines

After a successful scan, the app displays a detailed list of **all medicines** prescribed by the doctor, each with its recommended quantity.

---

### 3. Assign Dispensed Quantities

Pharmacists must assign how many units of each medicine will be dispensed.

* If a pharmacist enters a **quantity exceeding the prescribed amount**:

  * The quantity field turns **red** ⚠️
  * Saving is **blocked**
  * A pop-up displays all medicines where the assigned quantity is above the recommended amount

This prevents dispensing errors and ensures compliance with medical prescriptions.

---

### 4. Confirmation Step Before Saving

Upon pressing **Save**, a summary pop-up appears for final confirmation. Each medicine is color-coded to indicate dispensing status:

* 🟩 **Green** – Dispensed quantity matches the prescription
* 🟨 **Yellow** – Dispensed quantity is less than prescribed
* 🟥 **Red** – Medicine was not dispensed

The pharmacist must confirm this summary before the data is saved.

---

## 🌐 Connectivity Requirement

This application works **online only**.
A stable internet connection is required to:

* Log in to the DHIS2 instance
* Validate prescription codes
* Retrieve patient and prescription data
* Save dispensing results

