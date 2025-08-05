package com.linkable.paytrack;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PaymentResponse {
    @SerializedName("data")
    private List<Payment> data;
    
    @SerializedName("pagination")
    private PaginationInfo pagination;

    public PaymentResponse(List<Payment> data, PaginationInfo pagination) {
        this.data = data;
        this.pagination = pagination;
    }

    public List<Payment> getData() {
        return data;
    }

    public PaginationInfo getPagination() {
        return pagination;
    }

    public static class PaginationInfo {
        @SerializedName("current_page")
        private int currentPage;
        
        @SerializedName("total_pages")
        private int totalPages;
        
        @SerializedName("total_records")
        private int totalRecords;
        
        @SerializedName("limit")
        private int limit;
        
        @SerializedName("has_next_page")
        private boolean hasNextPage;
        
        @SerializedName("has_prev_page")
        private boolean hasPrevPage;

        public PaginationInfo(int currentPage, int totalPages, int totalRecords, 
                             int limit, boolean hasNextPage, boolean hasPrevPage) {
            this.currentPage = currentPage;
            this.totalPages = totalPages;
            this.totalRecords = totalRecords;
            this.limit = limit;
            this.hasNextPage = hasNextPage;
            this.hasPrevPage = hasPrevPage;
        }

        // Getters
        public int getCurrentPage() { return currentPage; }
        public int getTotalPages() { return totalPages; }
        public int getTotalRecords() { return totalRecords; }
        public int getLimit() { return limit; }
        public boolean hasNextPage() { return hasNextPage; }
        public boolean hasPrevPage() { return hasPrevPage; }


    }
} 