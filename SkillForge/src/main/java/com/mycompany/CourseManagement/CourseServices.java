package com.mycompany.CourseManagement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mycompany.JsonHandler.JsonHandler;
import com.mycompany.UserAccountManagement.Student;
import com.mycompany.QuizManagement.QuizAttempt;
import com.mycompany.QuizManagement.Quiz;
import java.io.IOException;
import java.util.ArrayList;

public class CourseServices {
    private static int foundAt;

    static {
        JsonHandler.loadCourses();
    }

    public static ArrayList<Course> getAllCourses() throws JsonProcessingException, IOException {
        return new ArrayList<>(JsonHandler.courses);
    }

    public static Course createCourse(String instructorId, String title, String description)
            throws JsonProcessingException, IOException {
        Course course = new Course(instructorId, title, description);
        JsonHandler.courses.add(course);
        JsonHandler.saveCourses();
        return course;
    }

    public static Course findCourseById(String courseId)
            throws IllegalArgumentException, JsonProcessingException, IOException {
        for (int i = 0; i < JsonHandler.courses.size(); i++) {
            Course course = JsonHandler.courses.get(i);
            if (course.getCourseId().equals(courseId)) {
                foundAt = i;
                return course;
            }
        }
        return null;
    }

    public static Course findCourseByLessonId(String lessonId) throws IOException {
        for (int i = 0; i < JsonHandler.courses.size(); i++) {
            Course course = JsonHandler.courses.get(i);
            ArrayList<Lesson> lessons = course.getLessons();
            if (lessons != null) {
                for (Lesson lesson : lessons) {
                    if (lesson.getLessonId().equals(lessonId)) {
                        foundAt = i;
                        return course;
                    }
                }
            }
        }
        return null;
    }

    public static Course updateCourse(String courseId, String newDescription, String newTitle)
            throws IllegalArgumentException, IOException {
        Course course = findCourseById(courseId);
        course.setDescription(newDescription);
        course.setTitle(newTitle);
        JsonHandler.saveCourses();
        return course;
    }

    public static boolean deleteCourseById(String courseId) throws IOException {
        for (int i = 0; i < JsonHandler.courses.size(); i++) {
            Course course = JsonHandler.courses.get(i);
            if (course.getCourseId().equals(courseId)) {
                ArrayList<String> enrolledStudents = course.getStudentIds();
                JsonHandler.courses.remove(i);

                // Remove CourseProgress from all enrolled students
                if (enrolledStudents != null) {
                    for (String studentId : enrolledStudents) {
                        Student student = JsonHandler.getStudent(studentId);
                        if (student != null) {
                            student.getCourseProgressMap().remove(courseId);
                        }
                    }
                }

                JsonHandler.saveCourses();
                JsonHandler.saveUsers();
                return true;
            }
        }
        return false;
    }

    public static Lesson addLessonToCourse(String courseId, Lesson lesson)
            throws IllegalArgumentException, JsonProcessingException, IOException {
        Course course = findCourseById(courseId);
        course.addLesson(lesson);
        JsonHandler.saveCourses();
        return lesson;
    }

    public static boolean enrollStudentInCourse(String courseId, String studentId)
            throws IllegalArgumentException, JsonProcessingException, IOException {
        Student student = JsonHandler.getStudent(studentId);

        if (student == null) {
            throw new IllegalArgumentException("Student with ID " + studentId + " not found");
        }

        Course course = findCourseById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course with ID " + courseId + " not found");
        }

        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }

        // Check if already enrolled in course's student list
        ArrayList<String> studentIds = course.getStudentIds();
        if (studentIds.contains(studentId)) {
            return false;
        }

        // Check if CourseProgress already exists
        if (student.getCourseProgressMap().containsKey(courseId)) {
            return false; // Already enrolled
        }

        boolean enrolled = course.enrollStudent(studentId);
        if (enrolled) {
            // Initialize CourseProgress for this course
            student.getCourseProgress(courseId);

            JsonHandler.saveCourses();
            JsonHandler.saveUsers();
        }
        return enrolled;
    }

    public static ArrayList<Course> getEnrolledCoursesByStudent(String studentId)
            throws JsonProcessingException, IOException {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }

        ArrayList<Course> enrolledCourses = new ArrayList<>();
        for (Course course : JsonHandler.courses) {
            ArrayList<String> studentIds = course.getStudentIds();
            if (studentIds != null && studentIds.contains(studentId)) {
                enrolledCourses.add(course);
            }
        }
        return enrolledCourses;
    }

    public static ArrayList<String> getEnrolledStudents(String courseId)
            throws IllegalArgumentException, JsonProcessingException, IOException {
        Course course = findCourseById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course with ID " + courseId + " not found");
        }
        return course.getStudentIds();
    }

    public static ArrayList<Lesson> getAllLessonsFromCourse(String courseId)
            throws IllegalArgumentException, JsonProcessingException, IOException {
        Course course = findCourseById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course with ID " + courseId + " not found");
        }
        return course.getLessons();
    }

    public static Lesson createLesson(String title, String content) {
        return new Lesson(title, content);
    }

    public static Lesson findLessonById(String courseId, String lessonId)
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

    public static Lesson findLessonById(String lessonId)
            throws JsonProcessingException, IOException {
        for (int i = 0; i < JsonHandler.courses.size(); i++) {
            Course course = JsonHandler.courses.get(i);
            ArrayList<Lesson> lessons = course.getLessons();
            if (lessons != null) {
                for (Lesson lesson : lessons) {
                    if (lesson.getLessonId().equals(lessonId)) {
                        foundAt = i;
                        return lesson;
                    }
                }
            }
        }
        return null;
    }

    public static Lesson updateLessonById(String lessonId, String newTitle, String newContent)
            throws IllegalArgumentException, JsonProcessingException, IOException {
        Lesson lesson = findLessonById(lessonId);
        if (lesson == null) {
            throw new IllegalArgumentException("Lesson with ID " + lessonId + " not found");
        }
        lesson.setTitle(newTitle);
        lesson.setContent(newContent);
        JsonHandler.saveCourses();
        return lesson;
    }

    public static boolean deleteLessonById(String lessonId)
            throws JsonProcessingException, IOException {
        Lesson lesson = findLessonById(lessonId);
        if (lesson == null) {
            return false;
        }

        Course course = JsonHandler.courses.get(getIndex());
        ArrayList<String> studentIds = course.getStudentIds();
        ArrayList<Lesson> lessons = course.getLessons();
        boolean removed = false;

        for (int i = 0; i < lessons.size(); i++) {
            if (lessons.get(i).getLessonId().equals(lessonId)) {
                lessons.remove(i);
                removed = true;
                break;
            }
        }

        // Remove lesson status from all enrolled students
        for (String studentId : studentIds) {
            Student student = JsonHandler.getStudent(studentId);
            if (student != null) {
                CourseProgress progress = student.getCourseProgress(course.getCourseId());
                if (progress != null && progress.getLessonStatus() != null) {
                    progress.getLessonStatus().remove(lessonId);
                }
            }
        }

        if (removed) {
            JsonHandler.saveCourses();
            JsonHandler.saveUsers();
        }
        return removed;
    }

    public static boolean isLessonCompleted(String studentId, String courseId, String lessonId) {
        Student student = JsonHandler.getStudent(studentId);

        if (student == null) {
            return false;
        }

        CourseProgress progress = student.getCourseProgress(courseId);
        if (progress == null || progress.getLessonStatus() == null) {
            return false;
        }

        return "PASSED".equals(progress.getLessonStatus().get(lessonId));
    }

    public static void markLessonCompleted(String studentId, String lessonId) throws Exception {
        Student student = JsonHandler.getStudent(studentId);

        if (student == null) {
            return;
        }

        try {
            Course course = findCourseByLessonId(lessonId);
            if (course != null) {
                student.markLessonCompleted(course.getCourseId(), lessonId);
                JsonHandler.saveUsers();
            }
        } catch (IOException ex) {
            System.err.println("Error marking lesson completed: " + ex.getMessage());
        }
    }

    public static int getIndex() {
        return foundAt;
    }

    public static void submitQuizAttempt(String studentId, QuizAttempt quizAttempt) throws IOException, Exception {
        System.out.println("=== SAVING QUIZ ATTEMPT ===");
        System.out.println("Student: " + studentId);
        System.out.println("Lesson: " + quizAttempt.getLessonId());
        System.out.println("Score: " + quizAttempt.getScorePercent() + "%");
        System.out.println("Passed: " + quizAttempt.isPassed());

        Student student = JsonHandler.getStudent(studentId);
        if (student != null) {
            student.submitQuiz(quizAttempt);

            // إذا نجح في الكويز، نكمل الدرس اتوماتيك
            if (quizAttempt.isPassed()) {
                System.out.println("✅ Student passed quiz - auto-completing lesson");
                try {
                    completeLessonViaQuiz(studentId, quizAttempt.getLessonId());
                } catch (Exception e) {
                    System.out.println("Error auto-completing lesson: " + e.getMessage());
                }
            } else {
                System.out.println("❌ Student failed quiz - lesson not completed");
            }

            // Update quiz average score across all students
            updateQuizAverageScore(quizAttempt.getCourseId(), quizAttempt.getLessonId());
        } else {
            throw new IOException("Student not found: " + studentId);
        }
    }

    private static void updateQuizAverageScore(String courseId, String lessonId) throws IOException {
        Course course = findCourseById(courseId);
        Lesson lesson = findLessonById(lessonId);

        if (lesson == null || lesson.getQuiz() == null || course == null) {
            return;
        }

        // Get all enrolled students
        ArrayList<String> studentIds = course.getStudentIds();
        int totalScore = 0;
        int studentCount = 0;

        // Calculate average across all students (best score for each)
        for (String sid : studentIds) {
            Student student = JsonHandler.getStudent(sid);
            if (student != null) {
                CourseProgress progress = student.getCourseProgress(courseId);
                if (progress != null) {
                    int bestScore = -1;
                    for (QuizAttempt attempt : progress.getQuizAttempts()) {
                        if (attempt.getLessonId().equals(lessonId)) {
                            if (attempt.getScorePercent() > bestScore) {
                                bestScore = attempt.getScorePercent();
                            }
                        }
                    }
                    if (bestScore >= 0) {
                        totalScore += bestScore;
                        studentCount++;
                    }
                }
            }
        }

        if (studentCount > 0) {
            double averageScore = (double) totalScore / studentCount;
            lesson.getQuiz().setAverageScore(Math.round(averageScore * 100.0) / 100.0);
            JsonHandler.saveCourses();
        }
    }

    public static boolean isCourseCompleted(String studentId, String courseId) throws IOException {
        return CertificateService.isCourseCompleted(studentId, courseId);
    }

    public static double getCourseProgress(String studentId, String courseId) throws IOException {
        Student student = JsonHandler.getStudent(studentId);
        if (student != null) {
            return student.getCourseProgressPercentage(courseId);
        }
        return 0.0;
    }

    // Add method to get course score
    public static double getCourseScore(String studentId, String courseId) throws IOException {
        Student student = JsonHandler.getStudent(studentId);
        if (student != null) {
            return student.getCourseScore(courseId);
        }
        return 0.0;
    }

    public static ArrayList<Course> getCoursesByStatus(Status status) throws IOException, JsonProcessingException {
        ArrayList<Course> courses = new ArrayList<>();
        for (Course course : CourseServices.getAllCourses()) {
            if (course.getStatus() == status) {
                courses.add(course);
            }
        }
        return courses;
    }

    public static Course updateCourseStatus(String courseId, Status status)
            throws IllegalArgumentException, IOException {
        Course course = CourseServices.findCourseById(courseId);
        course.setStatus(status);
        JsonHandler.saveCourses();
        return course;
    }

    public static Course updateCourseStatus(String courseId, String status)
            throws IllegalArgumentException, IOException {
        Course course = CourseServices.findCourseById(courseId);
        course.setStatus(Status.valueOf(status));
        JsonHandler.saveCourses();
        return course;
    }

    // الدالة الجديدة - تحقق إذا الطالب نجح في أي كويز
    public static boolean hasStudentPassedAnyQuiz(String studentId) throws IOException {
        return QuizServices.hasStudentPassedAnyQuiz(studentId);
    }

    public static void assignQuizToLesson(String lessonId, Quiz quiz) throws IOException {
        Lesson lesson = findLessonById(lessonId);
        if (lesson != null) {
            lesson.setQuizId(quiz.getQuizId());
            lesson.setQuiz(quiz);
            JsonHandler.saveCourses();
        }
    }

    public static com.mycompany.QuizManagement.Quiz getQuizByLessonId(String lessonId) throws IOException {
        Lesson lesson = findLessonById(lessonId);
        if (lesson != null && lesson.getQuiz() != null) {
            return lesson.getQuiz();
        }
        return null;
    }

    public static void completeLessonViaQuiz(String studentId, String lessonId) throws IOException, Exception {
        markLessonCompleted(studentId, lessonId);
        System.out.println("Lesson " + lessonId + " completed automatically for student " + studentId);
    }

    public static boolean canTakeQuiz(String studentId, String courseId, String lessonId) throws IOException {
        return QuizServices.getRemainingAttempts(studentId, courseId, lessonId) > 0;
    }

    public static boolean isLessonCompleted(String studentId, String lessonId) throws IOException {
        if (QuizServices.isLessonCompletedViaQuiz(studentId, lessonId)) {
            return true;
        }

        try {
            Course course = findCourseByLessonId(lessonId);
            if (course != null) {
                Student student = JsonHandler.getStudent(studentId);
                if (student != null) {
                    CourseProgress progress = student.getCourseProgress(course.getCourseId());
                    if (progress != null) {
                        String status = progress.getLessonStatus().get(lessonId);
                        return "PASSED".equals(status);
                    }
                }
            }
        } catch (Exception e) {
        }

        return false;
    }

    public static void markLessonAsComplete(String studentId, String lessonId) throws Exception {
        markLessonCompleted(studentId, lessonId);
    }

    public static com.mycompany.QuizManagement.Quiz getQuizForLesson(String lessonId) throws IOException {
        System.out.println("=== DEBUG GET QUIZ FOR LESSON ===");
        System.out.println("Lesson ID: " + lessonId);

        Lesson lesson = findLessonById(lessonId);
        if (lesson != null) {
            System.out.println("Lesson found: " + lesson.getTitle());
            System.out.println("Quiz exists: " + (lesson.getQuiz() != null));
            if (lesson.getQuiz() != null) {
                System.out.println("Quiz questions: " + lesson.getQuiz().getQuestions().size());
            }
            return lesson.getQuiz();
        }
        System.out.println("Lesson not found!");
        return null;
    }

    public static Lesson addLessonToCourse(String courseId, String lessonTitle, String lessonContent)
            throws IOException {
        Lesson lesson = createLesson(lessonTitle, lessonContent);
        return addLessonToCourse(courseId, lesson);
    }
}