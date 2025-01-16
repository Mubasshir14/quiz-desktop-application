package com.example.quizapplication;

import java.util.ArrayList;

public class Room {
    private String pin;
    private ArrayList<Question> questions;

    public Room(String pin, ArrayList<Question> questions) {
        this.pin = pin;
        this.questions = questions;
    }

    // Getters and setters
    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }
    public ArrayList<Question> getQuestions() { return questions; }
    public void setQuestions(ArrayList<Question> questions) { this.questions = questions; }
}