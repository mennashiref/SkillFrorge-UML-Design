/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.UserAccountManagement;

/**
 *
 * @author Hazem
 */
public class Admin extends User {
    private static final String userId = "c1c224b03cd9bc7b6a86d77f5dace40191766c485cd55dc48caf9ac873335d6f";
    private static final String password = "c1c224b03cd9bc7b6a86d77f5dace40191766c485cd55dc48caf9ac873335d6f";

    public static boolean authenticate(String userId, String password) {
        return (userId != null &&
                User.getHashedPassword(userId).equals(Admin.userId) &&
                password != null &&
                User.getHashedPassword(password).equals(Admin.password));
    }
}
