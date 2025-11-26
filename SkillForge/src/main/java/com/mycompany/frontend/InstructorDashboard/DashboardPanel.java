/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.frontend.InstructorDashboard;

import com.mycompany.CourseManagement.Course;
import com.mycompany.UserAccountManagement.Instructor;
import com.mycompany.frontend.Main.MainFrame;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author HP
 */
public class DashboardPanel extends JPanel {
    private JPanel cardsPanel;
    private JScrollPane scrollPane;
    private JButton btnCreateCourse;
    private JButton btnAddQuiz;
    private JButton btnLogout;
    private InstructorDashboardFrame parent;
    private Instructor instructor;

    public DashboardPanel(InstructorDashboardFrame parent, Instructor instructor) {
        this.parent = parent;
        this.instructor = instructor;

        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 245, 245));
        JLabel titleLabel = new JLabel("My Courses");
        titleLabel.setFont(titleLabel.getFont().deriveFont(24f).deriveFont(java.awt.Font.BOLD));
        titleLabel.setForeground(new Color(51, 51, 51));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Cards panel with grid layout
        cardsPanel = new JPanel();
        cardsPanel.setLayout(new GridLayout(0, 2, 20, 20)); // 2 columns, auto rows, 20px gaps
        cardsPanel.setBackground(new Color(245, 245, 245));

        // Scroll pane for cards
        scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Create button panel at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        btnCreateCourse = new JButton("Create New Course");
        btnCreateCourse.setFont(new Font("Arial", Font.BOLD, 14));

        btnAddQuiz = new JButton("Add Quiz");
        btnAddQuiz.setFont(new Font("Arial", Font.BOLD, 14));

        btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Arial", Font.BOLD, 14));

        // Button panel at the bottom

        btnCreateCourse = new JButton("Create New Course");
        btnCreateCourse.setPreferredSize(new Dimension(160, 35));
        btnCreateCourse.setBackground(new Color(33, 150, 243));
        btnCreateCourse.setForeground(Color.WHITE);
        btnCreateCourse.setFocusPainted(false);
        btnCreateCourse.setBorderPainted(false);
        btnCreateCourse.setFont(btnCreateCourse.getFont().deriveFont(14f));

        btnLogout = new JButton("Logout");
        btnLogout.setPreferredSize(new Dimension(120, 35));
        btnLogout.setBackground(new Color(220, 220, 220));
        btnLogout.setForeground(new Color(51, 51, 51));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setFont(btnLogout.getFont().deriveFont(14f));

        buttonPanel.add(btnCreateCourse);
        buttonPanel.add(btnAddQuiz);
        buttonPanel.add(btnLogout);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load courses
        loadCourses();

        // Create course button action
        btnCreateCourse.addActionListener(e -> {
            parent.showCourseFormPanel(null); // null means create new
        });

        btnAddQuiz.addActionListener(e -> {
            try {
                ArrayList<Course> courses = instructor.getMyCourses();
                if (courses.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "You need to create a course first!");
                    return;
                }

                // فتح واجهة إضافة الكويز
                AddQuizWizard wizard = new AddQuizWizard(
                        (javax.swing.JFrame) SwingUtilities.getWindowAncestor(this),
                        instructor);
                wizard.setVisible(true);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // Logout button action
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                parent.dispose();
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
                JOptionPane.showMessageDialog(mainFrame, "Logged Out Successfully", "Successful Operation!",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public void refreshCourses() {
        loadCourses();
    }

    private void loadCourses() {
        cardsPanel.removeAll();
        try {
            ArrayList<Course> courses = instructor.getMyCourses();

            btnAddQuiz.setEnabled(!courses.isEmpty());

            for (Course course : courses) {
                CourseCard card = new CourseCard();

                // Set course data on the card
                card.setCourseData(course.getTitle(), course.getDescription());

                // Add click listener to navigate to course view
                card.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        parent.showCourseView(course);
                    }
                });

                cardsPanel.add(card);
            }

            // If no courses, show message
            if (courses.isEmpty()) {
                JLabel noCourses = new JLabel("No courses yet. Create your first course!");
                noCourses.setFont(noCourses.getFont().deriveFont(16f));
                noCourses.setForeground(new Color(102, 102, 102));
                cardsPanel.add(noCourses);
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error loading courses: " + ex.getMessage());
            btnAddQuiz.setEnabled(false);
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }
}
