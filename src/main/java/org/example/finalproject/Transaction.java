package org.example.finalproject;

public abstract class Transaction {
    protected double amount;
    protected String date;
    protected String note;
    protected String transactionType;  // Transaction type field

    public Transaction(double amount, String date, String transactionType) {
        this.amount = amount;
        this.date = date;
        this.transactionType = transactionType;  // Set transaction type
    }

    // Getters and setters
    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public abstract String getType();  // Income or Expense

    @Override
    public String toString() {
        return date + ": " + amount + " | Note: " + note;
    }
}
