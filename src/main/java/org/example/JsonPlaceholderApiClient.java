package org.example;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JsonPlaceholderApiClient {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static void main() {
        main(null);
    }

    public static void main(String... args) {
        createUser();
    }

    public static void createUser() {
        try {
            URL url = new URL(BASE_URL + "/users");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String newUserJson = "{\"name\":\"John Doe\",\"username\":\"john doe\"}";

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = newUserJson.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            connection.connect(); // Establish the connection before getting the response code

            int responseCode = connection.getResponseCode();
            System.out.println("Create user response code: " + responseCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}




