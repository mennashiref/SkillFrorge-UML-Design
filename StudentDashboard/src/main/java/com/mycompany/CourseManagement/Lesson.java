/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.CourseManagement;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mycompany.JsonHandler.JsonHandler;
import java.io.IOException;

/**
 *
 * @author Zeyad
 */
public class Lesson {

    private String lessonId;
    private String title;
    private String content;
    private boolean completed;

    public Lesson() {
    }

    public Lesson(String title, String content) throws IOException {
        this.lessonId = "L"+generateNextLessonId();
        this.title = title;
        this.content = content;
        this.completed = false;
    }

    private int generateNextLessonId() throws IOException {
        try {
            ArrayNode courseList = JsonHandler.readArrayFromFile("courses.json");
            int maxId = 0;
            
            for (int i = 0; i < courseList.size(); i++) {
                JsonNode courseNode = courseList.get(i);
                
                if (courseNode.has("lessons") && courseNode.get("lessons").isArray()) {
                    ArrayNode lessons = (ArrayNode) courseNode.get("lessons");
                    
                    for (int j = 0; j < lessons.size(); j++) {
                        JsonNode lessonNode = lessons.get(j);
                        if (lessonNode.has("lessonId")) {
                            String id = lessonNode.get("lessonId").asText().substring(1);
                            if (Integer.parseInt(id) > maxId) {
                                maxId = Integer.parseInt(id);
                            }
                        }
                    }
                }
            }
            
            return maxId + 1;
        } catch (IOException e) {
            return 1; // If file doesn't exist or is empty, start from 1
        }
    }
    
    /**
     * @return the lessonId
     */

    public String getLessonId() {
        return lessonId;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return String.format("Lesson[ID=%s, Title='%s', Content='%s']",
                lessonId, title, content);
    }

    /**
     * @return the completed
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * @param completed the completed to set
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

}
