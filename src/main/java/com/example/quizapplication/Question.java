package com.example.quizapplication;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Question {
    private String question;
    private String[] options;
    private int correctAnswer;
    private int time;
    private String _id;

    // Default constructor
    public Question() {
    }

    // Parameterized constructor with ID
    public Question(String question, String[] options, int correctAnswer, int time, String _id) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.time = time;
        this._id = _id;
    }

    // Parameterized constructor without ID
    public Question(String question, String[] options, int correctAnswer, int time) {
        this(question, options, correctAnswer, time, null);
    }

    // Getters and setters
    @JsonProperty("question")
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @JsonProperty("options")
    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    @JsonProperty("correctAnswer")
    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @JsonProperty("time")
    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @JsonProperty("_id")
    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }
}
