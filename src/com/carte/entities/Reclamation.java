package com.carte.entities;



import com.carte.utils.RelationObject;

public class Reclamation {
    
    private int id;
    private RelationObject user;
    private String titre;
    private String description;
    private String image;
    
    public Reclamation(int id, RelationObject user, String titre, String description, String image) {
        this.id = id;
        this.user = user;
        this.titre = titre;
        this.description = description;
        this.image = image;
    }

    public Reclamation(RelationObject user, String titre, String description, String image) {
        this.user = user;
        this.titre = titre;
        this.description = description;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public RelationObject getUser() {
        return user;
    }

    public void setUser(RelationObject user) {
        this.user = user;
    }
    
    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    

    
}