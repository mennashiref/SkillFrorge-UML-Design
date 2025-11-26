package com.mycompany.QuizManagement;

import com.mycompany.CourseManagement.Course;
import com.mycompany.CourseManagement.CourseProgress;
import com.mycompany.CourseManagement.CourseServices;
import com.mycompany.CourseManagement.Lesson;
import com.mycompany.JsonHandler.JsonHandler;
import com.mycompany.UserAccountManagement.Student;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for Quiz operations
 */
public class QuizServices {

    private static final int MAX_ATTEMPTS = 3;

    /**
     * Create a quiz with questions
     */
    public static Quiz createQuiz(String quizId, ArrayList<Question> questions, int passingScore) {
        return new Quiz(quizId, questions, passingScore);
    }

    /**
     * Add quiz to a lesson
     */
    public static void addQuizToLesson(String courseId, String lessonId, Quiz quiz) throws IOException {
        Lesson lesson = CourseServices.findLessonById(courseId, lessonId);
        if (lesson == null) {
            throw new IllegalArgumentException("Lesson not found");
        }

        lesson.setQuiz(quiz);
        JsonHandler.saveCourses();
    }

    /**
     * -------------------------------
     * NEW: Get remaining attempts
     * -------------------------------
     */
    public static int getRemainingAttempts(String studentId, String courseId, String lessonId) {
        Student student = JsonHandler.getStudent(studentId);
        if (student == null) return MAX_ATTEMPTS;

        CourseProgress progress = student.getCourseProgress(courseId);
        if (progress == null) return MAX_ATTEMPTS;

        int attempts = 0;
        for (QuizAttempt attempt : progress.getQuizAttempts()) {
            if (attempt.getLessonId().equals(lessonId)) {
                attempts++;
            }
        }

        return Math.max(0, MAX_ATTEMPTS - attempts);
    }

    /**
     * Submit quiz answers and calculate score
     */
    public static QuizResult submitQuiz(String studentId, String courseId, String lessonId,
            ArrayList<Integer> studentAnswers) throws IOException {

        // ❗❗ BEFORE ANYTHING — check attempts
        int remaining = getRemainingAttempts(studentId, courseId, lessonId);
        if (remaining <= 0) {
            throw new IllegalStateException("No remaining quiz attempts. Maximum attempts reached.");
        }

        // Get the lesson and its quiz
        Lesson lesson = CourseServices.findLessonById(courseId, lessonId);
        if (lesson == null || lesson.getQuiz() == null) {
            throw new IllegalArgumentException("Lesson or quiz not found");
        }

        Quiz quiz = lesson.getQuiz();

        // Calculate score
        int correctAnswers = 0;
        ArrayList<Question> questions = quiz.getQuestions();

        for (int i = 0; i < questions.size() && i < studentAnswers.size(); i++) {
            if (questions.get(i).isCorrect(studentAnswers.get(i))) {
                correctAnswers++;
            }
        }

        int scorePercentage = (correctAnswers * 100) / questions.size();
        boolean passed = scorePercentage >= quiz.getPassingScore();

        // Get student
        Student student = JsonHandler.getStudent(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found");
        }

        // Get or create course progress
        CourseProgress progress = student.getCourseProgress(courseId);
        if (progress == null) {
            throw new IllegalArgumentException("Student not enrolled in course");
        }

        // Determine attempt number
        int attemptNumber = 1;
        List<QuizAttempt> attempts = progress.getQuizAttempts();
        for (QuizAttempt attempt : attempts) {
            if (attempt.getLessonId().equals(lessonId)) {
                attemptNumber++;
            }
        }

        // Create attempt
        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setQuizId(quiz.getQuizId());
        quizAttempt.setLessonId(lessonId);
        quizAttempt.setCourseId(courseId);
        quizAttempt.setScorePercent(scorePercentage);
        quizAttempt.setPassed(passed);
        quizAttempt.setAttemptTime(Instant.now().toString());

        // Add to course progress
        progress.addQuizAttempt(quizAttempt);

        // Save user progress
        JsonHandler.saveUsers();

        // Update quiz avg score
        updateQuizAverageScore(courseId, lessonId);

        // Create result object
        QuizResult result = new QuizResult(lessonId, scorePercentage, passed, attemptNumber, studentAnswers);
        return result;
    }

    /**
     * Get all quiz results for a student in a specific lesson
     */
    public static ArrayList<QuizResult> getQuizResultsForLesson(String studentId, String courseId, String lessonId) {
        Student student = JsonHandler.getStudent(studentId);
        ArrayList<QuizResult> results = new ArrayList<>();

        if (student == null) {
            return results;
        }

        CourseProgress progress = student.getCourseProgress(courseId);
        if (progress == null) {
            return results;
        }

        int attemptNum = 1;
        for (QuizAttempt attempt : progress.getQuizAttempts()) {
            if (attempt.getLessonId().equals(lessonId)) {
                QuizResult result = new QuizResult(
                        lessonId,
                        attempt.getScorePercent(),
                        attempt.isPassed(),
                        attemptNum++,
                        new ArrayList<>()
                );
                results.add(result);
            }
        }

        return results;
    }

    /**
     * Get best quiz score
     */
    public static int getBestScore(String studentId, String courseId, String lessonId) {
        Student student = JsonHandler.getStudent(studentId);
        if (student == null) {
            return -1;
        }

        CourseProgress progress = student.getCourseProgress(courseId);
        if (progress == null) {
            return -1;
        }

        int bestScore = -1;
        for (QuizAttempt attempt : progress.getQuizAttempts()) {
            if (attempt.getLessonId().equals(lessonId)) {
                bestScore = Math.max(bestScore, attempt.getScorePercent());
            }
        }

        return bestScore;
    }

    /**
     * Check if student passed quiz
     */
    public static boolean hasPassedQuiz(String studentId, String courseId, String lessonId) {
        Student student = JsonHandler.getStudent(studentId);
        if (student == null) {
            return false;
        }

        CourseProgress progress = student.getCourseProgress(courseId);
        if (progress == null) {
            return false;
        }

        String status = progress.getLessonStatus().get(lessonId);
        return "PASSED".equals(status);
    }

    /**
     * Update quiz average score
     */
    private static void updateQuizAverageScore(String courseId, String lessonId) throws IOException {
        Course course = CourseServices.findCourseById(courseId);
        Lesson lesson = CourseServices.findLessonById(courseId, lessonId);

        if (lesson == null || lesson.getQuiz() == null) {
            return;
        }

        ArrayList<String> studentIds = course.getStudentIds();
        int totalScore = 0;
        int studentCount = 0;

        for (String studentId : studentIds) {
            int bestScore = getBestScore(studentId, courseId, lessonId);
            if (bestScore >= 0) {
                totalScore += bestScore;
                studentCount++;
            }
        }

        if (studentCount > 0) {
            double averageScore = (double) totalScore / studentCount;
            lesson.getQuiz().setAverageScore(Math.round(averageScore * 100.0) / 100.0);
            JsonHandler.saveCourses();
        }
    }
}
