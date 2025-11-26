/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.CourseManagement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.JsonHandler.JsonHandler;
import java.util.ArrayList;

/**
 *
 * @author Zeyad
 */
public class Course {

    private String courseId;
    private String instructorId;
    private Status status;

    
    @JsonProperty(defaultValue = "[]")
    private ArrayList<String> studentIds;

    private String title;
    private String description;

    @JsonProperty(defaultValue = "[]")
    private ArrayList<Lesson> lessons;

    public Course() {
        this.studentIds = new ArrayList<>();
        this.lessons = new ArrayList<>();
        status = Status.PENDING;
    }

    public Course(String instructorId, String title, String description) {
        this.instructorId = instructorId;
        this.title = title;
        this.description = description;
        this.courseId = "C" + generateNextCourseId();
        this.studentIds = new ArrayList<>();
        this.lessons = new ArrayList<>();
        this.status = Status.PENDING;
    }

    private int generateNextCourseId() {
        int maxId = 0;

        if (JsonHandler.courses != null) {
            for (Course course : JsonHandler.courses) {
                if (course.getCourseId() != null && course.getCourseId().length() > 1) {
                    String id = course.getCourseId().substring(1);
                    int courseNum = Integer.parseInt(id);
                    if (courseNum > maxId) {
                        maxId = courseNum;
                    }
                }
            }
        }

        return maxId + 1;
    }

    /**
     * @return the courseId
     */
    public String getCourseId() {
        return courseId;
    }

    /**
     * @return the InstructorId
     */
    public String getInstructorId() {
        return instructorId;
    }

    /**
     * @return the studentIds
     */
    public ArrayList<String> getStudentIds() {
        return studentIds;
    }

    public boolean enrollStudent(String studentId) {
        if (studentIds == null) {
            studentIds = new ArrayList<>();
        }
        if (studentId != null && !studentIds.contains(studentId)) {
            return studentIds.add(studentId);
        }
        return false;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public void addLesson(Lesson lesson) {
        if (lessons == null) {
            lessons = new ArrayList<>();
        }
        if (lesson != null) {
            lessons.add(lesson);
        }
    }

    public ArrayList<Lesson> getLessons() {
        if (lessons == null) {
            lessons = new ArrayList<>();
        }
        return lessons;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return this.status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Course[ID=%s, Title='%s', Description='%s', ]",
                courseId, title, description);
    }

}
