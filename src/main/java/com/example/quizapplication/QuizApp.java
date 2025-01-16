package com.example.quizapplication;

import javafx.application.Application;
import javafx.stage.Stage;

public class QuizApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create an instance of WelcomeScene with the primaryStage
        WelcomeScene welcomeScene = new WelcomeScene(primaryStage);

        // Set up the primary stage
        primaryStage.setTitle("Quiz Application");
        primaryStage.setScene(welcomeScene.getScene());

        // Set the minimum width and height for the stage
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(500);

        // Center the window on screen
        primaryStage.setOnShown(event -> {
            primaryStage.centerOnScreen();
        });

        // Show the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
