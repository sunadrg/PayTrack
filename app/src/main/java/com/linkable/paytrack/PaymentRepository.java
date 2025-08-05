package com.linkable.paytrack;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PaymentRepository {
    private static final String TAG = "PaymentRepository";
    private PaymentDao paymentDao;
    private ExecutorService executorService;
    
    public PaymentRepository(Context context) {
        PaymentDatabase database = PaymentDatabase.getInstance(context);
        paymentDao = database.paymentDao();
        executorService = Executors.newFixedThreadPool(4);
    }
    
    // Save payments to local database
    public void savePayments(List<Payment> payments) {
        executorService.execute(() -> {
            try {
                List<PaymentEntity> entities = new ArrayList<>();
                for (Payment payment : payments) {
                    entities.add(PaymentEntity.fromPayment(payment));
                }
                paymentDao.insertPayments(entities);
                Log.d(TAG, "Saved " + entities.size() + " payments to local database");
            } catch (Exception e) {
                Log.e(TAG, "Error saving payments to database: " + e.getMessage());
            }
        });
    }
    
    // Refresh
    public void refreshPayments(List<Payment> payments) {
        executorService.execute(() -> {
            try {
                List<PaymentEntity> entities = new ArrayList<>();
                for (Payment payment : payments) {
                    entities.add(PaymentEntity.fromPayment(payment));
                }
                paymentDao.refreshPayments(entities);
                Log.d(TAG, "Refreshed " + entities.size() + " payments in local database");
            } catch (Exception e) {
                Log.e(TAG, "Error refreshing payments in database: " + e.getMessage());
            }
        });
    }
    
    // Get
    public void getAllPayments(DatabaseCallback<List<Payment>> callback) {
        executorService.execute(() -> {
            try {
                List<PaymentEntity> entities = paymentDao.getAllPayments();
                List<Payment> payments = new ArrayList<>();
                for (PaymentEntity entity : entities) {
                    payments.add(entity.toPayment());
                }
                callback.onSuccess(payments);
                Log.d(TAG, "Retrieved " + payments.size() + " payments from local database");
            } catch (Exception e) {
                Log.e(TAG, "Error retrieving payments from database: " + e.getMessage());
                callback.onError(e);
            }
        });
    }

    // Check if database has any data
    public void hasData(DatabaseCallback<Boolean> callback) {
        executorService.execute(() -> {
            try {
                int count = paymentDao.getPaymentCount();
                callback.onSuccess(count > 0);
            } catch (Exception e) {
                Log.e(TAG, "Error checking database data: " + e.getMessage());
                callback.onError(e);
            }
        });
    }

    public interface DatabaseCallback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }

    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
} 