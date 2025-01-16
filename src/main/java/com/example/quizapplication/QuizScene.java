package com.example.quizapplication;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class QuizScene {
    private Scene scene;
    private int questionIndex = 0;
    private int score = 0;
    private String[][] questions;
    private Button option1, option2, option3, option4;
    private Timeline timeline;
    private int timeSeconds = 10;
    private Label timerLabel;
    private Label questionLabel;
    private Stage primaryStage;
    private String userName;

    public QuizScene(Stage primaryStage, String subCategory, String userName) {
        this.primaryStage = primaryStage;
        this.userName = userName;

        // Initialize questions based on subcategory
        initializeQuestions(subCategory);

        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fx-padding: 20px; -fx-background-color: linear-gradient(to bottom right, #1a2a6c, #b21f1f, " +
                "#fdbb2d); -fx-font-family: Arial, sans-serif;");

        // Category label
        Label categoryLabel = new Label(subCategory + " Quiz");
        categoryLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold;");
        categoryLabel.setAlignment(Pos.CENTER);

        // Timer label
        timerLabel = new Label("Time: 10");
        timerLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold;");

        // Question label with improved centering
        questionLabel = new Label();
        questionLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10; -fx-background-color: rgba(0,0,0,0.3);");
        questionLabel.setWrapText(true);
        questionLabel.setMaxWidth(800);
        questionLabel.setAlignment(Pos.CENTER);
        questionLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);


        // Initialize option buttons
        option1 = createOptionButton();
        option2 = createOptionButton();
        option3 = createOptionButton();
        option4 = createOptionButton();

        // Set action handling for buttons
        option1.setOnAction(event -> handleAnswer(primaryStage, option1, option1.getText(), userName));
        option2.setOnAction(event -> handleAnswer(primaryStage, option2, option2.getText(), userName));
        option3.setOnAction(event -> handleAnswer(primaryStage, option3, option3.getText(), userName));
        option4.setOnAction(event -> handleAnswer(primaryStage, option4, option4.getText(), userName));

        // Next button
        Button nextButton = new Button("Next");
        nextButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px 20px; " +
                "-fx-font-size: 16px; -fx-border-radius: 20px;");

        nextButton.setOnAction(event -> moveToNextQuestion());

        // Back button
        Button backButton = new Button("Back to Categories");
        backButton.setStyle("-fx-background-color: #ff6a00; -fx-text-fill: white; -fx-padding: 10px 20px; " +
                "-fx-font-size: 16px; -fx-border-radius: 20px;");

        backButton.setOnAction(event -> {
            stopTimer();
            QuizCategoryScene categoryScene = new QuizCategoryScene(primaryStage, userName);
            primaryStage.setScene(categoryScene.getScene());
        });

        vBox.getChildren().addAll(categoryLabel, timerLabel, questionLabel, option1, option2, option3, option4, nextButton, backButton);
        scene = new Scene(vBox, 1200, 600);
        loadQuestion(questionLabel, option1, option2, option3, option4);
        startTimer();
    }

    private void startTimer() {
        timeSeconds = 10;
        timerLabel.setText("Time: " + timeSeconds);

        if (timeline != null) {
            timeline.stop();
        }

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeSeconds--;
            timerLabel.setText("Time: " + timeSeconds);

            if (timeSeconds <= 0) {
                timeline.stop();
                handleTimeUp();
            }
        }));

        timeline.setCycleCount(11); // 10 seconds + 1 for initial display
        timeline.play();
    }

    private void stopTimer() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    private void handleTimeUp() {
        disableButtons();
        int answerIndex = Integer.parseInt(questions[questionIndex][5]);
        highlightCorrectAnswer(answerIndex);

        // Wait for 2 seconds to show the correct answer, then move to next question
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> moveToNextQuestion());
        pause.play();
    }

    private void moveToNextQuestion() {
        stopTimer();
        questionIndex++;
        if (questionIndex < questions.length) {
            loadQuestion(questionLabel, option1, option2, option3, option4);
            resetButtonStyles();
            enableButtons();
            startTimer();
        } else {
            ResultsScene resultsScene = new ResultsScene(primaryStage, score, questions.length, userName);
            primaryStage.setScene(resultsScene.getScene());
        }
    }

    private Button createOptionButton() {
        Button button = new Button();
        button.setStyle("-fx-background-color: #E0E0E0; -fx-text-fill: black; -fx-padding: 20px 40px; " +
                "-fx-font-size: 18px; -fx-min-width: 300px; -fx-max-width: 600px; -fx-wrap-text: true;");
        return button;
    }

    private void initializeQuestions(String subCategory) {
        switch (subCategory) {
            case "Physics":
                questions = new String[][]{
                        {"What is Newton's First Law of Motion?", "Law of Inertia", "Law of Action-Reaction", "Law of Acceleration", "Law of Gravity", "1"},
                        {"What is the unit of electric current?", "Volt", "Watt", "Ampere", "Ohm", "3"},
                        {"What is the speed of light in vacuum?", "299,792,458 m/s", "300,000,000 m/s", "299,792 km/s", "300,000 km/s", "1"}
                        // Add more physics questions
                };
                break;
            case "Chemistry":
                questions = new String[][]{
                        {"What is the atomic number of Carbon?", "6", "12", "14", "8", "1"},
                        {"What is the chemical formula for table salt?", "NaCl", "H2O", "CO2", "O2", "1"},
                        {"What is the pH value of pure water?", "0", "7", "14", "1", "2"}
                        // Add more chemistry questions
                };
                break;
            case "Ancient":
                questions = new String[][]{
                        {"Who was the first emperor of the Roman Empire?", "Augustus", "Julius Caesar", "Nero", "Caligula", "1"},
                        {"What is the most famous wonder of the ancient world?", "Great Pyramid of Giza", "Hanging Gardens of Babylon", "Statue of Zeus", "Mausoleum at Halicarnassus", "1"},
                        // Add more ancient history questions here
                };
                break;

            case "Medieval":
                questions = new String[][]{
                        {"Who was the first king of England?", "Egbert", "Alfred the Great", "William the Conqueror", "Edward the Confessor", "1"},
                        {"What was the primary weapon of knights?", "Sword", "Spear", "Bow", "Axe", "1"},
                        // Add more medieval history questions here
                };
                break;

            case "Modern":
                questions = new String[][]{
                        {"Who invented the telephone?", "Alexander Graham Bell", "Thomas Edison", "Nikola Tesla", "Michael Faraday", "1"},
                        {"In which year did World War II end?", "1945", "1918", "1939", "1950", "1"},
                        // Add more modern history questions here
                };
                break;

            case "Java":
                questions = new String[][]{
                        {"What does JVM stand for?", "Java Virtual Machine", "Java Variable Machine", "Java Version Manager", "Java Visual Manager", "1"},
                        {"Which of the following is a valid declaration of a string in Java?", "String str = \"Hello\";", "String str = Hello;", "String str = 'Hello';", "String str = new String('Hello');", "1"},
                        // Add more Java questions here
                };
                break;

            case "Python":
                questions = new String[][]{
                        {"Which of the following is a Python data type?", "List", "Array", "Dictionary", "Tuple", "1"},
                        {"How do you start a comment in Python?", "# comment", "// comment", "/* comment */", "//", "1"},
                        // Add more Python questions here
                };
                break;

            case "C++":
                questions = new String[][]{
                        {"What is the correct syntax for creating a C++ class?", "class MyClass {}", "MyClass class {}", "class: MyClass {}", "MyClass {}", "1"},
                        {"Which of the following is not a valid C++ keyword?", "function", "class", "if", "while", "1"},
                        // Add more C++ questions here
                };
                break;

            case "JavaScript":
                questions = new String[][]{
                        {"What is the correct syntax for declaring a JavaScript variable?", "let x = 5;", "var = x 5;", "x = 5;", "int x = 5;", "1"},
                        {"Which of the following is used to define a function in JavaScript?", "function", "def", "func", "fun", "1"},
                        // Add more JavaScript questions here
                };
                break;

            case "Arrays":
                questions = new String[][]{
                        {"What is the index of the first element in an array?", "0", "1", "-1", "None of the above", "1"},
                        {"Which of the following methods adds an element at the end of an array?", "push()", "pop()", "shift()", "unshift()", "1"},
                        // Add more array questions here
                };
                break;

            case "Linked Lists":
                questions = new String[][]{
                        {"What is a linked list?", "A linear data structure", "A non-linear data structure", "A type of array", "A type of queue", "1"},
                        {"Which of the following is a characteristic of a linked list?", "Elements are not stored in contiguous memory locations", "Elements are stored in contiguous memory locations", "All elements are of the same type", "None of the above", "1"},
                        // Add more linked list questions here
                };
                break;

            case "Stacks":
                questions = new String[][]{
                        {"Which operation is not possible in a stack?", "Delete middle element", "Push", "Pop", "Peek", "1"},
                        {"Which data structure follows Last In First Out (LIFO)?", "Stack", "Queue", "Linked List", "Array", "1"},
                        // Add more stack questions here
                };
                break;

            case "Queues":
                questions = new String[][]{
                        {"Which data structure follows First In First Out (FIFO)?", "Queue", "Stack", "Linked List", "Array", "1"},
                        {"Which operation is not possible in a queue?", "Insert element at any position", "Enqueue", "Dequeue", "Peek", "1"},
                        // Add more queue questions here
                };
                break;

            case "Machine Learning":
                questions = new String[][]{
                        {"What is the main goal of supervised learning?", "Predict outcomes from labeled data", "Group similar data", "Classify data based on a distance measure", "Minimize error in unsupervised tasks", "1"},
                        {"Which algorithm is used in classification problems?", "Decision Trees", "Linear Regression", "K-Means", "Gradient Descent", "1"},
                        // Add more machine learning questions here
                };
                break;

            case "Ethical Hacking":
                questions = new String[][]{
                        {"What is the first step in ethical hacking?", "Reconnaissance", "Exploitation", "Reporting", "Scanning", "1"},
                        {"Which tool is used for network scanning?", "Nmap", "Wireshark", "Metasploit", "Burp Suite", "1"},
                        // Add more ethical hacking questions here
                };
                break;

            case "Penetration Testing":
                questions = new String[][]{
                        {"What is the main objective of penetration testing?", "To identify vulnerabilities in systems", "To build secure systems", "To patch vulnerabilities", "To scan for malware", "1"},
                        {"What is used to exploit a vulnerability in penetration testing?", "Exploit", "Payload", "Scan", "None of the above", "1"},
                        // Add more penetration testing questions here
                };
                break;

            case "TCP/IP":
                questions = new String[][]{
                        {"What does TCP stand for?", "Transmission Control Protocol", "Transmission Communication Protocol", "Time Control Protocol", "Time Communication Protocol", "1"},
                        {"What is the main difference between TCP and UDP?", "TCP is connection-oriented, UDP is connectionless", "TCP is faster than UDP", "UDP is more reliable than TCP", "TCP and UDP are the same", "1"},
                        // Add more TCP/IP questions here
                };
                break;

            case "Routing":
                questions = new String[][]{
                        {"What is the purpose of routing in networking?", "Directing data packets to their destination", "Establishing network connections", "Encrypting data packets", "Transmitting data over physical links", "1"},
                        {"Which device is used for routing?", "Router", "Switch", "Hub", "Firewall", "1"},
                        // Add more routing questions here
                };
                break;

            case "Switching":
                questions = new String[][]{
                        {"What is the primary function of a switch?", "Forwarding data to the correct device", "Encrypting data", "Directing data packets to their destination", "Routing data between networks", "1"},
                        {"Which layer of the OSI model does a switch operate on?", "Data Link Layer", "Network Layer", "Physical Layer", "Application Layer", "1"},
                        // Add more switching questions here
                };
                break;

            case "Linux":
                questions = new String[][]{
                        {"Which command is used to list files in Linux?", "ls", "dir", "list", "show", "1"},
                        {"What is the default text editor in Linux?", "Vim", "Notepad", "nano", "Emacs", "1"},
                        // Add more Linux questions here
                };
                break;

            case "Windows":
                questions = new String[][]{
                        {"What does Windows 10 offer over previous versions?", "Start Menu, Virtual Desktops, Cortana", "Multiple desktops, Task View, Windows Store", "Windows Media Player, Windows Defender", "All of the above", "1"},
                        {"What is the shortcut to open Task Manager in Windows?", "Ctrl + Shift + Esc", "Ctrl + Alt + Del", "Alt + Tab", "Ctrl + Esc", "1"},
                        // Add more Windows questions here
                };
                break;

            case "MacOS":
                questions = new String[][]{
                        {"Which company developed macOS?", "Apple", "Microsoft", "Google", "Linux Foundation", "1"},
                        {"Which feature is exclusive to macOS?", "Time Machine", "Cortana", "DirectX", "Task View", "1"},
                        // Add more MacOS questions here
                };
                break;

            case "MySQL":
                questions = new String[][]{
                        {"Which of the following is a correct SQL query to select all columns from a table?", "SELECT * FROM table;", "SELECT table FROM *;", "SELECT ALL FROM table;", "SHOW * FROM table;", "1"},
                        {"Which keyword is used to sort the result set in MySQL?", "ORDER BY", "SORT BY", "GROUP BY", "FILTER BY", "1"},
                        // Add more MySQL questions here
                };
                break;

            case "MongoDB":
                questions = new String[][]{
                        {"Which of the following is used to store data in MongoDB?", "Collections", "Tables", "Rows", "Documents", "1"},
                        {"Which method is used to insert a document in MongoDB?", "insertOne()", "add()", "insert()", "create()", "1"},
                        // Add more MongoDB questions here
                };
                break;

            case "PostgreSQL":
                questions = new String[][]{
                        {"Which SQL command is used to retrieve data from a PostgreSQL database?", "SELECT", "GET", "RETRIEVE", "FETCH", "1"},
                        {"Which data type is used to store dates in PostgreSQL?", "DATE", "DATETIME", "TIME", "YEAR", "1"},
                        // Add more PostgreSQL questions here
                };
                break;

            case "Sorting":
                questions = new String[][]{
                        {"Which of the following is not a sorting algorithm?", "Binary Search", "Quick Sort", "Merge Sort", "Bubble Sort", "1"},
                        {"What is the time complexity of Bubble Sort?", "O(n^2)", "O(n log n)", "O(log n)", "O(1)", "1"},
                        // Add more sorting questions here
                };
                break;

            case "Searching":
                questions = new String[][]{
                        {"What is the time complexity of Binary Search?", "O(log n)", "O(n)", "O(n log n)", "O(1)", "1"},
                        {"Which searching algorithm is used to find an element in a sorted list?", "Binary Search", "Linear Search", "Jump Search", "Exponential Search", "1"},
                        // Add more searching questions here
                };
                break;

            case "Graph Algorithms":
                questions = new String[][]{
                        {"Which of the following algorithms is used to find the shortest path in a graph?", "Dijkstra's Algorithm", "Kruskal's Algorithm", "Prim's Algorithm", "Bellman-Ford Algorithm", "1"},
                        {"Which of the following is a traversal algorithm for graphs?", "Breadth-First Search", "Depth-First Search", "Both", "None", "1"},
                        // Add more graph algorithm questions here
                };
                break;
            case "Biology":
                questions = new String[][]{
                        {"What is the powerhouse of the cell?", "Mitochondria", "Nucleus", "Endoplasmic Reticulum", "Golgi Apparatus", "1"},
                        {"Which part of the brain controls balance and coordination?", "Cerebellum", "Medulla Oblongata", "Cerebrum", "Pons", "1"},
                        {"What is the main function of red blood cells?", "Transport oxygen", "Fight infections", "Digest food", "Regulate body temperature", "1"},
                        {"What is the genetic material found in the nucleus of cells?", "DNA", "RNA", "Proteins", "Lipids", "1"},
                        {"Which organ in the human body is primarily responsible for detoxification?", "Liver", "Kidneys", "Lungs", "Heart", "1"},
                        {"What is the process by which plants make their own food?", "Photosynthesis", "Respiration", "Transpiration", "Fermentation", "1"},
                        {"What is the basic unit of life?", "Cell", "Tissue", "Organ", "Organism", "1"},
                        // Add more Biology questions here
                };
                break;

            default:
                questions = new String[][]{
                        {"Default question 1", "Option 1", "Option 2", "Option 3", "Option 4", "1"},
                        {"Default question 2", "Option 1", "Option 2", "Option 3", "Option 4", "2"}
                };
        }
    }

    private void handleAnswer(Stage primaryStage, Button selectedButton, String selectedAnswer, String userName) {
        stopTimer();
        int answerIndex = Integer.parseInt(questions[questionIndex][5]);
        if (selectedAnswer.equals(questions[questionIndex][answerIndex])) {
            score++;
            selectedButton.setStyle(selectedButton.getStyle() + "-fx-background-color: #4CAF50; -fx-text-fill: white;");
        } else {
            selectedButton.setStyle(selectedButton.getStyle() + "-fx-background-color: #FF0000; -fx-text-fill: white;");
            highlightCorrectAnswer(answerIndex);
        }
        disableButtons();

        // Wait for 2 seconds to show the result, then move to next question
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> moveToNextQuestion());
        pause.play();
    }

    private void highlightCorrectAnswer(int answerIndex) {
        Button correctButton = switch (answerIndex) {
            case 1 -> option1;
            case 2 -> option2;
            case 3 -> option3;
            case 4 -> option4;
            default -> null;
        };
        if (correctButton != null) {
            correctButton.setStyle(correctButton.getStyle() + "-fx-background-color: #4CAF50; -fx-text-fill: white;");
        }
    }

    private void loadQuestion(Label questionLabel, Button option1, Button option2, Button option3, Button option4) {
        questionLabel.setText("Question " + (questionIndex + 1) + " of " + questions.length + ":" + "  " + questions[questionIndex][0]);
//        System.out.println("Question text: " + questions[questionIndex][0]);
        option1.setText(questions[questionIndex][1]);
        option2.setText(questions[questionIndex][2]);
        option3.setText(questions[questionIndex][3]);
        option4.setText(questions[questionIndex][4]);
    }

    private void enableButtons() {
        option1.setDisable(false);
        option2.setDisable(false);
        option3.setDisable(false);
        option4.setDisable(false);
    }

    private void disableButtons() {
        option1.setDisable(true);
        option2.setDisable(true);
        option3.setDisable(true);
        option4.setDisable(true);
    }

    private void resetButtonStyles() {
        String defaultStyle = "-fx-background-color: #E0E0E0; -fx-text-fill: black; -fx-padding: 20px 40px; " +
                "-fx-font-size: 18px; -fx-min-width: 300px; -fx-max-width: 600px; -fx-wrap-text: true;";
        option1.setStyle(defaultStyle);
        option2.setStyle(defaultStyle);
        option3.setStyle(defaultStyle);
        option4.setStyle(defaultStyle);
    }

    public Scene getScene() {
        return scene;
    }

    public void cleanup() {
        stopTimer();
    }
}