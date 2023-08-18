package org.example;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPOutputStream;

public class JsonPlaceholderCommentsApp {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static void main(String[] args) {
        getAndSaveCommentsForLastPostOfUser(1);
    }

    public static void getAndSaveCommentsForLastPostOfUser(int userId) {
        try {
            URL userPostsUrl = new URL(BASE_URL + "/users/" + userId + "/posts");
            HttpURLConnection userPostsConnection = (HttpURLConnection) userPostsUrl.openConnection();
            userPostsConnection.setRequestMethod("GET");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(userPostsConnection.getInputStream()))) {
                String line;
                StringBuilder responseContent = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    responseContent.append(line);
                }

                // Parse the JSON array of posts using HuTool
                JSONArray userPosts = new JSONArray(responseContent.toString());

                if (userPosts.size() <= 0) {
                    System.out.println("No posts found for the user.");
                } else {
                    JSONObject lastPost = userPosts.getJSONObject(userPosts.size() - 1);
                    int postId = lastPost.getInt("id");
                    saveCommentsToFile(userId, postId);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveCommentsToFile(int userId, int postId) {
        try {
            URL postCommentsUrl = new URL(BASE_URL + "/posts/" + postId + "/comments");
            HttpURLConnection commentsConnection = (HttpURLConnection) postCommentsUrl.openConnection();
            commentsConnection.setRequestMethod("GET");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(commentsConnection.getInputStream()))) {
                String line;
                StringBuilder responseContent = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    responseContent.append(line);
                }

                // Parse the JSON array of comments using HuTool
                JSONArray comments = new JSONArray(responseContent.toString());

                String fileName = "user-" + userId + "-post-" + postId + "-comments.json";
                try (OutputStream os = new GZIPOutputStream(new FileOutputStream(fileName))) {
                    os.write(comments.toString().getBytes(StandardCharsets.UTF_8));
                }

                System.out.println("Comments saved to " + fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}