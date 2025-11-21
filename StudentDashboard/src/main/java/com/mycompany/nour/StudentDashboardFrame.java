package com.mycompany.nour;

import javax.swing.*;
import java.awt.*;
import com.mycompany.CourseManagement.Course;
import com.mycompany.CourseManagement.ProgressManager;
import com.mycompany.UserAccountManagement.Student;
import com.mycompany.nour.CourseDetailsFrame;
import java.util.ArrayList;

public class StudentDashboardFrame extends JFrame {
    private Student student;
    private JTabbedPane tabbedPane;
    private JPanel enrolledCoursesPanel;
    private JPanel browseCoursesPanel;

    public StudentDashboardFrame(Student student) {
        this.student = student;
        initializeFrame();
        createEnrolledCoursesTab();
        createBrowseCoursesTab();
    }

    private void initializeFrame() {
        setTitle("لوحة الطالب - Skill Forge");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        tabbedPane = new JTabbedPane();
        add(tabbedPane);
    }

    private void createEnrolledCoursesTab() {
        enrolledCoursesPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("الكورسات المسجلة", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        enrolledCoursesPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel coursesPanel = new JPanel();
        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));

        try {
            ArrayList<Course> enrolledCourses = student.getMyEnrolledCourses();
            if (enrolledCourses.isEmpty()) {
                coursesPanel.add(new JLabel("لا توجد كورسات مسجلة"));
            } else {
                for (Course course : enrolledCourses) {
                    JPanel courseCard = createCourseCard(course);
                    coursesPanel.add(courseCard);
                    coursesPanel.add(Box.createVerticalStrut(10));
                }
            }
        } catch (Exception e) {
            coursesPanel.add(new JLabel("خطأ في تحميل الكورسات"));
        }

        JScrollPane scrollPane = new JScrollPane(coursesPanel);
        enrolledCoursesPanel.add(scrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("كورساتي", enrolledCoursesPanel);
    }

    private void createBrowseCoursesTab() {
        browseCoursesPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("الكورسات المتاحة", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        browseCoursesPanel.add(titleLabel, BorderLayout.NORTH);

        JTable coursesTable = new JTable();

        try {
            ArrayList<Course> allCourses = student.viewAvailableCourses();
            Object[][] data = new Object[allCourses.size()][3];

            for (int i = 0; i < allCourses.size(); i++) {
                Course c = allCourses.get(i);
                data[i][0] = c.getTitle();
                data[i][1] = c.getInstructorId();
                data[i][2] = c.getDescription();
            }

            coursesTable.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[]{"العنوان", "المدرس", "الوصف"}
            ));
        } catch (Exception e) {
            browseCoursesPanel.add(new JLabel("خطأ في تحميل الكورسات"));
        }

        JScrollPane scrollPane = new JScrollPane(coursesTable);
        browseCoursesPanel.add(scrollPane, BorderLayout.CENTER);

        JButton enrollButton = new JButton("سجل في الكورس");
        enrollButton.addActionListener(e -> {
            int selectedRow = coursesTable.getSelectedRow();
            if (selectedRow >= 0) {
                try {
                    ArrayList<Course> availableCourses = student.viewAvailableCourses();
                    student.enroll(availableCourses.get(selectedRow).getCourseId());
                    JOptionPane.showMessageDialog(this, "تم التسجيل في الكورس بنجاح");
                    refreshEnrolledCoursesTab();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "خطأ في التسجيل: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "اختر كورس أولاً");
            }
        });

        browseCoursesPanel.add(enrollButton, BorderLayout.SOUTH);
        tabbedPane.addTab("استعراض الكورسات", browseCoursesPanel);
    }

private JPanel createCourseCard(Course course) {
    JPanel card = new JPanel(new BorderLayout());
    card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    card.setBackground(Color.WHITE);
    card.setPreferredSize(new Dimension(300, 120));

    JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
    JLabel titleLabel = new JLabel(course.getTitle());
    titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
    JLabel descLabel = new JLabel(course.getDescription());
    descLabel.setFont(new Font("Arial", Font.PLAIN, 12));


    JProgressBar progressBar = new JProgressBar(0, 100);
    try {
        int completed = ProgressManager.getCompletedLessonsCount(student.getUserId(), course.getCourseId());
        int total = course.getLessons().size();
        int progress = (total == 0) ? 0 : (completed * 100) / total;
        progressBar.setValue(progress);
    } catch (Exception e) {
        progressBar.setValue(0);
    }
    progressBar.setStringPainted(true);

    infoPanel.add(titleLabel);
    infoPanel.add(descLabel);
    infoPanel.add(progressBar); 

    card.add(infoPanel, BorderLayout.CENTER);

    JButton detailsButton = new JButton("عرض التفاصيل");
    detailsButton.addActionListener(e -> {
        new CourseDetailsFrame(course, student).setVisible(true);
    });

    card.add(detailsButton, BorderLayout.SOUTH);
    return card;
}

 private void refreshEnrolledCoursesTab() {

    enrolledCoursesPanel.removeAll();

    JLabel titleLabel = new JLabel("الكورسات المسجلة", JLabel.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
    enrolledCoursesPanel.add(titleLabel, BorderLayout.NORTH);

    JPanel coursesPanel = new JPanel();
    coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));

    try {
        ArrayList<Course> enrolledCourses = student.getMyEnrolledCourses();
        if (enrolledCourses.isEmpty()) {
            coursesPanel.add(new JLabel("لا توجد كورسات مسجلة"));
        } else {
            for (Course course : enrolledCourses) {
                JPanel courseCard = createCourseCard(course);
                coursesPanel.add(courseCard);
                coursesPanel.add(Box.createVerticalStrut(10));
            }
        }
    } catch (Exception e) {
        coursesPanel.add(new JLabel("خطأ في تحميل الكورسات"));
    }

    JScrollPane scrollPane = new JScrollPane(coursesPanel);
    enrolledCoursesPanel.add(scrollPane, BorderLayout.CENTER);
    
 
    enrolledCoursesPanel.revalidate();
    enrolledCoursesPanel.repaint();
}
}