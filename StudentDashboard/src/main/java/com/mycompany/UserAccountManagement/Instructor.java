/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.UserAccountManagement;

import com.mycompany.CourseManagement.Course;
import com.mycompany.CourseManagement.CourseServices;
import com.mycompany.CourseManagement.Lesson;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Hazem
 */
public class Instructor extends User {

    private final CourseServices courseManager;
    
    public Instructor(String userId, String name, String email, String password) throws IOException {
        super(userId, name, email, password);
        courseManager = new CourseServices();
    }
    
    public void createCourse(String title, String description) throws IOException {
        courseManager.createCourse(getUserId(), title, description);
    }
    
    public Lesson createLesson(String title, String content) throws IOException {
        return courseManager.createLesson(title, content);
    }
    
    public void uploadLesson(Lesson lesson, String courseId) throws IllegalArgumentException, IOException {
        Course course = courseManager.findCourseById(courseId);
        
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }
        
        if (!course.getInstructorId().equals(getUserId())) {
            throw new IllegalArgumentException("You don't have permission to add lessons to this course!");
        }
        
        courseManager.addLessonToCourse(courseId, lesson);
    }
    
    public void updateCourse(String courseId, String newTitle, String newDescription) throws IOException {
        Course course = courseManager.findCourseById(courseId);
        
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }
        
        if (!course.getInstructorId().equals(getUserId())) {
            throw new IllegalArgumentException("You don't have permission to update this course!");
        }
        
        courseManager.updateCourse(courseId, newDescription, newTitle);
    }
    
    public void deleteCourse(String courseId) throws IOException {
        Course course = courseManager.findCourseById(courseId);
        
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }
        
        if (!course.getInstructorId().equals(getUserId())) {
            throw new IllegalArgumentException("You don't have permission to delete this course!");
        }
        
        courseManager.deleteCourseById(courseId);
    }
    
    public void updateLesson(String lessonId, String newTitle, String newContent) throws IOException {
        Course course = courseManager.findCourseByLessonId(lessonId);
        
        if (course == null) {
            throw new IllegalArgumentException("Lesson not found");
        }
        
        if (!course.getInstructorId().equals(getUserId())) {
            throw new IllegalArgumentException("You don't have permission to update this lesson!");
        }
        
        courseManager.updateLessonById(lessonId, newTitle, newContent);
    }
    
    public void deleteLesson(String lessonId) throws IOException {
        Course course = courseManager.findCourseByLessonId(lessonId);
        
        if (course == null) {
            throw new IllegalArgumentException("Lesson not found");
        }
        
        if (!course.getInstructorId().equals(getUserId())) {
            throw new IllegalArgumentException("You don't have permission to delete this lesson!");
        }
        
        courseManager.deleteLessonById(lessonId);
    }
    
    public ArrayList<Course> getMyCourses() throws IOException {
        ArrayList<Course> allCourses = courseManager.getAllCourses();
        ArrayList<Course> myCourses = new ArrayList<>();
        
        for (Course course : allCourses) {
            if (course.getInstructorId().equals(getUserId())) {
                myCourses.add(course);
            }
        }
        return myCourses;
    }
    
    public ArrayList<String> getEnrolledStudents(String courseId) throws IOException {
        Course course = courseManager.findCourseById(courseId);
        
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }
        
        if (!course.getInstructorId().equals(getUserId())) {
            throw new IllegalArgumentException("You don't have permission to view this course's students!");
        }
        
        return courseManager.getEnrolledStudents(courseId);
    }
    
    public ArrayList<Lesson> getCourseLessons(String courseId) throws IOException {
        Course course = courseManager.findCourseById(courseId);
        
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }
        
        if (!course.getInstructorId().equals(getUserId())) {
            throw new IllegalArgumentException("You don't have permission to view this course's lessons!");
        }
        
        return courseManager.getAllLessonsFromCourse(courseId);
    }
}