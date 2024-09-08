package com.example.educationplatform;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class CoursesController {

    @FXML
    private ComboBox<String> departmentComboBox;

    @FXML
    private VBox coursesContainer;

    // ObservableList to hold department options
    private ObservableList<String> departments = FXCollections.observableArrayList("CSE", "BBA");

    @FXML
    public void initialize() {
        // Set up department options in the ComboBox
        departmentComboBox.setItems(departments);

        // Add a listener to load courses when a department is selected
        departmentComboBox.setOnAction(event -> {
            String selectedDepartment = departmentComboBox.getValue();
            if (selectedDepartment != null) {
                loadCoursesForDepartment(selectedDepartment);
            }
        });
    }

    private Connection connectToDatabase() throws SQLException {
        // Load JDBC driver (may be necessary for some setups)
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("JDBC Driver not found");
        }

        // Establish connection
        return DriverManager.getConnection("jdbc:mariadb://localhost:3306/education_platform", "root", "");
    }

    private void loadCoursesForDepartment(String department) {
        // Clear any previous course cards
        coursesContainer.getChildren().clear();

        if (department == null) {
            return; // Do not display any cards if no department is selected
        }

        String query = "SELECT DISTINCT course_name FROM courses WHERE department = ?"; // Use DISTINCT to avoid duplicate names

        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, department);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                HBox currentRow = new HBox();
                currentRow.setSpacing(20);
                currentRow.setPadding(new Insets(10));
                int cardCount = 0;
                String[] colors = {"#EEC5CE", "#AABBF3", "#D8B9ED", "#77CFDB"}; // Color codes
                int colorIndex = 0;

                Image image = new Image(getClass().getResourceAsStream("/course1.png")); // Image path relative to resources folder
                Set<String> displayedCourses = new HashSet<>(); // To keep track of displayed courses

                while (resultSet.next()) {
                    String courseName = resultSet.getString("course_name");

                    if (displayedCourses.contains(courseName)) {
                        continue; // Skip if the course is already displayed
                    }

                    displayedCourses.add(courseName);

                    // Create a new card for each course
                    AnchorPane card = new AnchorPane();
                    card.setPrefSize(230, 230); // Size of the card
                    card.setStyle("-fx-background-color: " + colors[colorIndex] + "; -fx-border-color: #ddd; -fx-border-width: 1; -fx-background-radius: 20;");

                    Label courseLabel = new Label(courseName);
                    courseLabel.setWrapText(true);
                    courseLabel.setMaxWidth(200); // Ensures wrapping
                    courseLabel.setLayoutX(10);
                    courseLabel.setLayoutY(10);
                    courseLabel.setFont(new Font(16)); // Adjust font size as needed

                    Button viewDetailsButton = new Button("View Details");
                    viewDetailsButton.setLayoutX(10);
                    viewDetailsButton.setLayoutY(215); // Set the button's Y position
                    viewDetailsButton.setStyle("-fx-background-color: #FB9300; -fx-border-radius: 5; -fx-background-radius: 5;");
                    viewDetailsButton.setPadding(new Insets(10, 20, 15, 20)); // Adjust padding

                    ImageView imageView = new ImageView(image); // Reusing the same image
                    imageView.setFitHeight(120); // Increased size for the image
                    imageView.setFitWidth(120);
                    imageView.setLayoutX(100); // Adjust image position
                    imageView.setLayoutY(100);

                    card.getChildren().addAll(courseLabel, viewDetailsButton, imageView);

                    currentRow.getChildren().add(card);
                    cardCount++;

                    // Add row to VBox if it contains 4 cards
                    if (cardCount % 4 == 0) {
                        coursesContainer.getChildren().add(currentRow);
                        currentRow = new HBox();
                        currentRow.setSpacing(20);
                        currentRow.setPadding(new Insets(10));
                    }

                    // Update colorIndex to use next color
                    colorIndex = (colorIndex + 1) % colors.length;
                }

                // Add remaining cards if there are fewer than 4 in the last row
                if (cardCount % 4 != 0) {
                    coursesContainer.getChildren().add(currentRow);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
