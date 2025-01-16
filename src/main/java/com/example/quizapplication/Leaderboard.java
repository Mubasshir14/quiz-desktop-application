package com.example.quizapplication;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class Leaderboard {
    private Scene scene;

    public Leaderboard(Stage primaryStage, String roomPIN) {
        VBox leaderboardContainer = new VBox(20);
        leaderboardContainer.setAlignment(Pos.CENTER);
        leaderboardContainer.setPadding(new Insets(30));
        leaderboardContainer.setStyle("-fx-background-color: #f4f4f4; -fx-border-radius: 10;");

        Label title = new Label("Leaderboard");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 36));
        title.setTextFill(Color.web("#2c3e50"));

        Label roomInfo = new Label("Room PIN: " + roomPIN);
        roomInfo.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 18));
        roomInfo.setTextFill(Color.web("#7f8c8d"));

        TableView<Participant> table = new TableView<>();
        table.setPrefWidth(400);
        table.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ddd; -fx-border-radius: 5;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Adjust columns to fill the table width
        table.setMinHeight(300); // Set a minimum height for the table

        TableColumn<Participant, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(200);

        TableColumn<Participant, Integer> marksColumn = new TableColumn<>("Marks");
        marksColumn.setCellValueFactory(new PropertyValueFactory<>("marks"));
        marksColumn.setPrefWidth(200);

        table.getColumns().addAll(nameColumn, marksColumn);

        // Fetch data and populate the table
        List<Participant> participants = fetchParticipants(roomPIN);
        table.getItems().addAll(participants);

        leaderboardContainer.getChildren().addAll(title, roomInfo, table);

        scene = new Scene(leaderboardContainer, 1200, 600);
    }

    private List<Participant> fetchParticipants(String roomPIN) {
        List<Participant> participants = new ArrayList<>();
        try {
            String url = "http://localhost:8000/api/rooms/results/" + roomPIN;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String jsonResponse = response.body();

            if (response.statusCode() != 200) {
                return participants;
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);

            for (JsonNode node : rootNode) {
                String participantName = node.path("participant").asText();
                int marks = node.path("marks").asInt();
                participants.add(new Participant(participantName, marks));
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return participants;
    }

    public Scene getScene() {
        return scene;
    }

    public static class Participant {
        private final String name;
        private final int marks;

        public Participant(String name, int marks) {
            this.name = name;
            this.marks = marks;
        }

        public String getName() {
            return name;
        }

        public int getMarks() {
            return marks;
        }
    }
}
