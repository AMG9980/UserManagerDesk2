package com.pidev.services;

import com.pidev.entities.Reclamation;
import com.pidev.utils.DatabaseConnection;
import com.pidev.utils.RelationObject;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService {

    private static ReclamationService instance;
    PreparedStatement preparedStatement;
    Connection connection;

    public ReclamationService() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public static ReclamationService getInstance() {
        if (instance == null) {
            instance = new ReclamationService();
        }
        return instance;
    }
    
    public List<Reclamation> getAll() {
        List<Reclamation> listReclamation = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM `reclamation` AS x RIGHT JOIN `user` AS y ON x.user_id = y.id WHERE x.user_id = y.id");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                listReclamation.add(new Reclamation(
                        resultSet.getInt("id"),
                        new RelationObject(resultSet.getInt("user_id"), resultSet.getString("y.email")),
                        resultSet.getString("titre"),
                        resultSet.getString("description"),
                        resultSet.getString("image")
                        
                ));
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) reclamation : " + exception.getMessage());
        }
        return listReclamation;
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

    public boolean add(Reclamation reclamation) {

        String request = "INSERT INTO `reclamation`(`user_id`, `titre`, `description`, `image`) VALUES(?, ?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(request);
            
            preparedStatement.setInt(1, reclamation.getUser().getId());
             preparedStatement.setString(2, reclamation.getTitre());
             preparedStatement.setString(3, reclamation.getDescription());
             preparedStatement.setString(4, reclamation.getImage());
            
            preparedStatement.executeUpdate();
            System.out.println("Reclamation added");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (add) reclamation : " + exception.getMessage());
        }
        return false;
    }

    public boolean edit(Reclamation reclamation) {
        
        String request = "UPDATE `reclamation` SET `user_id` = ?, `titre` = ?, `description` = ?, `image` = ? WHERE `id`=" + reclamation.getId();
        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setInt(1, reclamation.getUser().getId());
            preparedStatement.setString(2, reclamation.getTitre());
            preparedStatement.setString(3, reclamation.getDescription());
            preparedStatement.setString(4, reclamation.getImage());
            
            preparedStatement.executeUpdate();
            System.out.println("Reclamation edited");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (edit) reclamation : " + exception.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM `reclamation` WHERE `id`=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Reclamation deleted");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (delete) reclamation : " + exception.getMessage());
        }
        return false;
    }
}
