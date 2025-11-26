/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.UserAccountManagement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mycompany.JsonHandler.JsonHandler;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Hazem
 */
public class UserServices {

    // Static initializer block to load data when class is first accessed
    // refrences: https://www.geeksforgeeks.org/java/static-blocks-in-java/
    // FYI: it is executed on calling static members & methods, constructors
    static {
        JsonHandler.loadUsers();
    }

    public static ArrayList<User> getAllUsers() throws JsonProcessingException, IOException {
        ArrayList<User> users = new ArrayList<>();
        users.addAll(JsonHandler.students);
        users.addAll(JsonHandler.instructors);
        return users;
    }

    // check on the UserId that it should be displayed to the user after signing up
    public static <T extends User> T signup(Class<T> classType, String name, String email, String password)
            throws IllegalArgumentException, JsonProcessingException, IOException {
        String userId = User.generateId(classType);
        User user;
        if (classType == Instructor.class) {
            Instructor instructor = new Instructor(userId, name, email, password);
            JsonHandler.instructors.add(instructor);
            user = classType.cast(instructor);
        } else {
            Student student = new Student(userId, name, email, password);
            JsonHandler.students.add(student);
            user = classType.cast(student);
        }
        JsonHandler.saveUsers();

        return (T) user;
    }

    /**
     * 
     * @param userId
     * @param password UnHashedPassword
     * @throws java.lang.Exception
     * @return Null in case of invalid credentials, or User instance otherwise ,
     *         check on type before usage
     */
    public static User login(String userId, String password) throws Exception {
        String hashedPassword = User.getHashedPassword(password);

        if (userId.charAt(0) == 'i') {
            for (Instructor instructor : JsonHandler.instructors) {
                if (instructor.getUserId().equals(userId) && instructor.getPassword().equals(hashedPassword)) {
                    return instructor;
                }
            }
        } else if(userId.charAt(0) == 's') {
            for (Student student : JsonHandler.students) {
                if (student.getUserId().equals(userId) && student.getPassword().equals(hashedPassword)) {
                    return student;
                }
            }
        }
        else if(Admin.authenticate(userId, password))
        {
            return new Admin();
        }
        return null;
    }

    public static boolean updateUser(User user) throws Exception {
        if (user instanceof Instructor) {
            for (int i = 0; i < JsonHandler.instructors.size(); i++) {
                if (JsonHandler.instructors.get(i).getUserId().equals(user.getUserId())) {
                    JsonHandler.instructors.set(i, (Instructor) user);
                    JsonHandler.saveUsers();
                    return true;
                }
            }
        } else if (user instanceof Student) {
            for (int i = 0; i < JsonHandler.students.size(); i++) {
                if (JsonHandler.students.get(i).getUserId().equals(user.getUserId())) {
                    JsonHandler.students.set(i, (Student) user);
                    JsonHandler.saveUsers();
                    return true;
                }
            }
        }
        return false;
    }

    public  static boolean deleteUser(User user) throws Exception {
        return deleteUserById(user.getUserId());
    }

    public static boolean deleteUserById(String userId) throws Exception {
        if (userId.charAt(0) == 'i') {
            for (int i = 0; i < JsonHandler.instructors.size(); i++) {
                if (JsonHandler.instructors.get(i).getUserId().equals(userId)) {
                    JsonHandler.instructors.remove(i);
                    JsonHandler.saveUsers();
                    return true;
                }
            }
        } else {
            for (int i = 0; i < JsonHandler.students.size(); i++) {
                if (JsonHandler.students.get(i).getUserId().equals(userId)) {
                    JsonHandler.students.remove(i);
                    JsonHandler.saveUsers();
                    return true;
                }
            }
        }
        return false;
    }

    public static String getUserNameById(String userId) throws IOException {
        User user = getUserById(userId);
        if(user==null)
            return "";
        
        return user.getName();
    }
    
    public static User getUserById(String userId) throws IOException {
        if (userId.charAt(0) == 'i') {
            for (Instructor instructor : JsonHandler.instructors) {
                if (instructor.getUserId().equals(userId)) {
                    return instructor;
                }
            }
        } else {
            for (Student student : JsonHandler.students) {
                if (student.getUserId().equals(userId)) {
                    return student;
                }
            }
        }
        return null;
    }
}
