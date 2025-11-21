package com.mycompany.nour;

import javax.swing.*;
import java.awt.*;
import com.mycompany.CourseManagement.Course;
import com.mycompany.CourseManagement.Lesson;
import com.mycompany.UserAccountManagement.Student;
import java.util.ArrayList;

public class CourseDetailsFrame extends JFrame {
    private Course course;
    private Student student;
    
    public CourseDetailsFrame(Course course, Student student) {
        this.course = course;
        this.student = student;

        initializeFrame();
        createCourseDetails();
    }
    
    private void initializeFrame() {
        setTitle("تفاصيل الكورس - " + course.getTitle());
        setSize(600, 500);
        setLocationRelativeTo(null);
    }
    
    private void createCourseDetails() {
        setLayout(new BorderLayout());
        
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        infoPanel.add(new JLabel("العنوان: " + course.getTitle()));
        infoPanel.add(new JLabel("المدرس: " + course.getInstructorId()));
        infoPanel.add(new JLabel("الوصف: " + course.getDescription()));
        
        add(infoPanel, BorderLayout.NORTH);
        
        JPanel lessonsPanel = new JPanel();
        lessonsPanel.setLayout(new BoxLayout(lessonsPanel, BoxLayout.Y_AXIS));
        lessonsPanel.setBorder(BorderFactory.createTitledBorder("الدروس"));
        
        try {
            ArrayList<Lesson> lessons = student.getCourseLessons(course.getCourseId());
            
            if (lessons.isEmpty()) {
                lessonsPanel.add(new JLabel("لا توجد دروس في هذا الكورس"));
            } else {
                for (Lesson lesson : lessons) {
                    JPanel lessonPanel = createLessonPanel(lesson);
                    lessonsPanel.add(lessonPanel);
                    lessonsPanel.add(Box.createVerticalStrut(5));
                }
            }
        } catch (Exception e) {
            lessonsPanel.add(new JLabel("خطأ في تحميل الدروس"));
        }
        
        JScrollPane scrollPane = new JScrollPane(lessonsPanel);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createLessonPanel(Lesson lesson) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(lesson.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JButton contentButton = new JButton("عرض المحتوى");
        contentButton.addActionListener(e -> {
            showLessonContent(lesson);
        });
        
        JButton completeButton = new JButton(
            lesson.isCompleted() ? "مكتمل ✓" : "إكمال الدرس"
        );
        completeButton.setEnabled(!lesson.isCompleted());

        completeButton.addActionListener(e -> {
            try {
                student.completeLesson(lesson.getLessonId());
                completeButton.setEnabled(false);
                completeButton.setText("مكتمل ✓");
                JOptionPane.showMessageDialog(this, "تم إكمال الدرس: " + lesson.getTitle());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "خطأ في حفظ التقدم");
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(contentButton);
        buttonPanel.add(completeButton);
        
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);
        
        return panel;
    }

    private void showLessonContent(Lesson lesson) {
        JFrame contentFrame = new JFrame(lesson.getTitle());
        contentFrame.setSize(500, 400);
        contentFrame.setLocationRelativeTo(this);
        
        JTextArea contentArea = new JTextArea(lesson.getContent());
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        
        contentFrame.add(new JScrollPane(contentArea));
        contentFrame.setVisible(true);
    }
}
