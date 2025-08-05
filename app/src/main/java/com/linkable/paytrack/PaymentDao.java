package com.linkable.paytrack;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface PaymentDao {
    
    @Query("SELECT * FROM payments ORDER BY timestamp DESC")
    List<PaymentEntity> getAllPayments();
    
    @Query("SELECT * FROM payments ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    List<PaymentEntity> getPaymentsWithPagination(int limit, int offset);
    
    @Query("SELECT COUNT(*) FROM payments")
    int getPaymentCount();
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPayment(PaymentEntity payment);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPayments(List<PaymentEntity> payments);
    
    @Query("DELETE FROM payments")
    void deleteAllPayments();
    
    @Transaction
    default void refreshPayments(List<PaymentEntity> newPayments) {
        deleteAllPayments();
        insertPayments(newPayments);
    }
} 