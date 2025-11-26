/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.CourseManagement;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mycompany.JsonHandler.JsonHandler;
import com.mycompany.UserAccountManagement.Student;
import com.mycompany.UserAccountManagement.UserServices;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author menna
 */


public class CertificateService {

    private static final String CERTIFICATES_FILE = "certificates.json";

    static {
        initializeCertificatesFile();
    }

    private static void initializeCertificatesFile() {
        try {
            JsonHandler.initializeFileIfNeeded(CERTIFICATES_FILE);
        } catch (IOException e) {
            System.err.println("Error initializing certificates file: " + e.getMessage());
        }
    }

    public static Certificate generateCertificate(Student student, Course course, double finalScore) throws IOException {
        try {
            // Check if certificate already exists
            if (hasCertificate(student.getUserId(), course.getCourseId())) {
                throw new IOException("Certificate already exists for this course");
            }

            String instructorName = UserServices.getUserNameById(course.getInstructorId());
            Certificate certificate = new Certificate(
                student.getUserId(),
                student.getName(),
                course.getCourseId(),
                course.getTitle(),
                instructorName,
                finalScore
            );

            saveCertificate(certificate);
            updateStudentCertificate(student, certificate.getCertificateId());
            
            return certificate;
        } catch (Exception e) {
            throw new IOException("Error generating certificate: " + e.getMessage());
        }
    }

    private static void saveCertificate(Certificate certificate) throws IOException {
        ArrayNode certificatesArray = JsonHandler.readArrayFromFile(CERTIFICATES_FILE);
        
        ObjectNode certificateNode = JsonHandler.objectMapper.createObjectNode();
        certificateNode.put("certificateId", certificate.getCertificateId());
        certificateNode.put("studentId", certificate.getStudentId());
        certificateNode.put("studentName", certificate.getStudentName());
        certificateNode.put("courseId", certificate.getCourseId());
        certificateNode.put("courseTitle", certificate.getCourseTitle());
        certificateNode.put("instructorName", certificate.getInstructorName());
        certificateNode.put("finalScore", certificate.getFinalScore());
        certificateNode.put("issueDate", certificate.getIssueDate().getTime());

        certificatesArray.add(certificateNode);
        JsonHandler.writeToFile(certificatesArray, CERTIFICATES_FILE);
    }

    private static void updateStudentCertificate(Student student, String certificateId) throws IOException, Exception {
        student.getCertificateIds().add(certificateId);
        UserServices.updateUser(student);
    }

    public static ArrayList<Certificate> getStudentCertificates(String studentId) throws IOException {
        ArrayNode certificatesArray = JsonHandler.readArrayFromFile(CERTIFICATES_FILE);
        ArrayList<Certificate> certificates = new ArrayList<>();

        for (JsonNode node : certificatesArray) {
            if (node.get("studentId").asText().equals(studentId)) {
                Certificate cert = new Certificate();
                cert.setCertificateId(node.get("certificateId").asText());
                cert.setStudentId(node.get("studentId").asText());
                cert.setStudentName(node.get("studentName").asText());
                cert.setCourseId(node.get("courseId").asText());
                cert.setCourseTitle(node.get("courseTitle").asText());
                cert.setInstructorName(node.get("instructorName").asText());
                cert.setFinalScore(node.get("finalScore").asDouble());
                cert.setIssueDate(new Date(node.get("issueDate").asLong()));
                certificates.add(cert);
            }
        }

        return certificates;
    }

    public static boolean hasCertificate(String studentId, String courseId) throws IOException {
        ArrayNode certificatesArray = JsonHandler.readArrayFromFile(CERTIFICATES_FILE);
        
        for (JsonNode node : certificatesArray) {
            if (node.get("studentId").asText().equals(studentId) && 
                node.get("courseId").asText().equals(courseId)) {
                return true;
            }
        }
        return false;
    }

    public static Certificate getCertificateById(String certificateId) throws IOException {
        ArrayNode certificatesArray = JsonHandler.readArrayFromFile(CERTIFICATES_FILE);
        
        for (JsonNode node : certificatesArray) {
            if (node.get("certificateId").asText().equals(certificateId)) {
                Certificate cert = new Certificate();
                cert.setCertificateId(node.get("certificateId").asText());
                cert.setStudentId(node.get("studentId").asText());
                cert.setStudentName(node.get("studentName").asText());
                cert.setCourseId(node.get("courseId").asText());
                cert.setCourseTitle(node.get("courseTitle").asText());
                cert.setInstructorName(node.get("instructorName").asText());
                cert.setFinalScore(node.get("finalScore").asDouble());
                cert.setIssueDate(new Date(node.get("issueDate").asLong()));
                return cert;
            }
        }
        return null;
    }

    public static boolean isCourseCompleted(String studentId, String courseId) throws IOException {
        return hasCertificate(studentId, courseId);
    }
}