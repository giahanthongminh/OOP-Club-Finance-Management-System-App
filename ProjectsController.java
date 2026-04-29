package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;


import java.util.ArrayList;
import java.util.List;

public class ProjectsController {

    @FXML private Label usernameLabel;
    @FXML private Label sidebarUsername;
    @FXML private Label totalBalanceLabel;
    @FXML private Label totalSpentLabel;
    @FXML private Label remainingBalanceLabel;
    @FXML private Button createProjectBtn;
    @FXML private FlowPane projectsGrid;

    private String username = "";
    private double totalBalance = 0;
    private final List<Project> projects = new ArrayList<>();

    public void setUsername(String username) {
        this.username = username;
        if (usernameLabel != null) usernameLabel.setText(username);
        if (sidebarUsername != null) sidebarUsername.setText(username);
    }

    // Restore state after returning from ProjectPage
    public void restoreFrom(ProjectsController source) {
        this.username = source.username;
        this.totalBalance = source.totalBalance;
        this.projects.addAll(source.projects);
        if (usernameLabel != null) usernameLabel.setText(username);
        if (sidebarUsername != null) sidebarUsername.setText(username);
        totalBalanceLabel.setText(String.valueOf(totalBalance));
        updateSpentAndRemaining();
        rebuildAllCards();
    }

    private void updateSpentAndRemaining() {
        double spent = projects.stream().mapToDouble(Project::getTotalSpent).sum();
        double remaining = totalBalance - spent;
        totalSpentLabel.setText(String.format("%.2f", spent));
        remainingBalanceLabel.setText(String.format("%.2f", remaining));
    }

    private void rebuildAllCards() {
        projectsGrid.getChildren().clear();
        for (Project p : projects) {
            projectsGrid.getChildren().add(buildCard(p));
        }
    }

    public void refreshProjectCard(Project project) {
        rebuildAllCards();
    }

    public void removeProject(Project project) {
        projects.remove(project);
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
            try {
                totalBalance = Double.parseDouble(balField.getText().trim());
                totalBalanceLabel.setText(String.format("%.2f", totalBalance));
                updateSpentAndRemaining();
            } catch (NumberFormatException ignored) {}
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

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: #e53935; -fx-font-size: 12px;");

        Button saveBtn = new Button("Save");
        saveBtn.setPrefWidth(336);
        saveBtn.setStyle("-fx-background-color: #299D91; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: 700; -fx-padding: 12 0 12 0; -fx-background-radius: 8;");
        saveBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                errorLabel.setText("Project name is required.");
                return;
            }
            double budget = 0;
            if (!budgetField.getText().trim().isEmpty()) {
                try {
                    budget = Double.parseDouble(budgetField.getText().trim());
                } catch (NumberFormatException ex) {
                    errorLabel.setText("Budget must be a valid number.");
                    return;
                }
            }
            Project project = new Project(name, budget);
            projects.add(project);
            projectsGrid.getChildren().add(buildCard(project));
            popup.close();
        });

        content.getChildren().addAll(title, nameLabel, nameField, budgetLabel, budgetField, errorLabel, saveBtn);
        popup.setScene(new Scene(content));
        popup.showAndWait();
    }

    private VBox buildCard(Project project) {
        VBox card = new VBox(12);
        card.setPrefWidth(320);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20; -fx-cursor: hand;");

        Label projectName = new Label(project.getProjectName());
        projectName.setStyle("-fx-font-size: 16px; -fx-font-weight: 700; -fx-text-fill: #191919;");

        GridPane stats = new GridPane();
        stats.setHgap(40);
        stats.setVgap(6);
        addStatRow(stats, "Total Balance", String.format("$%.2f", project.getTotalBudget()), 0);
        addStatRow(stats, "Total Spent", String.format("$%.2f", project.getTotalSpent()), 1);
        addStatRow(stats, "Budget Pots", String.valueOf(project.getListOfPots().size()), 2);
        addStatRow(stats, "Transactions", String.valueOf(project.getListOfTransactions().size()), 3);

        Button openBtn = new Button("Open Project →");
        openBtn.setStyle("-fx-background-color: #299D91; -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: 600; -fx-padding: 8 16 8 16; -fx-background-radius: 8; -fx-cursor: hand;");
        openBtn.setMaxWidth(Double.MAX_VALUE);
        openBtn.setOnAction(e -> openProjectPage(project));

        card.getChildren().addAll(projectName, stats, openBtn);

        return card;
    }

    private void openProjectPage(Project project) {
        try {
            java.net.URL url = getClass().getResource("projectPage.fxml");
            if (url == null) {
                new Alert(Alert.AlertType.ERROR, "Cannot find projectPage.fxml.\nClass location: " + getClass().getProtectionDomain().getCodeSource().getLocation(), ButtonType.OK).showAndWait();
                return;
            }
            FXMLLoader loader = new FXMLLoader(url);
            javafx.scene.Parent root = loader.load();
            ProjectPageController ctrl = loader.getController();
            ctrl.setProject(project, username, this);
            Stage stage = (Stage) createProjectBtn.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Navigation error:\n" + e.getClass().getSimpleName() + ": " + e.getMessage(), ButtonType.OK).showAndWait();
        }
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
    private void handleGoToTransactions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("transactionPage.fxml"));
            Parent root = loader.load();

            TransactionPageController ctrl = loader.getController();
            ctrl.setContext(null, username, this);

            Stage stage = (Stage) createProjectBtn.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Navigation error:\n" + e.getClass().getSimpleName() + ": " + e.getMessage(),
                    ButtonType.OK).showAndWait();
        }
    }

    @FXML
    private void handleLogout() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to log out?", ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Logout");
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                UserSession.clear();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
                    Stage stage = (Stage) createProjectBtn.getScene().getWindow();
                    stage.getScene().setRoot(loader.load());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void handleViewProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("profilePage.fxml"));
            Parent root = loader.load();
            ProfileController ctrl = loader.getController();
            ctrl.setContext(username, this);
            Stage stage = (Stage) createProjectBtn.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
