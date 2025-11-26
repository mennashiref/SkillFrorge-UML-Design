/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.frontend.InstructorDashboard;

import com.mycompany.CourseManagement.Course;
import com.mycompany.CourseManagement.Lesson;
import com.mycompany.UserAccountManagement.Instructor;
import com.mycompany.UserAccountManagement.UserServices;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author Zeyad
 */
public class CourseView extends javax.swing.JPanel {

    private InstructorDashboardFrame parent;
    private Course course;
    private Instructor instructor;

    private JLabel lblCourseTitle;
    private JLabel lblCourseDescription;
    private JLabel lblCourseStatus;
    private JLabel lblEnrolledCount;
    private JLabel lblLessonCount;

    private JButton btnBack;
    private JButton btnEditCourse;
    private JButton btnDeleteCourse;
    private JButton btnViewStudents;
    private JButton btnCreateLesson;
    private JButton btnCourseAnalytics;
    private JButton btnStudentPerformance;

    private JList<String> lessonList;
    private DefaultListModel<String> lessonListModel;
    private JScrollPane lessonScrollPane;

    /**
     * Creates new form CourseView
     */
    public CourseView(InstructorDashboardFrame parent, Course course, Instructor instructor) {
        this.parent = parent;
        this.course = course;
        this.instructor = instructor;
        initComponents();
        loadCourseData();
        setupListeners();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 0));
        setBackground(new Color(245, 245, 245));

        // HEADER PANEL
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // CENTER PANEL - Lessons
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        // BOTTOM PANEL - Action Buttons
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(20, 15));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)));

        // Left side - Course Info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        lblCourseTitle = new JLabel("Course Title");
        lblCourseTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblCourseTitle.setForeground(new Color(33, 33, 33));
        lblCourseTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblCourseDescription = new JLabel("Course description goes here");
        lblCourseDescription.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblCourseDescription.setForeground(new Color(102, 102, 102));
        lblCourseDescription.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblCourseDescription.setBorder(BorderFactory.createEmptyBorder(8, 0, 15, 0));

        // Status and stats panel
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblCourseStatus = new JLabel("PENDING");
        lblCourseStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCourseStatus.setForeground(new Color(255, 152, 0));

        lblEnrolledCount = new JLabel("0 Students");
        lblEnrolledCount.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblEnrolledCount.setForeground(new Color(66, 66, 66));

        lblLessonCount = new JLabel("0 Lessons");
        lblLessonCount.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblLessonCount.setForeground(new Color(66, 66, 66));

        statsPanel.add(lblCourseStatus);
        statsPanel.add(lblEnrolledCount);
        statsPanel.add(lblLessonCount);

        infoPanel.add(lblCourseTitle);
        infoPanel.add(lblCourseDescription);
        infoPanel.add(statsPanel);

        // Right side - Action Buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(Color.WHITE);

        btnBack = createStyledButton("← Back", new Color(220, 220, 220), new Color(60, 60, 60));
        btnEditCourse = createStyledButton("✎ Edit", new Color(33, 150, 243), Color.WHITE);
        btnDeleteCourse = createStyledButton("🗑 Delete", new Color(244, 67, 54), Color.WHITE);

        actionPanel.add(btnBack);
        actionPanel.add(btnEditCourse);
        actionPanel.add(btnDeleteCourse);

        headerPanel.add(infoPanel, BorderLayout.CENTER);
        headerPanel.add(actionPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Section Header
        JPanel sectionHeader = new JPanel(new BorderLayout());
        sectionHeader.setBackground(new Color(245, 245, 245));

        JLabel lessonsLabel = new JLabel("Course Lessons");
        lessonsLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lessonsLabel.setForeground(new Color(33, 33, 33));

        btnCreateLesson = createStyledButton("+ Create Lesson", new Color(76, 175, 80), Color.WHITE);

        sectionHeader.add(lessonsLabel, BorderLayout.WEST);
        sectionHeader.add(btnCreateLesson, BorderLayout.EAST);

        // Lessons List
        lessonListModel = new DefaultListModel<>();
        lessonList = new JList<>(lessonListModel);
        lessonList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lessonList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lessonList.setFixedCellHeight(50);
        lessonList.setBackground(Color.WHITE);
        lessonList.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        lessonScrollPane = new JScrollPane(lessonList);
        lessonScrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        lessonScrollPane.setBackground(Color.WHITE);

        centerPanel.add(sectionHeader, BorderLayout.NORTH);
        centerPanel.add(lessonScrollPane, BorderLayout.CENTER);

        return centerPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(230, 230, 230)));

        btnViewStudents = createStyledButton("View Enrolled Students", new Color(103, 58, 183), Color.WHITE);
        btnViewStudents.setPreferredSize(new Dimension(220, 40));

        btnCourseAnalytics = createStyledButton("Course Analytics", new Color(33, 150, 243), Color.WHITE);
        btnCourseAnalytics.setPreferredSize(new Dimension(180, 40));

        btnStudentPerformance = createStyledButton("Student Performance", new Color(76, 175, 80), Color.WHITE);
        btnStudentPerformance.setPreferredSize(new Dimension(200, 40));

        bottomPanel.add(btnViewStudents);
        bottomPanel.add(btnCourseAnalytics);
        bottomPanel.add(btnStudentPerformance);

        return bottomPanel;
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(120, 38));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color original = bgColor;

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(original.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(original);
            }
        });

        return button;
    }

    private void loadCourseData() {
        // Set course info
        lblCourseTitle.setText(course.getTitle());
        lblCourseDescription.setText(course.getDescription());

        // Set status with color
        switch (course.getStatus()) {
            case APPROVED:
                lblCourseStatus.setText("APPROVED");
                lblCourseStatus.setForeground(new Color(76, 175, 80));
                break;
            case REJECTED:
                lblCourseStatus.setText("REJECTED");
                lblCourseStatus.setForeground(new Color(244, 67, 54));
                break;
            default:
                lblCourseStatus.setText("PENDING");
                lblCourseStatus.setForeground(new Color(255, 152, 0));
        }

        // Load students count
        try {
            ArrayList<String> students = instructor.getEnrolledStudents(course.getCourseId());
            lblEnrolledCount.setText(students.size() + " Student" + (students.size() != 1 ? "s" : ""));
        } catch (Exception e) {
            lblEnrolledCount.setText("0 Students");
        }

        // Load lessons
        loadLessons();
    }

    private void loadLessons() {
        lessonListModel.clear();
        try {
            ArrayList<Lesson> lessons = instructor.getCourseLessons(course.getCourseId());
            lblLessonCount.setText(lessons.size() + " Lesson" + (lessons.size() != 1 ? "s" : ""));

            if (lessons.isEmpty()) {
                lessonListModel.addElement("No lessons yet. Click 'Create Lesson' to add one.");
            } else {
                int index = 1;
                for (Lesson l : lessons) {
                    lessonListModel.addElement(index++ + ". " + l.getTitle());
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading lessons: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupListeners() {
        btnBack.addActionListener(e -> parent.showDashboard());

        btnEditCourse.addActionListener(e -> parent.showCourseFormPanel(course));

        btnDeleteCourse.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this course?\nThis action cannot be undone.",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    instructor.deleteCourse(course.getCourseId());
                    JOptionPane.showMessageDialog(this, "Course deleted successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    parent.showDashboard();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error deleting course: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCreateLesson.addActionListener(e -> parent.showLessonFormPanel(course, null));

        btnViewStudents.addActionListener(e -> {
            try {
                ArrayList<String> students = instructor.getEnrolledStudents(course.getCourseId());
                if (students.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No students enrolled in this course yet.",
                            "Enrolled Students", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    StringBuilder studentList = new StringBuilder("<html><body style='width: 350px;'>");
                    studentList.append("<h3>Enrolled Students (").append(students.size()).append(")</h3>");
                    studentList.append("<ul>");
                    for (String studentId : students) {
                        String name = UserServices.getUserNameById(studentId);
                        studentList.append("<li><b>").append(name).append("</b> (").append(studentId).append(")</li>");
                    }
                    studentList.append("</ul></body></html>");
                    JOptionPane.showMessageDialog(this, studentList.toString(),
                            "Enrolled Students", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading students: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        lessonList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && lessonList.getSelectedIndex() >= 0) {
                try {
                    ArrayList<Lesson> lessons = instructor.getCourseLessons(course.getCourseId());
                    if (!lessons.isEmpty() && lessonList.getSelectedIndex() < lessons.size()) {
                        Lesson selectedLesson = lessons.get(lessonList.getSelectedIndex());
                        parent.showLessonPanel(selectedLesson, course);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error loading lesson: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCourseAnalytics.addActionListener(e -> {
            parent.showCourseAnalytics(course.getCourseId());
        });

        btnStudentPerformance.addActionListener(e -> {
            try {
                ArrayList<String> students = instructor.getEnrolledStudents(course.getCourseId());
                if (students.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No students enrolled in this course yet.",
                            "Student Performance", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Create student selection dialog
                    String[] studentOptions = new String[students.size()];
                    for (int i = 0; i < students.size(); i++) {
                        String studentId = students.get(i);
                        String name = UserServices.getUserNameById(studentId);
                        studentOptions[i] = name + " (" + studentId + ")";
                    }

                    String selectedStudent = (String) JOptionPane.showInputDialog(
                            this,
                            "Select a student to view performance:",
                            "Student Performance",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            studentOptions,
                            studentOptions[0]);

                    if (selectedStudent != null) {
                        // Extract student ID from the selection
                        String studentId = selectedStudent.substring(
                                selectedStudent.lastIndexOf("(") + 1,
                                selectedStudent.lastIndexOf(")"));
                        parent.showStudentPerformance(studentId, course.getCourseId());
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading students: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
