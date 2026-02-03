package com.daiict;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // private static final String URL = "jdbc:postgresql://192.168.254.94/CulturalCommitteeDatabase";
    private static final String URL = "jdbc:postgresql://192.168.56.1/CulturalCommitteeDatabase";
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin";

    public static Connection connectDB() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to PostgreSQL database successfully.");
        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
        return connection;
    }

    // public static Connection getConnection() {  
    //     throw new UnsupportedOperationException("Unimplemented method 'getConnection'");
    // }

    public static Connection getConnection() throws SQLException { 
        Connection connection = null;
        try {
            // This is the correct, implemented JDBC call.
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            // System.out.println("Connected to PostgreSQL database successfully."); // Commented out to avoid excessive server logging
            return connection;
        } catch (SQLException e) {
            // Log the critical error and re-throw the exception for the calling DAO to catch.
            System.err.println("❌ Database Connection Failure.");
            System.err.println("   Check DB URL, credentials, and PostgreSQL service status.");
            throw e; 
        }
    }
}
