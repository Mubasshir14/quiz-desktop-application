package com.example.quizapplication;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class Leaderboard {
    private Scene scene;
    private final Stage primaryStage;
    private List<Participant> participants;

    public Leaderboard(Stage primaryStage, String roomPIN) {
        this.primaryStage = primaryStage;

        // Create main container with gradient background
        VBox leaderboardContainer = new VBox(25);
        leaderboardContainer.setAlignment(Pos.TOP_CENTER);
        leaderboardContainer.setPadding(new Insets(40));
        leaderboardContainer.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #1a237e, #283593);" +
                        "-fx-background-radius: 10;"
        );

        // Header section
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);

        Label title = new Label("🏆 Leaderboard");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 42));
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);");

        Label roomInfo = new Label("Room PIN: " + roomPIN);
        roomInfo.setFont(Font.font("Segoe UI", FontWeight.MEDIUM, 20));
        roomInfo.setTextFill(Color.web("#E0E0E0"));

        headerBox.getChildren().addAll(title, roomInfo);

        // Table setup
        TableView<Participant> table = new TableView<>();
        table.setStyle(
                "-fx-background-color: rgba(255,255,255,0.95);" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 15, 0, 0, 5);" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 14px;"
        );
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMinHeight(400);
        table.setPrefWidth(800);

        // Custom column styling
        TableColumn<Participant, String> rankColumn = new TableColumn<>("Rank");
        rankColumn.setCellValueFactory(param -> {
            int index = table.getItems().indexOf(param.getValue()) + 1;
            return javafx.beans.binding.Bindings.createStringBinding(() -> String.valueOf(index));
        });
        rankColumn.setPrefWidth(100);
        rankColumn.setStyle("-fx-alignment: CENTER; -fx-font-weight: BOLD;");

        TableColumn<Participant, String> nameColumn = new TableColumn<>("Participant");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(400);
        nameColumn.setStyle("-fx-font-weight: BOLD;");

        TableColumn<Participant, Integer> marksColumn = new TableColumn<>("Score");
        marksColumn.setCellValueFactory(new PropertyValueFactory<>("marks"));
        marksColumn.setPrefWidth(300);
        marksColumn.setStyle("-fx-alignment: CENTER; -fx-font-weight: BOLD;");

        // Enhanced cell styling
        nameColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
                    setStyle("-fx-padding: 10 15; -fx-text-fill: #2c3e50;");
                }
            }
        });

        marksColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.valueOf(item));
                    setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
                    setStyle("-fx-padding: 10; -fx-text-fill: #2c3e50;");
                }
            }
        });

        rankColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
                    setTextAlignment(TextAlignment.CENTER);

                    // Enhanced styling for top 3 ranks
                    switch (item) {
                        case "1" -> setStyle(
                                "-fx-text-fill: #FFD700; -fx-font-size: 18px; " +
                                        "-fx-padding: 10; -fx-background-color: rgba(255,215,0,0.1);"
                        );
                        case "2" -> setStyle(
                                "-fx-text-fill: #C0C0C0; -fx-font-size: 16px; " +
                                        "-fx-padding: 10; -fx-background-color: rgba(192,192,192,0.1);"
                        );
                        case "3" -> setStyle(
                                "-fx-text-fill: #CD7F32; -fx-font-size: 15px; " +
                                        "-fx-padding: 10; -fx-background-color: rgba(205,127,50,0.1);"
                        );
                        default -> setStyle(
                                "-fx-text-fill: #2c3e50; -fx-padding: 10;"
                        );
                    }
                }
            }
        });

        table.getColumns().addAll(rankColumn, nameColumn, marksColumn);

        // Fetch and populate data
        participants = fetchParticipants(roomPIN);
        table.getItems().addAll(participants);

        // Export buttons container
        HBox buttonContainer = new HBox(20);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(20, 0, 0, 0));

        Button exportPdfButton = createStyledButton("Export as PDF", "pdf");
        Button exportExcelButton = createStyledButton("Export as Excel", "excel");

        buttonContainer.getChildren().addAll(exportPdfButton, exportExcelButton);

        // Add components to main container
        leaderboardContainer.getChildren().addAll(headerBox, table, buttonContainer);

        // Create scene and add animation
        scene = new Scene(leaderboardContainer, 1200, 800);

        // Add fade-in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), leaderboardContainer);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        // Export button handlers
//        exportPdfButton.setOnAction(e -> exportToPdf(roomPIN));
//        exportExcelButton.setOnAction(e -> exportToExcel(roomPIN));
    }

    private Button createStyledButton(String text, String type) {
        Button button = new Button(text);
        String baseStyle =
                "-fx-background-radius: 20; " +
                        "-fx-font-family: 'Segoe UI'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: BOLD; " +
                        "-fx-padding: 12 25; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);";

        String pdfStyle = baseStyle +
                "-fx-background-color: #e74c3c; " +
                "-fx-text-fill: white;";

        String excelStyle = baseStyle +
                "-fx-background-color: #27ae60; " +
                "-fx-text-fill: white;";

        button.setStyle(type.equals("pdf") ? pdfStyle : excelStyle);

        // Add hover effect
        button.setOnMouseEntered(e -> {
            String hoveredPdfStyle = pdfStyle + "-fx-background-color: #c0392b;";
            String hoveredExcelStyle = excelStyle + "-fx-background-color: #219a52;";
            button.setStyle(type.equals("pdf") ? hoveredPdfStyle : hoveredExcelStyle);
        });

        button.setOnMouseExited(e -> {
            button.setStyle(type.equals("pdf") ? pdfStyle : excelStyle);
        });

        return button;
    }

//    private void exportToPdf(String roomPIN) {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Save PDF File");
//        fileChooser.getExtensionFilters().add(
//                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
//        );
//        fileChooser.setInitialFileName("leaderboard_" + roomPIN + ".pdf");
//
//        File file = fileChooser.showSaveDialog(primaryStage);
//        if (file != null) {
//            try {
//                Document document = new Document(PageSize.A4);
//                PdfWriter.getInstance(document, new FileOutputStream(file));
//                document.open();
//
//                // Add title
//                Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD);
//                Paragraph title = new Paragraph("Leaderboard - Room " + roomPIN, titleFont);
//                title.setAlignment(Element.ALIGN_CENTER);
//                title.setSpacingAfter(20);
//                document.add(title);
//
//                // Create table
//                PdfPTable pdfTable = new PdfPTable(3);
//                pdfTable.setWidthPercentage(100);
//                pdfTable.setWidths(new float[]{1, 3, 2});
//
//                // Add headers
//                Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
//                Stream.of("Rank", "Participant", "Score")
//                        .forEach(columnTitle -> {
//                            PdfPCell header = new PdfPCell();
//                            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
//                            header.setBorderWidth(2);
//                            header.setPhrase(new Phrase(columnTitle, headerFont));
//                            header.setPadding(8);
//                            pdfTable.addCell(header);
//                        });
//
//                // Add data
//                Font dataFont = new Font(Font.FontFamily.HELVETICA, 11);
//                for (int i = 0; i < participants.size(); i++) {
//                    Participant participant = participants.get(i);
//
//                    pdfTable.addCell(new Phrase(String.valueOf(i + 1), dataFont));
//                    pdfTable.addCell(new Phrase(participant.getName(), dataFont));
//                    pdfTable.addCell(new Phrase(String.valueOf(participant.getMarks()), dataFont));
//                }
//
//                document.add(pdfTable);
//                document.close();
//            } catch (Exception e) {
//                showError("Failed to export PDF", e.getMessage());
//            }
//        }
//    }
//
//    private void exportToExcel(String roomPIN) {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Save Excel File");
//        fileChooser.getExtensionFilters().add(
//                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
//        );
//        fileChooser.setInitialFileName("leaderboard_" + roomPIN + ".xlsx");
//
//        File file = fileChooser.showSaveDialog(primaryStage);
//        if (file != null) {
//            try (XSSFWorkbook workbook = new XSSFWorkbook()) {
//                XSSFSheet sheet = workbook.createSheet("Leaderboard");
//
//                // Create header row
//                Row headerRow = sheet.createRow(0);
//                String[] columns = {"Rank", "Participant", "Score"};
//
//                // Style for headers
//                XSSFCellStyle headerStyle = workbook.createCellStyle();
//                headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//                headerStyle.setBorderBottom(BorderStyle.THIN);
//                headerStyle.setBorderTop(BorderStyle.THIN);
//                headerStyle.setBorderLeft(BorderStyle.THIN);
//                headerStyle.setBorderRight(BorderStyle.THIN);
//
//                XSSFFont headerFont = workbook.createFont();
//                headerFont.setBold(true);
//                headerStyle.setFont(headerFont);
//
//                for (int i = 0; i < columns.length; i++) {
//                    Cell cell = headerRow.createCell(i);
//                    cell.setCellValue(columns[i]);
//                    cell.setCellStyle(headerStyle);
//                }
//
//                // Add data rows
//                XSSFCellStyle dataStyle = workbook.createCellStyle();
//                dataStyle.setBorderBottom(BorderStyle.THIN);
//                dataStyle.setBorderTop(BorderStyle.THIN);
//                dataStyle.setBorderLeft(BorderStyle.THIN);
//                dataStyle.setBorderRight(BorderStyle.THIN);
//
//                for (int i = 0; i < participants.size(); i++) {
//                    Row row = sheet.createRow(i + 1);
//                    Participant participant = participants.get(i);
//
//                    Cell rankCell = row.createCell(0);
//                    rankCell.setCellValue(i + 1);
//                    rankCell.setCellStyle(dataStyle);
//
//                    Cell nameCell = row.createCell(1);
//                    nameCell.setCellValue(participant.getName());
//                    nameCell.setCellStyle(dataStyle);
//
//                    Cell scoreCell = row.createCell(2);
//                    scoreCell.setCellValue(participant.getMarks());
//                    scoreCell.setCellStyle(dataStyle);
//                }
//
//                // Auto-size columns
//                for (int i = 0; i < columns.length; i++) {
//                    sheet.autoSizeColumn(i);
//                }
//
//                // Write to file
//                try (FileOutputStream fileOut = new FileOutputStream(file)) {
//                    workbook.write(fileOut);
//                }
//            } catch (Exception e) {
//                showError("Failed to export Excel", e.getMessage());
//            }
//        }
//    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Export Error");
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
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
                showError("Data Fetch Error", "Failed to fetch participant data");
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
            showError("Connection Error", "Failed to connect to the server: " + e.getMessage());
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