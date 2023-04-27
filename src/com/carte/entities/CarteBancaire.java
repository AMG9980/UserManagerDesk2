package com.carte.entities;

import com.carte.utils.Constants;

import java.time.LocalDate;

public class CarteBancaire implements Comparable<CarteBancaire> {

    private int id;
    private Type type;
    private Compte compte;
    private String nom;
    private Long numCarte;
    private int cvv;
    private String email;
    private LocalDate date;
    private LocalDate dateExp;
    private String etat;

    public CarteBancaire(int id, Type type, Compte compte, String nom, Long numCarte, int cvv, String email, LocalDate date, LocalDate dateExp, String etat) {
        this.id = id;
        this.type = type;
        this.compte = compte;
        this.nom = nom;
        this.numCarte = numCarte;
        this.cvv = cvv;
        this.email = email;
        this.date = date;
        this.dateExp = dateExp;
        this.etat = etat;
    }

    public CarteBancaire(Type type, Compte compte, String nom, Long numCarte, int cvv, String email, LocalDate date, LocalDate dateExp, String etat) {
        this.type = type;
        this.compte = compte;
        this.nom = nom;
        this.numCarte = numCarte;
        this.cvv = cvv;
        this.email = email;
        this.date = date;
        this.dateExp = dateExp;
        this.etat = etat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Long getNumCarte() {
        return numCarte;
    }

    public void setNumCarte(Long numCarte) {
        this.numCarte = numCarte;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDateExp() {
        return dateExp;
    }

    public void setDateExp(LocalDate dateExp) {
        this.dateExp = dateExp;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }


    @Override
    public int compareTo(CarteBancaire carteBancaire) {
        switch (Constants.compareVar) {
            case "Type":
                return Integer.compare(carteBancaire.getType().getId(), this.getType().getId());
            case "Compte":
                return Integer.compare(carteBancaire.getCompte().getId(), this.getCompte().getId());
            case "Nom":
                return carteBancaire.getNom().compareTo(this.getNom());
            case "NumCarte":
                return Long.compare(carteBancaire.getNumCarte(), this.getNumCarte());
            case "Cvv":
                return Integer.compare(carteBancaire.getCvv(), this.getCvv());
            case "Email":
                return carteBancaire.getEmail().compareTo(this.getEmail());
            case "Date":
                return carteBancaire.getDate().compareTo(this.getDate());
            case "DateExp":
                return carteBancaire.getDateExp().compareTo(this.getDateExp());
            case "Etat":
                return carteBancaire.getEtat().compareTo(this.getEtat());

            default:
                return 0;
        }
    }
}