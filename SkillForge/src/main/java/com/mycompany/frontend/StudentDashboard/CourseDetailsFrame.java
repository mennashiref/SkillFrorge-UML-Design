package com.mycompany.frontend.StudentDashboard;

import javax.swing.*;
import java.awt.*;
import com.mycompany.CourseManagement.Course;
import com.mycompany.CourseManagement.CourseServices;
import com.mycompany.CourseManagement.Lesson;
import com.mycompany.QuizManagement.Quiz;
import com.mycompany.CourseManagement.QuizServices;
import com.mycompany.UserAccountManagement.Student;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class CourseDetailsFrame extends JPanel {
    private Course course;
    private Student student;
    private Runnable backAction;
    private JPanel lessonsPanel;
    private static final int MAX_ATTEMPTS = 3;

    public CourseDetailsFrame(Course course, Student student, Runnable backAction) {
        this.course = course;
        this.student = student;
        this.backAction = backAction;
        createCourseDetails();
    }

    private void createCourseDetails() {
        removeAll();
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(50, 110, 160));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> backAction.run());
        headerPanel.add(backButton, BorderLayout.WEST);

        JLabel titleHeader = new JLabel("Course Details", JLabel.CENTER);
        titleHeader.setFont(new Font("Arial", Font.BOLD, 20));
        titleHeader.setForeground(Color.WHITE);
        headerPanel.add(titleHeader, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20),
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true)));

        JLabel titleLabel = new JLabel("Title: " + safeString(course.getTitle()));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(50, 50, 50));

        JLabel instructorLabel = new JLabel("Instructor: " + safeString(course.getInstructorId()));
        instructorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        instructorLabel.setForeground(new Color(70, 70, 70));

        JLabel descLabel = new JLabel("<html><b>Description:</b> " + safeString(course.getDescription()) + "</html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setForeground(new Color(70, 70, 70));

        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(instructorLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(descLabel);

        JPanel lessonsContainer = new JPanel(new BorderLayout());
        lessonsContainer.setBackground(new Color(245, 245, 250));
        lessonsContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lessonsTitle = new JLabel("Lessons");
        lessonsTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lessonsTitle.setForeground(new Color(50, 50, 50));
        lessonsTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        lessonsContainer.add(lessonsTitle, BorderLayout.NORTH);

        lessonsPanel = new JPanel();
        lessonsPanel.setLayout(new BoxLayout(lessonsPanel, BoxLayout.Y_AXIS));
        lessonsPanel.setBackground(new Color(245, 245, 250));

        try {
            ArrayList<Lesson> lessons = student.getCourseLessons(course.getCourseId());
            if (lessons == null || lessons.isEmpty()) {
                JLabel emptyLabel = new JLabel("No lessons found");
                emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
                emptyLabel.setForeground(Color.GRAY);
                lessonsPanel.add(emptyLabel);
            } else {
                for (int i = 0; i < lessons.size(); i++) {
                    Lesson lesson = lessons.get(i);
                    JPanel lessonPanel = createLessonPanel(lesson, i);
                    lessonsPanel.add(lessonPanel);
                    lessonsPanel.add(Box.createVerticalStrut(10));
                }
            }
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Error loading lessons: " + e.getMessage());
            errorLabel.setForeground(Color.RED);
            lessonsPanel.add(errorLabel);
        }

        JScrollPane scrollPane = new JScrollPane(lessonsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        lessonsContainer.add(scrollPane, BorderLayout.CENTER);

        JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
        contentPanel.setBackground(new Color(245, 245, 250));
        contentPanel.add(infoPanel, BorderLayout.NORTH);
        contentPanel.add(lessonsContainer, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private String safeString(String s) {
        return s == null ? "" : s;
    }

    private boolean canAccessLesson(Lesson lesson, int lessonIndex) {
        try {
            if (lessonIndex == 0)
                return true;

            ArrayList<Lesson> allLessons = student.getCourseLessons(course.getCourseId());
            if (allLessons == null || lessonIndex - 1 < 0 || lessonIndex - 1 >= allLessons.size())
                return false;

            Lesson prev = allLessons.get(lessonIndex - 1);
            return CourseServices.isLessonCompleted(student.getUserId(), prev.getLessonId());
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isQuizPassedForLesson(String lessonId) {
        try {
            ArrayList<Map<String, Object>> results = QuizServices.getStudentQuizResults(student.getUserId());
            if (results == null || results.isEmpty())
                return false;

            for (Map<String, Object> result : results) {
                boolean passed = (boolean) result.get("passed");
                if (passed) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error checking quiz: " + e.getMessage());
            return false;
        }
    }

    private JPanel createLessonPanel(Lesson lesson, int lessonIndex) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(700, 100));
        panel.setName("lesson_" + lesson.getLessonId());

        JLabel titleLabel = new JLabel(safeString(lesson.getTitle()));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        titleLabel.setForeground(new Color(40, 40, 40));

        boolean canAccess = canAccessLesson(lesson, lessonIndex);
        boolean isCompleted = false;
        try {
            isCompleted = CourseServices.isLessonCompleted(student.getUserId(), lesson.getLessonId());
        } catch (Exception e) {
            System.out.println("Error checking lesson completion: " + e.getMessage());
        }

        boolean quizPassed = isQuizPassedForLesson(lesson.getLessonId());

        int remainingAttempts = 0;
        int usedAttempts = 0;
        try {
            remainingAttempts = QuizServices.getRemainingAttempts(student.getUserId(), course.getCourseId(),
                    lesson.getLessonId());
            usedAttempts = QuizServices.getUsedAttempts(student.getUserId(), lesson.getLessonId());
        } catch (Exception e) {
            System.out.println("Error getting attempts: " + e.getMessage());
        }

        System.out.println("Lesson: " + lesson.getTitle() + " | Access: " + canAccess + " | Completed: " + isCompleted
                + " | QuizPassed: " + quizPassed + " | Attempts: " + usedAttempts + "/" + MAX_ATTEMPTS);

        JButton contentButton = new JButton("View Content");
        contentButton.setFont(new Font("Arial", Font.PLAIN, 12));
        contentButton.setBackground(canAccess ? new Color(70, 130, 180) : new Color(200, 200, 200));
        contentButton.setForeground(canAccess ? Color.WHITE : new Color(150, 150, 150));
        contentButton.setFocusPainted(false);
        contentButton.setCursor(canAccess ? new Cursor(Cursor.HAND_CURSOR) : new Cursor(Cursor.DEFAULT_CURSOR));
        contentButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        contentButton.setEnabled(canAccess);
        contentButton.addActionListener(e -> {
            if (canAccess)
                showLessonContent(lesson);
            else
                JOptionPane.showMessageDialog(this, "Please complete the previous lesson first!", "Lesson Locked",
                        JOptionPane.WARNING_MESSAGE);
        });

        JButton quizButton = new JButton("Take Quiz (" + remainingAttempts + " left)");
        quizButton.setFont(new Font("Arial", Font.PLAIN, 12));

        boolean canTakeQuiz = canAccess && !isCompleted && remainingAttempts > 0;
        quizButton.setBackground(canTakeQuiz ? new Color(255, 193, 7) : new Color(200, 200, 200));
        quizButton.setForeground(canTakeQuiz ? Color.BLACK : new Color(150, 150, 150));
        quizButton.setFocusPainted(false);
        quizButton.setCursor(canTakeQuiz ? new Cursor(Cursor.HAND_CURSOR) : new Cursor(Cursor.DEFAULT_CURSOR));
        quizButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        quizButton.setEnabled(canTakeQuiz);

        quizButton.addActionListener(e -> {
            int currentRemainingAttempts = 0;
            int currentUsedAttempts = 0;
            try {
                currentRemainingAttempts = QuizServices.getRemainingAttempts(student.getUserId(), course.getCourseId(),
                        lesson.getLessonId());
                currentUsedAttempts = QuizServices.getUsedAttempts(student.getUserId(), lesson.getLessonId());
            } catch (Exception ex) {
                System.out.println("Error getting attempts in action listener: " + ex.getMessage());
            }

            if (!canAccess) {
                JOptionPane.showMessageDialog(this, "Please complete the previous lesson first!", "Lesson Locked",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (currentRemainingAttempts <= 0) {
                JOptionPane.showMessageDialog(this,
                        "❌ No more quiz attempts left!\n\n" +
                                "You have used all " + MAX_ATTEMPTS + " attempts for this quiz.\n" +
                                "Used attempts: " + currentUsedAttempts + "/" + MAX_ATTEMPTS + "\n" +
                                "Please contact your instructor.",
                        "Maximum Attempts Reached", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                if (currentRemainingAttempts == 1) {
                    int choice = JOptionPane.showConfirmDialog(this,
                            "⚠ Last Attempt Warning!\n\n" +
                                    "This is your LAST attempt for this quiz.\n" +
                                    "Used attempts: " + currentUsedAttempts + "/" + MAX_ATTEMPTS + "\n" +
                                    "Do you want to continue?",
                            "Last Attempt", JOptionPane.YES_NO_OPTION);

                    if (choice != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                Quiz quiz = (Quiz) CourseServices.getQuizForLesson(lesson.getLessonId());
                if (quiz != null) {
                    QuizFrame.startQuiz(quiz, this, lesson.getLessonId(), course.getCourseId());
                } else {
                    QuizFrame.startQuiz();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error loading quiz: " + ex.getMessage());
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(contentButton);
        buttonPanel.add(quizButton);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        statusPanel.setBackground(Color.WHITE);

        // معلومات المحاولات
        JLabel attemptsLabel = new JLabel("Attempts: " + usedAttempts + "/" + MAX_ATTEMPTS);
        attemptsLabel.setFont(new Font("Arial", Font.BOLD, 11));
        if (remainingAttempts == 0) {
            attemptsLabel.setForeground(Color.RED);
        } else if (remainingAttempts == 1) {
            attemptsLabel.setForeground(new Color(255, 140, 0));
        } else {
            attemptsLabel.setForeground(new Color(40, 167, 69));
        }
        statusPanel.add(attemptsLabel);
        statusPanel.add(Box.createHorizontalStrut(10));

        if (isCompleted) {
            JLabel completedLabel = new JLabel("✅ Completed");
            completedLabel.setFont(new Font("Arial", Font.BOLD, 12));
            completedLabel.setForeground(new Color(40, 167, 69));
            statusPanel.add(completedLabel);
        } else if (quizPassed && canAccess && remainingAttempts > 0) {
            JLabel passedLabel = new JLabel("✅ Quiz Passed — Lesson completed automatically");
            passedLabel.setFont(new Font("Arial", Font.BOLD, 12));
            passedLabel.setForeground(new Color(40, 167, 69));
            statusPanel.add(passedLabel);
        } else if (remainingAttempts <= 0) {
            JLabel noAttemptsLabel = new JLabel("❌ No attempts left - Cannot complete");
            noAttemptsLabel.setFont(new Font("Arial", Font.BOLD, 12));
            noAttemptsLabel.setForeground(Color.RED);
            statusPanel.add(noAttemptsLabel);
        } else if (!quizPassed && canAccess && !isCompleted) {
            JLabel quizRequiredLabel = new JLabel("⚠ Pass the quiz to complete");
            quizRequiredLabel.setFont(new Font("Arial", Font.BOLD, 12));
            quizRequiredLabel.setForeground(new Color(255, 193, 7));
            statusPanel.add(quizRequiredLabel);
        } else if (!canAccess) {
            JLabel lockedLabel = new JLabel("🔒 Complete previous lesson first");
            lockedLabel.setFont(new Font("Arial", Font.ITALIC, 11));
            lockedLabel.setForeground(new Color(150, 150, 150));
            statusPanel.add(lockedLabel);
        }

        panel.add(statusPanel, BorderLayout.NORTH);
        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(buttonPanel, BorderLayout.EAST);
        return panel;
    }

    private void showLessonContent(Lesson lesson) {
        removeAll();
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton backButton = new JButton("← Back to Course");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(50, 110, 160));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> createCourseDetails());
        headerPanel.add(backButton, BorderLayout.WEST);

        JLabel titleHeader = new JLabel(safeString(lesson.getTitle()), JLabel.CENTER);
        titleHeader.setFont(new Font("Arial", Font.BOLD, 20));
        titleHeader.setForeground(Color.WHITE);
        headerPanel.add(titleHeader, BorderLayout.CENTER);

        // Display attempts information in header
        try {
            int usedAttempts = QuizServices.getUsedAttempts(student.getUserId(), lesson.getLessonId());

            JLabel attemptsLabel = new JLabel("Attempts: " + usedAttempts + "/" + MAX_ATTEMPTS);
            attemptsLabel.setFont(new Font("Arial", Font.BOLD, 12));
            attemptsLabel.setForeground(Color.WHITE);
            headerPanel.add(attemptsLabel, BorderLayout.EAST);
        } catch (Exception e) {
            System.out.println("Error getting attempts for header: " + e.getMessage());
        }

        JButton quizButton = new JButton("Take Quiz");
        quizButton.setFont(new Font("Arial", Font.BOLD, 14));
        quizButton.setBackground(new Color(255, 193, 7));
        quizButton.setForeground(Color.BLACK);
        quizButton.setFocusPainted(false);
        quizButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        quizButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        quizButton.addActionListener(e -> {
            try {
                Quiz q = (Quiz) CourseServices.getQuizForLesson(lesson.getLessonId());
                if (q != null)
                    QuizFrame.startQuiz(q, this, lesson.getLessonId(), course.getCourseId());
                else
                    QuizFrame.startQuiz();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error loading quiz: " + ex.getMessage());
            }
        });
        headerPanel.add(quizButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        JPanel contentContainer = new JPanel(new BorderLayout());
        contentContainer.setBackground(new Color(245, 245, 250));
        contentContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JTextArea contentArea = new JTextArea(safeString(lesson.getContent()));
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setFont(new Font("Arial", Font.PLAIN, 15));
        contentArea.setForeground(new Color(50, 50, 50));
        contentArea.setBackground(Color.WHITE);
        contentArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        contentContainer.add(contentPanel, BorderLayout.CENTER);
        add(contentContainer, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void updateLessonButton(String lessonId, boolean enabled) {
        if (lessonsPanel == null)
            return;

        for (Component comp : lessonsPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel lessonPanel = (JPanel) comp;
                String panelName = lessonPanel.getName();

                if (panelName != null && panelName.equals("lesson_" + lessonId)) {
                    updateCompleteButtonInPanel(lessonPanel, enabled);
                    return;
                }
            }
        }
    }

    private void updateCompleteButtonInPanel(JPanel lessonPanel, boolean enabled) {
        for (Component comp : lessonPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel buttonPanel = (JPanel) comp;
                for (Component btn : buttonPanel.getComponents()) {
                    if (btn instanceof JButton) {
                        JButton button = (JButton) btn;
                        String buttonName = button.getName();

                        if (buttonName != null && buttonName.startsWith("complete_")) {
                            button.setEnabled(enabled);
                            if (enabled) {
                                button.setText("Mark Complete");
                                button.setBackground(new Color(40, 167, 69));
                                button.setForeground(Color.WHITE);
                            }
                            revalidate();
                            repaint();
                            return;
                        }
                    }
                }
            }
        }
    }

    public void refreshCourseDetails() {
        createCourseDetails();
    }

    public void refreshAfterQuiz() {
        createCourseDetails();
    }

    public void updateLessonButtonByName(String lessonId, boolean enabled) {
        if (lessonsPanel == null)
            return;

        for (Component comp : lessonsPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel lessonPanel = (JPanel) comp;
                if (("lesson_" + lessonId).equals(lessonPanel.getName())) {
                    for (Component innerComp : lessonPanel.getComponents()) {
                        if (innerComp instanceof JPanel) {
                            JPanel buttonPanel = (JPanel) innerComp;
                            for (Component btn : buttonPanel.getComponents()) {
                                if (btn instanceof JButton) {
                                    JButton button = (JButton) btn;
                                    if (button.getName() != null && button.getName().equals("complete_" + lessonId)) {
                                        button.setEnabled(enabled);
                                        if (enabled) {
                                            button.setText("Mark Complete");
                                            button.setBackground(new Color(40, 167, 69));
                                            button.setForeground(Color.WHITE);
                                        }
                                        revalidate();
                                        repaint();
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}