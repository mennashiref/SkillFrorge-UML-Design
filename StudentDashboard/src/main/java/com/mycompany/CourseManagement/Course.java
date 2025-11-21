/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.CourseManagement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mycompany.JsonHandler.JsonHandler;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Zeyad
 */
public class Course {

    private String courseId;
    private String instructorId;

    @JsonProperty(defaultValue = "[]")
    private ArrayList<String> studentIds;

    private String title;
    private String description;

    @JsonProperty(defaultValue = "[]")
    private ArrayList<Lesson> lessons;

    public Course() {
        this.studentIds = new ArrayList<>();
        this.lessons = new ArrayList<>();
    }

    public Course(String instructorId, String title, String description) throws IOException {
        this.instructorId = instructorId;
        this.title = title;
        this.description = description;
        this.courseId = "C"+generateNextCourseId();
        this.studentIds = new ArrayList<>();
        this.lessons = new ArrayList<>();
    }

    private int generateNextCourseId() throws IOException {
        try {
            ArrayNode courseList = JsonHandler.readArrayFromFile("courses.json");
            int maxId = 0;

            for (int i = 0; i < courseList.size(); i++) {
                JsonNode node = courseList.get(i);
                if (node.has("courseId")) {
                    String id = node.get("courseId").asText().substring(1);
                    if (Integer.parseInt(id) > maxId) {
                        maxId = Integer.parseInt(id);
                    }
                }
            }

            return maxId + 1;
        } catch (IOException e) {
            return 1; // If file doesn't exist or is empty, start from 1
        }
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

    @Override
    public String toString() {
        return String.format("Course[ID=%s, Title='%s', Description='%s', ]",
                courseId, title, description);
    }

}
