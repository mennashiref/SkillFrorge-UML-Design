package com.mycompany.CourseManagement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mycompany.JsonHandler.JsonHandler;
import com.mycompany.QuizManagement.Quiz;

import java.util.ArrayList;

/**
 * Lesson entity with Quiz support
 */
public class Lesson {

    private String lessonId;
    private String title;
    private String content;
    private String quizId;
    
    // Quiz for this lesson
    private Quiz quiz;

    @JsonIgnore
    private boolean completed;

    public Lesson() {
    }

    public Lesson(String title, String content) {
        this.lessonId = "L" + generateNextLessonId();
        this.title = title;
        this.content = content;
        this.completed = false;
        this.quizId = null;
        this.quiz = null;
    }
    
    public Lesson(String title, String content, Quiz quiz) {
        this.lessonId = "L" + generateNextLessonId();
        this.title = title;
        this.content = content;
        this.completed = false;
        this.quiz = quiz;
    }

    private int generateNextLessonId() {
        int maxId = 0;

        if (JsonHandler.courses != null) {
            for (Course course : JsonHandler.courses) {
                ArrayList<Lesson> lessons = course.getLessons();
                if (lessons != null) {
                    for (Lesson lesson : lessons) {
                        if (lesson.getLessonId() != null && lesson.getLessonId().length() > 1) {
                            String id = lesson.getLessonId().substring(1);
                            int lessonNum = Integer.parseInt(id);
                            if (lessonNum > maxId) {
                                maxId = lessonNum;
                            }
                        }
                    }
                }
            }
        }

        return maxId + 1;
    }

    /**
     * @return the lessonId
     */
    public String getLessonId() {
        return lessonId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    /**
     * @return the quiz
     */
    public Quiz getQuiz() {
        return quiz;
    }

    /**
     * @param quiz the quiz to set
     */
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    /**
     * @return the quizId
     */
    public String getQuizId() {
        return quizId;
    }

    /**
     * @param quizId the quizId to set
     */
    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    @Override
    public String toString() {
        return String.format("Lesson[ID=%s, Title='%s', Content='%s', HasQuiz=%b]",
                lessonId, title, content, quiz != null);
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}