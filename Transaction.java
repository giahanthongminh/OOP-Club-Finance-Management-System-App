package com.example.demo;

public class Transaction {
    private String transactionType;   // "expense" or "income"
    private String transactionName;   // person in charge
    private double transactionValue;
    private String paymentMethod;
    private String date;              // YYYY/MM/DD
    private String description;       // notes
    private String potName;           // linked pot name
    private String proof;             // attachment file path

    public Transaction(String transactionType, String transactionName, double transactionValue,
                       String paymentMethod, String date, String description, String potName, String proof) {
        this.transactionType = transactionType;
        this.transactionName = transactionName;
        this.transactionValue = transactionValue;
        this.paymentMethod = paymentMethod;
        this.date = date;
        this.description = description;
        this.potName = potName;
        this.proof = proof;
    }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public String getTransactionName() { return transactionName; }
    public void setTransactionName(String transactionName) { this.transactionName = transactionName; }

    public double getTransactionValue() { return transactionValue; }
    public void setTransactionValue(double transactionValue) { this.transactionValue = transactionValue; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPotName() { return potName; }
    public void setPotName(String potName) { this.potName = potName; }

    public String getProof() { return proof; }
    public void setProof(String proof) { this.proof = proof; }

    public boolean isExpense() { return "expense".equalsIgnoreCase(transactionType); }
}
