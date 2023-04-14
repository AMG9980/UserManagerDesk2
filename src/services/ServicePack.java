/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

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
public class ServicePack implements IService<Pack> {

    public Connection conx = MyDB.getInstance().getConx();
    public Statement stm;

    @Override
    public void ajouter(Pack p) {
        String sql = "INSERT INTO pack (nom,prix,description,image) VALUES (?, ?, ?, ?)";
        try (
                PreparedStatement pstmt = conx.prepareStatement(sql)) {

            pstmt.setString(1, p.getNom());
            pstmt.setInt(2, p.getPrix());
            pstmt.setString(3, p.getDesc());
            pstmt.setString(4, p.getImage());

            pstmt.executeUpdate();
            System.out.println("Pack ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM pack WHERE id = ?";
        try {
            PreparedStatement pstmt = conx.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Pack deleted !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Pack p) {

        String sql = "UPDATE pack SET nom = ?, prix = ?,description= ?,image= ? WHERE id = ?";
        try (
                PreparedStatement pstmt = conx.prepareStatement(sql)) {

            pstmt.setString(1, p.getNom());
            pstmt.setInt(2, p.getPrix());
            pstmt.setString(3, p.getDesc());
            pstmt.setString(4, p.getImage());
            pstmt.setInt(5, p.getId());
            pstmt.executeUpdate();
            System.out.println("Pack updated");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Pack> getAll() {

        List<Pack> packs = new ArrayList<Pack>();
        try {
            String sql = "Select * From pack";
            PreparedStatement pstmt = conx.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Pack p = new Pack();
                p.setId(rs.getInt("id"));
                p.setNom(rs.getString("nom"));
                p.setPrix(rs.getInt("prix"));
                p.setDesc(rs.getString("description"));
                p.setImage(rs.getString("image"));

                packs.add(p);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServicePack.class.getName()).log(Level.SEVERE, null, ex);
        }

        return packs;
    }

    @Override
    public Pack getOneById(int id) {
        Pack p = new Pack();
        String sql = "SELECT * FROM pack WHERE id = ?";
           
        try {
            PreparedStatement pstmt = conx.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
             if (rs.next()) {

            p.setId(rs.getInt("id"));
            p.setNom(rs.getString("nom"));
            p.setPrix(rs.getInt("prix"));
            p.setDesc(rs.getString("description"));
            p.setImage(rs.getString("image"));
            
            return p;
             }
        } catch (SQLException ex) {
            Logger.getLogger(ServicePack.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null ; 
    }

}
