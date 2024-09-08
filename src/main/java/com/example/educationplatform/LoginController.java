package com.example.educationplatform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        if (authenticateUser(username, password)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/educationplatform/dashboard.fxml"));
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            controller.setUsername(username);
            controller.setupDashboard();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            if (usernameExists(username)) {
                showAlert("Error", "Invalid password.");
            } else {
                navigateToSignup();
            }
        }
    }

    private boolean authenticateUser(String username, String password) {
        String query = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    return verifyPassword(password, storedPassword);
                }
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database error: " + e.getMessage());
            return false;
        }
    }

    private boolean usernameExists(String username) {
        String query = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database error: " + e.getMessage());
            return false;
        }
    }

    private void navigateToSignup() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/educationplatform/signup.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private boolean verifyPassword(String password, String storedPassword) {
        return password.equals(storedPassword);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
