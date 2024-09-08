package com.example.educationplatform;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load the login.fxml file
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/educationplatform/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 650);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // Test the database connection before launching the application
        testDatabaseConnection();

        // Launch the JavaFX application
        launch();
    }

    private static void testDatabaseConnection() {
        try (Connection connection = DataBaseConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Database connection successful!");
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("An error occurred while connecting to the database: " + e.getMessage());
        }
    }
}
