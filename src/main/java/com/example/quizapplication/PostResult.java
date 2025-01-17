package com.example.quizapplication;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

public class PostResult {
    public static void main(String[] args) {
        String userName = "exampleUser";
        String pin = "1234";
        int score = 100;

        postResultsToBackend(userName, pin, score);
    }

    private static void postResultsToBackend(String userName, String pin, int score) {
        String url = "https://quiz-six-khaki.vercel.app/api/rooms/addParticipant";
        String jsonInputString = String.format("{\"pin\": \"%s\", \"name\": \"%s\", \"marks\": %d}", pin, userName, score);

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
}

