package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class ProjectPageController {

    @FXML private Label usernameLabel;
    @FXML private Label sidebarUsername;
    @FXML private Label totalBalanceLabel;
    @FXML private Label totalSpentLabel;
    @FXML private Label totalRemainingLabel;
    @FXML private Label projectNameLabel;
    @FXML private FlowPane potsGrid;
    @FXML private VBox transactionsContainer;

    private Project project;
    private String username;
    private ProjectsController projectsController;

    public void setProject(Project project, String username, ProjectsController projectsController) {
        this.project = project;
        this.username = username;
        this.projectsController = projectsController;
        if (usernameLabel != null) usernameLabel.setText(username);
        if (sidebarUsername != null) sidebarUsername.setText(username);
        if (projectNameLabel != null) projectNameLabel.setText(project.getProjectName());
        refreshPage();
    }

    private void refreshPage() {
        updateStats();
        refreshPots();
        refreshTransactions();
    }

    private void updateStats() {
        totalBalanceLabel.setText(String.format("$%.2f", project.getTotalBudget()));
        totalSpentLabel.setText(String.format("$%.2f", project.getTotalSpent()));
        totalRemainingLabel.setText(String.format("$%.2f", project.getTotalRemaining()));
    }

    // ── POT SECTION ─────────────────────────────────────────────────────────────

    private void refreshPots() {
        potsGrid.getChildren().clear();
        for (Pot pot : project.getListOfPots()) {
            potsGrid.getChildren().add(buildPotCard(pot));
        }
    }

    private VBox buildPotCard(Pot pot) {
        VBox card = new VBox(10);
        card.setPrefWidth(300);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 8, 0, 0, 2);");

        // Header row: pot name + edit/delete buttons
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Label nameLabel = new Label(pot.getPotName());
        nameLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: 700; -fx-text-fill: #191919;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button editBtn = new Button("Edit");
        editBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #299D91; -fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 0;");
        editBtn.setOnAction(e -> handleEditPot(pot));

        Button deleteBtn = new Button("Delete");
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #e53935; -fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 0;");
        deleteBtn.setOnAction(e -> handleDeletePot(pot));

        header.getChildren().addAll(nameLabel, spacer, editBtn, deleteBtn);

        // Stats grid
        GridPane stats = new GridPane();
        stats.setHgap(24);
        stats.setVgap(6);

        double pct = pot.getPercentAllocated(project.getTotalBudget());
        addPotStatRow(stats, "Amount Allocated", String.format("$%.2f", pot.getAllocatedAmount()), 0);
        addPotStatRow(stats, "% Allocated", String.format("%.1f%%", pct), 1);
        addPotStatRow(stats, "Total Spent", String.format("$%.2f", pot.getSpent()), 2);
        addPotStatRow(stats, "Remaining", String.format("$%.2f", pot.getRemaining()), 3);

        card.getChildren().addAll(header, stats);
        return card;
    }

    private void addPotStatRow(GridPane grid, String label, String value, int row) {
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #888888;");
        Label val = new Label(value);
        val.setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: #191919;");
        grid.add(lbl, 0, row);
        grid.add(val, 1, row);
    }

    @FXML
    private void handleCreatePot() {
        showPotDialog(null);
    }

    private void handleEditPot(Pot pot) {
        showPotDialog(pot);
    }

    private void showPotDialog(Pot existingPot) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle(existingPot == null ? "Create Pot" : "Edit Pot");

        VBox content = new VBox(16);
        content.setStyle("-fx-padding: 32; -fx-background-color: white;");
        content.setPrefWidth(380);

        // X close button
        HBox topRow = new HBox();
        topRow.setAlignment(Pos.CENTER_RIGHT);
        Button closeBtn = new Button("✕");
        closeBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 16px; -fx-cursor: hand; -fx-padding: 0;");
        closeBtn.setOnAction(e -> popup.close());
        topRow.getChildren().add(closeBtn);

        Label titleLabel = new Label(existingPot == null ? "Create Pot" : "Edit Pot");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: 700;");

        Label nameLabel = new Label("Pot name");
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: 600;");
        TextField nameField = new TextField(existingPot != null ? existingPot.getPotName() : "");
        nameField.setPromptText("Write pot name here");
        styleTextField(nameField);

        Label amountLabel = new Label("Amount");
        amountLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: 600;");
        TextField amountField = new TextField(existingPot != null ? String.valueOf(existingPot.getAllocatedAmount()) : "");
        amountField.setPromptText("Write presents amounts here");
        styleTextField(amountField);

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: #e53935; -fx-font-size: 12px;");

        Button actionBtn = new Button(existingPot == null ? "Create Pot" : "Save");
        actionBtn.setPrefWidth(316);
        styleActionButton(actionBtn);
        actionBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String amtText = amountField.getText().trim();
            if (name.isEmpty() || amtText.isEmpty()) {
                errorLabel.setText("Please fill in all fields.");
                return;
            }
            double amount;
            try {
                amount = Double.parseDouble(amtText);
            } catch (NumberFormatException ex) {
                errorLabel.setText("Amount must be a valid number.");
                return;
            }
            // Validate allocation: check remaining unallocated budget
            double currentAllocated = project.getTotalAllocated();
            if (existingPot != null) currentAllocated -= existingPot.getAllocatedAmount();
            if (currentAllocated + amount > project.getTotalBudget()) {
                errorLabel.setText("Allocated amount is exceeding this project's total balance.");
                return;
            }
            if (existingPot == null) {
                project.addPot(new Pot(name, amount));
            } else {
                existingPot.setPotName(name);
                existingPot.setAllocatedAmount(amount);
            }
            popup.close();
            refreshPage();
        });

        content.getChildren().addAll(topRow, titleLabel, nameLabel, nameField, amountLabel, amountField, errorLabel, actionBtn);
        popup.setScene(new Scene(content));
        popup.showAndWait();
    }

    private void handleDeletePot(Pot pot) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete pot \"" + pot.getPotName() + "\"?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Delete Pot");
        alert.setHeaderText(null);
        alert.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                project.removePot(pot);
                refreshPage();
            }
        });
    }

    // ── TRANSACTION SECTION ──────────────────────────────────────────────────────

    private void refreshTransactions() {
        transactionsContainer.getChildren().clear();
        List<Transaction> transactions = project.getListOfTransactions();
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            HBox row = buildTransactionRow(t, i < transactions.size() - 1);
            transactionsContainer.getChildren().add(row);
        }
    }

    private HBox buildTransactionRow(Transaction t, boolean hasBorder) {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new javafx.geometry.Insets(16, 20, 16, 20));
        if (hasBorder) {
            row.setStyle("-fx-border-color: #f0f0f0; -fx-border-width: 0 0 1 0;");
        }

        // Amount label
        boolean isExpense = t.isExpense();
        String amountText = (isExpense ? "-" : "+") + String.format("$%.2f", t.getTransactionValue());
        Label amountLabel = new Label(amountText);
        amountLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: 700; -fx-text-fill: " + (isExpense ? "#e53935" : "#299D91") + "; -fx-min-width: 120;");

        // Info column
        VBox info = new VBox(3);
        Label dateLabel = new Label(t.getDate());
        dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #888888;");
        Label personLabel = new Label(t.getTransactionName());
        personLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: #191919;");
        Label descLabel = new Label(t.getDescription());
        descLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555555;");
        info.getChildren().addAll(dateLabel, personLabel, descLabel);

        // Pot badge
        Label potBadge = new Label(t.getPotName());
        potBadge.setStyle("-fx-background-color: #e8f5f3; -fx-text-fill: #299D91; -fx-font-size: 12px; -fx-font-weight: 600; -fx-padding: 4 10 4 10; -fx-background-radius: 20;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Edit/Delete buttons
        Button editBtn = new Button("Edit");
        editBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #299D91; -fx-font-size: 12px; -fx-cursor: hand;");
        editBtn.setOnAction(e -> showTransactionDialog(t));

        Button deleteBtn = new Button("✕");
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #e53935; -fx-font-size: 13px; -fx-cursor: hand;");
        deleteBtn.setOnAction(e -> handleDeleteTransaction(t));

        row.getChildren().addAll(amountLabel, info, potBadge, spacer, editBtn, deleteBtn);
        return row;
    }

    @FXML
    private void handleCreateTransaction() {
        showTransactionDialog(null);
    }

    private void showTransactionDialog(Transaction existingTx) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle(existingTx == null ? "Create Transaction" : "Edit Transaction");

        VBox content = new VBox(12);
        content.setStyle("-fx-padding: 32; -fx-background-color: white;");
        content.setPrefWidth(420);

        Label titleLabel = new Label(existingTx == null ? "Create Transaction" : "Edit Transaction");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: 700;");

        // Budget Pot dropdown
        Label potLabel = new Label("Budget Pot");
        potLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 600;");
        ComboBox<String> potCombo = new ComboBox<>();
        potCombo.setPromptText("Select");
        potCombo.setPrefWidth(356);
        potCombo.setStyle("-fx-font-size: 13px;");
        for (Pot p : project.getListOfPots()) {
            potCombo.getItems().add(p.getPotName());
        }
        if (existingTx != null) potCombo.setValue(existingTx.getPotName());

        // Type + Amount row
        HBox typeAmountRow = new HBox(12);
        VBox typeBox = new VBox(6);
        Label typeLabel = new Label("Type");
        typeLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 600;");
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("expense", "income");
        typeCombo.setPromptText("Select");
        typeCombo.setPrefWidth(160);
        typeCombo.setStyle("-fx-font-size: 13px;");
        if (existingTx != null) typeCombo.setValue(existingTx.getTransactionType());
        typeBox.getChildren().addAll(typeLabel, typeCombo);

        VBox amountBox = new VBox(6);
        Label amountLabel = new Label("Amount");
        amountLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 600;");
        TextField amountField = new TextField(existingTx != null ? String.valueOf(existingTx.getTransactionValue()) : "");
        amountField.setPromptText("$");
        amountField.setPrefWidth(172);
        styleTextField(amountField);
        amountBox.getChildren().addAll(amountLabel, amountField);
        typeAmountRow.getChildren().addAll(typeBox, amountBox);

        // Payment Method
        Label pmLabel = new Label("Payment Method");
        pmLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 600;");
        ComboBox<String> pmCombo = new ComboBox<>();
        pmCombo.getItems().addAll("Cash", "Bank Transfer", "Credit Card", "Debit Card", "Other");
        pmCombo.setPromptText("Select");
        pmCombo.setPrefWidth(356);
        pmCombo.setStyle("-fx-font-size: 13px;");
        if (existingTx != null) pmCombo.setValue(existingTx.getPaymentMethod());

        // Date
        Label dateLabel = new Label("Date");
        dateLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 600;");
        TextField dateField = new TextField(existingTx != null ? existingTx.getDate() : "");
        dateField.setPromptText("YYYY/MM/DD");
        styleTextField(dateField);

        // Person in charge
        Label personLabel = new Label("Person in charge");
        personLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 600;");
        TextField personField = new TextField(existingTx != null ? existingTx.getTransactionName() : "");
        personField.setPromptText("Name");
        styleTextField(personField);

        // Notes
        Label notesLabel = new Label("Notes");
        notesLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 600;");
        TextField notesField = new TextField(existingTx != null ? existingTx.getDescription() : "");
        notesField.setPromptText("Name");
        styleTextField(notesField);

        // Attachments
        Label attachLabel = new Label("Attachments");
        attachLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 600;");
        HBox attachRow = new HBox(8);
        attachRow.setAlignment(Pos.CENTER_LEFT);
        Label attachName = new Label(existingTx != null && existingTx.getProof() != null ? existingTx.getProof() : "");
        attachName.setStyle("-fx-font-size: 12px; -fx-text-fill: #555555;");
        Button chooseFileBtn = new Button("Choose Files");
        chooseFileBtn.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-size: 12px; -fx-padding: 6 12 6 12; -fx-cursor: hand;");
        final String[] proofPath = {existingTx != null ? existingTx.getProof() : null};
        chooseFileBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Select Attachment");
            File chosen = fc.showOpenDialog(popup);
            if (chosen != null) {
                proofPath[0] = chosen.getAbsolutePath();
                attachName.setText(chosen.getName());
            }
        });
        attachRow.getChildren().addAll(chooseFileBtn, attachName);

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: #e53935; -fx-font-size: 12px;");

        Button actionBtn = new Button(existingTx == null ? "Create Transaction" : "Save Transaction");
        actionBtn.setPrefWidth(356);
        styleActionButton(actionBtn);
        actionBtn.setOnAction(e -> {
            String pot = potCombo.getValue();
            String type = typeCombo.getValue();
            String amtText = amountField.getText().trim();
            String pm = pmCombo.getValue();
            String date = dateField.getText().trim();
            String person = personField.getText().trim();
            String notes = notesField.getText().trim();

            if (pot == null || type == null || amtText.isEmpty() || pm == null || date.isEmpty()) {
                errorLabel.setText("Please fill in all required fields.");
                return;
            }
            double amount;
            try {
                amount = Double.parseDouble(amtText);
            } catch (NumberFormatException ex) {
                errorLabel.setText("Amount must be a valid number.");
                return;
            }
            if (existingTx == null) {
                Transaction tx = new Transaction(type, person, amount, pm, date, notes, pot, proofPath[0]);
                project.addTransaction(tx);
            } else {
                project.removeTransaction(existingTx);
                existingTx.setTransactionType(type);
                existingTx.setTransactionName(person);
                existingTx.setTransactionValue(amount);
                existingTx.setPaymentMethod(pm);
                existingTx.setDate(date);
                existingTx.setDescription(notes);
                existingTx.setPotName(pot);
                existingTx.setProof(proofPath[0]);
                project.addTransaction(existingTx);
            }
            popup.close();
            refreshPage();
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setPrefWidth(356);
        cancelBtn.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-size: 14px; -fx-padding: 10 0 10 0; -fx-cursor: hand;");
        cancelBtn.setOnAction(e -> popup.close());

        content.getChildren().addAll(titleLabel, potLabel, potCombo, typeAmountRow, pmLabel, pmCombo,
                dateLabel, dateField, personLabel, personField, notesLabel, notesField,
                attachLabel, attachRow, errorLabel, actionBtn, cancelBtn);

        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background: white; -fx-background-color: white;");
        popup.setScene(new Scene(sp, 450, 600));
        popup.showAndWait();
    }

    private void handleDeleteTransaction(Transaction t) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete this transaction?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Delete Transaction");
        alert.setHeaderText(null);
        alert.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                project.removeTransaction(t);
                refreshPage();
            }
        });
    }

    // ── EDIT PROJECT ─────────────────────────────────────────────────────────────

    @FXML
    private void handleChangeName() {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Change Project Name");

        VBox content = new VBox(16);
        content.setStyle("-fx-padding: 32; -fx-background-color: white;");
        content.setPrefWidth(380);

        Label titleLabel = new Label("Change Project Name");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: 700;");

        TextField nameField = new TextField(project.getProjectName());
        styleTextField(nameField);

        Button saveBtn = new Button("Save");
        saveBtn.setPrefWidth(316);
        styleActionButton(saveBtn);
        saveBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                project.setProjectName(name);
                projectNameLabel.setText(name);
                projectsController.refreshProjectCard(project);
                popup.close();
            }
        });

        content.getChildren().addAll(titleLabel, nameField, saveBtn);
        popup.setScene(new Scene(content));
        popup.showAndWait();
    }

    @FXML
    private void handleEditBudget() {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Edit Budget");

        VBox content = new VBox(16);
        content.setStyle("-fx-padding: 32; -fx-background-color: white;");
        content.setPrefWidth(380);

        Label titleLabel = new Label("Edit Budget");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: 700;");

        TextField budgetField = new TextField(String.valueOf(project.getTotalBudget()));
        budgetField.setPromptText("Enter new budget");
        styleTextField(budgetField);

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: #e53935; -fx-font-size: 12px;");

        Button saveBtn = new Button("Save");
        saveBtn.setPrefWidth(316);
        styleActionButton(saveBtn);
        saveBtn.setOnAction(e -> {
            try {
                double newBudget = Double.parseDouble(budgetField.getText().trim());
                project.setTotalBudget(newBudget);
                projectsController.refreshProjectCard(project);
                popup.close();
                refreshPage();
            } catch (NumberFormatException ex) {
                errorLabel.setText("Budget must be a valid number.");
            }
        });

        content.getChildren().addAll(titleLabel, budgetField, errorLabel, saveBtn);
        popup.setScene(new Scene(content));
        popup.showAndWait();
    }

    @FXML
    private void handleDeleteProject() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete project \"" + project.getProjectName() + "\"? This cannot be undone.",
                ButtonType.YES, ButtonType.NO);
        alert.setTitle("Delete Project");
        alert.setHeaderText(null);
        alert.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                projectsController.removeProject(project);
                navigateToProjects();
            }
        });
    }

    // ── NAVIGATION ───────────────────────────────────────────────────────────────

    @FXML
    private void handleGoToProjects() {
        navigateToProjects();
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Stage stage = (Stage) projectNameLabel.getScene().getWindow();
            stage.getScene().setRoot(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToProjects() {
        try {
            Stage stage = (Stage) projectNameLabel.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("projects.fxml"));
            javafx.scene.Parent root = loader.load();
            ProjectsController ctrl = loader.getController();
            ctrl.restoreFrom(projectsController);
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ── HELPERS ──────────────────────────────────────────────────────────────────

    private void styleTextField(TextField field) {
        field.setStyle("-fx-font-size: 13px; -fx-padding: 10 14 10 14; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #e0e0e0;");
        field.setPrefWidth(356);
    }

    private void styleActionButton(Button btn) {
        btn.setStyle("-fx-background-color: #299D91; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: 700; -fx-padding: 12 0 12 0; -fx-background-radius: 8; -fx-cursor: hand;");
    }
}
