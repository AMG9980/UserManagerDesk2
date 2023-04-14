package com.carte.services;

import com.carte.entities.CarteBancaire;
import com.carte.entities.Compte;
import com.carte.entities.Type;
import com.carte.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CarteBancaireService {

    private static CarteBancaireService instance;
    PreparedStatement preparedStatement;
    Connection connection;

    public CarteBancaireService() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public static CarteBancaireService getInstance() {
        if (instance == null) {
            instance = new CarteBancaireService();
        }
        return instance;
    }

    public List<CarteBancaire> getAll() {
        List<CarteBancaire> listCarteBancaire = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT * FROM `carte_bancaire` AS x " +
                    "RIGHT JOIN `type` AS y ON x.id_type = y.id " +
                    "RIGHT JOIN `compte` AS z ON x.compte = z.id " +
                    "WHERE x.id_type = y.id " +
                    "AND x.compte = z.id"
            );

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                LocalDate expirationDate = null;
                if (resultSet.getDate("date_exp") != null) {
                    expirationDate = LocalDate.parse(String.valueOf(resultSet.getDate("date_exp")));
                }

                listCarteBancaire.add(new CarteBancaire(
                        resultSet.getInt("id"),
                        new Type(
                                resultSet.getInt("y.id"),
                                resultSet.getString("y.nom"),
                                resultSet.getString("y.description")
                        ),
                        new Compte(
                                resultSet.getInt("z.id"),
                                resultSet.getString("z.name"),
                                resultSet.getFloat("z.balance"),
                                resultSet.getString("z.etat"),
                                resultSet.getLong("z.numero_compte"),
                                resultSet.getLong("z.rib")
                        ),
                        resultSet.getString("nom"),
                        resultSet.getLong("num_carte"),
                        resultSet.getInt("cvv"),
                        resultSet.getString("email"),
                        LocalDate.parse(String.valueOf(resultSet.getDate("date"))),
                        expirationDate,
                        resultSet.getString("etat")
                ));
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) carte bancaire : " + exception.getMessage());
        }
        return listCarteBancaire;
    }

    public boolean addFromUser(CarteBancaire carteBancaire) {


        String request = "INSERT INTO `carte_bancaire`(`id_type`, `compte`, `nom`, `num_carte`, `cvv`, `email`, `date`, `etat`) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setInt(1, carteBancaire.getType().getId());
            preparedStatement.setInt(2, carteBancaire.getCompte().getId());
            preparedStatement.setString(3, carteBancaire.getNom());
            preparedStatement.setLong(4, 0);
            preparedStatement.setInt(5, 0);
            preparedStatement.setString(6, carteBancaire.getEmail());
            preparedStatement.setDate(7, Date.valueOf(carteBancaire.getDate()));
            preparedStatement.setString(8, "");

            preparedStatement.executeUpdate();
            System.out.println("CarteBancaire added");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (add) carte bancaire : " + exception.getMessage());
        }
        return false;
    }

    public boolean add(CarteBancaire carteBancaire) {


        String request = "INSERT INTO `carte_bancaire`(`id_type`, `compte`, `nom`, `num_carte`, `cvv`, `email`, `date`, `date_exp`, `etat`) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setInt(1, carteBancaire.getType().getId());
            preparedStatement.setInt(2, carteBancaire.getCompte().getId());
            preparedStatement.setString(3, carteBancaire.getNom());
            preparedStatement.setLong(4, carteBancaire.getNumCarte());
            preparedStatement.setInt(5, carteBancaire.getCvv());
            preparedStatement.setString(6, carteBancaire.getEmail());
            preparedStatement.setDate(7, Date.valueOf(carteBancaire.getDate()));
            preparedStatement.setDate(8, Date.valueOf(carteBancaire.getDateExp()));
            preparedStatement.setString(9, carteBancaire.getEtat());

            preparedStatement.executeUpdate();
            System.out.println("CarteBancaire added");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (add) carte bancaire : " + exception.getMessage());
        }
        return false;
    }

    public boolean edit(CarteBancaire carteBancaire) {

        String request = "UPDATE `carte_bancaire` SET `id_type` = ?, `compte` = ?, `nom` = ?, `num_carte` = ?, `cvv` = ?, `email` = ?, `date` = ?, `date_exp` = ?, `etat` = ? WHERE `id`=" + carteBancaire.getId();
        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setInt(1, carteBancaire.getType().getId());
            preparedStatement.setInt(2, carteBancaire.getCompte().getId());
            preparedStatement.setString(3, carteBancaire.getNom());
            preparedStatement.setLong(4, carteBancaire.getNumCarte());
            preparedStatement.setInt(5, carteBancaire.getCvv());
            preparedStatement.setString(6, carteBancaire.getEmail());
            preparedStatement.setDate(7, Date.valueOf(carteBancaire.getDate()));
            preparedStatement.setDate(8, Date.valueOf(carteBancaire.getDateExp()));
            preparedStatement.setString(9, carteBancaire.getEtat());

            preparedStatement.executeUpdate();
            System.out.println("CarteBancaire edited");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (edit) carteBancaire : " + exception.getMessage());
        }
        return false;
    }

    public boolean editForUser(CarteBancaire carteBancaire) {

        String request = "UPDATE `carte_bancaire` SET `id_type` = ?, `compte` = ?, `nom` = ?, `email` = ?, `date` = ? WHERE `id`=" + carteBancaire.getId();
        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setInt(1, carteBancaire.getType().getId());
            preparedStatement.setInt(2, carteBancaire.getCompte().getId());
            preparedStatement.setString(3, carteBancaire.getNom());
            preparedStatement.setString(4, carteBancaire.getEmail());
            preparedStatement.setDate(5, Date.valueOf(carteBancaire.getDate()));

            preparedStatement.executeUpdate();
            System.out.println("CarteBancaire edited");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (edit) carteBancaire : " + exception.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM `carte_bancaire` WHERE `id`=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("CarteBancaire deleted");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (delete) carteBancaire : " + exception.getMessage());
        }
        return false;
    }
}
