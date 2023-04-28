package com.pidev.services;

import com.pidev.entities.Message;
import com.pidev.utils.DatabaseConnection;
import com.pidev.utils.RelationObject;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MessageService {

    private static MessageService instance;
    PreparedStatement preparedStatement;
    Connection connection;

    public MessageService() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public static MessageService getInstance() {
        if (instance == null) {
            instance = new MessageService();
        }
        return instance;
    }
    
    public List<Message> getAll() {
        List<Message> listMessage = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM `message` AS x RIGHT JOIN `user` AS y ON x.user_id = y.id WHERE x.user_id = y.id");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                listMessage.add(new Message(
                        resultSet.getInt("id"),
                        new RelationObject(resultSet.getInt("user_id"), resultSet.getString("y.email")),
                        resultSet.getString("description")
                        
                ));
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) message : " + exception.getMessage());
        }
        return listMessage;
    }
    
    public List<RelationObject> getAllUsers() {
        List<RelationObject> listUsers = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM `user`");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                listUsers.add(new RelationObject(resultSet.getInt("id"), resultSet.getString("email")));
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) users : " + exception.getMessage());
        }
        return listUsers;
    }
    
    
    public boolean add(Message message) {

    
        String request = "INSERT INTO `message`(`user_id`, `description`) VALUES(?, ?)";
        try {
            preparedStatement = connection.prepareStatement(request);
            
            preparedStatement.setInt(1, message.getUser().getId());
             preparedStatement.setString(2, message.getDescription());
            
            preparedStatement.executeUpdate();
            System.out.println("Message added");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (add) message : " + exception.getMessage());
        }
        return false;
    }

    public boolean edit(Message message) {

        String request = "UPDATE `message` SET `user_id` = ?, `description` = ? WHERE `id`=" + message.getId();
        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setInt(1, message.getUser().getId());
            preparedStatement.setString(2, message.getDescription());
            
            preparedStatement.executeUpdate();
            System.out.println("Message edited");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (edit) message : " + exception.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM `message` WHERE `id`=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Message deleted");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (delete) message : " + exception.getMessage());
        }
        return false;
    }
}
