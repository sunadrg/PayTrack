package com.linkable.paytrack;

import com.google.gson.annotations.SerializedName;

public class Payment {
    @SerializedName("serial_number")
    private String serialNumber;
    
    @SerializedName("amount")
    private String amount;
    
    @SerializedName("received_date")
    private String receivedDate;
    
    @SerializedName("received_through")
    private String receivedThrough;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("status")
    private String status;

    public Payment(String serialNumber, String amount, String receivedDate, 
                   String receivedThrough, String description, String status) {
        this.serialNumber = serialNumber;
        this.amount = amount;
        this.receivedDate = receivedDate;
        this.receivedThrough = receivedThrough;
        this.description = description;
        this.status = status;
    }

    // Getters
    public String getSerialNumber() { return serialNumber; }
    public String getAmount() { return amount; }
    public String getReceivedDate() { return receivedDate; }
    public String getReceivedThrough() { return receivedThrough; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }

    // Setters
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    public void setAmount(String amount) { this.amount = amount; }
    public void setReceivedDate(String receivedDate) { this.receivedDate = receivedDate; }
    public void setReceivedThrough(String receivedThrough) { this.receivedThrough = receivedThrough; }
    public void setDescription(String description) { this.description = description; }
    public void setStatus(String status) { this.status = status; }
} 