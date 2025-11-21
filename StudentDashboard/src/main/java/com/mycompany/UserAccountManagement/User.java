/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.UserAccountManagement;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mycompany.InputVerifiers.Validation;
import com.mycompany.JsonHandler.JsonHandler;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * note that password must be >= 8 characters, and doesn't contain whitespace
 * @author Hazem
 */
public abstract class User {
    private String userId;
    private String name;
    private String email;
    private String password;
    
    /**
     * 
     * @param userId
     * @param name
     * @param email
     * @param password
     * @throws IllegalArgumentException 
     */
    public User(String userId, String name, String email, String password) throws IllegalArgumentException
    {
        name = name.trim();
        email = email.trim();
        userId = userId.trim();
        password = password.trim();

        validateArgs(name,email,password);
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = getHashedPassword(password);
    }
    
    private static void validateArgs(String name, String email, String password)
    {
        if(!Validation.isEmail(email))
        {
            throw new IllegalArgumentException("Error: Invalid Email Input");
        }
        
        if(Validation.isAlphabetic(name) != 1)
        {
            throw new IllegalArgumentException("Error: Invalid Name Input");
        }
        
        if(!Validation.isPassword(password))
        {
            throw new IllegalArgumentException("Error: password length MUST be >= 8, password MUST NOT contain whitespaces");
        }
    }
    
    public static String getHashedPassword(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8)); // FYI: StandardCharsets.UTF_8 makes it standard output & consistent on all machines, References: https://medium.com/@AlexanderObregon/what-is-sha-256-hashing-in-java-0d46dfb83888
            String hexString = new String();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString = hexString.concat("0");
                }
                hexString = hexString.concat(hex);
            }
            return hexString;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static String generateId(Class<? extends User> classType)
    {
       String generatedId;
       if(classType == Instructor.class)
       {
           generatedId="i";
       }
       else if(classType == Student.class)
       {
           generatedId="s";
       }
       else
       {
           return null;
       }
       try
       {
            ArrayNode usersList = JsonHandler.readArrayFromFile("users.json");
            int maxId = 0;

            for (int i = 0; i < usersList.size(); i++) {
                JsonNode node = usersList.get(i);
                if (node.has("userId")) 
                {
                    String userIdStr = node.get("userId").asText();
                    if(userIdStr.charAt(0)!=generatedId.charAt(0))
                    {
                        continue;
                    }
                    int tempUserId = Integer.parseInt(userIdStr.substring(1));
                    if (tempUserId > maxId) {
                        maxId = tempUserId;
                    }
                }
            }
            generatedId = generatedId.concat("" + (maxId+1));
       }
       catch(IOException e)
       {
           generatedId = generatedId.concat("1");
       }
       return generatedId;
    }
    
    @Override
    
    public String toString()
    {
        return this.getUserId() + "," + this.getName() + "," + this.getEmail();
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    
}
