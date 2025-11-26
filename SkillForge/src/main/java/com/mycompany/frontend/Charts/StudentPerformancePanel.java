package com.mycompany.frontend.Charts;

import com.mycompany.Analytics.AnalyticsServices;
import com.mycompany.Analytics.LessonProgress;
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
 * Student Performance Panel - Shows individual student performance
 * 
 */
public class StudentPerformancePanel extends javax.swing.JPanel {

    private com.mycompany.frontend.InstructorDashboard.InstructorDashboardFrame parent;
    private String studentId;
    private String courseId;
    private StudentPerformance performance;

    /**
     * Creates new form StudentPerformancePanel
     */
    public StudentPerformancePanel() {
        initComponents();
    }

    /**
     * Creates new form with student and course data
     */
    public StudentPerformancePanel(com.mycompany.frontend.InstructorDashboard.InstructorDashboardFrame parent,
            String studentId, String courseId) {
        this.parent = parent;
        this.studentId = studentId;
        this.courseId = courseId;
        initComponents();
        loadPerformance();
    }

    /**
     * Load performance data
     */
    private void loadPerformance() {
        try {
            performance = AnalyticsServices.getStudentPerformance(studentId, courseId);
            displayStatistics();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading performance: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Display statistics
     */
    private void displayStatistics() {
        if (performance == null)
            return;

        studentNameLabel.setText(performance.getStudentName());
        completedLessonsLabel.setText(performance.getCompletedLessons() + " / " + performance.getTotalLessons());
        completionPercentageLabel.setText(String.format("%.2f%%", performance.getCompletionPercentage()));
        avgScoreLabel.setText(String.format("%.2f%%", performance.getAverageScore()));
        totalAttemptsLabel.setText(String.valueOf(performance.getTotalQuizAttempts()));
    }

    /**
     * Create dataset for lesson progress
     */
    private DefaultCategoryDataset createLessonProgressDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (performance != null) {
            for (LessonProgress lp : performance.getLessonProgress()) {
                dataset.addValue(lp.getBestScore(), "Score", lp.getLessonTitle());
            }
        }

        return dataset;
    }

    /**
     * Create dataset for attempts per lesson
     */
    private DefaultCategoryDataset createAttemptsDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (performance != null) {
            for (LessonProgress lp : performance.getLessonProgress()) {
                dataset.addValue(lp.getAttempts(), "Attempts", lp.getLessonTitle());
            }
        }

        return dataset;
    }

    /**
     * Show charts window
     */
    private void showChartsWindow() {
        if (performance == null) {
            JOptionPane.showMessageDialog(this,
                    "No performance data available",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        javax.swing.JFrame chartFrame = new javax.swing.JFrame(
                "Student Performance Charts - " + performance.getStudentName());
        chartFrame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        chartFrame.setSize(1000, 600);
        chartFrame.setLayout(new GridLayout(1, 2, 10, 10));

        // Chart 1: Lesson Scores
        JFreeChart scoresChart = ChartFactory.createBarChart(
                "Best Scores per Lesson",
                "Lessons",
                "Score (%)",
                createLessonProgressDataset(),
                PlotOrientation.VERTICAL,
                false,
                true,
                false);

        CategoryPlot scoresPlot = scoresChart.getCategoryPlot();
        scoresPlot.setBackgroundPaint(Color.WHITE);
        scoresPlot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        BarRenderer scoresRenderer = (BarRenderer) scoresPlot.getRenderer();
        scoresRenderer.setSeriesPaint(0, new Color(51, 204, 51));

        ChartPanel scoresChartPanel = new ChartPanel(scoresChart);

        // Chart 2: Attempts per Lesson
        JFreeChart attemptsChart = ChartFactory.createBarChart(
                "Quiz Attempts per Lesson",
                "Lessons",
                "Number of Attempts",
                createAttemptsDataset(),
                PlotOrientation.VERTICAL,
                false,
                true,
                false);

        CategoryPlot attemptsPlot = attemptsChart.getCategoryPlot();
        attemptsPlot.setBackgroundPaint(Color.WHITE);
        attemptsPlot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        BarRenderer attemptsRenderer = (BarRenderer) attemptsPlot.getRenderer();
        attemptsRenderer.setSeriesPaint(0, new Color(255, 102, 102));

        ChartPanel attemptsChartPanel = new ChartPanel(attemptsChart);

        chartFrame.add(scoresChartPanel);
        chartFrame.add(attemptsChartPanel);
        chartFrame.setLocationRelativeTo(this);
        chartFrame.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        titlePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        studentNameLabel = new javax.swing.JLabel();
        statsPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        completedLessonsLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        completionPercentageLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        avgScoreLabel = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        totalAttemptsLabel = new javax.swing.JLabel();
        buttonPanel = new javax.swing.JPanel();
        backButton = new javax.swing.JButton();
        viewChartsButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));

        titlePanel.setBackground(new java.awt.Color(51, 204, 51));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Student Performance");

        studentNameLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        studentNameLabel.setForeground(new java.awt.Color(255, 255, 255));
        studentNameLabel.setText("Loading...");

        javax.swing.GroupLayout titlePanelLayout = new javax.swing.GroupLayout(titlePanel);
        titlePanel.setLayout(titlePanelLayout);
        titlePanelLayout.setHorizontalGroup(
                titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(titlePanelLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(
                                        titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(studentNameLabel)
                                                .addComponent(jLabel1))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        titlePanelLayout.setVerticalGroup(
                titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(titlePanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(studentNameLabel)
                                .addContainerGap(20, Short.MAX_VALUE)));

        statsPanel.setBackground(new java.awt.Color(255, 255, 255));
        statsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Performance Statistics",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Completed Lessons:");

        completedLessonsLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        completedLessonsLabel.setText("0 / 0");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Completion Percentage:");

        completionPercentageLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        completionPercentageLabel.setText("0.00%");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Average Score:");

        avgScoreLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        avgScoreLabel.setText("0.00%");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Total Quiz Attempts:");

        totalAttemptsLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        totalAttemptsLabel.setText("0");

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
                                                        .addComponent(completedLessonsLabel))
                                                .addGroup(statsPanelLayout.createSequentialGroup()
                                                        .addComponent(jLabel4)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(completionPercentageLabel))
                                                .addGroup(statsPanelLayout.createSequentialGroup()
                                                        .addComponent(jLabel6)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(avgScoreLabel))
                                                .addGroup(statsPanelLayout.createSequentialGroup()
                                                        .addComponent(jLabel8)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(totalAttemptsLabel)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        statsPanelLayout.setVerticalGroup(
                statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(statsPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(
                                        statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel2)
                                                .addComponent(completedLessonsLabel))
                                .addGap(18, 18, 18)
                                .addGroup(
                                        statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel4)
                                                .addComponent(completionPercentageLabel))
                                .addGap(18, 18, 18)
                                .addGroup(
                                        statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel6)
                                                .addComponent(avgScoreLabel))
                                .addGap(18, 18, 18)
                                .addGroup(
                                        statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel8)
                                                .addComponent(totalAttemptsLabel))
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

        viewChartsButton.setBackground(new java.awt.Color(51, 204, 51));
        viewChartsButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        viewChartsButton.setForeground(new java.awt.Color(255, 255, 255));
        viewChartsButton.setText("View Performance Charts");
        viewChartsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewChartsButtonActionPerformed(evt);
            }
        });

        refreshButton.setBackground(new java.awt.Color(51, 153, 255));
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
                                .addComponent(viewChartsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 210,
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
        loadPerformance();
    }

    // Variables declaration - do not modify
    private javax.swing.JLabel avgScoreLabel;
    private javax.swing.JButton backButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JLabel completedLessonsLabel;
    private javax.swing.JLabel completionPercentageLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton refreshButton;
    private javax.swing.JPanel statsPanel;
    private javax.swing.JLabel studentNameLabel;
    private javax.swing.JPanel titlePanel;
    private javax.swing.JLabel totalAttemptsLabel;
    private javax.swing.JButton viewChartsButton;
    // End of variables declaration
}