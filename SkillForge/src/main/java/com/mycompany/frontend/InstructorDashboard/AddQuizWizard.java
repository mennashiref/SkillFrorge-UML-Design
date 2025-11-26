/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.frontend.InstructorDashboard;

import javax.swing.*;
import java.awt.*;
import com.mycompany.CourseManagement.*;
import com.mycompany.QuizManagement.Quiz;
import com.mycompany.QuizManagement.Question;
import com.mycompany.UserAccountManagement.Instructor;
import java.io.IOException;
import java.util.ArrayList;

public class AddQuizWizard extends JDialog {
    private Instructor instructor;
    private Course selectedCourse;
    private Lesson selectedLesson;
    private ArrayList<Question> questions;

    private JComboBox<Course> courseComboBox;
    private JComboBox<Lesson> lessonComboBox;
    private JPanel questionsPanel;
    private JButton btnAddQuestion;
    private JButton btnSaveQuiz;

    public AddQuizWizard(JFrame parent, Instructor instructor) {
        super(parent, "Add Quiz Wizard", true);
        this.instructor = instructor;
        this.questions = new ArrayList<>();
        initializeUI();
        loadInstructorCourses();
    }

    private void initializeUI() {
        setSize(900, 700);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        // Panel لاختيار الكورس والدرس
        JPanel selectionPanel = createSelectionPanel();
        add(selectionPanel, BorderLayout.NORTH);

        // Panel لعرض وإضافة الأسئلة
        questionsPanel = new JPanel();
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));
        questionsPanel.setBorder(BorderFactory.createTitledBorder("Quiz Questions"));

        JScrollPane scrollPane = new JScrollPane(questionsPanel);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        add(scrollPane, BorderLayout.CENTER);

        // Panel للأزرار
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createSelectionPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Select Course and Lesson"));
        panel.setPreferredSize(new Dimension(800, 100));

        panel.add(new JLabel("Course:"));
        courseComboBox = new JComboBox<>();
        courseComboBox.addActionListener(e -> onCourseSelected());
        panel.add(courseComboBox);

        panel.add(new JLabel("Lesson:"));
        lessonComboBox = new JComboBox<>();
        lessonComboBox.addActionListener(e -> onLessonSelected());
        panel.add(lessonComboBox);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        btnAddQuestion = new JButton("Add Question");
        btnAddQuestion.addActionListener(e -> addNewQuestion());
        panel.add(btnAddQuestion);

        btnSaveQuiz = new JButton("Save Quiz");
        btnSaveQuiz.addActionListener(e -> saveQuiz());
        panel.add(btnSaveQuiz);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());
        panel.add(btnCancel);

        return panel;
    }

    private void loadInstructorCourses() {
        try {
            ArrayList<Course> courses = instructor.getMyCourses();
            courseComboBox.removeAllItems();

            for (Course course : courses) {
                courseComboBox.addItem(course);
            }

            if (courses.isEmpty()) {
                JOptionPane.showMessageDialog(this, "You don't have any courses yet!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading courses: " + e.getMessage());
        }
    }

    private void onCourseSelected() {
        selectedCourse = (Course) courseComboBox.getSelectedItem();
        lessonComboBox.removeAllItems();

        if (selectedCourse != null) {
            try {
                ArrayList<Lesson> lessons = selectedCourse.getLessons();
                for (Lesson lesson : lessons) {
                    lessonComboBox.addItem(lesson);
                }

                if (lessons.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "This course doesn't have any lessons yet!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error loading lessons: " + e.getMessage());
            }
        }

        updateButtonsState();
    }

    private void onLessonSelected() {
        selectedLesson = (Lesson) lessonComboBox.getSelectedItem();
        updateButtonsState();
    }

    private void updateButtonsState() {
        boolean canAddQuestions = (selectedCourse != null && selectedLesson != null);
        btnAddQuestion.setEnabled(canAddQuestions);
        btnSaveQuiz.setEnabled(canAddQuestions && !questions.isEmpty());
    }

    private void addNewQuestion() {
        if (selectedLesson == null) {
            JOptionPane.showMessageDialog(this, "Please select a lesson first!");
            return;
        }

        QuestionPanel questionPanel = new QuestionPanel(questions.size() + 1);
        questionsPanel.add(questionPanel);
        questionsPanel.add(Box.createVerticalStrut(10));

        questions.add(null); // Placeholder for the question

        questionsPanel.revalidate();
        questionsPanel.repaint();

        updateButtonsState();
    }

    private void saveQuiz() {
        if (selectedLesson == null) {
            JOptionPane.showMessageDialog(this, "Please select a lesson first!");
            return;
        }

        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add at least one question!");
            return;
        }

        collectQuestionsFromUI();

        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i) == null) {
                JOptionPane.showMessageDialog(this,
                        "Question " + (i + 1) + " is not complete!\nPlease fill all fields.");
                return;
            }
        }

        try {
            Quiz quiz = QuizServices.createQuiz(selectedLesson.getLessonId(), questions);

            CourseServices.assignQuizToLesson(selectedLesson.getLessonId(), quiz);

            JOptionPane.showMessageDialog(this, "Quiz saved successfully!");
            dispose();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving quiz: " + e.getMessage());
        }
    }

    private void collectQuestionsFromUI() {
        Component[] components = questionsPanel.getComponents();
        int questionIndex = 0;

        for (Component comp : components) {
            if (comp instanceof QuestionPanel) {
                QuestionPanel qp = (QuestionPanel) comp;
                Question question = qp.getQuestion();

                if (question != null) {
                    if (questionIndex < questions.size()) {
                        questions.set(questionIndex, question);
                    } else {
                        questions.add(question);
                    }
                    questionIndex++;
                }
            }
        }
    }
}