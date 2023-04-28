package com.carte.services;

import com.carte.entities.Compte;
import com.carte.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompteService {

    private static CompteService instance;
    PreparedStatement preparedStatement;
    Connection connection;

    public CompteService() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public static CompteService getInstance() {
        if (instance == null) {
            instance = new CompteService();
        }
        return instance;
    }

    public List<Compte> getAll() {
        List<Compte> listCompte = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM `compte`");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                listCompte.add(new Compte(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getFloat("balance"),
                        resultSet.getString("etat"),
                        resultSet.getLong("numero_compte"),
                        resultSet.getLong("rib")

                ));
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) compte : " + exception.getMessage());
        }
        return listCompte;
    }


    public boolean add(Compte compte) {


        String request = "INSERT INTO `compte`(`name`, `balance`, `etat`, `numero_compte`, `rib`) VALUES(?, ?, ?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, compte.getName());
            preparedStatement.setFloat(2, compte.getBalance());
            preparedStatement.setString(3, compte.getEtat());
            preparedStatement.setLong(4, compte.getNumeroCompte());
            preparedStatement.setLong(5, compte.getRib());

            preparedStatement.executeUpdate();
            System.out.println("Compte added");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (add) compte : " + exception.getMessage());
        }
        return false;
    }

    public boolean edit(Compte compte) {

        String request = "UPDATE `compte` SET `name` = ?, `balance` = ?, `etat` = ?, `numero_compte` = ?, `rib` = ? WHERE `id`=" + compte.getId();
        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, compte.getName());
            preparedStatement.setFloat(2, compte.getBalance());
            preparedStatement.setString(3, compte.getEtat());
            preparedStatement.setLong(4, compte.getNumeroCompte());
            preparedStatement.setLong(5, compte.getRib());

            preparedStatement.executeUpdate();
            System.out.println("Compte edited");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (edit) compte : " + exception.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM `compte` WHERE `id`=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Compte deleted");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (delete) compte : " + exception.getMessage());
        }
        return false;
    }
}
