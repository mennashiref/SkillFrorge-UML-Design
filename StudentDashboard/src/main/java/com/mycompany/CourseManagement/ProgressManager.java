package com.mycompany.CourseManagement;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mycompany.JsonHandler.JsonHandler;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProgressManager {
    private static final String PROGRESS_FILE = "progress.json";

    public static void markLessonCompleted(String studentId, String courseId, String lessonId) throws IOException {
        JsonHandler.initializeFileIfNeeded(PROGRESS_FILE);
        ArrayNode progressArray = JsonHandler.readArrayFromFile(PROGRESS_FILE);

  
        for (int i = 0; i < progressArray.size(); i++) {
            JsonNode node = progressArray.get(i);
            if (node.get("studentId").asText().equals(studentId) &&
                node.get("courseId").asText().equals(courseId) &&
                node.get("lessonId").asText().equals(lessonId)) {
                return; 
            }
        }

        Map<String, String> progress = new HashMap<>();
        progress.put("studentId", studentId);
        progress.put("courseId", courseId);
        progress.put("lessonId", lessonId);
        progress.put("completed", "true");

        progressArray.add(JsonHandler.objectMapper.valueToTree(progress));
        JsonHandler.writeToFile(progressArray, PROGRESS_FILE);
    }

    public static int getCompletedLessonsCount(String studentId, String courseId) throws IOException {
        JsonHandler.initializeFileIfNeeded(PROGRESS_FILE);
        ArrayNode progressArray = JsonHandler.readArrayFromFile(PROGRESS_FILE);
        int count = 0;

        for (int i = 0; i < progressArray.size(); i++) {
            JsonNode node = progressArray.get(i);
            if (node.get("studentId").asText().equals(studentId) &&
                node.get("courseId").asText().equals(courseId)) {
                count++;
            }
        }
        return count;
    }


    public static boolean isLessonCompleted(String studentId, String courseId, String lessonId) throws IOException {
        JsonHandler.initializeFileIfNeeded(PROGRESS_FILE);
        ArrayNode progressArray = JsonHandler.readArrayFromFile(PROGRESS_FILE);

        for (int i = 0; i < progressArray.size(); i++) {
            JsonNode node = progressArray.get(i);
            if (node.get("studentId").asText().equals(studentId) &&
                node.get("courseId").asText().equals(courseId) &&
                node.get("lessonId").asText().equals(lessonId)) {
                return true;
            }
        }
        return false;
    }
}
