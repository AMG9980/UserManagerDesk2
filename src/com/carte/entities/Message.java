package com.carte.entities;

import com.carte.utils.Constants;
import com.carte.utils.RelationObject;

public class Message implements Comparable<Message> {
    
    private int id;
    private RelationObject user;
    private String description;
    
    public Message(int id, RelationObject user, String description) {
        this.id = id;
        this.user = user;
        this.description = description;
    }

    public Message(RelationObject user, String description) {
        this.user = user;
        this.description = description;
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
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    

    @Override
    public int compareTo(Message message) {
        switch (Constants.compareVar) {
            case "User":
                return Integer.compare(message.getUser().getId(), this.getUser().getId());
            case "Description":
                 return message.getDescription().compareTo(this.getDescription());
            
            default:
                return 0;
        }
    }
    
}