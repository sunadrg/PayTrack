# 💰 PayTrack

**PayTrack** is a modern Android application designed to help users **track and manage their payment transactions** with an **offline-first architecture** and a **seamless user experience**.

---

## 🚀 Features

### 📄 Payment Management
- View payment transactions with detailed info:
  - Transaction ID
  - Amount
  - Payment method
  - Date & time
  - Status (Completed / Pending)
- Real-time status indicators with **color-coded badges**
- **Formatted currency** display and **readable date/time** formatting

### 📶 Offline-First Architecture
- **Local SQLite database** using Room persistence library for offline data access
- Automatic data synchronization when connectivity is restored
- Smooth transition between **online** and **offline** modes
- Cached payment history available **without internet connection**

### 🌐 Network Status Awareness
- Real-time **network connectivity monitoring**
- Smart status banner showing current network state
- Automatic fallback to cached data when offline
- **Pull-to-refresh** with network status feedback

### 🎨 Modern UI/UX
- **Material Design 3** components with a clean, intuitive interface
- Responsive layout using **SDP/SSP** for consistent sizing across devices
- Smooth animations and transitions
- Pull-to-refresh with loading indicators

### 🔌 API Integration
- RESTful API integration using **Retrofit** + **OkHttp**
- **Pagination support** for efficient data loading
- Comprehensive **error handling** and user feedback
- Automatic retry mechanisms with graceful degradation

---

## 🛠 Technical Stack

| Layer         | Technology |
|---------------|------------|
| **Language**  | Java |
| **Architecture** | MVVM + Repository pattern |
| **Database**  | Room persistence library (SQLite) |
| **Networking**| Retrofit 2.9.0 + OkHttp |
| **UI**        | Material Design, RecyclerView, SwipeRefreshLayout |
| **Build**     | Gradle with Version Catalogs |
| **Min SDK**   | Android 7.0 (API 24) |
| **Target SDK**| Android 15 (API 35) |

---

## 🖥 Server-Side Specifications

| Component       | Specification |
|-----------------|---------------|
| **Language**    | PHP 7.4+ |
| **Database**    | MySQL 5.7+ |
| **API**         | RESTful endpoints with JSON responses |
| **Security**    | Prepared statements for SQL injection prevention |
| **Pagination**  | Server-side pagination with metadata |
| **Error Handling** | Comprehensive HTTP status codes & descriptive error messages |

---

## 📱 Key Components

- **PaymentAdapter** → Handles list display with pagination & loading states  
- **PaymentRepository** → Manages data between local DB & remote API  
- **NetworkCallback** → Real-time network connectivity monitoring  
- **Room Database** → Local data persistence with schema management  

---

## 💡 Use Cases
Perfect for:
- Businesses tracking **real-time payments**
- Users needing **offline payment history access**
- Monitoring payment statuses with visual indicators
- Maintaining **data consistency** across network changes

---

## 📷 Screenshots

<p align="center">
  <img src="https://github.com/sunadrg/PayTrack/blob/main/Screenshots/Screenshot_20250805_211953.png" width="30%" />
  <img src="https://github.com/sunadrg/PayTrack/blob/main/Screenshots/Screenshot_20250805_212049.png" width="30%" />
  <img src="https://github.com/sunadrg/PayTrack/blob/main/Screenshots/Screenshot_20250805_213040.png" width="30%" />
</p> 

--

## 🌐 Backend Simulation / Admin Panel

A simple **PHP + MySQL** webpage has been developed to simulate backend operations for PayTrack.  
This panel allows adding, editing, and deleting payment records, enabling the app to display **real-time updates**.

## 🌐 Backend Simulation / Admin Panel

A simple **PHP + MySQL** webpage has been developed to simulate backend operations for PayTrack.  
This panel allows adding, editing, and deleting payment records, enabling the app to display **real-time updates**.

🔗 **Live Demo:** [Open Admin Panel](https://your-domain.com/paytrack-admin)  
📦 **Backend Source Code:** [View on GitHub](https://github.com/sunadrg/paytrack-backend)  

### 🔹 Features
- Add new payment records
- Edit existing transactions
- Delete payments from the database
- Real-time sync with the PayTrack Android app

### 🔹 Tech Stack
- **Language:** PHP 7.4+
- **Database:** MySQL 5.7+
- **UI:** Simple HTML + CSS + JavaScript
- **Security:** Prepared statements to prevent SQL injection



