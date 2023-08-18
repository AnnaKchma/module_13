package org.example;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.IntStream;

public class JsonPlaceholderTasksApp {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static void main(String[] args) {
        getAndPrintOpenTasksForUser(1);
    }

    public static void getAndPrintOpenTasksForUser(int userId) {
        try {
            URL userTasksUrl = new URL(BASE_URL + "/users/" + userId + "/todos");
            HttpURLConnection userTasksConnection = (HttpURLConnection) userTasksUrl.openConnection();
            userTasksConnection.setRequestMethod("GET");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(userTasksConnection.getInputStream()))) {
                String line;
                StringBuilder responseContent = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    responseContent.append(line);
                }

                JSONArray userTasks = new JSONArray(responseContent.toString());

                System.out.println("Open tasks for user " + userId + ":");
                IntStream.range(0, userTasks.size())
                        .mapToObj(userTasks::getJSONObject)
                        .forEach(JsonPlaceholderTasksApp::accept);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void accept(JSONObject task) {
        boolean completed = task.getBool("completed");
        if (!completed) {
            String title = task.getStr("title");
            System.out.println("- " + title);
        }
    }
}