package com.example.quizapplication;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.paint.Color; // Add this import

import java.util.HashMap;
import java.util.Map;

public class QuizCategoryScene {
    private Scene scene;
    private Map<String, String[]> categories = new HashMap<>();

    public QuizCategoryScene(Stage primaryStage, String userName) {
        // Initialize categories (same as before)
        initializeCategories();

        // Main container
        VBox vBox = new VBox(40);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(30));
        vBox.setStyle("-fx-background-color: linear-gradient(to bottom right, #1a2a6c, #b21f1f, #fdbb2d);");

        // Header section with animated welcome
        VBox headerBox = createHeaderSection(userName);

        // Button container with enhanced styling
        FlowPane buttonPane = new FlowPane();
        buttonPane.setHgap(25);
        buttonPane.setVgap(25);
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.setPadding(new Insets(20));

        // Add the new "Live" button
        Button liveButton = createLiveButton(primaryStage, userName);
        Button leaderboardButton = new Button("View Leaderboard");
        leaderboardButton.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.15);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25px;" +
                        "-fx-padding: 15px 30px;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 0);" +
                        "-fx-border-color: rgba(255, 255, 255, 0.2);" +
                        "-fx-border-radius: 25px;" +
                        "-fx-border-width: 1px;"
        );

        leaderboardButton.setOnAction(e -> {
            // Create a TextInputDialog to ask the user for the room PIN
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("ROOM PIN");
            dialog.setHeaderText("ROOM PIN REQUIRED");
            dialog.setContentText("PLEASE ENTER ROOM PIN:");


            // Add custom styles to the dialog
            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.setStyle(
                    "-fx-background-color: linear-gradient(to bottom right, #1a2a6c, #b21f1f, #fdbb2d);" +
                            "-fx-border-color: #ffffff;" +
                            "-fx-border-width: 2px;" +
                            "-fx-border-radius: 15px;" +
                            "-fx-background-radius: 15px;" +
                            "-fx-padding: 20px;" +
                            "-fx-font-family: 'Arial';" +
                            "-fx-font-size: 16px;" +
                            "-fx-text-fill: white;"
            );

            // Style the header text
            dialogPane.lookup(".header-panel").setStyle(
                    "-fx-font-size: 20px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: white;" +
                            "-fx-alignment: center;"
            );

            // Style the content text
            dialogPane.lookup(".content").setStyle(
                    "-fx-font-size: 14px;" +
                            "-fx-text-fill: #e0e0e0;"
            );

            // Style the buttons
            dialogPane.lookupButton(ButtonType.OK).setStyle(
                    "-fx-background-color: #ff4b2b;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 16px;" +
                            "-fx-background-radius: 25px;" +
                            "-fx-padding: 10px 20px;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 0);"
            );
            dialogPane.lookupButton(ButtonType.CANCEL).setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.15);" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 16px;" +
                            "-fx-background-radius: 25px;" +
                            "-fx-padding: 10px 20px;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 0);"
            );

            // Wait for the user's input
            dialog.showAndWait().ifPresent(roomPIN -> {
                // Navigate to the Leaderboard with the provided room PIN
                Leaderboard leaderboard = new Leaderboard(primaryStage, roomPIN);
                primaryStage.setScene(leaderboard.getScene());
                System.out.println("Room PIN entered: " + roomPIN);
            });
        });
        vBox.getChildren().addAll(headerBox, liveButton, leaderboardButton, buttonPane);

        // Create and add category buttons with animations
        addCategoryButtons(buttonPane, primaryStage, userName);

        // Create scene with fade-in animation
        scene = new Scene(vBox, 1200, 600);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        // Add fade-in animation for the entire scene
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), vBox);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    private VBox createHeaderSection(String userName) {
        VBox headerBox = new VBox(15);
        headerBox.setAlignment(Pos.CENTER);

        // Welcome label with enhanced styling
        Label welcomeLabel = new Label("");
        welcomeLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #FFD700;");

        // App name label
        Label appNameLabel = new Label("Welcome To Quiz Master");
        appNameLabel.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-text-fill: white; "
                + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10, 0.5, 0, 0);");

        // User greeting with dynamic effect
        Label greetingLabel = new Label(userName.toUpperCase());
        greetingLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #FFD700;");

        // Add glow effect
        Glow glow = new Glow(0.8);
        greetingLabel.setEffect(glow);

        Label chooseLabel = new Label("Choose Your Category");
        chooseLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: normal; -fx-text-fill: white;");

        headerBox.getChildren().addAll(welcomeLabel, appNameLabel, greetingLabel, chooseLabel);
        return headerBox;
    }

    private void addCategoryButtons(FlowPane buttonPane, Stage primaryStage, String userName) {
        categories.forEach((category, subcategories) -> {
            Button button = createEnhancedCategoryButton(category, primaryStage, userName);
            buttonPane.getChildren().add(button);
        });
    }

    private Button createEnhancedCategoryButton(String categoryName, Stage primaryStage, String userName) {
        Button button = new Button(categoryName);
        button.setOnAction(event -> showChildCategories(primaryStage, categoryName, userName));
        styleEnhancedButton(button);
        return button;
    }

    private void styleEnhancedButton(Button button) {
        // Modern glass-morphism style
        button.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.15);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25px;" +
                        "-fx-padding: 15px 30px;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 0);" +
                        "-fx-border-color: rgba(255, 255, 255, 0.2);" +
                        "-fx-border-radius: 25px;" +
                        "-fx-border-width: 1px;"
        );

        // Hover effects
        button.setOnMouseEntered(e -> {
            button.setStyle(button.getStyle() +
                    "-fx-background-color: rgba(255, 255, 255, 0.25);" +
                    "-fx-scale-x: 1.1;" +
                    "-fx-scale-y: 1.1;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 15, 0, 0, 0);"
            );
        });

        button.setOnMouseExited(e -> {
            button.setStyle(button.getStyle() +
                    "-fx-background-color: rgba(255, 255, 255, 0.15);" +
                    "-fx-scale-x: 1;" +
                    "-fx-scale-y: 1;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 0);"
            );
        });

        // Add click effect
        button.setOnMousePressed(e -> button.setStyle(button.getStyle() + "-fx-scale-x: 0.95; -fx-scale-y: 0.95;"));
        button.setOnMouseReleased(e -> button.setStyle(button.getStyle() + "-fx-scale-x: 1; -fx-scale-y: 1;"));
    }

    private Button createLiveButton(Stage primaryStage, String userName) {
        Button liveButton = new Button("Live");
        liveButton.setOnAction(event -> {
            LiveQuizCategory liveQuizCategory = new LiveQuizCategory(primaryStage, userName);
            primaryStage.setScene(liveQuizCategory.getScene());
        });
        styleEnhancedButton(liveButton);
        return liveButton;
    }

    private void showChildCategories(Stage primaryStage, String parentCategory, String userName) {
        VBox vBox = new VBox(30);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(30));
        vBox.setStyle("-fx-background-color: linear-gradient(to bottom right, #1a2a6c, #b21f1f, #fdbb2d);");

        // Enhanced subcategory header
        Label parentCategoryLabel = new Label(parentCategory);
        parentCategoryLabel.setStyle("-fx-font-size: 42px; -fx-font-weight: bold; -fx-text-fill: #FFD700;");
        parentCategoryLabel.setEffect(new DropShadow(20, Color.BLACK));

        Label subtitleLabel = new Label("Select Your Topic");
        subtitleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: normal; -fx-text-fill: white;");

        // Enhanced button container
        FlowPane buttonPane = new FlowPane();
        buttonPane.setHgap(25);
        buttonPane.setVgap(25);
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.setPadding(new Insets(20));

        // Add subcategory buttons with animations
        for (String childCategory : categories.get(parentCategory)) {
            Button childButton = new Button(childCategory);
            childButton.setOnAction(event -> {
                QuizScene quizScene = new QuizScene(primaryStage, childCategory, userName);
                primaryStage.setScene(quizScene.getScene());
            });
            styleEnhancedButton(childButton);
            buttonPane.getChildren().add(childButton);
        }

        // Enhanced back button
        Button backButton = new Button("← Back to Categories");
        backButton.setOnAction(event -> {
            QuizCategoryScene categoryScene = new QuizCategoryScene(primaryStage, userName);
            primaryStage.setScene(categoryScene.getScene());
        });

        // Style back button differently
        backButton.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-background-radius: 20px;" +
                        "-fx-cursor: hand;"
        );

        vBox.getChildren().addAll(parentCategoryLabel, subtitleLabel, buttonPane, backButton);

        Scene childCategoryScene = new Scene(vBox, 1200, 600);

        // Add fade transition for smooth scene change
        FadeTransition fadeIn = new FadeTransition(Duration.millis(400), vBox);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        primaryStage.setScene(childCategoryScene);
    }

    private void initializeCategories() {
        categories.put("Science", new String[]{"Physics", "Chemistry", "Biology"});
        categories.put("History", new String[]{"Ancient", "Medieval", "Modern"});
        categories.put("Programming", new String[]{"Java", "Python", "C++", "JavaScript"});
        categories.put("Data Structures", new String[]{"Arrays", "Linked Lists", "Stacks", "Queues"});
        categories.put("Artificial Intelligence", new String[]{"Machine Learning", "Neural Networks"});
        categories.put("Cybersecurity", new String[]{"Ethical Hacking", "Penetration Testing"});
        categories.put("Networking", new String[]{"TCP/IP", "Routing", "Switching"});
        categories.put("Operating Systems", new String[]{"Linux", "Windows", "MacOS"});
        categories.put("Database Systems", new String[]{"MySQL", "MongoDB", "PostgreSQL"});
        categories.put("Algorithms", new String[]{"Sorting", "Searching", "Graph Algorithms"});
    }

    public Scene getScene() {
        return scene;
    }
}
