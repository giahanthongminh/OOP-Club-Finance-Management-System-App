package com.example.demo;

public class Pot {
    private String potName;
    private double allocatedAmount;
    private double spent;

    public Pot(String potName, double allocatedAmount) {
        this.potName = potName;
        this.allocatedAmount = allocatedAmount;
        this.spent = 0;
    }

    public String getPotName() { return potName; }
    public void setPotName(String potName) { this.potName = potName; }

    public double getAllocatedAmount() { return allocatedAmount; }
    public void setAllocatedAmount(double allocatedAmount) { this.allocatedAmount = allocatedAmount; }

    public double getSpent() { return spent; }
    public void setSpent(double spent) { this.spent = spent; }

    public double getRemaining() { return allocatedAmount - spent; }

    public double getPercentAllocated(double projectBudget) {
        if (projectBudget <= 0) return 0;
        return (allocatedAmount / projectBudget) * 100;
    }
}
