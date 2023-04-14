/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.esprit.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Role implements Serializable {

    private int id;
    private String nom;
    private List<User> users;

    public Role(int id, String nom) {
        this.id = id;
        this.nom = nom;
        this.users = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }
}
