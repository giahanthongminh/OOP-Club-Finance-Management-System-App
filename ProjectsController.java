package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProjectsController {

    @FXML private Label usernameLabel;
    @FXML private Label sidebarUsername;
    @FXML private Label totalBalanceLabel;
    @FXML private Label totalSpentLabel;
    @FXML private Label remainingBalanceLabel;
    @FXML private Button createProjectBtn;
    @FXML private FlowPane projectsGrid;

    public void setUsername(String username) {
        if (usernameLabel != null) usernameLabel.setText(username);
        if (sidebarUsername != null) sidebarUsername.setText(username);
    }

    @FXML
    private void handleEditBalance() {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Edit Balance");

        VBox content = new VBox(16);
        content.setStyle("-fx-padding: 32; -fx-background-color: white;");
        content.setPrefWidth(400);

        Label title = new Label("Total Balance");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: 700;");

        TextField balField = new TextField();
        balField.setPromptText("Enter your Total Balance here");
        balField.setStyle("-fx-font-size: 14px; -fx-padding: 10 14 10 14; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #e0e0e0;");

        Button saveBtn = new Button("Save");
        saveBtn.setPrefWidth(336);
        saveBtn.setStyle("-fx-background-color: #299D91; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: 700; -fx-padding: 12 0 12 0; -fx-background-radius: 8;");
        saveBtn.setOnAction(e -> {
            totalBalanceLabel.setText(balField.getText());
            // backend dev updates balance here
            popup.close();
        });

        content.getChildren().addAll(title, balField, saveBtn);
        popup.setScene(new Scene(content));
        popup.showAndWait();
    }

    @FXML
    private void handleCreateProject() {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Create Project");

        VBox content = new VBox(16);
        content.setStyle("-fx-padding: 32; -fx-background-color: white;");
        content.setPrefWidth(400);

        Label title = new Label("Create Project");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: 700;");

        Label nameLabel = new Label("Project Name");
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: 600;");
        TextField nameField = new TextField();
        nameField.setPromptText("Write project name here");
        nameField.setStyle("-fx-font-size: 14px; -fx-padding: 10 14 10 14; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #e0e0e0;");

        Label budgetLabel = new Label("Initial Budget");
        budgetLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: 600;");
        TextField budgetField = new TextField();
        budgetField.setPromptText("Write present amounts here");
        budgetField.setStyle("-fx-font-size: 14px; -fx-padding: 10 14 10 14; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #e0e0e0;");

        Button saveBtn = new Button("Save");
        saveBtn.setPrefWidth(336);
        saveBtn.setStyle("-fx-background-color: #299D91; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: 700; -fx-padding: 12 0 12 0; -fx-background-radius: 8;");
        saveBtn.setOnAction(e -> {
            if (!nameField.getText().isEmpty()) {
                addProjectCard(nameField.getText(), budgetField.getText());
                // backend dev stores project here
                popup.close();
            }
        });

        content.getChildren().addAll(title, nameLabel, nameField, budgetLabel, budgetField, saveBtn);
        popup.setScene(new Scene(content));
        popup.showAndWait();
    }

    private void addProjectCard(String name, String budget) {
        VBox card = new VBox(12);
        card.setPrefWidth(320);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20;");

        Label projectName = new Label(name);
        projectName.setStyle("-fx-font-size: 16px; -fx-font-weight: 700; -fx-text-fill: #191919;");

        GridPane stats = new GridPane();
        stats.setHgap(40);
        stats.setVgap(6);

        addStatRow(stats, "Total Balance", "0", 0);
        addStatRow(stats, "Total Spent", "0", 1);
        addStatRow(stats, "Budget Pots", "0", 2);
        addStatRow(stats, "Transactions", "0", 3);

        card.getChildren().addAll(projectName, stats);
        projectsGrid.getChildren().add(card);
    }

    private void addStatRow(GridPane grid, String label, String value, int row) {
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 13px; -fx-text-fill: #888888;");
        Label val = new Label(value);
        val.setStyle("-fx-font-size: 13px; -fx-text-fill: #191919;");
        grid.add(lbl, 0, row);
        grid.add(val, 1, row);
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/login.fxml"));
            Stage stage = (Stage) createProjectBtn.getScene().getWindow();
            stage.getScene().setRoot(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
