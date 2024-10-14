package org.example.finalproject;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.time.LocalDate;

public class TransactionForm {
    private static Transaction transaction = null;

    public static Transaction displayAndReturn(Transaction existingTransaction) {
        Stage window = new Stage();
        window.setTitle(existingTransaction == null ? "Add New Transaction" : "Edit Transaction");

        // GridPane layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Amount
        Label amountLabel = new Label("Amount:");
        GridPane.setConstraints(amountLabel, 0, 0);
        TextField amountInput = new TextField();
        amountInput.setPromptText("0.00");
        GridPane.setConstraints(amountInput, 1, 0);

        // Date selection using DatePicker
        Label dateLabel = new Label("Date:");
        GridPane.setConstraints(dateLabel, 0, 1);
        DatePicker datePicker = new DatePicker();
        GridPane.setConstraints(datePicker, 1, 1);

        // Radio Buttons for Income/Expense selection
        ToggleGroup transactionTypeGroup = new ToggleGroup();
        RadioButton incomeButton = new RadioButton("Income");
        incomeButton.setToggleGroup(transactionTypeGroup);
        incomeButton.setSelected(true);  // Default to income

        RadioButton expenseButton = new RadioButton("Expense");
        expenseButton.setToggleGroup(transactionTypeGroup);

        GridPane.setConstraints(incomeButton, 1, 2);
        GridPane.setConstraints(expenseButton, 1, 3);

        // Income types ComboBox
        ComboBox<String> incomeTypeComboBox = new ComboBox<>();
        incomeTypeComboBox.setItems(FXCollections.observableArrayList(
                "Salary", "Freelance Income", "Rental Income",
                "Investment Income", "Bonuses and Commissions",
                "Side Income", "Pension/Retirement Income",
                "Government Assistance", "Gifts or Inheritance"
        ));
        incomeTypeComboBox.setPromptText("Select Income Type");
        GridPane.setConstraints(incomeTypeComboBox, 1, 4);

        // Expense types ComboBox
        ComboBox<String> expenseTypeComboBox = new ComboBox<>();
        expenseTypeComboBox.setItems(FXCollections.observableArrayList(
                "Rent/Mortgage", "Food/Groceries", "Bills/Utilities",
                "Transportation", "Entertainment", "Healthcare",
                "Credit Card Payments", "Education", "Clothing",
                "Taxes", "Donations", "Vacation/Travel",
                "Home Maintenance"
        ));
        expenseTypeComboBox.setPromptText("Select Expense Type");
        GridPane.setConstraints(expenseTypeComboBox, 1, 4);

        // By default, only show the income type options
        expenseTypeComboBox.setVisible(false);

        // Show the appropriate ComboBox based on the selection (Income/Expense)
        incomeButton.setOnAction(e -> {
            incomeTypeComboBox.setVisible(true);
            expenseTypeComboBox.setVisible(false);
        });
        expenseButton.setOnAction(e -> {
            expenseTypeComboBox.setVisible(true);
            incomeTypeComboBox.setVisible(false);
        });

        // Note (Optional note field)
        Label noteLabel = new Label("Note:");
        GridPane.setConstraints(noteLabel, 0, 5);
        TextField noteInput = new TextField();
        noteInput.setPromptText("Optional note");
        GridPane.setConstraints(noteInput, 1, 5);

        // Save Button
        Button saveButton = new Button("Save");
        GridPane.setConstraints(saveButton, 1, 6);

        // Label for error messages
        Label errorMessage = new Label();
        GridPane.setConstraints(errorMessage, 1, 7);

        // Populate fields if editing an existing transaction
        if (existingTransaction != null) {
            amountInput.setText(String.valueOf(existingTransaction.getAmount()));
            datePicker.setValue(LocalDate.parse(existingTransaction.getDate()));
            noteInput.setText(existingTransaction.getNote());
            if (existingTransaction instanceof Income) {
                incomeButton.setSelected(true);
                incomeTypeComboBox.setVisible(true);
                expenseTypeComboBox.setVisible(false);
            } else if (existingTransaction instanceof Expense) {
                expenseButton.setSelected(true);
                expenseTypeComboBox.setVisible(true);
                incomeTypeComboBox.setVisible(false);
            }
        }

        // Save Button Action
        saveButton.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountInput.getText());
                LocalDate date = datePicker.getValue();
                String note = noteInput.getText();
                String type = incomeButton.isSelected() ? incomeTypeComboBox.getValue() : expenseTypeComboBox.getValue();

                if (type == null) {
                    errorMessage.setText("Please select a type.");
                    errorMessage.setStyle("-fx-text-fill: red;");
                    return;
                }

                if (incomeButton.isSelected()) {
                    transaction = new Income(amount, date.toString(), type);
                } else if (expenseButton.isSelected()) {
                    transaction = new Expense(amount, date.toString(), type);
                }

                transaction.setNote(note);
                window.close();
            } catch (NumberFormatException ex) {
                errorMessage.setText("Invalid amount entered!");
                errorMessage.setStyle("-fx-text-fill: red;");
            }
        });

        grid.getChildren().addAll(amountLabel, amountInput, dateLabel, datePicker, incomeButton, expenseButton, incomeTypeComboBox, expenseTypeComboBox, noteLabel, noteInput, saveButton, errorMessage);

        Scene scene = new Scene(grid, 400, 400);
        window.setScene(scene);
        window.showAndWait();

        return transaction;
    }
}
