package org.example.finalproject;

public class Expense extends Transaction {
    public Expense(double amount, String date, String transactionType) {
        super(amount, date, transactionType);
    }

    @Override
    public String getType() {
        return "Expense";
    }
}
