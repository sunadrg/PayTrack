package com.linkable.paytrack;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PaymentEntity.class}, version = 1, exportSchema = false)
public abstract class PaymentDatabase extends RoomDatabase {
    
    private static final String DATABASE_NAME = "payment_database";
    private static PaymentDatabase instance;
    
    public abstract PaymentDao paymentDao();
    
    public static synchronized PaymentDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.getApplicationContext(),
                PaymentDatabase.class,
                DATABASE_NAME
            ).build();
        }
        return instance;
    }
} 