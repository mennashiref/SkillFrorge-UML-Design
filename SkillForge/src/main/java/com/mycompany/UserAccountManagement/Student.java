/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.UserAccountManagement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mycompany.CourseManagement.Certificate;
import com.mycompany.CourseManagement.CertificateService;
import com.mycompany.CourseManagement.Course;
import com.mycompany.CourseManagement.CourseProgress;
import com.mycompany.CourseManagement.CourseServices;
import com.mycompany.CourseManagement.Lesson;
import com.mycompany.CourseManagement.Status;
import com.mycompany.QuizManagement.QuizAttempt;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 *
 */
public class Student extends User {

    private HashMap<String, CourseProgress> courseProgressMap; // courseId -> CourseProgress
    private ArrayList<String> certificateIds;

    public Student() {
        super();
        this.courseProgressMap = new HashMap<>();
        this.certificateIds = new ArrayList<>();

    }

    public Student(String userId, String name, String email, String password) {
        super(userId, name, email, password);
        this.courseProgressMap = new HashMap<>();
        this.certificateIds = new ArrayList<>();
    }

    public void enroll(String courseId) throws IllegalArgumentException, IOException {
        CourseServices.enrollStudentInCourse(courseId, getUserId());
    }

    public void completeLesson(String userId, String lessonId) throws IOException, Exception {
        CourseServices.markLessonCompleted(userId, lessonId);
    }

    @JsonIgnore
    public ArrayList<Course> viewAvailableCourses() throws IOException {
        ArrayList<Course> allCourses = CourseServices.getAllCourses();
        ArrayList<Course> enrolledCourses = getMyEnrolledCourses();
        ArrayList<Course> availableCourses = new ArrayList<>();

        for (Course course : allCourses) {
            if (!enrolledCourses.contains(course) && course.getStatus() == Status.APPROVED) {
                availableCourses.add(course);
            }
        }

        return availableCourses;
    }

    @JsonIgnore
    public ArrayList<Course> getMyEnrolledCourses() throws IOException {
        return CourseServices.getEnrolledCoursesByStudent(getUserId());
    }

    @JsonIgnore
    public ArrayList<Lesson> getCourseLessons(String courseId) throws IOException {
        Course course = CourseServices.findCourseById(courseId);

        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }

        return CourseServices.getAllLessonsFromCourse(courseId);
    }

    // Add method to submit quiz and update progress
    public void submitQuiz(QuizAttempt quizAttempt) throws IOException, Exception {
        String courseId = quizAttempt.getCourseId();

        // Get or create course progress
        CourseProgress progress = getCourseProgress(courseId);

        progress.addQuizAttempt(quizAttempt);

        // Check if course is completed and generate certificate
        if (progress.isReadyForCertificate() && !hasCertificateForCourse(courseId)) {
            generateCertificate(courseId, progress.getOverallScore());
        }

        // Save changes
        UserServices.updateUser(this);
    }

    private void generateCertificate(String courseId, double finalScore) throws IOException {
        Course course = CourseServices.findCourseById(courseId);
        if (course != null) {
            // CertificateService.generateCertificate() already adds the certificateId to
            // the student
            CertificateService.generateCertificate(this, course, finalScore);
        }
    }

    public CourseProgress getCourseProgress(String courseId) {
        if (!courseProgressMap.containsKey(courseId)) {
            courseProgressMap.put(courseId, new CourseProgress());
        }
        return courseProgressMap.get(courseId);
    }

    public void markLessonCompleted(String courseId, String lessonId) throws IOException, Exception {
        CourseProgress progress = getCourseProgress(courseId);
        progress.updateLessonStatus(lessonId, true);

        // Check if course is completed
        if (progress.isReadyForCertificate() && !hasCertificateForCourse(courseId)) {
            generateCertificate(courseId, progress.getOverallScore());
        }

        UserServices.updateUser(this);
    }

    // Check if student has certificate for course
    public boolean hasCertificateForCourse(String courseId) throws IOException {
        return CertificateService.hasCertificate(getUserId(), courseId);
    }

    // Get all student certificates
    public ArrayList<Certificate> getCertificates() throws IOException {
        return CertificateService.getStudentCertificates(getUserId());
    }

    // Get course progress for specific course
    public double getCourseProgressPercentage(String courseId) {
        CourseProgress progress = courseProgressMap.get(courseId);
        return progress != null ? progress.getProgressPercentage() : 0.0;
    }

    // Get overall score for course
    public double getCourseScore(String courseId) {
        CourseProgress progress = courseProgressMap.get(courseId);
        return progress != null ? progress.getOverallScore() : 0.0;
    }

    public HashMap<String, CourseProgress> getCourseProgressMap() {
        if (courseProgressMap == null) {
            courseProgressMap = new HashMap<>();
        }
        return courseProgressMap;
    }

    public void setCourseProgressMap(HashMap<String, CourseProgress> courseProgressMap) {
        this.courseProgressMap = courseProgressMap;
    }

    public ArrayList<String> getCertificateIds() {
        if (certificateIds == null) {
            certificateIds = new ArrayList<>();
        }
        return certificateIds;
    }

    public void setCertificateIds(ArrayList<String> certificateIds) {
        this.certificateIds = certificateIds;
    }
}