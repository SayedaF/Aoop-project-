package com.example.educationplatform;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.IOException;

public class DashboardController {

    @FXML
    private Label greetingLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label tipLabel;

    @FXML
    private Label authorLabel;

    @FXML
    private ImageView authorImage;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label challengeLabel;

    @FXML
    private Hyperlink coursesLink;

    private String username;

    // List of shorter success tips and their authors
    private List<String[]> successTips = Arrays.asList(
            new String[]{"Act now, succeed later.", "Anonymous"},
            new String[]{"Progress is progress, no matter how small.", "Anonymous"},
            new String[]{"Dream big, start small.", "Anonymous"},
            new String[]{"Believe you can, and you’re halfway there.", "Theodore Roosevelt"},
            new String[]{"Success is a journey, not a destination.", "Arthur Ashe"},
            new String[]{"Focus on the present, not the past.", "Anonymous"},
            new String[]{"Every step forward is a step closer to success.", "Anonymous"},
            new String[]{"Your only limit is your mind.", "Anonymous"},
            new String[]{"Stay positive, work hard, make it happen.", "Anonymous"},
            new String[]{"Embrace the challenge, celebrate the progress.", "Anonymous"}
    );


    private Random random = new Random();

    @FXML
    private void initialize() {
        setGreeting();
        setCurrentDate();
        showRandomSuccessTip();
    }

    private void setGreeting() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        String greeting;
        if (hour >= 5 && hour < 12) {
            greeting = "Good Morning!";
        } else if (hour >= 12 && hour < 17) {
            greeting = "Good Afternoon!";
        } else if (hour >= 17 && hour < 21) {
            greeting = "Good Evening!";
        } else {
            greeting = "Good Night!";
        }
        greetingLabel.setText(greeting);
    }

    private void setCurrentDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM");
        String formattedDate = now.format(formatter);
        dateLabel.setText("It's a beautiful " + formattedDate + ".");
    }

    private void showRandomSuccessTip() {
        String[] selectedTip = successTips.get(random.nextInt(successTips.size()));
        String tip = selectedTip[0];
        String author = selectedTip[1];

        tipLabel.setText(tip);
        authorLabel.setText("- " + author);

        // Load and set the image
        try {
            Image image = new Image(getClass().getResourceAsStream("/success.png"));
            authorImage.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
            // Optionally, handle the error if the image can't be loaded
        }
    }

    @FXML
    public void setupDashboard() {
        // Ensure text wrapping is enabled and update labels
        welcomeLabel.setWrapText(true);
        challengeLabel.setWrapText(true);
        updateLabels();
    }

    public void setUsername(String username) {
        this.username = capitalizeFirstLetter(username);
        updateLabels();
    }

    private void updateLabels() {
        if (username != null) {
            welcomeLabel.setText("Welcome, " + username + "!");
            challengeLabel.setText("Ready to conquer today’s challenges?");
        }
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    @FXML
    private void openCoursesPage(ActionEvent event) {
        try {
            // Load the FXML file for the courses page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/educationplatform/courses.fxml"));
            Parent coursesPage = loader.load();

            // Get the current stage
            Stage stage = (Stage) coursesLink.getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(coursesPage));
        } catch (IOException e) {
            e.printStackTrace();
            // Optionally, show an error message to the user
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load courses page.");
            alert.showAndWait();
        }
    }
}
