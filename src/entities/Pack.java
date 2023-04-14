/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

/**
 *
 * @author HP
 */
public class Pack {
    
      
   private int id;
   private int prix;
   private String nom,image,desc;

    public Pack() {
    }

    public Pack(int id, int prix, String nom, String image, String desc) {
        this.id = id;
        this.prix = prix;
        this.nom = nom;
        this.image = image;
        this.desc = desc;
    }

    public Pack(int prix, String nom, String image, String desc) {
        this.prix = prix;
        this.nom = nom;
        this.image = image;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Pack{" + "id=" + id + ", prix=" + prix + ", nom=" + nom + ", image=" + image + ", desc=" + desc + '}';
    }
   
   
   
    
}
