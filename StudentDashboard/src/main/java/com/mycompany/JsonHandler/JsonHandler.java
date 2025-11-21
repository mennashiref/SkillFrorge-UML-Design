/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.JsonHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Zeyad
 */

public class JsonHandler {
    public static final ObjectMapper objectMapper = new ObjectMapper();
    
    public static void writeToFile(JsonNode node, String fileName) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter()
                   .writeValue(new File(fileName), node);
    }
    
    public static JsonNode readFromFile(String fileName) throws IOException {
        File file = new File(fileName);
        
        if (!file.exists() || file.length() == 0) {
            return null;
        }
        
        return objectMapper.readTree(file);
    }
    
    public static ArrayNode readArrayFromFile(String fileName) throws IOException {
        JsonNode jsonNode = readFromFile(fileName);
        
        // If file was empty or doesn't exist, return empty array
        if (jsonNode == null) {
            return objectMapper.createArrayNode();
        }
        
        // Verify it's an array
        if (jsonNode.isArray()) {
            return (ArrayNode) jsonNode;
        } else {
            throw new IllegalStateException(
                "File '" + fileName + "' does not contain a JSON array"
            );
        }
    }
    
   
     // Initialize file with empty array if it doesn't exist or is empty
    public static void initializeFileIfNeeded(String fileName) throws IOException {
        File file = new File(fileName);
        
        if (!file.exists() || file.length() == 0) {
            ArrayNode emptyArray = objectMapper.createArrayNode();
            writeToFile(emptyArray, fileName);
        }
    }
    
    public static JsonNode convertJavatoJson(Object obj) throws JsonProcessingException {
        return objectMapper.valueToTree(obj);
    }
}