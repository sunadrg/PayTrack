package com.linkable.paytrack;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("fetch_transactions.php")
    Call<PaymentResponse> getPayments(@Query("page") int page, @Query("limit") int limit);
} 