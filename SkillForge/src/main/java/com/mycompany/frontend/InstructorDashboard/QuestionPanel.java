/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.frontend.InstructorDashboard;

import javax.swing.*;
import java.awt.*;
import com.mycompany.QuizManagement.Question;
import com.mycompany.CourseManagement.QuizServices;

public class QuestionPanel extends JPanel {
    private JTextField questionText;
    private JTextField[] optionFields;
    private JComboBox<String> correctAnswerCombo;
    private int questionNumber;

    public QuestionPanel(int questionNumber) {
        this.questionNumber = questionNumber;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Question " + questionNumber));
        setBackground(new Color(240, 240, 240));

        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel questionLabel = new JLabel("Question Text:");
        questionText = new JTextField();
        questionText.setPreferredSize(new Dimension(400, 30));

        questionPanel.add(questionLabel, BorderLayout.NORTH);
        questionPanel.add(questionText, BorderLayout.CENTER);

        JPanel optionsPanel = new JPanel(new GridLayout(4, 2, 10, 5));
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Options"));

        optionFields = new JTextField[4];
        String[] optionLabels = { "Option A:", "Option B:", "Option C:", "Option D:" };

        for (int i = 0; i < 4; i++) {
            optionsPanel.add(new JLabel(optionLabels[i]));
            optionFields[i] = new JTextField();
            optionsPanel.add(optionFields[i]);
        }

        JPanel settingsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        settingsPanel.add(new JLabel("Correct Answer:"));
        correctAnswerCombo = new JComboBox<>(new String[] { "A", "B", "C", "D" });
        settingsPanel.add(correctAnswerCombo);

        // Remove button
        JButton btnRemove = new JButton("Remove Question");
        btnRemove.addActionListener(e -> removeQuestion());
        settingsPanel.add(btnRemove);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(questionPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(optionsPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(settingsPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(btnRemove);

        add(mainPanel, BorderLayout.CENTER);
    }

    public Question getQuestion() {
        if (questionText.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter question text");
            return null;
        }

        for (int i = 0; i < 4; i++) {
            if (optionFields[i].getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter all options");
                return null;
            }
        }

        try {
            String[] options = new String[4];
            for (int i = 0; i < 4; i++) {
                options[i] = optionFields[i].getText().trim();
            }

            int correctIndex = correctAnswerCombo.getSelectedIndex();

            return QuizServices.createQuestion(
                    questionText.getText().trim(),
                    options,
                    correctIndex);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            return null;
        }
    }

    private void removeQuestion() {
        Container parent = getParent();
        if (parent != null) {
            parent.remove(this);
            parent.revalidate();
            parent.repaint();
        }
    }
}
