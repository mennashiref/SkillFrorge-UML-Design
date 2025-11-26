package com.mycompany.frontend.StudentDashboard;

import com.mycompany.CourseManagement.CourseServices;

import javax.swing.*;
import java.awt.*;
import com.mycompany.QuizManagement.Quiz;
import com.mycompany.QuizManagement.Question;
import com.mycompany.QuizManagement.QuizAttempt;
import com.mycompany.CourseManagement.QuizServices;
import java.time.Instant;
import java.util.ArrayList;

public class QuizFrame extends JFrame {
    private ArrayList<ButtonGroup> buttonGroups;
    private JRadioButton[][] optionButtonsArray;
    private JButton nextButton;
    private JButton submitButton;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    private Quiz currentQuiz;
    private ArrayList<Integer> userAnswers;
    private int currentQuestionIndex;
    private int score;
    private CourseDetailsFrame parentFrame;
    private String lessonId;
    private String courseId;
    private String studentId;
    private static final int MAX_ATTEMPTS = 3;

    public QuizFrame(Quiz quiz, CourseDetailsFrame parentFrame, String lessonId, String courseId) {
        this.currentQuiz = quiz;
        this.parentFrame = parentFrame;
        this.lessonId = lessonId;
        this.courseId = courseId;
        this.studentId = (parentFrame != null) ? getStudentIdFromParent(parentFrame) : "s1";
        initializeQuiz();
        setupUI();
    }

    public QuizFrame(Quiz quiz) {
        this.currentQuiz = quiz;
        this.parentFrame = null;
        this.lessonId = null;
        this.studentId = "s1";
        initializeQuiz();
        setupUI();
    }

    public QuizFrame() {
        this.currentQuiz = createSampleQuiz();
        this.parentFrame = null;
        this.lessonId = null;
        this.studentId = "s1";
        initializeQuiz();
        setupUI();
    }

    private String getStudentIdFromParent(CourseDetailsFrame parentFrame) {
        try {
            java.lang.reflect.Field studentField = parentFrame.getClass().getDeclaredField("student");
            studentField.setAccessible(true);
            Object student = studentField.get(parentFrame);
            if (student != null) {
                java.lang.reflect.Method getUserIdMethod = student.getClass().getMethod("getUserId");
                return (String) getUserIdMethod.invoke(student);
            }
        } catch (Exception e) {
            System.out.println("Could not get student ID from parent: " + e.getMessage());
        }
        return "s1";
    }

    private int getTotalQuestions() {
        return (currentQuiz != null && currentQuiz.getQuestions() != null) ? currentQuiz.getQuestions().size() : 0;
    }

    private Question getQuestion(int index) {
        if (currentQuiz != null && currentQuiz.getQuestions() != null &&
                index >= 0 && index < currentQuiz.getQuestions().size()) {
            return currentQuiz.getQuestions().get(index);
        }
        return null;
    }

    private void initializeQuiz() {
        this.userAnswers = new ArrayList<>();
        this.buttonGroups = new ArrayList<>();

        int totalQuestions = getTotalQuestions();
        if (currentQuiz != null && totalQuestions > 0) {
            this.optionButtonsArray = new JRadioButton[totalQuestions][4];

            for (int i = 0; i < totalQuestions; i++) {
                userAnswers.add(-1);
                buttonGroups.add(new ButtonGroup());
            }
        } else {
            this.optionButtonsArray = new JRadioButton[0][4];
        }

        this.currentQuestionIndex = 0;
        this.score = 0;
    }

    private void setupUI() {
        setTitle("Quiz");
        setSize(550, 380);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        createQuestionPanels();

        add(mainPanel);
        cardLayout.first(mainPanel);
    }

    private void createQuestionPanels() {
        int totalQuestions = getTotalQuestions();
        if (currentQuiz == null || totalQuestions == 0) {
            JPanel noQuestionsPanel = new JPanel(new BorderLayout());
            noQuestionsPanel.add(new JLabel("No questions available for this quiz", JLabel.CENTER),
                    BorderLayout.CENTER);
            mainPanel.add(noQuestionsPanel, "noQuestions");
            return;
        }

        for (int i = 0; i < totalQuestions; i++) {
            JPanel questionPanel = createQuestionPanel(i);
            mainPanel.add(questionPanel, "question" + i);
        }

        JPanel resultsPanel = createResultsPanel();
        mainPanel.add(resultsPanel, "results");
    }

    private JPanel createQuestionPanel(int questionIndex) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        Question question = getQuestion(questionIndex);
        JLabel questionLabel = new JLabel((questionIndex + 1) + ". " + question.getQuestionText());
        questionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(questionLabel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        ButtonGroup currentButtonGroup = buttonGroups.get(questionIndex);
        JRadioButton[] optionButtons = new JRadioButton[4];

        ArrayList<String> currentOptions = question.getOptions();
        for (int j = 0; j < currentOptions.size() && j < 4; j++) {
            optionButtons[j] = new JRadioButton(currentOptions.get(j));
            optionButtons[j].setActionCommand(String.valueOf(j));
            currentButtonGroup.add(optionButtons[j]);
            optionsPanel.add(optionButtons[j]);
            optionButtonsArray[questionIndex][j] = optionButtons[j];
        }

        panel.add(optionsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        if (questionIndex > 0) {
            JButton prevButton = new JButton("Previous");
            prevButton.addActionListener(e -> showPreviousQuestion());
            buttonPanel.add(prevButton);
        }

        int totalQuestions = getTotalQuestions();
        if (questionIndex < totalQuestions - 1) {
            nextButton = new JButton("Next");
            nextButton.addActionListener(e -> saveAndNext());
            buttonPanel.add(nextButton);
        } else {
            submitButton = new JButton("Submit");
            submitButton.addActionListener(e -> submitQuiz());
            buttonPanel.add(submitButton);
        }

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel resultsLabel = new JLabel("", JLabel.CENTER);
        resultsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(resultsLabel, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        panel.add(closeButton, BorderLayout.SOUTH);

        return panel;
    }

    private void saveAndNext() {
        saveCurrentAnswer();
        currentQuestionIndex++;
        cardLayout.next(mainPanel);
        displaySavedAnswer(currentQuestionIndex);
    }

    private void showPreviousQuestion() {
        saveCurrentAnswer();
        currentQuestionIndex--;
        cardLayout.show(mainPanel, "question" + currentQuestionIndex);
        displaySavedAnswer(currentQuestionIndex);
    }

    private void saveCurrentAnswer() {
        if (currentQuestionIndex < 0 || currentQuestionIndex >= userAnswers.size())
            return;

        ButtonModel selectedModel = buttonGroups.get(currentQuestionIndex).getSelection();
        if (selectedModel != null) {
            int selectedIndex = Integer.parseInt(selectedModel.getActionCommand());
            userAnswers.set(currentQuestionIndex, selectedIndex);
        } else {
            userAnswers.set(currentQuestionIndex, -1);
        }
    }

    private void displaySavedAnswer(int questionIndex) {
        if (questionIndex < 0 || questionIndex >= userAnswers.size())
            return;

        int savedAnswer = userAnswers.get(questionIndex);
        if (savedAnswer != -1) {
            buttonGroups.get(questionIndex).clearSelection();
            if (savedAnswer >= 0 && savedAnswer < 4) {
                optionButtonsArray[questionIndex][savedAnswer].setSelected(true);
            }
        }
    }

    private void submitQuiz() {
        saveCurrentAnswer();
        calculateScore();
        showResults();
    }

    private void calculateScore() {
        this.score = 0;
        if (currentQuiz == null)
            return;

        int totalQuestions = getTotalQuestions();
        for (int i = 0; i < totalQuestions; i++) {
            if (i < userAnswers.size()) {
                int userAnswer = userAnswers.get(i);
                Question q = getQuestion(i);
                if (q != null) {
                    int correctAnswer = q.getCorrectAnswerIndex();

                    System.out.println("Q" + i + ": User=" + userAnswer + ", Correct=" + correctAnswer + " -> "
                            + (userAnswer == correctAnswer ? "CORRECT" : "WRONG"));

                    if (userAnswer != -1 && userAnswer == correctAnswer) {
                        score++;
                    }
                }
            }
        }
        System.out.println("Final Score: " + score + "/" + totalQuestions);
    }

    private void showResults() {
        System.out.println("=== DEBUG QUIZ INFO ===");
        System.out.println("Current Quiz: " + (currentQuiz != null ? "EXISTS" : "NULL"));
        System.out.println("Quiz ID: " + (currentQuiz != null ? currentQuiz.getQuizId() : "N/A"));
        System.out.println("Questions Count: "
                + (currentQuiz != null && currentQuiz.getQuestions() != null ? currentQuiz.getQuestions().size()
                        : "0"));

        if (currentQuiz != null && currentQuiz.getQuestions() != null) {
            for (int i = 0; i < currentQuiz.getQuestions().size(); i++) {
                Question q = currentQuiz.getQuestions().get(i);
                System.out.println("Q" + (i + 1) + ": " + q.getQuestionText());
            }
        }

        if (currentQuiz == null) {
            JOptionPane.showMessageDialog(this, "Quiz data is missing!", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        int totalQuestions = getTotalQuestions();
        double percentage = (totalQuestions > 0) ? (score * 100.0 / totalQuestions) : 0;
        boolean passed = percentage >= currentQuiz.getPassingScore();

        System.out.println("=== QUIZ FINAL RESULTS ===");
        System.out.println("Student: " + studentId);
        System.out.println("Score: " + score + "/" + totalQuestions);
        System.out.println("Percentage: " + percentage + "%");
        System.out.println("Passed: " + passed);
        System.out.println("Lesson ID: " + lessonId);

        try {
            QuizServices.submitQuizResult(studentId, currentQuiz.getQuizId(), (int) percentage, passed);

            QuizAttempt attempt = new QuizAttempt();
            attempt.setQuizId(currentQuiz.getQuizId());
            attempt.setLessonId(this.lessonId);
            attempt.setCourseId(this.courseId);
            attempt.setScorePercent((int) percentage);
            attempt.setPassed(passed);
            attempt.setAttemptTime(Instant.now().toString());

            CourseServices.submitQuizAttempt(studentId, attempt);

            int remainingAttempts = 0;
            try {
                remainingAttempts = QuizServices.getRemainingAttempts(studentId, courseId, lessonId);
            } catch (Exception ex) {
                System.out.println("Error getting remaining attempts after submission: " + ex.getMessage());
            }

            String message = String.format(
                    "Score: %d/%d (%.0f%%)\n%s\n\nRemaining attempts: %d/%d",
                    score, totalQuestions, percentage,
                    passed ? "✅ You passed!" : "❌ Failed",
                    remainingAttempts, MAX_ATTEMPTS);

            System.out.println("Remaining attempts after submission: " + remainingAttempts + "/" + MAX_ATTEMPTS);

            if (passed && this.lessonId != null) {
                System.out.println("Completing lesson: " + this.lessonId);

                CourseServices.completeLessonViaQuiz(studentId, this.lessonId);

                if (parentFrame != null) {
                    parentFrame.refreshCourseDetails();

                    JOptionPane.showMessageDialog(this,
                            "✅ Quiz passed! Lesson completed automatically.\n\nRemaining attempts: " + remainingAttempts
                                    + "/" + MAX_ATTEMPTS,
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                if (remainingAttempts > 0) {
                    JOptionPane.showMessageDialog(this,
                            "❌ Quiz failed! You have " + remainingAttempts
                                    + " attempt(s) remaining.\n\nPlease review the material and try again.",
                            "Try Again",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "❌ Quiz failed! No more attempts remaining.\n\nPlease contact your instructor.",
                            "No Attempts Left",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            System.out.println("Error saving quiz results: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error saving results: " + ex.getMessage());
        }

        dispose();
    }

    public static void startQuiz() {
        SwingUtilities.invokeLater(() -> {
            new QuizFrame().setVisible(true);
        });
    }

    public static void startQuiz(Quiz quiz) {
        SwingUtilities.invokeLater(() -> {
            new QuizFrame(quiz).setVisible(true);
        });
    }

    public static void startQuiz(Quiz quiz, CourseDetailsFrame parentFrame, String lessonId, String courseId) {
        SwingUtilities.invokeLater(() -> {
            new QuizFrame(quiz, parentFrame, lessonId, courseId).setVisible(true);
        });
    }

    private Quiz createSampleQuiz() {
        return new Quiz("L1", new ArrayList<>(), 70);
    }
}