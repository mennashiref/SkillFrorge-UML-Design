package com.mycompany.frontend.Charts;

import com.mycompany.Analytics.AnalyticsServices;
import com.mycompany.Analytics.CourseAnalytics;
import com.mycompany.Analytics.LessonAnalytics;
import com.mycompany.Analytics.StudentPerformance;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Course Analytics Panel - Shows comprehensive course statistics
 * 
 */
public class CourseAnalyticsPanel extends javax.swing.JPanel {

    private com.mycompany.frontend.InstructorDashboard.InstructorDashboardFrame parent;
    private String courseId;
    private CourseAnalytics analytics;

    /**
     * Creates new form CourseAnalyticsPanel
     */
    public CourseAnalyticsPanel() {
        initComponents();
    }

    /**
     * Creates new form with course data
     * 
     * @param parent
     * @param courseId
     */
    public CourseAnalyticsPanel(com.mycompany.frontend.InstructorDashboard.InstructorDashboardFrame parent,
            String courseId) {
        this.parent = parent;
        this.courseId = courseId;
        initComponents();
        loadAnalytics();
    }

    /**
     * Load analytics data from backend
     */
    private void loadAnalytics() {
        try {
            analytics = AnalyticsServices.getCourseAnalytics(courseId);
            displayStatistics();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading analytics: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Display statistics in labels
     */
    private void displayStatistics() {
        if (analytics == null)
            return;

        courseTitleLabel.setText(analytics.getCourseTitle());
        totalStudentsLabel.setText(String.valueOf(analytics.getTotalStudents()));
        activeStudentsLabel.setText(String.valueOf(analytics.getActiveStudents()));

        // Calculate completed students (100% completion)
        int completedStudents = 0;
        for (StudentPerformance sp : analytics.getStudentPerformances()) {
            if (sp.getCompletionPercentage() >= 100.0) {
                completedStudents++;
            }
        }
        completedStudentsLabel.setText(String.valueOf(completedStudents));

        avgCompletionLabel.setText(String.format("%.2f%%", analytics.getAverageCompletionPercentage()));
        avgScoreLabel.setText(String.format("%.2f%%", analytics.getAverageScore()));
    }

    /**
     * Create dataset for lesson average scores chart
     */
    private DefaultCategoryDataset createLessonScoresDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (analytics != null) {
            for (LessonAnalytics la : analytics.getLessonAnalytics()) {
                dataset.addValue(la.getAverageScore(), "Average Score", la.getLessonTitle());
            }
        }

        return dataset;
    }

    /**
     * Create dataset for student performance comparison
     */
    private DefaultCategoryDataset createStudentPerformanceDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (analytics != null) {
            for (StudentPerformance sp : analytics.getStudentPerformances()) {
                dataset.addValue(sp.getAverageScore(), "Score", sp.getStudentName());
                dataset.addValue(sp.getCompletionPercentage(), "Completion", sp.getStudentName());
            }
        }

        return dataset;
    }

    /**
     * Show charts in a new window
     */
    private void showChartsWindow() {
        if (analytics == null) {
            JOptionPane.showMessageDialog(this,
                    "No analytics data available",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Create new frame for charts
        javax.swing.JFrame chartFrame = new javax.swing.JFrame("Course Analytics Charts");
        chartFrame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        chartFrame.setSize(1000, 600);
        chartFrame.setLayout(new GridLayout(1, 2, 10, 10));

        // Chart 1: Lesson Average Scores
        JFreeChart lessonChart = ChartFactory.createBarChart(
                "Average Score per Lesson",
                "Lessons",
                "Average Score (%)",
                createLessonScoresDataset(),
                PlotOrientation.VERTICAL,
                false,
                true,
                false);

        CategoryPlot lessonPlot = lessonChart.getCategoryPlot();
        lessonPlot.setBackgroundPaint(Color.WHITE);
        lessonPlot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        BarRenderer lessonRenderer = (BarRenderer) lessonPlot.getRenderer();
        lessonRenderer.setSeriesPaint(0, new Color(51, 153, 255));

        ChartPanel lessonChartPanel = new ChartPanel(lessonChart);

        // Chart 2: Student Performance
        JFreeChart studentChart = ChartFactory.createBarChart(
                "Student Performance Overview",
                "Students",
                "Percentage",
                createStudentPerformanceDataset(),
                PlotOrientation.VERTICAL,
                true,
                true,
                false);

        CategoryPlot studentPlot = studentChart.getCategoryPlot();
        studentPlot.setBackgroundPaint(Color.WHITE);
        studentPlot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        BarRenderer studentRenderer = (BarRenderer) studentPlot.getRenderer();
        studentRenderer.setSeriesPaint(0, new Color(255, 153, 51));
        studentRenderer.setSeriesPaint(1, new Color(51, 204, 51));

        ChartPanel studentChartPanel = new ChartPanel(studentChart);

        chartFrame.add(lessonChartPanel);
        chartFrame.add(studentChartPanel);
        chartFrame.setLocationRelativeTo(this);
        chartFrame.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        titlePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        courseTitleLabel = new javax.swing.JLabel();
        statsPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        totalStudentsLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        activeStudentsLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        completedStudentsLabel = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        avgCompletionLabel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        avgScoreLabel = new javax.swing.JLabel();
        buttonPanel = new javax.swing.JPanel();
        backButton = new javax.swing.JButton();
        viewChartsButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));

        titlePanel.setBackground(new java.awt.Color(51, 153, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Course Analytics");

        courseTitleLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        courseTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        courseTitleLabel.setText("Loading...");

        javax.swing.GroupLayout titlePanelLayout = new javax.swing.GroupLayout(titlePanel);
        titlePanel.setLayout(titlePanelLayout);
        titlePanelLayout.setHorizontalGroup(
                titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(titlePanelLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(
                                        titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(courseTitleLabel)
                                                .addComponent(jLabel1))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        titlePanelLayout.setVerticalGroup(
                titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(titlePanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(courseTitleLabel)
                                .addContainerGap(20, Short.MAX_VALUE)));

        statsPanel.setBackground(new java.awt.Color(255, 255, 255));
        statsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Course Statistics",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Total Students:");

        totalStudentsLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        totalStudentsLabel.setText("0");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Active Students:");

        activeStudentsLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        activeStudentsLabel.setText("0");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Completed Students:");

        completedStudentsLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        completedStudentsLabel.setText("0");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Average Completion:");

        avgCompletionLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        avgCompletionLabel.setText("0.00%");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("Average Score:");

        avgScoreLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        avgScoreLabel.setText("0.00%");

        javax.swing.GroupLayout statsPanelLayout = new javax.swing.GroupLayout(statsPanel);
        statsPanel.setLayout(statsPanelLayout);
        statsPanelLayout.setHorizontalGroup(
                statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(statsPanelLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(
                                        statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(statsPanelLayout.createSequentialGroup()
                                                        .addComponent(jLabel2)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(totalStudentsLabel))
                                                .addGroup(statsPanelLayout.createSequentialGroup()
                                                        .addComponent(jLabel4)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(activeStudentsLabel))
                                                .addGroup(statsPanelLayout.createSequentialGroup()
                                                        .addComponent(jLabel6)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(completedStudentsLabel))
                                                .addGroup(statsPanelLayout.createSequentialGroup()
                                                        .addComponent(jLabel8)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(avgCompletionLabel))
                                                .addGroup(statsPanelLayout.createSequentialGroup()
                                                        .addComponent(jLabel10)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(avgScoreLabel)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        statsPanelLayout.setVerticalGroup(
                statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(statsPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(
                                        statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel2)
                                                .addComponent(totalStudentsLabel))
                                .addGap(18, 18, 18)
                                .addGroup(
                                        statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel4)
                                                .addComponent(activeStudentsLabel))
                                .addGap(18, 18, 18)
                                .addGroup(
                                        statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel6)
                                                .addComponent(completedStudentsLabel))
                                .addGap(18, 18, 18)
                                .addGroup(
                                        statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel8)
                                                .addComponent(avgCompletionLabel))
                                .addGap(18, 18, 18)
                                .addGroup(
                                        statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel10)
                                                .addComponent(avgScoreLabel))
                                .addContainerGap(30, Short.MAX_VALUE)));

        buttonPanel.setBackground(new java.awt.Color(255, 255, 255));

        backButton.setBackground(new java.awt.Color(158, 158, 158));
        backButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        backButton.setForeground(new java.awt.Color(255, 255, 255));
        backButton.setText("← Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        viewChartsButton.setBackground(new java.awt.Color(51, 153, 255));
        viewChartsButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        viewChartsButton.setForeground(new java.awt.Color(255, 255, 255));
        viewChartsButton.setText("View Charts");
        viewChartsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewChartsButtonActionPerformed(evt);
            }
        });

        refreshButton.setBackground(new java.awt.Color(51, 204, 51));
        refreshButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        refreshButton.setForeground(new java.awt.Color(255, 255, 255));
        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setHorizontalGroup(
                buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(buttonPanelLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(viewChartsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        buttonPanelLayout.setVerticalGroup(
                buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(buttonPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(buttonPanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(viewChartsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(20, Short.MAX_VALUE)));

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(titlePanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(mainPanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(statsPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(buttonPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(20, Short.MAX_VALUE)));
        mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(titlePanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(statsPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(20, Short.MAX_VALUE)));

        add(mainPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (parent != null) {
            parent.showDashboard();
        }
    }

    private void viewChartsButtonActionPerformed(java.awt.event.ActionEvent evt) {
        showChartsWindow();
    }

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {
        loadAnalytics();
    }

    // Variables declaration - do not modify
    private javax.swing.JLabel activeStudentsLabel;
    private javax.swing.JLabel avgCompletionLabel;
    private javax.swing.JLabel avgScoreLabel;
    private javax.swing.JButton backButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JLabel completedStudentsLabel;
    private javax.swing.JLabel courseTitleLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton refreshButton;
    private javax.swing.JPanel statsPanel;
    private javax.swing.JPanel titlePanel;
    private javax.swing.JLabel totalStudentsLabel;
    private javax.swing.JButton viewChartsButton;
    // End of variables declaration
}