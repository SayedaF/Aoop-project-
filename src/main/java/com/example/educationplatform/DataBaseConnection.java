package com.example.educationplatform;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {

    private static final String URL = "jdbc:mariadb://localhost:3306/education_platform";
    private static final String USER = "root"; // default username for XAMPP
    private static final String PASSWORD = ""; // default password is empty

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}