package com.example.quizapplication;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QuestionBuilder {

    @JsonCreator
    public QuestionBuilder(
            @JsonProperty("question") String question,
            @JsonProperty("options") String[] options,
            @JsonProperty("correctAnswer") int correctAnswer,
            @JsonProperty("time") int time,
            @JsonProperty("_id") String _id) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.time = time;
        this._id = _id;
    }

    // Method to build a Question instance
    public Question build() {
        return new Question(question, options, correctAnswer, time, _id);
    }

    // Attributes to hold data
    private String question;
    private String[] options;
    private int correctAnswer;
    private int time;
    private String _id;

    // Getters and setters
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }
}
