/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.CourseManagement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.QuizManagement.QuizAttempt;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author menna
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseProgress {
    private Map<String, String> lessonStatus = new HashMap<>(); // lessonId -> PASSED or INCOMPLETE
    private List<QuizAttempt> quizAttempts = new ArrayList<>();
    private Boolean completed = false;
    private String certificateId;
    private Date completionDate;
    private double overallScore;

    public Map<String, String> getLessonStatus() {
        return lessonStatus;
    }

    public void setLessonStatus(Map<String, String> lessonStatus) {
        this.lessonStatus = lessonStatus;
    }

    public List<QuizAttempt> getQuizAttempts() {
        return quizAttempts;
    }

    public void setQuizAttempts(List<QuizAttempt> quizAttempts) {
        this.quizAttempts = quizAttempts;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(double overallScore) {
        this.overallScore = overallScore;
    }

    public void updateLessonStatus(String lessonId, boolean passed) {
        lessonStatus.put(lessonId, passed ? "PASSED" : "INCOMPLETE");
        checkCourseCompletion();
    }

    // Check if all lessons are completed and passed
    private void checkCourseCompletion() {
        if (lessonStatus.isEmpty()) {
            completed = false;
            return;
        }

        // Check if all lessons are passed
        for (String status : lessonStatus.values()) {
            if (!status.equals("PASSED")) {
                completed = false;
                return;
            }
        }

        calculateOverallScore();

        // If not already completed, mark as completed
        if (!completed) {
            completed = true;
            completionDate = new Date();
        }
    }

    // Calculate average score from all quiz attempts
    private void calculateOverallScore() {
        if (quizAttempts.isEmpty()) {
            overallScore = 0.0;
            return;
        }

        double totalScore = 0;
        int count = 0;

        // Get the best attempt for each lesson
        Map<String, Integer> bestScores = new HashMap<>();
        for (QuizAttempt attempt : quizAttempts) {
            String lessonId = attempt.getLessonId();
            int score = attempt.getScorePercent();

            if (!bestScores.containsKey(lessonId) || score > bestScores.get(lessonId)) {
                bestScores.put(lessonId, score);
            }
        }

        // Calculate average of best scores
        for (int score : bestScores.values()) {
            totalScore += score;
            count++;
        }

        overallScore = count > 0 ? totalScore / count : 0.0;
    }

    // Add quiz attempt and update progress
    public void addQuizAttempt(QuizAttempt attempt) {
        quizAttempts.add(attempt);
        updateLessonStatus(attempt.getLessonId(), attempt.isPassed());
    }

    // Check if course is ready for certificate
    public boolean isReadyForCertificate() {
        return completed && overallScore >= 70.0;
    }

    public double getProgressPercentage() {
        if (lessonStatus.isEmpty())
            return 0.0;

        int passedCount = 0;
        for (String status : lessonStatus.values()) {
            if (status.equals("PASSED")) {
                passedCount++;
            }
        }

        return (double) passedCount / lessonStatus.size() * 100.0;
    }
}