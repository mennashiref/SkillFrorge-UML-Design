package com.mycompany.QuizManagement;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

/**
 * Quiz entity containing questions for a lesson
 */
public class Quiz {

    private String quizId;

    @JsonProperty(defaultValue = "[]")
    private ArrayList<Question> questions;

    private int passingScore; 

    private double averageScore;

    private int maxAttempts = 3; 

    public Quiz() {
        this.questions = new ArrayList<>();
        this.passingScore = 70; // Default passing score
        this.averageScore = 0.0;
        this.maxAttempts = 3;
    }

    public Quiz(String quizId, ArrayList<Question> questions, int passingScore) {
        this.quizId = quizId;
        this.questions = questions != null ? questions : new ArrayList<>();
        this.passingScore = passingScore;
        this.averageScore = 0.0;
        this.maxAttempts = 3;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public ArrayList<Question> getQuestions() {
        if (questions == null) {
            questions = new ArrayList<>();
        }
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public int getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(int passingScore) {
        this.passingScore = passingScore;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    // إضافة getter و setter لخاصية maxAttempts
    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public void addQuestion(Question question) {
        if (questions == null) {
            questions = new ArrayList<>();
        }
        if (question != null) {
            questions.add(question);
        }
    }

    @Override
    public String toString() {
        return String.format("Quiz[ID=%s, Questions=%d, PassingScore=%d%%, MaxAttempts=%d]",
                quizId, questions.size(), passingScore, maxAttempts);
    }
}