package com.example.quizapplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HttpRequest {
    public static String sendPostRequest(String urlString, String jsonInputString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set the request method and headers
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        // Send the POST request body
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }

        // Get the response
        int responseCode = connection.getResponseCode();

        // Read the response - handle both success and error streams
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        responseCode >= 200 && responseCode < 300
                                ? connection.getInputStream()
                                : connection.getErrorStream()))) {

            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }

        connection.disconnect();

        // Return both response code and body
        if (responseCode >= 200 && responseCode < 300) {
            return "Success - Response Code: " + responseCode + ", Body: " + response.toString();
        } else {
            return "Error - Response Code: " + responseCode + ", Body: " + response.toString();
        }
    }
}