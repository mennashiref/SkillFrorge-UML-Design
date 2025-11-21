/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.UserAccountManagement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mycompany.JsonHandler.JsonHandler;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Hazem
 */
public class UserServices {
    private final String fileName;
    private ArrayNode userList;
    
    public UserServices() throws IOException
    {
        this.fileName = "users.json";
        JsonHandler.initializeFileIfNeeded(fileName);
        userList = JsonHandler.readArrayFromFile(fileName);
    }
    
    public ArrayList<User> getAllUsers() throws JsonProcessingException, IOException
    {
        userList = JsonHandler.readArrayFromFile(fileName);
        ArrayList<User> users = new ArrayList<>();
        
        for (int i = 0; i < userList.size(); i++) {
            JsonNode node = userList.get(i);
            User user;
            String userId = node.get("userId").asText();
            if (userId.charAt(0) == 'i') {
                user = JsonHandler.objectMapper.treeToValue(node, Instructor.class);
            } else {
                user = JsonHandler.objectMapper.treeToValue(node, Student.class);
            }
            users.add(user);
        }
        return users;
    }
    
    // check on the UserId that it should be displayed to the user after signing up
    public <T extends User> T signup(Class<T> classType, String name,String email, String password) throws IllegalArgumentException, JsonProcessingException, IOException
    {
        String userId = User.generateId(classType);
        User user;
        if(classType == Instructor.class)
        {
            user =  classType.cast(new Instructor(userId, name, email, password));
        }
        else 
        {
            user = classType.cast(new Student(userId, name, email, password));
        }
        JsonNode node = JsonHandler.convertJavatoJson(user);
        userList = JsonHandler.readArrayFromFile(fileName);
        userList.add(node);
        JsonHandler.writeToFile(userList, fileName);

        return (T)user;
    }
    
    /**
     * 
     * @param userId
     * @param password
     * @throws java.lang.Exception
     * @return Null in case of invalid credentials, or User instance otherwise , check on type before usage
     */
    public User login(String userId,String password) throws Exception
    {
        userList = JsonHandler.readArrayFromFile(fileName);
        
        for (JsonNode node : userList) 
        {
            if(node.get("userId").asText().equals(userId)&&
               node.get("password").asText().equals(User.getHashedPassword(password)))
            {
                if(userId.charAt(0)=='i')
                {
                    return JsonHandler.objectMapper.treeToValue(node, Instructor.class);
                }
                else
                {
                    return JsonHandler.objectMapper.treeToValue(node, Student.class);
                }
                    
            }
        }
        return null;
    }
    
    public boolean updateUser(User user) throws Exception
    {
        userList = JsonHandler.readArrayFromFile(fileName);
        
        for (int i =0;i<userList.size();i++) 
        {
            if(userList.get(i).get("userId").asText().equals(user.getUserId()))
            {
                userList.set(i,JsonHandler.convertJavatoJson(user));
                JsonHandler.writeToFile(userList, fileName);
                return true;
            }
        }
        return false;
    }
    
    public boolean deleteUser(User user) throws Exception
    {
        return deleteUserById(user.getUserId());
    }
    
    public boolean deleteUserById(String userId) throws Exception
    {
        userList = JsonHandler.readArrayFromFile(fileName);
        
        for (int i =0;i<userList.size();i++) 
        {
            if(userList.get(i).get("userId").asText().equals(userId))
            {
                userList.remove(i);
                JsonHandler.writeToFile(userList, fileName);
                return true;
            }
        }
        return false;
    }
}
