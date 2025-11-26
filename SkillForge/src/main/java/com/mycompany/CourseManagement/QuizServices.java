package com.mycompany.CourseManagement;

import com.mycompany.JsonHandler.JsonHandler;
import com.mycompany.QuizManagement.Question;
import com.mycompany.QuizManagement.Quiz;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuizServices {
    private static final String QUIZ_RESULTS_FILE = "quiz_results.json";
    private static final int MAX_ATTEMPTS = 3;

    public static Quiz createQuiz(String lessonId, ArrayList<Question> questions) throws IOException {
        Quiz quiz = new Quiz(lessonId, questions, 70);
        return quiz;
    }

    public static Question createQuestion(String questionText, String[] options, int correctIndex) {
        ArrayList<String> optionsList = new ArrayList<>();
        for (String option : options) {
            optionsList.add(option);
        }
        return new Question(questionText, optionsList, correctIndex);
    }

    public static void submitQuizResult(String studentId, String quizId, int score, boolean passed) throws IOException {
        JsonHandler.initializeFileIfNeeded(QUIZ_RESULTS_FILE);
        var resultsArray = JsonHandler.readArrayFromFile(QUIZ_RESULTS_FILE);

        System.out.println("=== DEBUG SUBMITTING QUIZ RESULT ===");
        System.out.println("Student: " + studentId);
        System.out.println("Quiz ID: " + quizId);
        System.out.println("Score: " + score + ", Passed: " + passed);

        Map<String, Object> result = new HashMap<>();
        result.put("resultId", "R" + System.currentTimeMillis());
        result.put("studentId", studentId);
        result.put("quizId", quizId);
        result.put("score", score);
        result.put("passed", passed);
        result.put("attemptDate", System.currentTimeMillis());

        String lessonId = extractLessonIdFromQuizId(quizId);
        result.put("lessonId", lessonId);

        System.out.println("Extracted Lesson ID: " + lessonId);

        resultsArray.add(JsonHandler.objectMapper.valueToTree(result));
        JsonHandler.writeToFile(resultsArray, QUIZ_RESULTS_FILE);

        System.out.println("✅ QUIZ RESULT SAVED SUCCESSFULLY");
        System.out.println("Total results for student: " + resultsArray.size());
    }

    private static String extractLessonIdFromQuizId(String quizId) {
        System.out.println("Extracting lesson ID from: " + quizId);

        if (quizId == null || quizId.isEmpty()) {
            System.out.println("Empty quiz ID, defaulting to L1");
            return "L1";
        }

        if (quizId.contains("_L")) {
            String[] parts = quizId.split("_");
            if (parts.length >= 2 && parts[1].startsWith("L")) {
                String lessonId = parts[1];
                System.out.println("Extracted lesson ID from pattern: " + lessonId);
                return lessonId;
            }
        }

        if (quizId.startsWith("L") && quizId.length() >= 2) {
            System.out.println("Quiz ID is already a lesson ID: " + quizId);
            return quizId;
        }

        if (quizId.contains("L1") || quizId.startsWith("QZ1")) {
            System.out.println("Matched L1");
            return "L1";
        }
        if (quizId.contains("L2") || quizId.startsWith("QZ2")) {
            System.out.println("Matched L2");
            return "L2";
        }
        if (quizId.contains("L3") || quizId.startsWith("QZ3")) {
            System.out.println("Matched L3");
            return "L3";
        }

        System.out.println("No pattern matched, defaulting to L1");
        return "L1";
    }

    public static ArrayList<Map<String, Object>> getStudentQuizResults(String studentId) throws IOException {
        JsonHandler.initializeFileIfNeeded(QUIZ_RESULTS_FILE);
        var resultsArray = JsonHandler.readArrayFromFile(QUIZ_RESULTS_FILE);

        ArrayList<Map<String, Object>> studentResults = new ArrayList<>();

        System.out.println("=== DEBUG GETTING STUDENT RESULTS ===");
        System.out.println("Student: " + studentId);
        System.out.println("Total results in file: " + resultsArray.size());

        for (var resultNode : resultsArray) {
            if (resultNode.get("studentId").asText().equals(studentId)) {
                Map<String, Object> result = new HashMap<>();
                result.put("quizId", resultNode.get("quizId").asText());
                result.put("score", resultNode.get("score").asInt());
                result.put("passed", resultNode.get("passed").asBoolean());
                result.put("attemptDate", resultNode.get("attemptDate").asLong());

                String lessonId;
                if (resultNode.has("lessonId")) {
                    lessonId = resultNode.get("lessonId").asText();
                } else {
                    lessonId = extractLessonIdFromQuizId(resultNode.get("quizId").asText());
                }
                result.put("lessonId", lessonId);

                studentResults.add(result);
                System.out
                        .println("Found result - Lesson: " + lessonId + ", Quiz: " + resultNode.get("quizId").asText());
            }
        }

        System.out.println("Total results for student: " + studentResults.size());
        return studentResults;
    }

    public static int getRemainingAttempts(String studentId, String courseId, String lessonId) throws IOException {
        ArrayList<Map<String, Object>> results = getStudentQuizResults(studentId);

        System.out.println("=== DEBUG CHECKING ATTEMPTS ===");
        System.out.println("Student: " + studentId);
        System.out.println("Course: " + courseId);
        System.out.println("Target Lesson: " + lessonId);
        System.out.println("Max attempts: " + MAX_ATTEMPTS);
        System.out.println("Total results found for student: " + (results != null ? results.size() : 0));

        if (results == null || results.isEmpty()) {
            System.out.println("❌ No attempts found - " + MAX_ATTEMPTS + " attempts remaining");
            return MAX_ATTEMPTS;
        }

        int attemptCount = 0;

        for (Map<String, Object> result : results) {
            String resultLessonId = String.valueOf(result.get("lessonId"));
            String quizId = String.valueOf(result.get("quizId"));
            boolean passed = (boolean) result.get("passed");

            System.out.println(
                    "Checking result - Lesson: " + resultLessonId + ", Quiz: " + quizId + ", Passed: " + passed);

            // Count all attempts for this specific lesson (regardless of course for now,
            // since lessonId should be unique per course)
            if (resultLessonId.equals(lessonId)) {
                attemptCount++;
                System.out.println("✅ MATCH! Attempt #" + attemptCount + " for lesson " + lessonId);
            } else {
                System.out.println("❌ NO MATCH - Expected: " + lessonId + ", Got: " + resultLessonId);
            }
        }

        int remaining = Math.max(0, MAX_ATTEMPTS - attemptCount);
        System.out.println("📊 FINAL ATTEMPTS COUNT:");
        System.out.println("Course: " + courseId);
        System.out.println("Lesson: " + lessonId);
        System.out.println("Used attempts: " + attemptCount + "/" + MAX_ATTEMPTS);
        System.out.println("Remaining attempts: " + remaining + "/" + MAX_ATTEMPTS);

        return remaining;
    }

    public static int getUsedAttempts(String studentId, String lessonId) throws IOException {
        ArrayList<Map<String, Object>> results = getStudentQuizResults(studentId);
        if (results == null || results.isEmpty()) {
            return 0;
        }

        int attemptCount = 0;

        for (Map<String, Object> result : results) {
            String resultLessonId = String.valueOf(result.get("lessonId"));
            if (resultLessonId.equals(lessonId)) {
                attemptCount++;
            }
        }

        return attemptCount;
    }

    public static boolean isLessonCompletedViaQuiz(String studentId, String lessonId) throws IOException {
        var results = getStudentQuizResults(studentId);

        for (var result : results) {
            String resultLessonId = String.valueOf(result.get("lessonId"));
            boolean passed = (boolean) result.get("passed");

            if (resultLessonId.equals(lessonId) && passed) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasStudentPassedAnyQuiz(String studentId) throws IOException {
        ArrayList<Map<String, Object>> results = getStudentQuizResults(studentId);
        if (results == null || results.isEmpty()) {
            return false;
        }

        for (Map<String, Object> result : results) {
            boolean passed = (boolean) result.get("passed");
            if (passed) {
                return true;
            }
        }

        return false;
    }

    public static void resetAllAttempts() throws IOException {
        JsonHandler.initializeFileIfNeeded(QUIZ_RESULTS_FILE);
        var emptyArray = JsonHandler.objectMapper.createArrayNode();
        JsonHandler.writeToFile(emptyArray, QUIZ_RESULTS_FILE);
        System.out.println("🎯 ALL ATTEMPTS RESET - Start fresh with " + MAX_ATTEMPTS + " attempts each");
    }

    public static void debugQuizResults() throws IOException {
        JsonHandler.initializeFileIfNeeded(QUIZ_RESULTS_FILE);
        var resultsArray = JsonHandler.readArrayFromFile(QUIZ_RESULTS_FILE);

        System.out.println("=== DEBUG QUIZ RESULTS FILE ===");
        System.out.println("Total results in file: " + resultsArray.size());

        for (var result : resultsArray) {
            System.out.println("Result: " + result.toString());
        }
    }
}