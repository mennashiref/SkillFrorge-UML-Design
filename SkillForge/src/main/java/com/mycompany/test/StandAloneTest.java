package com.mycompany.test;

import com.mycompany.Analytics.AnalyticsServices;
import com.mycompany.Analytics.CourseAnalytics;
import com.mycompany.CourseManagement.Certificate;
import com.mycompany.CourseManagement.CertificateService;
import com.mycompany.CourseManagement.Course;
import com.mycompany.CourseManagement.CourseServices;
import com.mycompany.CourseManagement.Lesson;
import com.mycompany.CourseManagement.Status;
import com.mycompany.JsonHandler.JsonHandler;
import com.mycompany.QuizManagement.Question;
import com.mycompany.QuizManagement.Quiz;
import com.mycompany.QuizManagement.QuizResult;
import com.mycompany.QuizManagement.QuizServices;
import com.mycompany.UserAccountManagement.Instructor;
import com.mycompany.UserAccountManagement.Student;
import com.mycompany.UserAccountManagement.UserServices;
import java.util.ArrayList;

/**
 * Standalone test that creates complete data matching your JSON template
 * structure
 * Run this to generate clean test data
 * 
 * @author Lab8_Team
 */
public class StandAloneTest {

    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("STANDALONE TEST - Creating Complete Test Data");
        System.out.println("=".repeat(80));

        try {
            // Step 1: Initialize
            System.out.println("\n1. Initializing system...");
            JsonHandler.loadUsers();
            JsonHandler.loadCourses();

            // Clear existing data (optional - comment out if you want to keep existing
            // data)
            JsonHandler.students.clear();
            JsonHandler.instructors.clear();
            JsonHandler.courses.clear();

            // Step 2: Create Users
            System.out.println("\n2. Creating users...");
            Instructor instructor1 = createInstructor("Sarah Johnson", "sarah.johnson@skillforge.com", "password123");

            Student student1 = createStudent("Alice Williams", "alice.williams@student.com", "password123");
            Student student2 = createStudent("Bob Smith", "bob.smith@student.com", "password123");
            Student student3 = createStudent("Carol Davis", "carol.davis@student.com", "password123");

            System.out.println("✓ Created 1 instructor and 3 students");

            // Step 3: Create Courses
            System.out.println("\n3. Creating courses...");
            Course course1 = createCourseWithLessons(
                    instructor1.getUserId(),
                    "Java Programming Fundamentals",
                    "Learn Java from basics to advanced concepts");

            Course course2 = createCourseWithLessons(
                    instructor1.getUserId(),
                    "Python for Data Science",
                    "Master Python for data analysis and machine learning");

            System.out.println("✓ Created 2 courses with lessons and quizzes");

            // Step 4: Approve Courses
            System.out.println("\n4. Approving courses...");
            CourseServices.updateCourseStatus(course1.getCourseId(), Status.APPROVED);
            CourseServices.updateCourseStatus(course2.getCourseId(), Status.APPROVED);
            System.out.println("✓ Courses approved");

            // Step 5: Enroll Students
            System.out.println("\n5. Enrolling students...");
            // Course 1: All 3 students
            CourseServices.enrollStudentInCourse(course1.getCourseId(), student1.getUserId());
            CourseServices.enrollStudentInCourse(course1.getCourseId(), student2.getUserId());
            CourseServices.enrollStudentInCourse(course1.getCourseId(), student3.getUserId());

            // Course 2: Only students 1 and 3
            CourseServices.enrollStudentInCourse(course2.getCourseId(), student1.getUserId());
            CourseServices.enrollStudentInCourse(course2.getCourseId(), student3.getUserId());

            System.out.println("✓ Students enrolled in courses");

            // Step 6: Submit Quizzes
            System.out.println("\n6. Simulating quiz attempts...");
            simulateQuizAttempts(course1, student1, 0.80, 0.85); // Student 1 - 80% then 85%
            simulateQuizAttempts(course1, student2, 0.60, 0.75); // Student 2 - 60% then 75%
            simulateQuizAttempts(course1, student3, 0.40, 0.70); // Student 3 - 40% then 70%

            simulateQuizAttempts(course2, student1, 0.80, 0.85); // Student 1 in course 2 - 80% then 85%
            simulateQuizAttempts(course2, student3, 0.60, 0.75); // Student 3 in course 2 - 60% then 75%

            System.out.println("✓ Quiz attempts recorded");

            // Step 7: Complete all lessons for student1 to generate certificate
            System.out.println("\n7. Completing all lessons for Alice to generate certificate...");
            completeAllLessons(course1, student1);
            System.out.println("✓ Alice completed all lessons in " + course1.getTitle());

            // Step 8: Generate Analytics Report
            System.out.println("\n8. Generating analytics report...");
            printAnalyticsReport(course1);

            // Final message
            System.out.println("\n" + "=".repeat(80));
            System.out.println("✅ TEST COMPLETED SUCCESSFULLY!");
            System.out.println("=".repeat(80));
            System.out.println("\nGenerated files:");
            System.out.println("  📄 users.json    - Contains students and instructors with quiz results");
            System.out.println("  📄 courses.json  - Contains courses with lessons, quizzes, and analytics");
            System.out.println("\n" + "=".repeat(80));

        } catch (Exception e) {
            System.err.println("\n❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Create instructor with validation
     */
    private static Instructor createInstructor(String name, String email, String password) throws Exception {
        // Check if already exists
        for (Instructor instructor : JsonHandler.instructors) {
            if (instructor.getEmail().equals(email)) {
                System.out.println("  - Instructor exists: " + name);
                return instructor;
            }
        }

        Instructor instructor = UserServices.signup(Instructor.class, name, email, password);
        System.out.println("  - Created instructor: " + name + " (" + instructor.getUserId() + ")");
        return instructor;
    }

    /**
     * Create student with validation
     */
    private static Student createStudent(String name, String email, String password) throws Exception {
        // Check if already exists
        for (Student student : JsonHandler.students) {
            if (student.getEmail().equals(email)) {
                System.out.println("  - Student exists: " + name);
                return student;
            }
        }

        Student student = UserServices.signup(Student.class, name, email, password);
        System.out.println("  - Created student: " + name + " (" + student.getUserId() + ")");
        return student;
    }

    /**
     * Create a complete course with lessons and quizzes
     */
    private static Course createCourseWithLessons(String instructorId, String title, String description)
            throws Exception {
        // Check if course already exists
        for (Course course : JsonHandler.courses) {
            if (course.getTitle().equals(title)) {
                System.out.println("  - Course exists: " + title);
                return course;
            }
        }

        // Create course
        Course course = CourseServices.createCourse(instructorId, title, description);
        System.out.println("  - Created course: " + title + " (" + course.getCourseId() + ")");

        // Determine lesson content based on course title
        String[][] lessonData;
        if (title.contains("Java")) {
            lessonData = new String[][] {
                    { "Introduction to Java", "This lesson covers introduction to java in detail." },
                    { "Variables and Data Types", "This lesson covers variables and data types in detail." },
                    { "Control Flow Statements", "This lesson covers if-else, loops, and switch statements." },
                    { "Methods and Functions", "This lesson covers method declaration and usage." }
            };
        } else {
            lessonData = new String[][] {
                    { "Python Basics", "This lesson covers python basics in detail." },
                    { "NumPy and Pandas", "This lesson covers numpy and pandas in detail." },
                    { "Data Visualization", "This lesson covers matplotlib and seaborn." },
                    { "Machine Learning Intro", "This lesson covers basic ML concepts." }
            };
        }

        // Create lessons with quizzes
        for (int i = 0; i < lessonData.length; i++) {
            Lesson lesson = new Lesson(lessonData[i][0], lessonData[i][1]);

            // Create quiz for lesson
            Quiz quiz = createQuizForLesson(lesson.getLessonId(), lessonData[i][0], i);
            lesson.setQuiz(quiz);

            // Add to course
            CourseServices.addLessonToCourse(course.getCourseId(), lesson);
            System.out.println("    + Added lesson: " + lesson.getTitle());
        }

        return course;
    }

    /**
     * Create a quiz with 5 questions
     */
    private static Quiz createQuizForLesson(String lessonId, String lessonTitle, int seed) {
        ArrayList<Question> questions = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            ArrayList<String> options = new ArrayList<>();
            options.add("Option A");
            options.add("Option B");
            options.add("Option C");
            options.add("Option D");

            // Vary correct answers: 0, 1, 2, 3, then repeat
            int correctAnswer = (seed + i - 1) % 4;

            Question question = new Question(
                    "Question " + i + " for " + lessonTitle + "?",
                    options,
                    correctAnswer);

            questions.add(question);
        }

        return new Quiz("quiz_" + lessonId, questions, 70);
    }

    /**
     * Simulate quiz attempts for a student in a course with varied scores
     * 
     * @param firstAttemptRate  - percentage for first attempt (0.0 to 1.0)
     * @param secondAttemptRate - percentage for second attempt (0.0 to 1.0)
     */
    private static void simulateQuizAttempts(Course course, Student student, double firstAttemptRate,
            double secondAttemptRate) throws Exception {
        System.out.println("  - Student: " + student.getName() + " (Lesson 1: " + (int) (firstAttemptRate * 100) +
                "%, Lesson 2: " + (int) (secondAttemptRate * 100) + "%)");

        int lessonCount = 0;
        double currentRate = firstAttemptRate;

        for (Lesson lesson : course.getLessons()) {
            if (lesson.getQuiz() == null)
                continue;

            lessonCount++;
            Quiz quiz = lesson.getQuiz();

            // Alternate between first and second attempt rates for different lessons
            currentRate = (lessonCount == 1) ? firstAttemptRate : secondAttemptRate;

            // Generate answers based on success rate
            ArrayList<Integer> answers = new ArrayList<>();
            int correctCount = (int) (quiz.getQuestions().size() * currentRate);

            for (int i = 0; i < quiz.getQuestions().size(); i++) {
                Question q = quiz.getQuestions().get(i);

                if (i < correctCount) {
                    // Correct answer
                    answers.add(q.getCorrectAnswerIndex());
                } else {
                    // Wrong answer
                    int wrongAnswer = (q.getCorrectAnswerIndex() + 1) % 4;
                    answers.add(wrongAnswer);
                }
            }

            // Submit quiz
            QuizResult result = QuizServices.submitQuiz(
                    student.getUserId(),
                    course.getCourseId(),
                    lesson.getLessonId(),
                    answers);

            System.out.println("    Lesson " + lessonCount + " - Attempt 1: " + result.getScore() + "% " +
                    (result.isPassed() ? "✓ PASSED" : "✗ FAILED"));

            // If failed, retry with improved performance
            if (!result.isPassed()) {
                ArrayList<Integer> retryAnswers = new ArrayList<>();
                // Retry with 15% improvement or 75%, whichever is higher
                double retryRate = Math.max(currentRate + 0.15, 0.75);
                int retryCorrectCount = (int) (quiz.getQuestions().size() * retryRate);

                for (int i = 0; i < quiz.getQuestions().size(); i++) {
                    Question q = quiz.getQuestions().get(i);
                    if (i < retryCorrectCount) {
                        retryAnswers.add(q.getCorrectAnswerIndex());
                    } else {
                        retryAnswers.add((q.getCorrectAnswerIndex() + 1) % 4);
                    }
                }

                QuizResult retryResult = QuizServices.submitQuiz(
                        student.getUserId(),
                        course.getCourseId(),
                        lesson.getLessonId(),
                        retryAnswers);

                System.out.println("    Lesson " + lessonCount + " - Attempt 2: " + retryResult.getScore() + "% " +
                        (retryResult.isPassed() ? "✓ PASSED" : "✗ FAILED"));
            }

            // Test first 2 lessons only to keep it manageable
            if (lessonCount >= 2)
                break;
        }
    }

    /**
     * Complete all remaining lessons for a student to generate certificate
     */
    private static void completeAllLessons(Course course, Student student) throws Exception {
        System.out.println("  - Completing remaining lessons for: " + student.getName());

        for (Lesson lesson : course.getLessons()) {
            if (lesson.getQuiz() == null)
                continue;

            Quiz quiz = lesson.getQuiz();

            // Create answers with 85% correct (4 out of 5)
            ArrayList<Integer> answers = new ArrayList<>();
            for (int i = 0; i < quiz.getQuestions().size(); i++) {
                Question q = quiz.getQuestions().get(i);
                if (i < 4) {
                    answers.add(q.getCorrectAnswerIndex());
                } else {
                    answers.add((q.getCorrectAnswerIndex() + 1) % 4);
                }
            }

            QuizResult result = QuizServices.submitQuiz(
                    student.getUserId(),
                    course.getCourseId(),
                    lesson.getLessonId(),
                    answers);

            System.out.println("    " + lesson.getTitle() + ": " + result.getScore() + "% " +
                    (result.isPassed() ? "✓" : "✗"));
        }

        // Generate certificate since all lessons are completed
        double finalScore = 80.0; // Average score from quiz attempts
        Certificate cert = CertificateService.generateCertificate(student, course, finalScore);
        System.out.println("  🎓 Certificate generated: " + cert.getCertificateId());
    }

    /**
     * Print analytics report for a course
     */
    private static void printAnalyticsReport(Course course) throws Exception {
        CourseAnalytics analytics = AnalyticsServices.getCourseAnalytics(course.getCourseId());

        System.out.println("\n📊 COURSE ANALYTICS REPORT");
        System.out.println("-".repeat(80));
        System.out.println("Course: " + analytics.getCourseTitle());
        System.out.println("Total Students: " + analytics.getTotalStudents());
        System.out.println("Active Students: " + analytics.getActiveStudents());
        System.out
                .println("Average Completion: " + String.format("%.2f%%", analytics.getAverageCompletionPercentage()));
        System.out.println("Average Score: " + String.format("%.2f%%", analytics.getAverageScore()));
        System.out.println("-".repeat(80));
    }
}