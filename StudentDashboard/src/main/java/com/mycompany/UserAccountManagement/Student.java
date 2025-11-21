/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.UserAccountManagement;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mycompany.CourseManagement.Course;
import com.mycompany.CourseManagement.CourseServices;
import com.mycompany.CourseManagement.Lesson;
import com.mycompany.JsonHandler.JsonHandler;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Hazem
 */
public class Student extends User {
    private final CourseServices courseManager;
    
    public Student(String userId, String name, String email, String password) throws IOException {
        super(userId, name, email, password);
        courseManager = new CourseServices();
    }
    
    public void enroll(String courseId) throws IllegalArgumentException, IOException {
        courseManager.enrollStudentInCourse(courseId, getUserId());
    }
    
    public void completeLesson(String lessonId) throws IOException {
        ArrayNode courseList = JsonHandler.readArrayFromFile(courseManager.getFileName());
        
        // Find the lesson to get the course index
        courseManager.findLessonById(lessonId);
        
        // Get the course from JSON
        JsonNode node = courseList.get(courseManager.getIndex());
        Course course = JsonHandler.objectMapper.treeToValue(node, Course.class);
        
        // Check if student is enrolled
        if (!course.getStudentIds().contains(getUserId())) {
            throw new IllegalArgumentException("You must enroll in this course first!");
        }
        
        // Find and update the lesson
        ArrayList<Lesson> lessons = course.getLessons();
        for (Lesson lesson : lessons) {
            if (lesson.getLessonId().equals(lessonId)) {
                lesson.setCompleted(true);
                break;
            }
        }
        
        JsonNode jsonNode = JsonHandler.convertJavatoJson(course);
        courseList.set(courseManager.getIndex(), jsonNode);
        JsonHandler.writeToFile(courseList, courseManager.getFileName());
    }
    
    // View all available courses (not enrolled)
    public ArrayList<Course> viewAvailableCourses() throws IOException {
        ArrayList<Course> allCourses = courseManager.getAllCourses();
        ArrayList<Course> availableCourses = new ArrayList<>();
        
        for (Course course : allCourses) {
            // Show courses the student is NOT enrolled in
            if (!course.getStudentIds().contains(getUserId())) {
                availableCourses.add(course);
            }
        }
        
        return availableCourses;
    }
    
    // View all enrolled courses
    public ArrayList<Course> getMyEnrolledCourses() throws IOException {
        return courseManager.getEnrolledCoursesByStudent(getUserId());
    }
    
    // View lessons from an enrolled course
    public ArrayList<Lesson> getCourseLessons(String courseId) throws IOException {
        Course course = courseManager.findCourseById(courseId);
        
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }
        
        // Check if enrolled
        if (!course.getStudentIds().contains(getUserId())) {
            throw new IllegalArgumentException("You are not enrolled in this course!");
        }
        
        return courseManager.getAllLessonsFromCourse(courseId);
    }
}