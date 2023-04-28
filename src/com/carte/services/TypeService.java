package com.carte.services;

import com.carte.entities.Type;
import com.carte.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TypeService {

    private static TypeService instance;
    PreparedStatement preparedStatement;
    Connection connection;

    public TypeService() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public static TypeService getInstance() {
        if (instance == null) {
            instance = new TypeService();
        }
        return instance;
    }

    public List<Type> getAll() {
        List<Type> listType = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM `type`");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                listType.add(new Type(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("description")
                ));
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) type : " + exception.getMessage());
        }
        return listType;
    }


    public boolean add(Type type) {


        String request = "INSERT INTO `type`(`nom`, `description`) VALUES(?, ?)";
        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, type.getNom());
            preparedStatement.setString(2, type.getDescription());

            preparedStatement.executeUpdate();
            System.out.println("Type added");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (add) type : " + exception.getMessage());
        }
        return false;
    }

    public boolean edit(Type type) {

        String request = "UPDATE `type` SET `nom` = ?, `description` = ? WHERE `id`=" + type.getId();
        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, type.getNom());
            preparedStatement.setString(2, type.getDescription());

            preparedStatement.executeUpdate();
            System.out.println("Type edited");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (edit) type : " + exception.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM `type` WHERE `id`=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Type deleted");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (delete) type : " + exception.getMessage());
        }
        return false;
    }
}
