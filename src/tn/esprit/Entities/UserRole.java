/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.esprit.Entities;

/**
 *
 * @author DELL
 */
public class UserRole {

    private int userId;
    private int roleId;

    public UserRole(int userId, int roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public int getUserId() {
        return userId;
    }

    public int getRoleId() {
        return roleId;
    }
}
