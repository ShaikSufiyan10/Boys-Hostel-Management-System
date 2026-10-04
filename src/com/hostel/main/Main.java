package com.hostel.main;

// Import LoginPage class
import com.hostel.login.LoginPage;

public class Main {

    // Main method - starting point of the application
    public static void main(String[] args) {

        // Create LoginPage object
        LoginPage loginPage = new LoginPage();

        // Display the Login Page
        loginPage.setVisible(true);
    }
}