package org.example.finalproject;

public class Income extends Transaction {
    public Income(double amount, String date, String transactionType) {
        super(amount, date, transactionType);
    }

    @Override
    public String getType() {
        return "Income";
    }
}
