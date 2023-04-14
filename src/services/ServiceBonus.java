/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import entities.Bonus;
import entities.Pack;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import tests.MyDB;

/**
 *
 * @author HP
 */
public class ServiceBonus implements IService<Bonus> {

    public Connection conx = MyDB.getInstance().getConx();
    public Statement stm;

    @Override
    public void ajouter(Bonus b) {
        String sql = "INSERT INTO bonus (nom_bonus1,nom_bonus2,nom_bonus3,nom_bonus4,bonus_id) VALUES (?, ?, ?, ? , ?)";
        try (
                PreparedStatement pstmt = conx.prepareStatement(sql)) {

            pstmt.setString(1, b.getBonus1());
            pstmt.setString(2, b.getBonus2());
            pstmt.setString(3, b.getBonus3());
            pstmt.setString(4, b.getBonus4());
            pstmt.setInt(5, b.getP().getId());

            pstmt.executeUpdate();
            System.out.println("Bonus ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM bonus WHERE id = ?";
        try {
            PreparedStatement pstmt = conx.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Bonus deleted !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Bonus b) {
        String sql = "UPDATE bonus SET nom_bonus1 = ?, nom_bonus2 = ?,nom_bonus3= ?,nom_bonus4= ? , bonus_id = ? WHERE id = ?";
        try (
                PreparedStatement pstmt = conx.prepareStatement(sql)) {

            pstmt.setString(1, b.getBonus1());
            pstmt.setString(2, b.getBonus2());
            pstmt.setString(3, b.getBonus3());
            pstmt.setString(4, b.getBonus4());
            pstmt.setInt(5, b.getP().getId());
            pstmt.setInt(6, b.getId());

            pstmt.executeUpdate();
            System.out.println("Pack updated");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Bonus> getAll() {
        List<Bonus> bonuss = new ArrayList<Bonus>();
        ServicePack sp = new ServicePack();
        try {
            String sql = "Select * From bonus";
            PreparedStatement pstmt = conx.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Bonus p = new Bonus();
                p.setId(rs.getInt("id"));
                p.setBonus1(rs.getString("nom_bonus1"));
                p.setBonus2(rs.getString("nom_bonus2"));
                p.setBonus3(rs.getString("nom_bonus3"));
                p.setBonus4(rs.getString("nom_bonus4"));
                p.setP(sp.getOneById(rs.getInt("bonus_id")));

                bonuss.add(p);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServicePack.class.getName()).log(Level.SEVERE, null, ex);
        }

        return bonuss;
    }

    @Override
    public Bonus getOneById(int id) {
 
        ServicePack sp = new ServicePack();
        try {
            String sql = "Select * From bonus where id = ? ";
            PreparedStatement pstmt = conx.prepareStatement(sql);
             pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Bonus p = new Bonus();
                p.setId(rs.getInt("id"));
                p.setBonus1(rs.getString("nom_bonus1"));
                p.setBonus2(rs.getString("nom_bonus2"));
                p.setBonus3(rs.getString("nom_bonus3"));
                p.setBonus4(rs.getString("nom_bonus4"));
                p.setP(sp.getOneById(rs.getInt("bonus_id")));
                 return p ; 
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServicePack.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;    
    }

}
