package com.carte.entities;

import com.carte.utils.Constants;

public class Compte implements Comparable<Compte> {

    private int id;
    private String name;
    private float balance;
    private String etat;
    private Long numeroCompte;
    private Long rib;

    public Compte(int id, String name, float balance, String etat, Long numeroCompte, Long rib) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.etat = etat;
        this.numeroCompte = numeroCompte;
        this.rib = rib;
    }

    public Compte(String name, float balance, String etat, Long numeroCompte, Long rib) {
        this.name = name;
        this.balance = balance;
        this.etat = etat;
        this.numeroCompte = numeroCompte;
        this.rib = rib;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Long getNumeroCompte() {
        return numeroCompte;
    }

    public void setNumeroCompte(Long numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    public Long getRib() {
        return rib;
    }

    public void setRib(Long rib) {
        this.rib = rib;
    }


    @Override
    public int compareTo(Compte compte) {
        switch (Constants.compareVar) {
            case "Name":
                return compte.getName().compareTo(this.getName());
            case "Balance":
                return Float.compare(compte.getBalance(), this.getBalance());
            case "Etat":
                return compte.getEtat().compareTo(this.getEtat());
            case "NumeroCompte":
                return Long.compare(compte.getNumeroCompte(), this.getNumeroCompte());
            case "Rib":
                return Long.compare(compte.getRib(), this.getRib());

            default:
                return 0;
        }
    }

    @Override
    public String toString() {
        return "name : " + name;
    }
}