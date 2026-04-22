package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private String projectName;
    private double totalBudget;
    private List<Pot> listOfPots;
    private List<Transaction> listOfTransactions;

    public Project(String projectName, double totalBudget) {
        this.projectName = projectName;
        this.totalBudget = totalBudget;
        this.listOfPots = new ArrayList<>();
        this.listOfTransactions = new ArrayList<>();
    }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public double getTotalBudget() { return totalBudget; }
    public void setTotalBudget(double totalBudget) { this.totalBudget = totalBudget; }

    public List<Pot> getListOfPots() { return listOfPots; }
    public List<Transaction> getListOfTransactions() { return listOfTransactions; }

    public double getTotalSpent() {
        return listOfTransactions.stream()
                .filter(Transaction::isExpense)
                .mapToDouble(Transaction::getTransactionValue)
                .sum();
    }

    public double getTotalRemaining() {
        return totalBudget - getTotalSpent();
    }

    public double getTotalAllocated() {
        return listOfPots.stream().mapToDouble(Pot::getAllocatedAmount).sum();
    }

    public void addPot(Pot pot) { listOfPots.add(pot); }
    public void removePot(Pot pot) { listOfPots.remove(pot); }

    public void addTransaction(Transaction t) {
        listOfTransactions.add(t);
        // Update pot spent amount
        for (Pot pot : listOfPots) {
            if (pot.getPotName().equals(t.getPotName()) && t.isExpense()) {
                pot.setSpent(pot.getSpent() + t.getTransactionValue());
            }
        }
    }

    public void removeTransaction(Transaction t) {
        listOfTransactions.remove(t);
        // Revert pot spent amount
        for (Pot pot : listOfPots) {
            if (pot.getPotName().equals(t.getPotName()) && t.isExpense()) {
                pot.setSpent(Math.max(0, pot.getSpent() - t.getTransactionValue()));
            }
        }
    }
}
