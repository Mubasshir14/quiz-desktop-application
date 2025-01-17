package com.example.quizapplication;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.*;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import javafx.scene.effect.*;
import javafx.scene.shape.Circle;
import javafx.scene.Node;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class JoinLiveQuiz {
    private Scene scene;
    private int score = 0;
    private int currentQuestionIndex = 0;
    private List<Question> questions;
    private VBox mainContainer;
    private VBox questionsContainer;
    private Stage primaryStage;
    private ProgressBar progressBar;
    private Label timerLabel;
    private PauseTransition questionTimer;
    private String userName;
    private String pin;

    // Modern style constants
    private static final String MAIN_CONTAINER_STYLE = """
            -fx-background-color: linear-gradient(to bottom right, #f6f8ff, #ffffff);
            -fx-font-family: 'Segoe UI', Arial, sans-serif;
            """;

    private static final String CONTAINER_STYLE = """
            -fx-background-color: white;
            -fx-background-radius: 20;
            -fx-border-radius: 20;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);
            """;

    private static final String OPTION_BUTTON_STYLE = """
            -fx-background-color: white;
            -fx-border-color: #e0e6ff;
            -fx-border-width: 2;
            -fx-border-radius: 10;
            -fx-background-radius: 10;
            -fx-padding: 15;
            -fx-font-size: 14px;
            -fx-pref-width: 400;
            -fx-alignment: center-left;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 1);
            """;

    private static final String OPTION_HOVER_STYLE = """
            -fx-background-color: #f8f9ff;
            -fx-border-color: #3498db;
            -fx-effect: dropshadow(gaussian, rgba(52,152,219,0.2), 10, 0, 0, 4);
            -fx-translate-y: -2;
            """;

    private static final String CORRECT_STYLE = """
            -fx-background-color: #27ae60;
            -fx-text-fill: white;
            -fx-border-color: #27ae60;
            """;

    private static final String INCORRECT_STYLE = """
            -fx-background-color: #e74c3c;
            -fx-text-fill: white;
            -fx-border-color: #e74c3c;
            """;

    private static final String NEXT_BUTTON_STYLE = """
            -fx-background-color: #3498db;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-padding: 12 30;
            -fx-background-radius: 8;
            -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian, rgba(52,152,219,0.3), 8, 0, 0, 2);
            """;

    public JoinLiveQuiz(Stage primaryStage, String userName, String pin, List<Question> questions) {
        this.primaryStage = primaryStage;
        this.questions = questions;
        this.userName = userName;
        this.pin = pin;

        mainContainer = new VBox(25);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setStyle(MAIN_CONTAINER_STYLE);
        System.out.println(userName + " " + pin);

        VBox headerContainer = createHeader(userName, pin);
        HBox progressContainer = createProgressSection();

        questionsContainer = new VBox(20);
        questionsContainer.setStyle(CONTAINER_STYLE);
        questionsContainer.setPadding(new Insets(25));
        questionsContainer.setAlignment(Pos.CENTER);

        if (questions != null && !questions.isEmpty()) {
            showQuestion(currentQuestionIndex);
        }

        ScrollPane scrollPane = new ScrollPane(questionsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), mainContainer);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        mainContainer.getChildren().addAll(headerContainer, progressContainer, scrollPane);
        scene = new Scene(mainContainer, 1200, 700);
    }

    private VBox createHeader(String userName, String pin) {
        VBox header = new VBox(15);
        header.setAlignment(Pos.CENTER);

        Circle avatar = new Circle(30);
        avatar.setFill(Color.web("#3498db"));
        Text initials = new Text(userName.substring(0, 1).toUpperCase());
        initials.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        initials.setFill(Color.WHITE);
        StackPane avatarPane = new StackPane(avatar, initials);

        Label title = new Label("Interactive Quiz");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: #2c3e50;");

        Label userInfo = new Label(userName + " | PIN: " + pin);
        userInfo.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 14px;");
        System.out.println(userInfo);

        header.getChildren().addAll(avatarPane, title, userInfo);
        return header;
    }

    private HBox createProgressSection() {
        HBox progress = new HBox(20);
        progress.setAlignment(Pos.CENTER);

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(300);
        progressBar.setStyle("-fx-accent: #3498db;");

        timerLabel = new Label("Time remaining: --");
        timerLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 14px;");

        progress.getChildren().addAll(progressBar, timerLabel);
        return progress;
    }

    private void showQuestion(int index) {
        questionsContainer.getChildren().clear();
        Question question = questions.get(index);

        Timeline progressTimeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(progressBar.progressProperty(), (double) index / questions.size())),
                new KeyFrame(Duration.millis(500),
                        new KeyValue(progressBar.progressProperty(), (double) (index + 1) / questions.size()))
        );
        progressTimeline.play();

        Label questionLabel = new Label("Q" + (index + 1) + ". " + question.getQuestion());
        questionLabel.setStyle("""
            -fx-font-size: 18px;
            -fx-font-weight: bold;
            -fx-text-fill: #2c3e50;
            -fx-wrap-text: true;
            """);

        VBox optionsContainer = new VBox(12);
        String[] options = question.getOptions();

        for (int i = 0; i < options.length; i++) {
            Button optionButton = createOptionButton(options[i], i);
            final int optionIndex = i;

            optionButton.setOpacity(0);
            optionButton.setTranslateX(-20);

            PauseTransition delay = new PauseTransition(Duration.millis(100 * i));
            delay.setOnFinished(event -> {
                FadeTransition ft = new FadeTransition(Duration.millis(400), optionButton);
                ft.setFromValue(0);
                ft.setToValue(1);

                TranslateTransition tt = new TranslateTransition(Duration.millis(400), optionButton);
                tt.setFromX(-20);
                tt.setToX(0);

                ParallelTransition pt = new ParallelTransition(ft, tt);
                pt.play();
            });
            delay.play();

            optionButton.setOnAction(e -> handleAnswer(optionButton, optionIndex,
                    question.getCorrectAnswer(), optionsContainer));
            optionsContainer.getChildren().add(optionButton);
            optionsContainer.setAlignment(Pos.CENTER);
        }

        Button nextButton = new Button("Next Question");
        nextButton.setStyle(NEXT_BUTTON_STYLE);
        nextButton.setDisable(true);
        nextButton.setOnAction(e -> moveToNextQuestion());

        questionsContainer.getChildren().addAll(questionLabel, optionsContainer, nextButton);

        // Start the timer for this question
        startQuestionTimer(question.getTime(), nextButton, optionsContainer);
    }

    private Button createOptionButton(String text, int index) {
        Button button = new Button((char)('A' + index) + ". " + text);
        button.setStyle(OPTION_BUTTON_STYLE);

        button.setOnMouseEntered(e -> {
            if (!button.isDisable()) {
                button.setStyle(OPTION_BUTTON_STYLE + OPTION_HOVER_STYLE);
            }
        });

        button.setOnMouseExited(e -> {
            if (!button.isDisable()) {
                button.setStyle(OPTION_BUTTON_STYLE);
            }
        });

        return button;
    }

    private void handleAnswer(Button optionButton, int selectedOption, int correctAnswer,
                              VBox optionsContainer) {
        if (questionTimer != null) {
            questionTimer.stop();
            timerLabel.setText("Time stopped");
        }

        ScaleTransition clickEffect = new ScaleTransition(Duration.millis(100), optionButton);
        clickEffect.setToX(0.95);
        clickEffect.setToY(0.95);
        clickEffect.setAutoReverse(true);
        clickEffect.setCycleCount(2);
        clickEffect.play();

        if (selectedOption == (correctAnswer - 1)) {
            optionButton.setStyle(OPTION_BUTTON_STYLE + CORRECT_STYLE);
            score++;
            showFeedback("✅ Correct! +1 point", true);
        } else {
            optionButton.setStyle(OPTION_BUTTON_STYLE + INCORRECT_STYLE);
            showCorrectAnswer(optionsContainer, correctAnswer - 1);
            showFeedback("❌ Incorrect. Try to remember the correct answer!", false);
        }

        disableAllButtons(optionsContainer);
        Button nextButton = (Button) questionsContainer.getChildren().get(
                questionsContainer.getChildren().size() - 1);
        nextButton.setDisable(false);
    }

    private void showFeedback(String message, boolean isCorrect) {
        Label feedbackLabel = new Label(message);
        feedbackLabel.setStyle(String.format("""
            -fx-background-color: %s;
            -fx-text-fill: white;
            -fx-padding: 10 20;
            -fx-background-radius: 20;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);
            """, isCorrect ? "#27ae60" : "#e74c3c"));

        feedbackLabel.setTranslateY(-20);
        feedbackLabel.setOpacity(0);

        Timeline slideIn = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(feedbackLabel.translateYProperty(), -20),
                        new KeyValue(feedbackLabel.opacityProperty(), 0)
                ),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(feedbackLabel.translateYProperty(), 0),
                        new KeyValue(feedbackLabel.opacityProperty(), 1)
                )
        );
        slideIn.play();

        questionsContainer.getChildren().add(2, feedbackLabel);
    }

    private void showQuizResults() {
        questionsContainer.getChildren().clear();

        VBox resultsContainer = new VBox(25);
        resultsContainer.setAlignment(Pos.CENTER);
        resultsContainer.setPadding(new Insets(30));

        double percentage = (double) score / questions.size() * 100;

        Circle scoreCircle = new Circle(60);
        scoreCircle.setFill(Color.web("#3498db"));

        Label scoreLabel = new Label(String.format("%.0f%%", percentage));
        scoreLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        StackPane scoreDisplay = new StackPane(scoreCircle, scoreLabel);

        Label congratsLabel = new Label(getPerformanceMessage(percentage));
        congratsLabel.setStyle("""
            -fx-font-size: 24px;
            -fx-font-weight: bold;
            -fx-text-fill: #2c3e50;
            -fx-text-alignment: center;
            -fx-wrap-text: true;
            """);

        Label detailsLabel = new Label(String.format("You got %d out of %d questions correct",
                score, questions.size()));
        detailsLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 16px;");

        Button finishButton = new Button("Finish Quiz");
        finishButton.setStyle(NEXT_BUTTON_STYLE);
        finishButton.setOnAction(e -> primaryStage.close());

        System.out.println("pin" + pin + "usrname" + userName + "score" + score);
        postResultsToBackend(getUserName(), getPin(), getScore());

        Button leaderboardButton = new Button("View Leaderboard");
        leaderboardButton.setStyle(NEXT_BUTTON_STYLE);
        leaderboardButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("ROOM PIN");
            dialog.setHeaderText("ROOM PIN REQUIRED");
            dialog.setContentText("PLEASE ENTER ROOM PIN:");

            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.setStyle(
                    "-fx-background-color: linear-gradient(to bottom right, #1a2a6c, #b21f1f, #fdbb2d);" +
                            "-fx-border-color: #ffffff;" +
                            "-fx-border-width: 2px;" +
                            "-fx-border-radius: 15px;" +
                            "-fx-background-radius: 15px;" +
                            "-fx-padding: 20px;" +
                            "-fx-font-family: 'Arial';" +
                            "-fx-font-size: 16px;" +"-fx-text-fill: white;"
            );

            dialogPane.lookup(".header-panel").setStyle(
                    "-fx-font-size: 20px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: white;" +
                            "-fx-alignment: center;"
            );

            dialogPane.lookup(".content").setStyle(
                    "-fx-font-size: 14px;" +
                            "-fx-text-fill: #e0e0e0;"
            );

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

            dialog.showAndWait().ifPresent(roomPIN -> {
                Leaderboard leaderboard = new Leaderboard(primaryStage, roomPIN);
                primaryStage.setScene(leaderboard.getScene());
                System.out.println("Room PIN entered: " + roomPIN);
            });
        });

        resultsContainer.setOpacity(0);
        resultsContainer.getChildren().addAll(scoreDisplay, congratsLabel, detailsLabel, finishButton, leaderboardButton);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), resultsContainer);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        questionsContainer.getChildren().add(resultsContainer);
    }

    private void postResultsToBackend(String userName, String pin, int score) {
        String url = "https://quiz-six-khaki.vercel.app/api/rooms/addParticipantWithMarks";
        String jsonInputString = String.format("{\"pin\": \"%s\", \"name\": \"%s\", \"marks\": %d}", pin, userName, score);
        System.out.println(jsonInputString);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    System.out.println("Response from server: " + response);
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
    }

    private String getPerformanceMessage(double percentage) {
        if (percentage >= 90) return "🏆 Outstanding!";
        if (percentage >= 80) return "🌟 Excellent Work!";
        if (percentage >= 70) return "👏 Good Job!";
        if (percentage >= 60) return "💪 Nice Try!";
        return "📚 Keep Learning!";
    }

    private void moveToNextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            showQuestion(currentQuestionIndex);
        } else {
            showQuizResults();
        }
    }

    private void showCorrectAnswer(VBox optionsContainer, int correctAnswer) {
        for (Node node : optionsContainer.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                int buttonIndex = optionsContainer.getChildren().indexOf(node);
                if (buttonIndex == correctAnswer) {
                    Timeline highlight = new Timeline(
                            new KeyFrame(Duration.ZERO,
                                    new KeyValue(button.scaleXProperty(), 1),
                                    new KeyValue(button.scaleYProperty(), 1)),
                            new KeyFrame(Duration.millis(200),
                                    new KeyValue(button.scaleXProperty(), 1.05),
                                    new KeyValue(button.scaleYProperty(), 1.05)),
                            new KeyFrame(Duration.millis(400),
                                    new KeyValue(button.scaleXProperty(), 1),
                                    new KeyValue(button.scaleYProperty(), 1))
                    );
                    highlight.play();

                    button.setStyle(OPTION_BUTTON_STYLE + CORRECT_STYLE);
                }
            }
        }
    }

    private void disableAllButtons(VBox optionsContainer) {
        for (Node node : optionsContainer.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setDisable(true);
                button.setOpacity(0.7);
            }
        }
    }

    public Scene getScene() {
        return scene;
    }

    public String getUserName() {
        return userName;
    }

    public String getPin() {
        return pin;
    }

    public int getScore() {
        return score;
    }

    private void startQuestionTimer(int seconds, Button nextButton, VBox optionsContainer) {
        if (questionTimer != null) {
            questionTimer.stop();
        }

        final int[] timeLeft = {seconds};
        questionTimer = new PauseTransition(Duration.seconds(1));

        questionTimer.setOnFinished(event -> {
            timeLeft[0]--;
            if (timeLeft[0] >= 0) {
                if (timeLeft[0] <= 5) {
                    timerLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 14px; -fx-font-weight: bold;");

                    ScaleTransition pulse = new ScaleTransition(Duration.millis(500), timerLabel);
                    pulse.setFromX(1);
                    pulse.setFromY(1);
                    pulse.setToX(1.1);
                    pulse.setToY(1.1);
                    pulse.setCycleCount(2);
                    pulse.setAutoReverse(true);
                    pulse.play();
                }

                timerLabel.setText(String.format("Time remaining: %ds", timeLeft[0]));
                questionTimer.play();
            } else {
                showTimeUpFeedback();
                showCorrectAnswer(optionsContainer, questions.get(currentQuestionIndex).getCorrectAnswer() - 1);
                disableAllButtons(optionsContainer);
                nextButton.setDisable(false);
            }
        });
        questionTimer.play();
    }

    private void showTimeUpFeedback() {
        Label timeUpLabel = new Label("⏰ Time's up!");
        timeUpLabel.setStyle("""
            -fx-background-color: #f39c12;
            -fx-text-fill: white;
            -fx-padding: 10 20;
            -fx-background-radius: 20;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);
            """);

        Timeline bounce = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(timeUpLabel.translateYProperty(), -50),
                        new KeyValue(timeUpLabel.opacityProperty(), 0)
                ),
                new KeyFrame(Duration.millis(500),
                        new KeyValue(timeUpLabel.translateYProperty(), 0),
                        new KeyValue(timeUpLabel.opacityProperty(), 1),
                        new KeyValue(timeUpLabel.scaleXProperty(), 1.2),
                        new KeyValue(timeUpLabel.scaleYProperty(), 1.2)
                ),
                new KeyFrame(Duration.millis(700),
                        new KeyValue(timeUpLabel.scaleXProperty(), 1),
                        new KeyValue(timeUpLabel.scaleYProperty(), 1)
                )
        );
        bounce.play();

        questionsContainer.getChildren().add(2, timeUpLabel);
    }
}