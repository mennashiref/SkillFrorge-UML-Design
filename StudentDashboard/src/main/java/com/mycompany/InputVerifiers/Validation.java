/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.InputVerifiers;

/**
 *
 * @author Hazem
 */
public class Validation {
    public static boolean isEmpty(String value) {
        return (value == null || value.trim().isEmpty());
    }
    
    /**
     * 
     * @param value
     * @return 0: is not Alphabetic,
     *         -1: null String,
     *          1: is Alphabetic
     */
    public static int isAlphabetic(String value) {
        if(isEmpty(value))
        {
            return -1;
        }
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == ' ')) {
                return 0;
            }
        }
        return 1;
    }
    
    public static boolean isPositiveInt(int value) {
        return value >= 0;
    }
    public static boolean isPositiveFloat(float value) {
        return value >= 0.0;
    }

    public static boolean isPositiveInt(String str) {
        if(isInt(str))
        {
            return Integer.parseInt(str)>=0;
        }
        return false;
    }
    
    public static boolean isPositiveFloat(String str) {
        if(isFloat(str))
        {
            return Double.parseDouble(str)>=0.0;
        }
        return false;
    }
    
    public static boolean isInt(String str) {
        if(isEmpty(str))
        {
            return false;
        }
        try
        {
            Integer.parseInt(str);
            return true;
        }
        catch(NumberFormatException e)
        {
            return false;
        }
    }
    
    public static boolean isFloat(String str) {
        if(isEmpty(str))
        {
            return false;
        }
        try
        {
            Double.parseDouble(str);
            return true;
        }
        catch(NumberFormatException e)
        {
            return false;
        }
    }
    
    public static boolean isEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }

        int atIndex = email.indexOf('@');
        if (atIndex == -1) {
            return false;
        }

        int dotIndex = email.indexOf('.', atIndex);
        if (dotIndex == -1) {
            return false;
        }

        if (email.startsWith("@") || email.endsWith("@") || email.startsWith(".") || email.endsWith(".")) {
            return false;
        }

 
        String namePart = email.substring(0, atIndex);
        String domainPart = email.substring(atIndex + 1, dotIndex);
        String extensionPart = email.substring(dotIndex + 1);

        if (namePart.isEmpty() || domainPart.isEmpty() || extensionPart.length() < 2) {
            return false;
        }

        return true;
    }
    
    public static boolean isPassword(String password)
    {
        if(isEmpty(password))
            return false;
        if(password.contains(" ")||password.length()<8)
            return false;
        
        return true;
    }
}
