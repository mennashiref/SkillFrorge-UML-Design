/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.CourseManagement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mycompany.JsonHandler.JsonHandler;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Zeyad
 */
public class CourseServices {

    private final String fileName;
    private ArrayNode courseList;
    private int index;

    public CourseServices() throws IOException {
        this.fileName = "courses.json";
        // Initialize file if it doesn't exist or is empty
        JsonHandler.initializeFileIfNeeded(fileName);
        courseList = JsonHandler.readArrayFromFile(fileName);
    }

    // Course Management Methods
    public ArrayList<Course> getAllCourses() throws JsonProcessingException, IOException {
        courseList = JsonHandler.readArrayFromFile(getFileName());
        ArrayList<Course> courses = new ArrayList<>();

        for (int i = 0; i < courseList.size(); i++) {
            JsonNode node = courseList.get(i);
            Course course = JsonHandler.objectMapper.treeToValue(node, Course.class);
            courses.add(course);
        }

        return courses;
    }

    public Course createCourse(String instructorId, String title, String description)
            throws JsonProcessingException, IOException {

        Course course = new Course(instructorId, title, description);
        JsonNode node = JsonHandler.convertJavatoJson(course);

        courseList = JsonHandler.readArrayFromFile(getFileName());
        courseList.add(node);
        JsonHandler.writeToFile(courseList, getFileName());

        return course;
    }

    public Course findCourseById(String courseId) throws IllegalArgumentException, JsonProcessingException, IOException {
        courseList = JsonHandler.readArrayFromFile(getFileName());
        for (int i = 0; i < courseList.size(); i++) {
            JsonNode node = courseList.get(i);
            if (node.get("courseId").asText().equals(courseId)) {
                index = i;
                return JsonHandler.objectMapper.treeToValue(node, Course.class);
            }
        }
        return null;
    }

    public Course findCourseByLessonId(String lessonId) throws IOException {
        courseList = JsonHandler.readArrayFromFile(getFileName());

        for (int i = 0; i < courseList.size(); i++) {
            JsonNode node = courseList.get(i);
            Course course = JsonHandler.objectMapper.treeToValue(node, Course.class);

            ArrayList<Lesson> lessons = course.getLessons();
            if (lessons != null) {
                for (Lesson lesson : lessons) {
                    if (lesson.getLessonId().equals(lessonId)) {
                        index = i;
                        return course;
                    }
                }
            }
        }

        return null;
    }

    public Course updateCourse(String courseId, String newDescription, String newTitle) throws IllegalArgumentException, IOException {
        Course course = findCourseById(courseId);
        course.setDescription(newDescription);
        course.setTitle(newTitle);
        JsonNode jsonNode = JsonHandler.convertJavatoJson(course);
        courseList.set(getIndex(), jsonNode);
        JsonHandler.writeToFile(courseList, getFileName());
        return course;
    }

    public boolean deleteCourseById(String courseId) throws IOException {
        ArrayNode courseList = JsonHandler.readArrayFromFile(getFileName());

        for (int i = 0; i < courseList.size(); i++) {
            JsonNode node = courseList.get(i);
            if (node.get("courseId").asText().equals(courseId)) {
                courseList.remove(i);
                JsonHandler.writeToFile(courseList, getFileName());
                return true;
            }
        }

        return false;
    }

    public Lesson addLessonToCourse(String courseId, Lesson lesson) throws IllegalArgumentException, JsonProcessingException, IOException {
        Course course = findCourseById(courseId);
        course.addLesson(lesson);
        JsonNode jsonNode = JsonHandler.convertJavatoJson(course);
        courseList.set(getIndex(), jsonNode);
        JsonHandler.writeToFile(courseList, getFileName());
        return lesson;
    }

    //------------------------
    //Enroll course and return enrolled courses by a student
    //------------------------
    //Enroll student in a course
    public boolean enrollStudentInCourse(String courseId, String studentId)
            throws IllegalArgumentException, JsonProcessingException, IOException {

        Course course = findCourseById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course with ID " + courseId + " not found");
        }

        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }

        ArrayList<String> studentIds = course.getStudentIds();
        if (studentIds.contains(studentId)) {
            return false; // Already enrolled
        }

        boolean enrolled = course.enrollStudent(studentId);

        if (enrolled) {
            JsonNode jsonNode = JsonHandler.convertJavatoJson(course);
            courseList.set(getIndex(), jsonNode);
            JsonHandler.writeToFile(courseList, getFileName());
        }

        return enrolled;
    }

    // return list of enrolled courses by specific student
    public ArrayList<Course> getEnrolledCoursesByStudent(String studentId)
            throws JsonProcessingException, IOException {

        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }

        courseList = JsonHandler.readArrayFromFile(getFileName());
        ArrayList<Course> enrolledCourses = new ArrayList<>();

        for (int i = 0; i < courseList.size(); i++) {
            JsonNode node = courseList.get(i);
            Course course = JsonHandler.objectMapper.treeToValue(node, Course.class);

            ArrayList<String> studentIds = course.getStudentIds();
            if (studentIds != null && studentIds.contains(studentId)) {
                enrolledCourses.add(course);
            }
        }

        return enrolledCourses;
    }

    //return list of students id who enrolled in specific course
    public ArrayList<String> getEnrolledStudents(String courseId)
            throws IllegalArgumentException, JsonProcessingException, IOException {

        Course course = findCourseById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course with ID " + courseId + " not found");
        }

        return course.getStudentIds();
    }

    //--------------------------------------------------------
    //Lesson Management Methods
    //--------------------------------------------------------
    public ArrayList<Lesson> getAllLessonsFromCourse(String courseId)
            throws IllegalArgumentException, JsonProcessingException, IOException {

        Course course = findCourseById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course with ID " + courseId + " not found");
        }

        return course.getLessons();
    }

    public Lesson createLesson(String title, String content) throws IOException {
        return new Lesson(title, content);
    }

    // Use this if you know already the course id which have the lesson
    public Lesson findLessonById(String courseId, String lessonId)
            throws IllegalArgumentException, JsonProcessingException, IOException {

        Course course = findCourseById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course with ID " + courseId + " not found");
        }

        ArrayList<Lesson> lessons = course.getLessons();
        if (lessons == null) {
            return null;
        }

        for (Lesson lesson : lessons) {
            if (lesson.getLessonId().equals(lessonId)) {
                return lesson;
            }
        }

        return null;
    }

    // Use this if you don't know the course id which have the lesson
    public Lesson findLessonById(String lessonId)
            throws JsonProcessingException, IOException {

        courseList = JsonHandler.readArrayFromFile(getFileName());

        for (int i = 0; i < courseList.size(); i++) {
            JsonNode node = courseList.get(i);
            Course course = JsonHandler.objectMapper.treeToValue(node, Course.class);

            ArrayList<Lesson> lessons = course.getLessons();
            if (lessons != null) {
                for (Lesson lesson : lessons) {
                    if (lesson.getLessonId().equals(lessonId)) {
                        index = i; // Store index for later use
                        return lesson;
                    }
                }
            }
        }

        return null;
    }

    public Lesson updateLessonById(String lessonId, String newTitle, String newContent)
            throws IllegalArgumentException, JsonProcessingException, IOException {

        Lesson lesson = findLessonById(lessonId);

        if (lesson == null) {
            throw new IllegalArgumentException("Lesson with ID " + lessonId + " not found");
        }

        lesson.setTitle(newTitle);
        lesson.setContent(newContent);

        JsonNode node = courseList.get(getIndex());
        Course course = JsonHandler.objectMapper.treeToValue(node, Course.class);

        JsonNode jsonNode = JsonHandler.convertJavatoJson(course);
        courseList.set(getIndex(), jsonNode);
        JsonHandler.writeToFile(courseList, getFileName());

        return lesson;
    }

    public boolean deleteLessonById(String lessonId)
            throws JsonProcessingException, IOException {

        Lesson lesson = findLessonById(lessonId);

        if (lesson == null) {
            return false;
        }

        JsonNode node = courseList.get(getIndex());
        Course course = JsonHandler.objectMapper.treeToValue(node, Course.class);

        ArrayList<Lesson> lessons = course.getLessons();
        boolean removed = false;

        for (int i = 0; i < lessons.size(); i++) {
            if (lessons.get(i).getLessonId().equals(lessonId)) {
                lessons.remove(i);
                removed = true;
                break;
            }
        }

        if (removed) {
            JsonNode jsonNode = JsonHandler.convertJavatoJson(course);
            courseList.set(getIndex(), jsonNode);
            JsonHandler.writeToFile(courseList, getFileName());
        }

        return removed;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

}
