package com.example.quizapplication;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.*;
import javafx.scene.text.Font;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Alert;

public class LiveQuizCategory {
    private Scene scene;
    private static HashMap<String, ArrayList<String>> activeRooms = new HashMap<>();

    public LiveQuizCategory(Stage primaryStage, String userName) {
        VBox mainContainer = new VBox(30);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(30));
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom right, #1a2a6c, #b21f1f, #fdbb2d);");


        Label titleLabel = new Label("Live Quiz Room");
        titleLabel.setStyle("-fx-font-size: 42px; -fx-font-weight: bold; -fx-text-fill: #FFD700;");


        Button createRoomButton = createStyledButton("Create Quiz Room");
        Button joinRoomButton = createStyledButton("Join Quiz Room");


        Button backButton = createStyledButton("← Back to Categories");
        backButton.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-padding: 10px 20px;" +
                "-fx-background-radius: 20px;" +
                "-fx-cursor: hand;");


        createRoomButton.setOnAction(e -> showCreateRoomDialog(primaryStage, userName));
        joinRoomButton.setOnAction(e -> showJoinRoomDialog(primaryStage, userName));
        backButton.setOnAction(e -> {
            QuizCategoryScene categoryScene = new QuizCategoryScene(primaryStage, userName);
            primaryStage.setScene(categoryScene.getScene());
        });

        mainContainer.getChildren().addAll(titleLabel, createRoomButton, joinRoomButton, backButton);
        scene = new Scene(mainContainer, 1200, 600);
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.15);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25px;" +
                        "-fx-padding: 15px 30px;" +
                        "-fx-min-width: 250px;" +
                        "-fx-cursor: hand;"
        );
        return button;
    }

    private void showCreateRoomDialog(Stage primaryStage, String userName) {
        Stage dialog = new Stage();
        dialog.setTitle("Create Quiz Room");


        VBox dialogContent = new VBox(20);
        dialogContent.setPadding(new Insets(30));
        dialogContent.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #dcdcdc; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 10px;");


        Label titleLabel = new Label("Create New Quiz Room");
        titleLabel.setFont(new Font("Arial", 20));
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #FFD700;");


        Label pinLabel = new Label("Enter Room Pin(4 Digits)");
        pinLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #555;");
        TextField pinField = new TextField();
        pinField.setPromptText("Enter Room PIN (4 digits)");
        pinField.setStyle("-fx-padding: 10px; -fx-font-size: 14px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-border-color: #ccc;");


        Label questionNumberLabel = new Label("Give Number of Questions");
        questionNumberLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #555;");
        TextField numQuestionsField = new TextField();
        numQuestionsField.setPromptText("Enter number of questions");
        numQuestionsField.setStyle("-fx-padding: 10px; -fx-font-size: 14px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-border-color: #ccc;");


        Button nextButton = new Button("Next");
        nextButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        nextButton.setOnMouseEntered(e -> nextButton.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-radius: 8px; -fx-background-radius: 8px;"));
        nextButton.setOnMouseExited(e -> nextButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-radius: 8px; -fx-background-radius: 8px;"));

        nextButton.setOnAction(e -> {
            String pin = pinField.getText();
            if (pin.length() != 4 || !pin.matches("\\d+")) {
                showAlert("Invalid PIN", "Please enter a 4-digit PIN.");
                return;
            }

            if (activeRooms.containsKey(pin)) {
                showAlert("Room Exists", "This PIN is already in use. Please choose another.");
                return;
            }

            int numQuestions;
            try {
                numQuestions = Integer.parseInt(numQuestionsField.getText());
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter a valid number for questions.");
                return;
            }

            showQuestionsInputDialog(dialog, pin, numQuestions, primaryStage, userName);
        });


        dialogContent.getChildren().addAll(
                titleLabel,
                pinLabel,
                pinField,
                questionNumberLabel,
                numQuestionsField,
                nextButton
        );
        dialogContent.setAlignment(Pos.CENTER);


        Scene dialogScene = new Scene(dialogContent, 450, 400);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void showQuestionsInputDialog(Stage dialog, String pin, int numQuestions, Stage primaryStage, String userName) {
        VBox dialogContent = new VBox(15);
        dialogContent.setPadding(new Insets(20));
        dialogContent.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ddd; -fx-border-width: 1px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Label titleLabel = new Label("Create Quiz Questions");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #FFD700;");

        Label pinLabel = new Label(pin); // Using string concatenation
        pinLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #FFD700;");


        Separator separator = new Separator();
        dialogContent.getChildren().addAll(titleLabel, separator);

        for (int i = 1; i <= numQuestions; i++) {
            VBox questionBox = new VBox(10);
            questionBox.setStyle("-fx-background-color: #ffffff; -fx-padding: 15px; -fx-border-color: #ddd; -fx-border-width: 1px; -fx-border-radius: 8px; -fx-background-radius: 8px;");

            Label questionLabel = new Label("Question " + i);
            questionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #555;");

            TextField questionField = new TextField();
            questionField.setPromptText("Enter Question " + i);

            TextField option1Field = new TextField();
            option1Field.setPromptText("Option 1");

            TextField option2Field = new TextField();
            option2Field.setPromptText("Option 2");

            TextField option3Field = new TextField();
            option3Field.setPromptText("Option 3");

            TextField option4Field = new TextField();
            option4Field.setPromptText("Option 4");

            Label correctAnswerLabel = new Label("Select Correct Answer");
            correctAnswerLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #555;");

            ChoiceBox<String> correctAnswerField = new ChoiceBox<>();
            correctAnswerField.getItems().addAll("1", "2", "3", "4");
            correctAnswerField.setValue("1"); // Default to "1"
            correctAnswerField.setStyle("-fx-padding: 10px; -fx-font-size: 14px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-border-color: #ccc;");

            TextField timeField = new TextField();
            timeField.setPromptText("Time (seconds)");

            questionBox.getChildren().addAll(
                    questionLabel, questionField, option1Field, option2Field, option3Field, option4Field,
                    correctAnswerLabel, correctAnswerField, timeField
            );
            dialogContent.getChildren().add(questionBox);
        }

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);




        Button createButton = new Button("Create Room");
        createButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-font-size: 14px; -fx-background-radius: 8px; -fx-border-radius: 8px;");
        createButton.setOnMouseEntered(e -> createButton.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-font-size: 14px; -fx-background-radius: 8px; -fx-border-radius: 8px;"));
        createButton.setOnMouseExited(e -> createButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-font-size: 14px; -fx-background-radius: 8px; -fx-border-radius: 8px;"));
        createButton.setOnAction(e -> {
            try {
                ArrayList<Question> questions = getQuestion(dialog);
                createRoom(pin, numQuestions, questions);
                activeRooms.put(pin, new ArrayList<>());
                dialog.close();
                showAlert("Success", "Room created successfully with PIN: " + pin);
            } catch (Exception ex) {
                showAlert("Error", "Failed to create room: " + ex.getMessage());
                ex.printStackTrace();
            }
        });


        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-font-size: 14px; -fx-background-radius: 8px; -fx-border-radius: 8px;");
        closeButton.setOnMouseEntered(e -> closeButton.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-font-size: 14px; -fx-background-radius: 8px; -fx-border-radius: 8px;"));
        closeButton.setOnMouseExited(e -> closeButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-font-size: 14px; -fx-background-radius: 8px; -fx-border-radius: 8px;"));
        closeButton.setOnAction(e -> dialog.close());

        buttonBox.getChildren().addAll(createButton, closeButton);
        dialogContent.getChildren().add(buttonBox);

        ScrollPane scrollPane = new ScrollPane(dialogContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f0f0f0;");

        Scene dialogScene = new Scene(scrollPane, 450, 600);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    // Add the new getQuestion method here
    private ArrayList<Question> getQuestion(Stage dialog) {
        ArrayList<Question> questions = new ArrayList<>();
        VBox dialogContent = (VBox) ((ScrollPane) dialog.getScene().getRoot()).getContent();

        // Skip the title and separator (first two children)
        for (int i = 2; i < dialogContent.getChildren().size() - 1; i++) { // -1 to skip the button box
            VBox questionBox = (VBox) dialogContent.getChildren().get(i);

            TextField questionField = (TextField) questionBox.getChildren().get(1);
            TextField option1Field = (TextField) questionBox.getChildren().get(2);
            TextField option2Field = (TextField) questionBox.getChildren().get(3);
            TextField option3Field = (TextField) questionBox.getChildren().get(4);
            TextField option4Field = (TextField) questionBox.getChildren().get(5);
            ChoiceBox<String> correctAnswerField = (ChoiceBox<String>) questionBox.getChildren().get(7);
            TextField timeField = (TextField) questionBox.getChildren().get(8);

            String questionText = questionField.getText();
            String[] options = {
                    option1Field.getText(),
                    option2Field.getText(),
                    option3Field.getText(),
                    option4Field.getText()
            };
            int correctAnswer = Integer.parseInt(correctAnswerField.getValue());
            int time = Integer.parseInt(timeField.getText());

            questions.add(new Question(questionText, options, correctAnswer, time));
        }

        return questions;
    }


//    private void showJoinRoomDialog(Stage primaryStage, String userName) {
//        Stage dialog = new Stage();
//        dialog.setTitle("Join Quiz Room");
//
//        VBox dialogContent = new VBox(20);
//        dialogContent.setPadding(new Insets(25));
//        dialogContent.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ccc; -fx-border-radius: 10px; -fx-background-radius: 10px;");
//
//        Label titleLabel = new Label("Join Quiz Room");
//        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #FFD700;");
//
//        Label roomPinLabel = new Label("Enter Room Pin");
//        roomPinLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #555;");
//        TextField pinField = new TextField();
//        pinField.setPromptText("Enter Room PIN");
//        pinField.setStyle("-fx-padding: 10px; -fx-font-size: 14px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-border-color: #ddd;");
//
//        Label idLabel = new Label("Enter Your ID Number");
//        idLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #555;");
//        TextField idField = new TextField();
//        idField.setPromptText("Enter Your ID Number");
//        idField.setStyle("-fx-padding: 10px; -fx-font-size: 14px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-border-color: #ddd;");
//
//        Button joinButton = new Button("Join Room");
//        joinButton.setStyle(
//                "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; " +
//                        "-fx-padding: 10px 20px; -fx-border-radius: 8px; -fx-background-radius: 8px;");
//        joinButton.setOnMouseEntered(e -> joinButton.setStyle(
//                "-fx-background-color: #45a049; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; " +
//                        "-fx-padding: 10px 20px; -fx-border-radius: 8px; -fx-background-radius: 8px;"));
//        joinButton.setOnMouseExited(e -> joinButton.setStyle(
//                "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; " +
//                        "-fx-padding: 10px 20px; -fx-border-radius: 8px; -fx-background-radius: 8px;"));
//
//        joinButton.setOnAction(e -> {
//            String pin = pinField.getText();
//            String participantId = idField.getText();
//
//            if (!activeRooms.containsKey(pin)) {
//                showAlert("Room Not Found", "No room exists with this PIN.");
//                return;
//            }
//
//            ArrayList<String> participants = activeRooms.get(pin);
//            participants.add(participantId + " - " + userName);
//            dialog.close();
//            showAlert("Success", "Successfully joined room with PIN: " + pin);
//        });
//
//
//        Button closeButton = new Button("Close");
//        closeButton.setStyle(
//                "-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; " +
//                        "-fx-padding: 10px 20px; -fx-border-radius: 8px; -fx-background-radius: 8px;");
//        closeButton.setOnMouseEntered(e -> closeButton.setStyle(
//                "-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; " +
//                        "-fx-padding: 10px 20px; -fx-border-radius: 8px; -fx-background-radius: 8px;"));
//        closeButton.setOnMouseExited(e -> closeButton.setStyle(
//                "-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; " +
//                        "-fx-padding: 10px 20px; -fx-border-radius: 8px; -fx-background-radius: 8px;"));
//
//        closeButton.setOnAction(e -> dialog.close());
//
//
//        HBox buttonLayout = new HBox(10, joinButton, closeButton);
//        buttonLayout.setAlignment(Pos.CENTER);
//
//
//        dialogContent.getChildren().addAll(titleLabel, roomPinLabel, pinField, idLabel, idField, buttonLayout);
//        dialogContent.setAlignment(Pos.CENTER);
//
//
//        Scene dialogScene = new Scene(dialogContent, 400, 400);
//        dialog.setScene(dialogScene);
//        dialog.show();
//    }


    private void showJoinRoomDialog(Stage primaryStage, String userName) {
        Stage dialog = new Stage();
        dialog.setTitle("Join Quiz Room");

        VBox dialogContent = new VBox(20);
        dialogContent.setPadding(new Insets(25));
        dialogContent.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ccc; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Label titleLabel = new Label("Join Quiz Room");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #FFD700;");

        Label roomPinLabel = new Label("Enter Room Pin");
        roomPinLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #555;");
        TextField pinField = new TextField();
        pinField.setPromptText("Enter Room PIN");
        pinField.setStyle("-fx-padding: 10px; -fx-font-size: 14px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-border-color: #ddd;");

        Label idLabel = new Label("Enter Your ID Number");
        idLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #555;");
        TextField idField = new TextField();
        idField.setPromptText("Enter Your ID Number");
        idField.setStyle("-fx-padding: 10px; -fx-font-size: 14px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-border-color: #ddd;");

        Button joinButton = new Button("Join Room");
        joinButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        joinButton.setOnMouseEntered(e -> joinButton.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-radius: 8px; -fx-background-radius: 8px;"));
        joinButton.setOnMouseExited(e -> joinButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-radius: 8px; -fx-background-radius: 8px;"));

        joinButton.setOnAction(e -> {
            String pin = pinField.getText();
            String participantId = idField.getText();

            try {
                String jsonInputString = "{\"pin\": \"" + pin + "\", \"userName\": \"" + userName + "\"}";
                String response = HttpRequestJoin.sendPostRequest("http://localhost:8000/api/rooms/join", jsonInputString);

                if (response.contains("Error")) {
                    showAlert("Error", response);
                    return;
                }

                // Parse the response to get questions
                ObjectMapper mapper = new ObjectMapper();
                List<Question> questions = mapper.readValue(response, mapper.getTypeFactory().constructCollectionType(List.class, Question.class));

                JoinLiveQuiz joinLiveQuiz = new JoinLiveQuiz(primaryStage, userName, pin, questions);
                primaryStage.setScene(joinLiveQuiz.getScene());
                dialog.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to join room: " + ex.getMessage());
            }
        });

        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        closeButton.setOnMouseEntered(e -> closeButton.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-radius: 8px; -fx-background-radius: 8px;"));
        closeButton.setOnMouseExited(e -> closeButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-radius: 8px; -fx-background-radius: 8px;"));
        closeButton.setOnAction(e -> dialog.close());

        HBox buttonLayout = new HBox(10, joinButton, closeButton);
        buttonLayout.setAlignment(Pos.CENTER);

        dialogContent.getChildren().addAll(titleLabel, roomPinLabel, pinField, idLabel, idField, buttonLayout);
        dialogContent.setAlignment(Pos.CENTER);

        Scene dialogScene = new Scene(dialogContent, 400, 400);
        dialog.setScene(dialogScene);
        dialog.show();
    }




    private void createRoom(String pin, int numQuestions, ArrayList<Question> questions) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Room room = new Room(pin, questions);
            String jsonString = mapper.writeValueAsString(room);
            System.out.println("Sending JSON to server: " + jsonString); // Add this debug line

            String response = HttpRequest.sendPostRequest("http://localhost:8000/api/rooms/create", jsonString);
            System.out.println("Server Response: " + response);

            showAlert("Success", "Room created successfully with PIN: " + pin);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while creating the room: " + e.getMessage());
        }
    }



    private void joinRoom(String pin, String participantName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
// Correct the Participant class instantiation
            Participant participantObj = new Participant(pin, participantName);
            String jsonString = mapper.writeValueAsString(participantObj);
            String response = HttpRequest.sendPostRequest("http://localhost:8000/api/rooms/join", jsonString);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public Scene getScene() {
        return scene;
    }
}