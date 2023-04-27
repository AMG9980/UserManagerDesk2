package com.carte.services;

import com.carte.entities.Compte;
import com.carte.entities.Transaction;
import com.carte.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {

    private static TransactionService instance;
    PreparedStatement preparedStatement;
    Connection connection;

    public TransactionService() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public static TransactionService getInstance() {
        if (instance == null) {
            instance = new TransactionService();
        }
        return instance;
    }

    public List<Transaction> getAll() {
        List<Transaction> listTransaction = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM `transaction` AS x " +
                            "RIGHT JOIN `compte` AS y ON x.source_account_id = y.id " +
                            "RIGHT JOIN `compte` AS z ON x.destination_account = z.id " +
                            "WHERE x.source_account_id = y.id " +
                            "AND x.destination_account = z.id"
            );

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                listTransaction.add(new Transaction(
                        resultSet.getInt("id"),
                        new Compte(
                                resultSet.getInt("y.id"),
                                resultSet.getString("y.name"),
                                resultSet.getFloat("y.balance"),
                                resultSet.getString("y.etat"),
                                resultSet.getLong("y.numero_compte"),
                                resultSet.getLong("y.rib")
                        ),
                        new Compte(
                                resultSet.getInt("z.id"),
                                resultSet.getString("z.name"),
                                resultSet.getFloat("z.balance"),
                                resultSet.getString("z.etat"),
                                resultSet.getLong("z.numero_compte"),
                                resultSet.getLong("z.rib")
                        ),
                        resultSet.getFloat("amount"),
                        LocalDate.parse(String.valueOf(resultSet.getDate("created_at")))
                ));
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) transaction : " + exception.getMessage());
        }
        return listTransaction;
    }

    public boolean add(Transaction transaction) {

        String request = "INSERT INTO `transaction`(`source_account_id`, `destination_account`, `amount`, `created_at`) VALUES(?, ?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setInt(1, transaction.getSourceAccount().getId());
            preparedStatement.setInt(2, transaction.getDestinationAccount().getId());
            preparedStatement.setFloat(3, transaction.getAmount());
            preparedStatement.setDate(4, Date.valueOf(transaction.getCreatedAt()));

            preparedStatement.executeUpdate();
            System.out.println("Transaction added");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (add) transaction : " + exception.getMessage());
        }
        return false;
    }

    public boolean edit(Transaction transaction) {

        String request = "UPDATE `transaction` SET `source_account_id` = ?, `destination_account` = ?, `amount` = ?, `created_at` = ? WHERE `id`=" + transaction.getId();
        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setInt(1, transaction.getSourceAccount().getId());
            preparedStatement.setInt(2, transaction.getDestinationAccount().getId());
            preparedStatement.setFloat(3, transaction.getAmount());
            preparedStatement.setDate(4, Date.valueOf(transaction.getCreatedAt()));

            preparedStatement.executeUpdate();
            System.out.println("Transaction edited");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (edit) transaction : " + exception.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM `transaction` WHERE `id`=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Transaction deleted");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (delete) transaction : " + exception.getMessage());
        }
        return false;
    }
}
