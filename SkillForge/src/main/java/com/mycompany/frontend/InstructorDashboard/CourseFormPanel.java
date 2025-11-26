package com.mycompany.frontend.InstructorDashboard;

import com.mycompany.CourseManagement.Course;
import com.mycompany.UserAccountManagement.Instructor;
import java.awt.*;
import javax.swing.*;

/**
 * Form panel for creating or updating courses
 */
public class CourseFormPanel extends JPanel {

    private InstructorDashboardFrame parent;
    private Instructor instructor;
    private Course course; // null for create, populated for update

    private JTextField txtTitle;
    private JTextArea txtDescription;
    private JLabel lblError;
    private JButton btnSave;
    private JButton btnCancel;

    public CourseFormPanel(InstructorDashboardFrame parent, Instructor instructor, Course course) {
        this.parent = parent;
        this.instructor = instructor;
        this.course = course;

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
        JLabel headerLabel = new JLabel(course == null ? "Create New Course" : "Update Course");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(new Color(33, 33, 33));
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Title Field
        JLabel lblTitle = new JLabel("Course Title *");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtTitle = new JTextField(course != null ? course.getTitle() : "");
        txtTitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtTitle.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        txtTitle.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String text = ((javax.swing.text.JTextComponent) input).getText().trim();
                return !text.isEmpty();
            }
        });

        // Description Field
        JLabel lblDescription = new JLabel("Course Description *");
        lblDescription.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDescription.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtDescription = new JTextArea(course != null ? course.getDescription() : "");
        txtDescription.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        txtDescription.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String text = ((javax.swing.text.JTextComponent) input).getText().trim();
                return !text.isEmpty();
            }
        });

        JScrollPane scrollDesc = new JScrollPane(txtDescription);
        scrollDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        scrollDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollDesc.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        // Error Label
        lblError = new JLabel(" ");
        lblError.setForeground(new Color(244, 67, 54));
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblError.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Add components to content panel
        contentPanel.add(headerLabel);
        contentPanel.add(lblTitle);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        contentPanel.add(txtTitle);
        contentPanel.add(lblDescription);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        contentPanel.add(scrollDesc);
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

        btnSave = new JButton(course == null ? "Create Course" : "Update Course");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setPreferredSize(new Dimension(150, 40));
        btnSave.setBackground(new Color(33, 150, 243));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setBorderPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSave);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnCancel.addActionListener(e -> {
            if (course == null) {
                parent.showDashboard();
            } else {
                parent.showCourseView(course);
            }
        });

        btnSave.addActionListener(e -> saveCourse());
    }

    private void saveCourse() {
        String title = txtTitle.getText().trim();
        String description = txtDescription.getText().trim();

        // Validation
        if (title.isEmpty()) {
            lblError.setText("Course title cannot be empty");
            txtTitle.requestFocus();
            txtTitle.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(244, 67, 54), 2),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)));
            return;
        }

        if (description.isEmpty()) {
            lblError.setText("Course description cannot be empty");
            txtDescription.requestFocus();
            txtDescription.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(244, 67, 54), 2),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)));
            return;
        }

        try {
            if (course == null) {
                // Create new course
                instructor.createCourse(title, description);
                JOptionPane.showMessageDialog(this, "Course created successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                parent.showDashboard();
            } else {
                // Update existing course
                instructor.updateCourse(course.getCourseId(), title, description);
                course.setTitle(title);
                course.setDescription(description);
                JOptionPane.showMessageDialog(this, "Course updated successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                parent.showCourseView(course);
            }
        } catch (Exception ex) {
            lblError.setText(" Error: " + ex.getMessage());
        }
    }
}
