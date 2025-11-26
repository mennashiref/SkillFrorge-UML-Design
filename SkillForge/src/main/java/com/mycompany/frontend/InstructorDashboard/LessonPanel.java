/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.frontend.InstructorDashboard;

import com.mycompany.CourseManagement.Course;
import com.mycompany.CourseManagement.Lesson;
import com.mycompany.UserAccountManagement.Instructor;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author HP
 */
public class LessonPanel extends JPanel {

    private InstructorDashboardFrame parent;
    private Lesson lesson;
    private Course course;
    private Instructor instructor;
    private JTextArea txtContent;

    public LessonPanel(InstructorDashboardFrame parent, Lesson lesson, Course course, Instructor instructor) {
        this.parent = parent;
        this.lesson = lesson;
        this.course = course;
        this.instructor = instructor;

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        JLabel lblTitle = new JLabel("Lesson: " + lesson.getTitle());
        topPanel.add(lblTitle);

        txtContent = new JTextArea(lesson.getContent());
        txtContent.setEditable(false);
        txtContent.setLineWrap(true);
        txtContent.setWrapStyleWord(true);

        JPanel buttonPanel = new JPanel();
        JButton btnBack = new JButton("Back to Course");
        JButton btnEditLesson = new JButton("Edit Lesson");
        JButton btnDeleteLesson = new JButton("Delete Lesson");
        JButton btnLessonAnalytics = new JButton("Lesson Analytics");

        buttonPanel.add(btnBack);
        buttonPanel.add(btnEditLesson);
        buttonPanel.add(btnDeleteLesson);
        buttonPanel.add(btnLessonAnalytics);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(txtContent), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnBack.addActionListener(e -> parent.showCourseView(course));

        btnEditLesson.addActionListener(e -> parent.showLessonFormPanel(course, lesson));

        btnDeleteLesson.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this lesson?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    instructor.deleteLesson(lesson.getLessonId());
                    JOptionPane.showMessageDialog(this, "Lesson deleted successfully!");
                    parent.showCourseView(course);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error deleting lesson: " + ex.getMessage());
                }
            }
        });

        btnLessonAnalytics.addActionListener(e -> {
            parent.showLessonAnalytics(course.getCourseId(), lesson.getLessonId());
        });
    }

}
