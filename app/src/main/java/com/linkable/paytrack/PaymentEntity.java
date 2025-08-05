package com.linkable.paytrack;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "payments")
public class PaymentEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String serialNumber;
    private String amount;
    private String receivedDate;
    private String receivedThrough;
    private String description;
    private String status;
    private long timestamp;

    public PaymentEntity(String serialNumber, String amount, String receivedDate, 
                        String receivedThrough, String description, String status) {
        this.serialNumber = serialNumber;
        this.amount = amount;
        this.receivedDate = receivedDate;
        this.receivedThrough = receivedThrough;
        this.description = description;
        this.status = status;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters
    public int getId() { return id; }
    public String getSerialNumber() { return serialNumber; }
    public String getAmount() { return amount; }
    public String getReceivedDate() { return receivedDate; }
    public String getReceivedThrough() { return receivedThrough; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public long getTimestamp() { return timestamp; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    public void setAmount(String amount) { this.amount = amount; }
    public void setReceivedDate(String receivedDate) { this.receivedDate = receivedDate; }
    public void setReceivedThrough(String receivedThrough) { this.receivedThrough = receivedThrough; }
    public void setDescription(String description) { this.description = description; }
    public void setStatus(String status) { this.status = status; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public Payment toPayment() {
        return new Payment(serialNumber, amount, receivedDate, receivedThrough, description, status);
    }

    public static PaymentEntity fromPayment(Payment payment) {
        return new PaymentEntity(
            payment.getSerialNumber(),
            payment.getAmount(),
            payment.getReceivedDate(),
            payment.getReceivedThrough(),
            payment.getDescription(),
            payment.getStatus()
        );
    }
} 