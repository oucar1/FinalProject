package org.example.finalproject;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application {

    private TableView<Transaction> table;
    private ObservableList<Transaction> transactionList;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Budget Tracking Application");

        // Transaction list and table
        transactionList = FXCollections.observableArrayList();
        table = new TableView<>();
        table.setItems(transactionList);

        // Category Column (Income/Expense)
        TableColumn<Transaction, String> typeColumn = new TableColumn<>("Category");
        typeColumn.setMinWidth(100);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        // Income/Expense Type Column
        TableColumn<Transaction, String> transactionTypeColumn = new TableColumn<>("Income/Expense Type");
        transactionTypeColumn.setMinWidth(150);
        transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));

        // Amount Column
        TableColumn<Transaction, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setMinWidth(100);
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        // Date Column
        TableColumn<Transaction, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setMinWidth(100);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Note Column
        TableColumn<Transaction, String> noteColumn = new TableColumn<>("Note");
        noteColumn.setMinWidth(150);
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));

        // Add columns to the table
        table.getColumns().addAll(typeColumn, transactionTypeColumn, amountColumn, dateColumn, noteColumn);

        // "Add Transaction" button
        Button btnAddTransaction = new Button("Add Transaction");
        btnAddTransaction.setOnAction(e -> {
            // Open the TransactionForm and add a new transaction to the table
            Transaction newTransaction = TransactionForm.displayAndReturn(null);
            if (newTransaction != null) {
                transactionList.add(newTransaction);
            }
        });

        // "Edit" button
        Button btnEditTransaction = new Button("Edit");
        btnEditTransaction.setOnAction(e -> {
            Transaction selectedTransaction = table.getSelectionModel().getSelectedItem();
            if (selectedTransaction != null) {
                Transaction updatedTransaction = TransactionForm.displayAndReturn(selectedTransaction);
                if (updatedTransaction != null) {
                    int selectedIndex = table.getSelectionModel().getSelectedIndex();
                    transactionList.set(selectedIndex, updatedTransaction);
                }
            } else {
                showAlert("Please select a transaction to edit.");
            }
        });

        // "Delete" button
        Button btnDeleteTransaction = new Button("Delete");
        btnDeleteTransaction.setOnAction(e -> {
            Transaction selectedTransaction = table.getSelectionModel().getSelectedItem();
            if (selectedTransaction != null) {
                transactionList.remove(selectedTransaction);
            } else {
                showAlert("Please select a transaction to delete.");
            }
        });

        // "Monthly Balance" button
        Button btnMonthlyBalance = new Button("Monthly Balance");
        btnMonthlyBalance.setOnAction(e -> {
            // Show a dialog to ask the user for the year and month
            String yearMonth = askForYearAndMonthWithSpinner();
            if (yearMonth != null) {
                calculateMonthlyBalance(yearMonth);
            }
        });

        // "Yearly Balance" button
        Button btnYearlyBalance = new Button("Yearly Balance");
        btnYearlyBalance.setOnAction(e -> {
            // Show a dialog to ask the user for the year
            String year = askForYearWithSpinner();
            if (year != null) {
                calculateYearlyBalance(year);
            }
        });

        // Add buttons to an HBox
        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10, 10, 10, 10));
        buttonBox.getChildren().addAll(btnAddTransaction, btnEditTransaction, btnDeleteTransaction, btnMonthlyBalance, btnYearlyBalance);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(buttonBox, table);

        Scene scene = new Scene(vbox, 700, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Calculate Monthly Balance
    private void calculateMonthlyBalance(String yearMonth) {
        List<Transaction> monthlyTransactions = transactionList.stream()
                .filter(t -> t.getDate().startsWith(yearMonth))
                .collect(Collectors.toList());

        double totalIncome = monthlyTransactions.stream()
                .filter(t -> t.getType().equals("Income"))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalExpense = monthlyTransactions.stream()
                .filter(t -> t.getType().equals("Expense"))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double balance = totalIncome - totalExpense;
        showAlert("Monthly Balance for " + yearMonth + ": $ " + balance);
    }

    // Calculate Yearly Balance with user-selected year
    private void calculateYearlyBalance(String year) {
        List<Transaction> yearlyTransactions = transactionList.stream()
                .filter(t -> t.getDate().startsWith(year))
                .collect(Collectors.toList());

        double totalIncome = yearlyTransactions.stream()
                .filter(t -> t.getType().equals("Income"))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalExpense = yearlyTransactions.stream()
                .filter(t -> t.getType().equals("Expense"))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double balance = totalIncome - totalExpense;
        showAlert("Yearly Balance for " + year + ": $ " + balance);
    }

    // Ask the user for a year and month using Spinner for year selection
    private String askForYearAndMonthWithSpinner() {
        Stage dialog = new Stage();
        dialog.setTitle("Select Year and Month");

        // Spinner for selecting year (allows any year from 1900 to 2100)
        Spinner<Integer> yearSpinner = new Spinner<>(1900, 2100, LocalDate.now().getYear());
        yearSpinner.setEditable(true);  // Allows user to input any year

        // ComboBox for selecting month
        ComboBox<String> monthComboBox = new ComboBox<>();
        monthComboBox.setItems(FXCollections.observableArrayList(
                "01 - January", "02 - February", "03 - March", "04 - April",
                "05 - May", "06 - June", "07 - July", "08 - August",
                "09 - September", "10 - October", "11 - November", "12 - December"));
        monthComboBox.setPromptText("Select Month");

        // Save button
        Button saveButton = new Button("OK");
        saveButton.setOnAction(e -> dialog.close());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(new Label("Year:"), yearSpinner, new Label("Month:"), monthComboBox, saveButton);

        Scene dialogScene = new Scene(layout, 300, 200);
        dialog.setScene(dialogScene);
        dialog.showAndWait();

        Integer selectedYear = yearSpinner.getValue();
        String selectedMonth = monthComboBox.getValue() != null ? monthComboBox.getValue().substring(0, 2) : null;

        if (selectedYear != null && selectedMonth != null) {
            return selectedYear + "-" + selectedMonth;
        }
        return null;
    }

    // Ask the user for a year using Spinner
    private String askForYearWithSpinner() {
        Stage dialog = new Stage();
        dialog.setTitle("Select Year");

        // Spinner for selecting year (allows any year from 1900 to 2100)
        Spinner<Integer> yearSpinner = new Spinner<>(1900, 2100, LocalDate.now().getYear());
        yearSpinner.setEditable(true);  // Allows user to input any year

        // Save button
        Button saveButton = new Button("OK");
        saveButton.setOnAction(e -> dialog.close());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(new Label("Year:"), yearSpinner, saveButton);

        Scene dialogScene = new Scene(layout, 300, 150);
        dialog.setScene(dialogScene);
        dialog.showAndWait();

        Integer selectedYear = yearSpinner.getValue();

        if (selectedYear != null) {
            return selectedYear.toString();
        }
        return null;
    }

    // Helper method to show alert messages
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Balance Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
