package com.mycompany.frontend.Charts;

import com.mycompany.Analytics.AnalyticsServices;
import com.mycompany.Analytics.LessonAnalytics;
import com.mycompany.UserAccountManagement.UserServices;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.Map;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Lesson Analytics Panel - Shows detailed lesson statistics
 */
public class LessonAnalyticsPanel extends javax.swing.JPanel {

    private com.mycompany.frontend.InstructorDashboard.InstructorDashboardFrame parent;
    private String courseId;
    private String lessonId;
    private LessonAnalytics analytics;

    /**
     * Creates new form LessonAnalyticsPanel
     */
    public LessonAnalyticsPanel() {
        initComponents();
    }

    /**
     * Creates new form with lesson data
     */
    public LessonAnalyticsPanel(com.mycompany.frontend.InstructorDashboard.InstructorDashboardFrame parent,
            String courseId, String lessonId) {
        this.parent = parent;
        this.courseId = courseId;
        this.lessonId = lessonId;
        initComponents();
        loadAnalytics();
    }

    /**
     * Load analytics data
     */
    private void loadAnalytics() {
        try {
            analytics = AnalyticsServices.getLessonAnalytics(courseId, lessonId);
            displayStatistics();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading analytics: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Display statistics
     */
    private void displayStatistics() {
        if (analytics == null)
            return;

        lessonTitleLabel.setText(analytics.getLessonTitle());
        studentsCompletedLabel.setText(String.valueOf(analytics.getStudentsCompleted()));
        studentsAttemptedLabel.setText(String.valueOf(analytics.getStudentsAttempted()));
        totalAttemptsLabel.setText(String.valueOf(analytics.getTotalAttempts()));
        avgScoreLabel.setText(String.format("%.2f%%", analytics.getAverageScore()));
        passRateLabel.setText(String.format("%.2f%%", analytics.getPassRate()));
    }

    /**
     * Create dataset for student scores comparison
     */
    private DefaultCategoryDataset createStudentScoresDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            Map<String, Integer> performanceMap = AnalyticsServices.getQuizPerformanceComparison(courseId, lessonId);

            for (Map.Entry<String, Integer> entry : performanceMap.entrySet()) {
                String studentName = UserServices.getUserNameById(entry.getKey());
                dataset.addValue(entry.getValue(), "Score", studentName);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading student scores: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        return dataset;
    }

    /**
     * Show charts window
     */
    private void showChartsWindow() {
        if (analytics == null) {
            JOptionPane.showMessageDialog(this,
                    "No analytics data available",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        javax.swing.JFrame chartFrame = new javax.swing.JFrame(
                "Lesson Analytics Charts - " + analytics.getLessonTitle());
        chartFrame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        chartFrame.setSize(800, 500);
        chartFrame.setLayout(new GridLayout(1, 1));

        // Create student scores chart
        JFreeChart chart = ChartFactory.createBarChart(
                "Student Quiz Scores",
                "Students",
                "Score (%)",
                createStudentScoresDataset(),
                PlotOrientation.VERTICAL,
                false,
                true,
                false);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(255, 153, 51));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartFrame.add(chartPanel);
        chartFrame.setLocationRelativeTo(this);
        chartFrame.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        titlePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lessonTitleLabel = new javax.swing.JLabel();
        statsPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        studentsCompletedLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        studentsAttemptedLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        totalAttemptsLabel = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        avgScoreLabel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        passRateLabel = new javax.swing.JLabel();
        buttonPanel = new javax.swing.JPanel();
        backButton = new javax.swing.JButton();
        viewChartsButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));

        titlePanel.setBackground(new java.awt.Color(255, 153, 51));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Lesson Analytics");

        lessonTitleLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lessonTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        lessonTitleLabel.setText("Loading...");

        javax.swing.GroupLayout titlePanelLayout = new javax.swing.GroupLayout(titlePanel);
        titlePanel.setLayout(titlePanelLayout);
        titlePanelLayout.setHorizontalGroup(
                titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(titlePanelLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(
                                        titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(lessonTitleLabel)
                                                .addComponent(jLabel1))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        titlePanelLayout.setVerticalGroup(
                titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(titlePanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lessonTitleLabel)
                                .addContainerGap(20, Short.MAX_VALUE)));

        statsPanel.setBackground(new java.awt.Color(255, 255, 255));
        statsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Lesson Statistics",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Students Completed:");

        studentsCompletedLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        studentsCompletedLabel.setText("0");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Students Attempted Quiz:");

        studentsAttemptedLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        studentsAttemptedLabel.setText("0");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Total Quiz Attempts:");

        totalAttemptsLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        totalAttemptsLabel.setText("0");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Average Score:");

        avgScoreLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        avgScoreLabel.setText("0.00%");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("Pass Rate:");

        passRateLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        passRateLabel.setText("0.00%");

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
                                                        .addComponent(studentsCompletedLabel))
                                                .addGroup(statsPanelLayout.createSequentialGroup()
                                                        .addComponent(jLabel4)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(studentsAttemptedLabel))
                                                .addGroup(statsPanelLayout.createSequentialGroup()
                                                        .addComponent(jLabel6)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(totalAttemptsLabel))
                                                .addGroup(statsPanelLayout.createSequentialGroup()
                                                        .addComponent(jLabel8)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(avgScoreLabel))
                                                .addGroup(statsPanelLayout.createSequentialGroup()
                                                        .addComponent(jLabel10)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(passRateLabel)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        statsPanelLayout.setVerticalGroup(
                statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(statsPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(
                                        statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel2)
                                                .addComponent(studentsCompletedLabel))
                                .addGap(18, 18, 18)
                                .addGroup(
                                        statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel4)
                                                .addComponent(studentsAttemptedLabel))
                                .addGap(18, 18, 18)
                                .addGroup(
                                        statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel6)
                                                .addComponent(totalAttemptsLabel))
                                .addGap(18, 18, 18)
                                .addGroup(
                                        statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel8)
                                                .addComponent(avgScoreLabel))
                                .addGap(18, 18, 18)
                                .addGroup(
                                        statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel10)
                                                .addComponent(passRateLabel))
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

        viewChartsButton.setBackground(new java.awt.Color(255, 153, 51));
        viewChartsButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        viewChartsButton.setForeground(new java.awt.Color(255, 255, 255));
        viewChartsButton.setText("View Student Scores");
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
                                .addComponent(viewChartsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 180,
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
    private javax.swing.JLabel avgScoreLabel;
    private javax.swing.JButton backButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel lessonTitleLabel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel passRateLabel;
    private javax.swing.JButton refreshButton;
    private javax.swing.JPanel statsPanel;
    private javax.swing.JLabel studentsAttemptedLabel;
    private javax.swing.JLabel studentsCompletedLabel;
    private javax.swing.JPanel titlePanel;
    private javax.swing.JLabel totalAttemptsLabel;
    private javax.swing.JButton viewChartsButton;
    // End of variables declaration
}