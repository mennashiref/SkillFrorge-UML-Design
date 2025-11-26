package com.mycompany.frontend.InstructorDashboard;

import com.mycompany.CourseManagement.Course;
import com.mycompany.CourseManagement.Lesson;
import com.mycompany.UserAccountManagement.Instructor;
import java.awt.*;
import javax.swing.*;

/**
 * Form panel for creating or updating lessons
 */
public class LessonFormPanel extends JPanel {

    private InstructorDashboardFrame parent;
    private Instructor instructor;
    private Course course;
    private Lesson lesson; // null for create, populated for update

    private JTextField txtTitle;
    private JTextArea txtContent;
    private JLabel lblError;
    private JButton btnSave;
    private JButton btnCancel;

    public LessonFormPanel(InstructorDashboardFrame parent, Instructor instructor, Course course, Lesson lesson) {
        this.parent = parent;
        this.instructor = instructor;
        this.course = course;
        this.lesson = lesson;

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Main Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Header
        JLabel headerLabel = new JLabel(lesson == null ? "Create New Lesson" : "Update Lesson");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(new Color(33, 33, 33));
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Course Name Label
        JLabel lblCourseName = new JLabel("Course: " + course.getTitle());
        lblCourseName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblCourseName.setForeground(new Color(102, 102, 102));
        lblCourseName.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblCourseName.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));

        // Title Field
        JLabel lblTitle = new JLabel("Lesson Title *");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtTitle = new JTextField(lesson != null ? lesson.getTitle() : "");
        txtTitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtTitle.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        txtTitle.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String text = ((JTextField) input).getText();
                return text != null && !text.trim().isEmpty();
            }
        });

        // Content Label
        JLabel lblContent = new JLabel("Lesson Content *");
        lblContent.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblContent.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblContent.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        txtContent = new JTextArea(lesson != null ? lesson.getContent() : "", 12, 30);
        txtContent.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtContent.setLineWrap(true);
        txtContent.setWrapStyleWord(true);
        txtContent.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        txtContent.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String text = ((JTextArea) input).getText();
                return text != null && !text.trim().isEmpty();
            }
        });

        JScrollPane scrollContent = new JScrollPane(txtContent);
        scrollContent.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        scrollContent.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        // Error Label
        lblError = new JLabel(" ");
        lblError.setForeground(new Color(244, 67, 54));
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblError.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Add components to content panel
        contentPanel.add(headerLabel);
        contentPanel.add(lblCourseName);
        contentPanel.add(lblTitle);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        contentPanel.add(txtTitle);
        contentPanel.add(lblContent);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        contentPanel.add(scrollContent);
        contentPanel.add(lblError);
        contentPanel.add(Box.createVerticalGlue());

        // Wrap in scroll pane
        JScrollPane mainScroll = new JScrollPane(contentPanel);
        mainScroll.setBorder(BorderFactory.createEmptyBorder());
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        add(mainScroll, BorderLayout.CENTER);

        // Bottom Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(230, 230, 230)));

        btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setPreferredSize(new Dimension(120, 40));
        btnCancel.setBackground(new Color(220, 220, 220));
        btnCancel.setForeground(new Color(51, 51, 51));
        btnCancel.setFocusPainted(false);
        btnCancel.setBorderPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSave = new JButton(lesson == null ? "Create Lesson" : "Update Lesson");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setPreferredSize(new Dimension(150, 40));
        btnSave.setBackground(new Color(76, 175, 80));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setBorderPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSave);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnCancel.addActionListener(e -> parent.showCourseView(course));

        btnSave.addActionListener(e -> saveLesson());
    }

    private void saveLesson() {
        String title = txtTitle.getText().trim();
        String content = txtContent.getText().trim();

        // Validation
        if (title.isEmpty()) {
            lblError.setText("⚠ Lesson title cannot be empty");
            txtTitle.requestFocus();
            txtTitle.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(244, 67, 54), 2),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)));
            return;
        }

        if (content.isEmpty()) {
            lblError.setText("⚠ Lesson content cannot be empty");
            txtContent.requestFocus();
            txtContent.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(244, 67, 54), 2),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)));
            return;
        }

        try {
            if (lesson == null) {
                // Create new lesson
                Lesson newLesson = instructor.createLesson(title, content);
                instructor.uploadLesson(newLesson, course.getCourseId());
                JOptionPane.showMessageDialog(this, "Lesson created successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                parent.showCourseView(course);
            } else {
                // Update existing lesson
                instructor.updateLesson(lesson.getLessonId(), title, content);
                lesson.setTitle(title);
                lesson.setContent(content);
                JOptionPane.showMessageDialog(this, "Lesson updated successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                parent.showCourseView(course);
            }
        } catch (Exception ex) {
            lblError.setText("⚠ Error: " + ex.getMessage());
        }
    }
}
