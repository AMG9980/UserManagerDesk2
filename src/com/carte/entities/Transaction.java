package com.carte.entities;


import java.time.LocalDate;

public class Transaction {

    private int id;
    private Compte sourceAccount;
    private Compte destinationAccount;
    private float amount;
    private LocalDate createdAt;

    public Transaction(int id, Compte sourceAccount, Compte destinationAccount, float amount, LocalDate createdAt) {
        this.id = id;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public Transaction(Compte sourceAccount, Compte destinationAccount, float amount, LocalDate createdAt) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Compte getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Compte sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public Compte getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(Compte destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}