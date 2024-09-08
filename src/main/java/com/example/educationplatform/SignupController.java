package com.example.educationplatform;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignupController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleChoiceBox;

    @FXML
    private Button signUpButton;

    @FXML
    public void initialize() {
        if (roleChoiceBox != null) {
            // Initialize ComboBox with roles
            roleChoiceBox.getItems().addAll("Student", "Admin");
            roleChoiceBox.setValue("Select Your Role");
        } else {
            System.err.println("roleChoiceBox is not initialized!");
        }
    }

    @FXML
    private void handleSignUp() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String role = roleChoiceBox.getValue();

        // Validate inputs
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || "Select Your Role".equals(role)) {
            showAlert(AlertType.ERROR, "Form Error!", "Please fill in all fields.");
            return;
        }

        // Save to database
        try (Connection conn = DataBaseConnection.getConnection()) {
            String sql = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, role);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                showAlert(AlertType.INFORMATION, "Sign Up Successful", "Welcome, " + username + "!");
                switchToLogin();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Error saving data.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void switchToLogin() {
        try {
            // Load the login FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/educationplatform/login.fxml"));
            Parent loginRoot = loader.load();

            // Get the current stage
            Stage currentStage = (Stage) signUpButton.getScene().getWindow();

            // Set the new scene
            Scene loginScene = new Scene(loginRoot);
            currentStage.setScene(loginScene);
            currentStage.setTitle("Login");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Loading Error", "Error loading login page.");
        }
    }
}
