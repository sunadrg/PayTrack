package com.linkable.paytrack;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int PAGE_SIZE = 10;
    
    private RecyclerView recyclerView;
    private PaymentAdapter paymentAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PaymentRepository paymentRepository;
    
    private LinearLayout networkStatusBanner;
    private ImageView ivNetworkStatus;
    private TextView tvNetworkStatus;
    private ImageView ivCloseBanner;
    private boolean isBannerDismissed = false;
    
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;
    
    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean hasMoreData = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        paymentRepository = new PaymentRepository(this);

        networkStatusBanner = findViewById(R.id.networkStatusBanner);
        ivNetworkStatus = findViewById(R.id.ivNetworkStatus);
        tvNetworkStatus = findViewById(R.id.tvNetworkStatus);
        ivCloseBanner = findViewById(R.id.ivCloseBanner);
        
        ivCloseBanner.setOnClickListener(v -> {
            isBannerDismissed = true;
            networkStatusBanner.setVisibility(View.GONE);
        });

        setupNetworkMonitoring();

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isBannerDismissed = false;
                
                if (isNetworkAvailable()) {
                    showOnlineStatus(false); // Don't auto-hide for manual refresh
                    fetchPaymentsFromApi(true); // true for refresh
                } else {
                    showOfflineStatus();
                    loadPaymentsFromDatabase();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Set up adapter
        paymentAdapter = new PaymentAdapter(new ArrayList<>());
        recyclerView.setAdapter(paymentAdapter);
        
        // scroll listener
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                    
                    if (!isLoading && hasMoreData) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                                && firstVisibleItemPosition >= 0
                                && totalItemCount >= PAGE_SIZE) {
                            loadMorePayments();
                        }
                    }
                }
            }
        });
        
        // Check network connectivity a
        if (isNetworkAvailable()) {
            fetchPaymentsFromApi(true);
        } else {
            // local database when offline
            showOfflineStatus();
            loadPaymentsFromDatabase();
            checkLocalDataAvailability();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (paymentRepository != null) {
            paymentRepository.shutdown();
        }
        if (connectivityManager != null && networkCallback != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
    }

    private void fetchPaymentsFromApi(boolean isRefresh) {
        if (isRefresh) {
            currentPage = 1;
            hasMoreData = true;
        }
        
        if (isLoading) return;
        
        isLoading = true;
        if (!isRefresh) {
            paymentAdapter.setLoading(true);
        }
        
        ApiService apiService = RetrofitClient.getInstance(this).getApiService();
        
        Call<PaymentResponse> call = apiService.getPayments(currentPage, PAGE_SIZE);
        call.enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                swipeRefreshLayout.setRefreshing(false);
                isLoading = false;
                paymentAdapter.setLoading(false);

                hideNetworkBanner();
                
                if (response.isSuccessful() && response.body() != null) {
                    PaymentResponse paymentResponse = response.body();
                    List<Payment> payments = paymentResponse.getData();
                    PaymentResponse.PaginationInfo pagination = paymentResponse.getPagination();
                    
                    Log.d(TAG, "API Response: " + payments.size() + " payments received for page " + currentPage);
                    
                    if (isRefresh) {
                        paymentAdapter.updatePayments(payments);
                        // Save to local database
                        paymentRepository.refreshPayments(payments);
                    } else {
                        paymentAdapter.addPayments(payments);
                        // Save to local database
                        paymentRepository.savePayments(payments);
                    }

                    hasMoreData = pagination.hasNextPage();
                    currentPage = pagination.getCurrentPage();
                    
                    if (payments.isEmpty() && isRefresh) {
                        Toast.makeText(MainActivity.this, "No payments found", Toast.LENGTH_SHORT).show();
                    } else if (!isRefresh && payments.size() > 0) {
                        Toast.makeText(MainActivity.this, 
                            "Loaded " + payments.size() + " more payments (Page " + currentPage + " of " + pagination.getTotalPages() + ")", 
                            Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "API Error: " + response.code() + " - " + response.message());
                    String errorMessage = getErrorMessage(response.code());
                    Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    
                    // load from database
                    if (isRefresh) {
                        loadPaymentsFromDatabase();
                    }
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                isLoading = false;
                paymentAdapter.setLoading(false);

                hideNetworkBanner();
                
                Log.e(TAG, "Network Error: " + t.getMessage());
                String errorMessage = getNetworkErrorMessage(t);
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();

                if (isRefresh) {
                    loadPaymentsFromDatabase();
                }
            }
        });
    }

    private void loadMorePayments() {
        if (!isLoading && hasMoreData && isNetworkAvailable()) {
            currentPage++;
            fetchPaymentsFromApi(false); // false for load more
        }
    }

    private String getErrorMessage(int responseCode) {
        switch (responseCode) {
            case 404:
                return "Server not found. Please check the API endpoint.";
            case 500:
                return "Server error. Please try again later.";
            case 401:
                return "Unauthorized access. Please check your credentials.";
            case 403:
                return "Access forbidden. Please contact administrator.";
            default:
                return "Error connecting to server. Please try again.";
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    private String getNetworkErrorMessage(Throwable t) {
        if (t instanceof java.net.SocketTimeoutException) {
            return "Connection timeout. Please check your internet connection and try again.";
        } else if (t instanceof java.net.UnknownHostException) {
            return "Cannot connect to server. Please check your internet connection.";
        } else if (t instanceof java.net.ConnectException) {
            return "Failed to connect to server. Please try again later.";
        } else if (t instanceof javax.net.ssl.SSLException) {
            return "Security connection error. Please try again.";
        } else {
            return "Network connection error. Please check your internet and try again.";
        }
    }

    private void loadPaymentsFromDatabase() {
        paymentRepository.getAllPayments(new PaymentRepository.DatabaseCallback<List<Payment>>() {
            @Override
            public void onSuccess(List<Payment> payments) {
                runOnUiThread(() -> {
                    if (payments.isEmpty()) {
                        Toast.makeText(MainActivity.this, "No payments found in local database", Toast.LENGTH_SHORT).show();
                    } else {
                        paymentAdapter.updatePayments(payments);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Error loading from database: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void checkLocalDataAvailability() {
        paymentRepository.hasData(new PaymentRepository.DatabaseCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean hasData) {
                runOnUiThread(() -> {
                    if (!hasData && !isNetworkAvailable()) {
                        Toast.makeText(MainActivity.this, "No data available offline. Please connect to internet first.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Error checking local data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void setupNetworkMonitoring() {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        
        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                runOnUiThread(() -> {
                    Log.d(TAG, "Network available");
                    showOnlineStatus();
                    if (!isBannerDismissed) {
                        hideNetworkBanner();
                    }
                });
            }

            @Override
            public void onLost(Network network) {
                runOnUiThread(() -> {
                    Log.d(TAG, "Network lost");
                    showOfflineStatus();
                });
            }
        };

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();
        
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    private void showOfflineStatus() {
        if (!isBannerDismissed) {
            networkStatusBanner.setBackgroundResource(android.R.color.holo_orange_light);
            ivNetworkStatus.setImageResource(R.drawable.ic_wifi_off);
            tvNetworkStatus.setText("Device offline, displaying cached data");
            networkStatusBanner.setVisibility(View.VISIBLE);
        }
    }

    private void showOnlineStatus() {
        showOnlineStatus(true); // Default behavior with auto-hide
    }

    private void showOnlineStatus(boolean autoHide) {
        networkStatusBanner.setBackgroundResource(android.R.color.holo_green_light);
        ivNetworkStatus.setImageResource(R.drawable.ic_wifi);
        tvNetworkStatus.setText("Device online, syncing data");
        networkStatusBanner.setVisibility(View.VISIBLE);

        if (autoHide) {
            networkStatusBanner.postDelayed(this::hideNetworkBanner, 3000);
        }
    }

    private void hideNetworkBanner() {
        networkStatusBanner.setVisibility(View.GONE);
    }
}