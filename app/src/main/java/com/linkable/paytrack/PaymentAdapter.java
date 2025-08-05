package com.linkable.paytrack;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PaymentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_PAYMENT = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private List<Payment> paymentList;
    private boolean isLoading = false;

    public PaymentAdapter(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_PAYMENT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_payment, parent, false);
            return new PaymentViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PaymentViewHolder) {
            PaymentViewHolder paymentHolder = (PaymentViewHolder) holder;
            if (position < paymentList.size()) {
                Payment payment = paymentList.get(position);
                
                paymentHolder.tvSerialNumber.setText(payment.getSerialNumber());
                paymentHolder.tvAmount.setText(formatAmount(payment.getAmount()));
                paymentHolder.tvDescription.setText(payment.getDescription());
                paymentHolder.tvPaymentMethod.setText(payment.getReceivedThrough());
                paymentHolder.tvDate.setText(formatDate(payment.getReceivedDate()));
                paymentHolder.tvStatus.setText(payment.getStatus().toUpperCase());
                
                // Set status background color based on status
                if ("completed".equalsIgnoreCase(payment.getStatus())) {
                    paymentHolder.tvStatus.setBackgroundResource(R.drawable.status_completed_background);
                } else if ("pending".equalsIgnoreCase(payment.getStatus())) {
                    paymentHolder.tvStatus.setBackgroundResource(R.drawable.status_pending_background);
                } else {
                    // Default background for other statuses
                    paymentHolder.tvStatus.setBackgroundResource(R.drawable.status_completed_background);
                }
            }
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingHolder = (LoadingViewHolder) holder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoading && position == paymentList.size()) {
            return VIEW_TYPE_LOADING;
        }
        return VIEW_TYPE_PAYMENT;
    }

    @Override
    public int getItemCount() {
        return paymentList.size() + (isLoading ? 1 : 0);
    }

    public void addPayments(List<Payment> newPayments) {
        int startPosition = paymentList.size();
        paymentList.addAll(newPayments);
        notifyItemRangeInserted(startPosition, newPayments.size());
    }

    public void updatePayments(List<Payment> newPayments) {
        this.paymentList = new ArrayList<>(newPayments);
        notifyDataSetChanged();
    }

    public void setLoading(boolean loading) {
        if (this.isLoading != loading) {
            this.isLoading = loading;
            if (loading) {
                notifyItemInserted(paymentList.size());
            } else {
                notifyItemRemoved(paymentList.size());
            }
        }
    }

    public boolean isLoading() {
        return isLoading;
    }

    private String formatAmount(String amount) {
        try {
            double value = Double.parseDouble(amount);
            return String.format("%,.2f", value);
        } catch (NumberFormatException e) {
            return amount;
        }
    }

    private String formatDate(String dateString) {
        try {
            String[] possibleFormats = {
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd'T'HH:mm:ss",
                "yyyy-MM-dd HH:mm",
                "yyyy-MM-dd",
                "dd/MM/yyyy HH:mm:ss",
                "dd/MM/yyyy HH:mm",
                "dd/MM/yyyy"
            };
            
            for (String format : possibleFormats) {
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat(format, Locale.getDefault());
                    Date date = inputFormat.parse(dateString);
                    
                    if (date != null) {
                        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM d, yyyy • h:mm a", Locale.getDefault());
                        return outputFormat.format(date);
                    }
                } catch (ParseException e) {
                    continue;
                }
            }

            return dateString;
            
        } catch (Exception e) {
            return dateString;
        }
    }

    static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView tvSerialNumber, tvAmount, tvDescription, tvPaymentMethod, tvDate, tvStatus;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSerialNumber = itemView.findViewById(R.id.tvSerialNumber);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPaymentMethod = itemView.findViewById(R.id.tvPaymentMethod);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        TextView tvLoading;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
            tvLoading = itemView.findViewById(R.id.tvLoading);
        }
    }
} 