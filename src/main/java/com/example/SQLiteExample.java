package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

public class SQLiteExample {
    private static final String DATABASE_NAME = "example.db";
    private static final String TABLE_NAME = "records";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide input as a command-line argument.");
            return;
        }

        String inputData = args[0]; // User-controlled input

        // 🔹 Apply Sanitization
        inputData = sanitizeInput(inputData);

        SQLiteExample example = new SQLiteExample();

        try {
            example.createDatabaseAndTable();
            example.insertData(inputData); // Secure insertion
        } catch (SQLException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    // 🔹 Simple Input Sanitization to Prevent SQL Injection
    private static String sanitizeInput(String input) {
        // Allow only alphanumeric, spaces, hyphens, underscores, and basic punctuation
        // Adjust the regex pattern based on your specific input requirements
        Pattern allowedPattern = Pattern.compile("[^a-zA-Z0-9\\s_\\-.,!?']");
        String sanitized = allowedPattern.matcher(input).replaceAll("");

        // Escape single quotes for SQLite (secondary defense)
        sanitized = sanitized.replace("'", "''");

        System.out.println("Sanitized Input: " + sanitized);
        return sanitized.trim();
    }

    // Establish connection to the SQLite database
    private Connection connect() throws SQLException {
        String url = "jdbc:sqlite:" + DATABASE_NAME;
        return DriverManager.getConnection(url);
    }

    // Create the database and table if they do not exist
    private void createDatabaseAndTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (\n"
                + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "    data TEXT NOT NULL\n"
                + ");";

        try (Connection conn = connect()) {
            if (conn != null) {
                conn.createStatement().execute(createTableSQL);
                System.out.println("Database and table created (if not already present).");
            }
        }
    }

    // Insert data into the table (Now Secure)
    private void insertData(String data) throws SQLException {
        String insertSQL = "INSERT INTO " + TABLE_NAME + " (data) VALUES ('" + data + "');"; // Using sanitized input

        try (Connection conn = connect();
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(insertSQL);
            System.out.println("Data inserted successfully: " + data);
        }
    }
}
