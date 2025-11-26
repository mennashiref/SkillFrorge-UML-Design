/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.frontend.InstructorDashboard;

import com.mycompany.CourseManagement.Certificate;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import com.mycompany.UserAccountManagement.Student;

/**
 *
 * @author HP
 */
public class CertificateFrame extends JFrame {
    private Student student;
    private JPanel mainPanel;
    private JButton backButton;

    public CertificateFrame(Student student) {
        this.student = student;
        setupFrame();
        createUI();
        loadCertificates();
    }

    private void setupFrame() {
        setTitle("My Certificates");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void createUI() {
        mainPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        backButton = new JButton("Back");
        backButton.addActionListener(e -> dispose());

        JLabel titleLabel = new JLabel("My Certificates", JLabel.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 16));

        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel certificatesPanel = new JPanel();
        certificatesPanel.setLayout(new BoxLayout(certificatesPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(certificatesPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void loadCertificates() {
        JPanel certificatesPanel = (JPanel) ((JScrollPane) mainPanel.getComponent(1)).getViewport().getView();
        certificatesPanel.removeAll();

        try {
            ArrayList<Certificate> certificates = student.getCertificates();

            if (certificates.isEmpty()) {
                showNoCertificates(certificatesPanel);
            } else {
                for (Certificate cert : certificates) {
                    certificatesPanel.add(createCertificateItem(cert));
                    certificatesPanel.add(Box.createVerticalStrut(10));
                }
            }
        } catch (IOException e) {
            showError(certificatesPanel, "Error loading certificates");
        }

        certificatesPanel.revalidate();
        certificatesPanel.repaint();
    }

    private JPanel createCertificateItem(Certificate cert) {
        JPanel item = new JPanel(new BorderLayout(10, 5));
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        item.setMaximumSize(new Dimension(700, 100));

        JPanel infoPanel = new JPanel(new GridLayout(3, 1));

        JLabel courseLabel = new JLabel(cert.getCourseTitle());
        courseLabel.setFont(new Font("Tahoma", Font.BOLD, 14));

        JLabel scoreLabel = new JLabel("Score: " + cert.getFinalScore() + "%");
        scoreLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));

        JLabel dateLabel = new JLabel("Date: " + cert.getFormattedDate());
        dateLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
        dateLabel.setForeground(Color.DARK_GRAY);

        infoPanel.add(courseLabel);
        infoPanel.add(scoreLabel);
        infoPanel.add(dateLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 5));

        JButton viewButton = new JButton("View");
        viewButton.addActionListener(e -> viewCertificate(cert));

        JButton downloadButton = new JButton("Download");
        downloadButton.addActionListener(e -> downloadCertificate(cert));

        buttonPanel.add(viewButton);
        buttonPanel.add(downloadButton);

        item.add(infoPanel, BorderLayout.CENTER);
        item.add(buttonPanel, BorderLayout.EAST);

        return item;
    }

    private void viewCertificate(Certificate cert) {
        JTextArea textArea = new JTextArea(20, 50);
        textArea.setText(buildCertificateText(cert));
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);

        JOptionPane.showMessageDialog(this, scrollPane,
                "Certificate - " + cert.getCourseTitle(),
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void downloadCertificate(Certificate cert) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(cert.getCourseTitle() + ".txt"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(fileChooser.getSelectedFile())) {
                writer.write(buildCertificateText(cert));
                JOptionPane.showMessageDialog(this, "Certificate saved successfully");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage());
            }
        }

    }

    private String buildCertificateText(Certificate cert) {
        return "CERTIFICATE OF COMPLETION\n\n" +
                "This certifies that\n\n" +
                cert.getStudentName() + "\n\n" +
                "has successfully completed\n" +
                cert.getCourseTitle() + "\n\n" +
                "Score: " + cert.getFinalScore() + "%\n" +
                "Instructor: " + cert.getInstructorName() + "\n" +
                "Date: " + cert.getFormattedDate() + "\n" +
                "Certificate ID: " + cert.getCertificateId();
    }

    private void showNoCertificates(JPanel panel) {
        JLabel message = new JLabel("No certificates available", JLabel.CENTER);
        message.setFont(new Font("Tahoma", Font.PLAIN, 14));
        message.setForeground(Color.GRAY);
        panel.add(message);
    }

    private void showError(JPanel panel, String message) {
        JLabel errorLabel = new JLabel(message, JLabel.CENTER);
        errorLabel.setForeground(Color.RED);
        panel.add(errorLabel);

    }

    public static void showCertificates(Student student) {
        CertificateFrame frame = new CertificateFrame(student);
        frame.setVisible(true);
    }

}
