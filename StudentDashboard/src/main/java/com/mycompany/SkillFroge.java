package com.mycompany;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
import com.mycompany.CourseManagement.Course;
import com.mycompany.CourseManagement.CourseServices;
import com.mycompany.CourseManagement.Lesson;
import com.mycompany.UserAccountManagement.Instructor;
import com.mycompany.UserAccountManagement.Student;
import com.mycompany.nour.StudentDashboardFrame;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Zeyad
 */
public class SkillFroge {

    public static void main(String[] args) {
        try {
            // 1️⃣ إنشاء CourseServices وإنشاء بعض الكورسات
            CourseServices courseManagment = new CourseServices();
            courseManagment.createCourse("TABLIA", "JAVA", "2025");
            courseManagment.createCourse("DR AMR", "MATH3", "4 LESSON");
            courseManagment.createCourse("instructor3", "Title3", "description 3");
            courseManagment.createCourse("instructor4", "Title4", "description 4");
            courseManagment.createCourse("instructor5", "Title5", "description 5");

            // 2️⃣ عرض بعض المعلومات على الكونسول
            System.out.println(courseManagment.findCourseById("C3").toString());
            System.out.println(courseManagment.updateCourse("C5", "newDescription", "newTitle").toString());

            ArrayList<Course> courses = courseManagment.getAllCourses();
            ArrayList<Lesson> lessons = courseManagment.getAllLessonsFromCourse("C2");

            for (Lesson lesson : lessons) {
                System.out.println(lesson.toString());
            }
            for (Course course : courses) {
                System.out.println(course.toString());
            }


            Lesson lesson = courseManagment.createLesson("lesson one", "content one");
            courseManagment.addLessonToCourse("C1", lesson);
            courseManagment.updateLessonById("L2", "newTitle", "newContent");
            courseManagment.deleteLessonById("L3");


            courseManagment.enrollStudentInCourse("C2", "st1");
            courseManagment.enrollStudentInCourse("C2", "st1");
            courseManagment.enrollStudentInCourse("C2", "st2");
            courseManagment.enrollStudentInCourse("C3", "st2");

            System.out.println(courseManagment.getEnrolledStudents("C2"));

 
            Student st = new Student("st1", "StudentOne", "student1@example.com", "password123");

      
            new StudentDashboardFrame(st).setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Error creating student: " + e.getMessage());
        }
    }

}
