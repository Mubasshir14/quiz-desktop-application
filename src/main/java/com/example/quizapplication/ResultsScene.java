package com.example.quizapplication;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ResultsScene {
    private Scene scene;

    public ResultsScene(Stage primaryStage, int score, int totalQuestions, String userName) {
        VBox vBox = new VBox(25);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(30));
        vBox.setStyle("-fx-background-color: linear-gradient(to bottom right, #1a2a6c, #b21f1f, #fdbb2d);");

        // Title Label
        Label resultLabel = new Label("Congratulations, " + userName + "!");
        resultLabel.setStyle(
                "-fx-font-size: 36px; -fx-text-fill: white; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, black, 10, 0.5, 2, 2);"
        );

        // Calculate percentage
        double percentage = ((double) score / totalQuestions) * 100;

        // Create score and percentage labels
        Label scoreLabel = new Label("Your Score: " + score + "/" + totalQuestions);
        scoreLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: 500;");

        Label percentageLabel = new Label(String.format("Percentage: %.2f%%", percentage));
        percentageLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: 500;");

        // Feedback label
        Label feedbackLabel = new Label();
        feedbackLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: 500;");

        if (percentage >= 80) {
            feedbackLabel.setText("🏆 Excellent! You're a quiz master!");
        } else if (percentage >= 60) {
            feedbackLabel.setText("👏 Good job! Keep improving!");
        } else {
            feedbackLabel.setText("💡 Don't give up! Try again!");
        }

        // Pie Chart
        PieChart pieChart = new PieChart();
        PieChart.Data correctData = new PieChart.Data("Correct", score);
        PieChart.Data wrongData = new PieChart.Data("Wrong", totalQuestions - score);

        pieChart.getData().addAll(correctData, wrongData);
        pieChart.setStartAngle(90);
        pieChart.setClockwise(false);
        pieChart.setLabelsVisible(true);
        pieChart.setLegendVisible(false);
        pieChart.setStyle("-fx-font-size: 14px; -fx-font-family: Arial;");

        // Custom styling for the PieChart Data labels (Correct & Wrong)
        correctData.getNode().setStyle("-fx-pie-color: #4caf50; -fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white; "
                + "-fx-effect: dropshadow(gaussian, black, 5, 0.5, 2, 2); "
                + "-fx-font-family: 'Segoe UI', sans-serif;");

        wrongData.getNode().setStyle("-fx-pie-color: #f44336; -fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white; "
                + "-fx-effect: dropshadow(gaussian, black, 5, 0.5, 2, 2); "
                + "-fx-font-family: 'Segoe UI', sans-serif;");

        pieChart.setEffect(new DropShadow(20, Color.BLACK));
        pieChart.setPrefSize(350, 350);

        // Fade transition for Pie Chart
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), pieChart);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();

        // Back Button with enhanced design and hover effect, no border
        Button backButton = new Button("Back to Categories");
        backButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #ff7e5f, #feb47b); -fx-text-fill: white; "
                        + "-fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 25px; "
                        + "-fx-padding: 12px 30px; -fx-cursor: hand; -fx-border-width: 0;"
        );
        backButton.setOnMouseEntered(event -> backButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #feb47b, #ff7e5f); -fx-text-fill: white; "
                        + "-fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 25px; "
                        + "-fx-padding: 12px 30px; -fx-cursor: hand; -fx-border-width: 0;"
        ));
        backButton.setOnMouseExited(event -> backButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #ff7e5f, #feb47b); -fx-text-fill: white; "
                        + "-fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 25px; "
                        + "-fx-padding: 12px 30px; -fx-cursor: hand; -fx-border-width: 0;"
        ));
        backButton.setOnAction(event -> {
            QuizCategoryScene quizCategoryScene = new QuizCategoryScene(primaryStage, userName);
            primaryStage.setScene(quizCategoryScene.getScene());
        });

        // Add components to VBox
        vBox.getChildren().addAll(resultLabel, scoreLabel, percentageLabel, pieChart, feedbackLabel, backButton);
        scene = new Scene(vBox, 1200, 600);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
    }

    public Scene getScene() {
        return scene;
    }
}
