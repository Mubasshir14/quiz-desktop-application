package com.example.quizapplication;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WelcomeScene {
    private Scene scene;

    public WelcomeScene(Stage primaryStage) {
        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));
        vBox.setStyle("-fx-background-color: linear-gradient(to bottom right, #1a2a6c, #b21f1f, #fdbb2d);");

        // Add logo with shadow effect
        Image logoImage = new Image(getClass().getResourceAsStream("logo.png"));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(150);
        logoView.setPreserveRatio(true);
        logoView.setEffect(new DropShadow(15, Color.BLACK));

        // Animated Welcome Label
        Label welcomeLabel = new Label("Welcome to the Quiz App!");
        welcomeLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: white; -fx-font-weight: bold;");
        welcomeLabel.setEffect(new DropShadow(15, Color.BLACK));

        // Fade-in effect for the welcome label
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), welcomeLabel);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();

        // Name Label and Field
        Label nameLabel = new Label("Enter Your Name");
        nameLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: 500; -fx-font-weight: bold;");
        nameLabel.setEffect(new DropShadow(10, Color.BLACK));

        TextField nameField = new TextField();
        nameField.setPromptText("Your Name");
        nameField.setStyle("-fx-background-color: white; -fx-background-radius: 15px; -fx-border-radius: 15px; -fx-padding: 8px;");
        nameField.setMaxWidth(250);

        // Start Button with smooth hover effect
        Button startButton = new Button("Start Quiz");
        startButton.setStyle("-fx-background-color: rgba(255, 255, 255, 0.15);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25px;" +
                "-fx-padding: 15px 15px;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 0);" +
                "-fx-border-color: rgba(255, 255, 255, 0.2);" +
                "-fx-border-radius: 25px;" +
                "-fx-border-width: 1px;");

        startButton.setOnMouseEntered(event -> startButton.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.25);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25px;" +
                        "-fx-padding: 15px 30px;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 15, 0, 0, 0);" +
                        "-fx-border-color: rgba(255, 255, 255, 0.3);" +
                        "-fx-border-radius: 25px;" +
                        "-fx-border-width: 1px;"));

        startButton.setOnMouseExited(event -> startButton.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.15);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25px;" +
                        "-fx-padding: 15px 15px;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 0);" +
                        "-fx-border-color: rgba(255, 255, 255, 0.2);" +
                        "-fx-border-radius: 25px;" +
                        "-fx-border-width: 1px;"));

        // Button Action
        startButton.setOnAction(event -> {
            if (!nameField.getText().isEmpty()) {
                QuizCategoryScene quizCategoryScene = new QuizCategoryScene(primaryStage, nameField.getText());
                primaryStage.setScene(quizCategoryScene.getScene());
            }
        });

        // Add all components to VBox
        vBox.getChildren().addAll(logoView, welcomeLabel, nameLabel, nameField, startButton);

        // Create and style scene
        scene = new Scene(vBox, 1200, 600);

        // Make the scene responsive
        primaryStage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            if (newWidth.doubleValue() < 600) {
                vBox.setSpacing(10);
                welcomeLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: white; -fx-font-weight: bold;");
                nameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: 500;");
                nameField.setMaxWidth(200);
                startButton.setStyle("-fx-background-color: #ff6a00; -fx-text-fill: white; -fx-font-size: 14px; "
                        + "-fx-font-weight: bold; -fx-background-radius: 15px; -fx-padding: 8px 15px;");
            } else {
                vBox.setSpacing(20);
                welcomeLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: white; -fx-font-weight: bold;");
                nameLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: 500;");
                nameField.setMaxWidth(250);
                startButton.setStyle("-fx-background-color: #ff6a00; -fx-text-fill: white; -fx-font-size: 16px; "
                        + "-fx-font-weight: bold; -fx-background-radius: 15px; -fx-padding: 10px 20px;");
            }
        });

        primaryStage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            if (newHeight.doubleValue() < 400) {
                welcomeLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");
                nameLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white; -fx-font-weight: 500;");
            } else {
                welcomeLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: white; -fx-font-weight: bold;");
                nameLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: 500;");
            }
        });

        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
    }

    public Scene getScene() {
        return scene;
    }
}
