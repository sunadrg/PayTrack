# Paytrack - Android Payment Tracking App

A modern Android application for tracking payment transactions with offline-first functionality and PHP/MySQL backend integration.

## 📱 Features

- **Payment Management**: View transactions with detailed information and status indicators
- **Offline-First**: Local SQLite database with automatic synchronization
- **Network Status**: Real-time connectivity monitoring with status banners
- **Pagination**: Efficient data loading with pull-to-refresh
- **Modern UI**: Material Design 3 with responsive layout

## 🏗️ Project Structure

```
Paytrack/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/linkable/paytrack/
│   │   │   │   ├── MainActivity.java                 # Main activity with payment list
│   │   │   │   ├── splash_screen.java               # Splash screen activity
│   │   │   │   ├── Payment.java                     # Payment data model
│   │   │   │   ├── PaymentResponse.java             # API response wrapper
│   │   │   │   ├── PaymentAdapter.java              # RecyclerView adapter
│   │   │   │   ├── ApiService.java                  # Retrofit API interface
│   │   │   │   ├── RetrofitClient.java              # HTTP client configuration
│   │   │   │   ├── PaymentEntity.java               # Room database entity
│   │   │   │   ├── PaymentDao.java                  # Database access object
│   │   │   │   ├── PaymentDatabase.java             # Room database class
│   │   │   │   └── PaymentRepository.java           # Data repository
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_main.xml            # Main activity layout
│   │   │   │   │   ├── activity_splash_screen.xml   # Splash screen layout
│   │   │   │   │   ├── item_payment.xml             # Payment item layout
│   │   │   │   │   └── item_loading.xml             # Loading footer layout
│   │   │   │   ├── drawable/
│   │   │   │   │   ├── ic_wifi.xml                  # WiFi icon
│   │   │   │   │   ├── ic_wifi_off.xml              # WiFi off icon
│   │   │   │   │   ├── ic_close.xml                 # Close icon
│   │   │   │   │   ├── ic_payment_method.xml        # Payment method icon
│   │   │   │   │   ├── ic_calendar.xml              # Calendar icon
│   │   │   │   │   ├── status_completed_background.xml # Completed status background
│   │   │   │   │   ├── status_pending_background.xml  # Pending status background
│   │   │   │   │   └── pulse_animation.xml          # Animated spinner
│   │   │   │   ├── values/
│   │   │   │   │   ├── strings.xml                  # String resources
│   │   │   │   │   ├── colors.xml                   # Color definitions
│   │   │   │   │   └── themes.xml                   # App themes
│   │   │   │   └── xml/
│   │   │   │       ├── network_security_config.xml  # Network security config
│   │   │   │       ├── data_extraction_rules.xml    # Data extraction rules
│   │   │   │       └── backup_rules.xml             # Backup rules
│   │   │   └── AndroidManifest.xml                  # App manifest
│   │   ├── test/                                     # Unit tests
│   │   └── androidTest/                              # Instrumented tests
│   ├── build.gradle                                  # App-level build config
│   └── proguard-rules.pro                           # ProGuard rules
├── gradle/
│   └── libs.versions.toml                           # Dependency versions
├── gradle.properties                                 # Gradle properties
├── build.gradle                                      # Project-level build config
├── settings.gradle                                   # Project settings
├── fetch_transactions.php                            # PHP API endpoint
└── README.md                                         # This file
```

## 🛠️ Technical Stack

### Frontend (Android)
- **Language**: Java
- **Architecture**: MVVM with Repository pattern
- **Database**: Room persistence library with SQLite
- **Networking**: Retrofit 2.9.0 with OkHttp
- **UI Components**: Material Design, RecyclerView, SwipeRefreshLayout
- **Build System**: Gradle with version catalogs

### Backend (PHP/MySQL)
- **Server Language**: PHP 7.4+
- **Database**: MySQL 5.7+
- **API Structure**: RESTful endpoints with JSON responses
- **Security**: Prepared statements for SQL injection prevention

## 📋 Prerequisites

- Android Studio Arctic Fox or later
- Java Development Kit (JDK) 11 or later
- PHP 7.4+ with MySQL support
- MySQL 5.7+ database server
- Android device or emulator (API 24+)

## ⚙️ Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
cd Paytrack
```

### 2. Backend Setup (PHP/MySQL)

#### Database Configuration
1. Create a MySQL database named `test`
2. Create the `transaction_records` table:
```sql
CREATE TABLE transaction_records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    serial_number VARCHAR(50) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    received_date DATETIME NOT NULL,
    received_through VARCHAR(100) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL
);
```

#### PHP Configuration
1. Update database credentials in `fetch_transactions.php`:
```php
$host = 'localhost';
$db   = 'test'; 
$user = 'your_username';     
$pass = 'your_password';
```

2. Deploy `fetch_transactions.php` to your web server

### 3. Android App Setup

#### Update API Endpoint
1. Open `app/src/main/res/values/strings.xml`
2. Update the API endpoint:
```xml
<string name="api_end_point">http://your-server.com/fetch_transactions.php</string>
```

#### Build and Run
1. Open the project in Android Studio
2. Sync Gradle files
3. Build the project
4. Run on device or emulator

## 🔧 Configuration

### Network Security
The app allows cleartext traffic for development. For production:
1. Update `network_security_config.xml`
2. Set `android:usesCleartextTraffic="false"` in AndroidManifest.xml
3. Use HTTPS endpoints

### Database Configuration
- **Local Database**: Room automatically manages SQLite database
- **Remote Database**: Update MySQL credentials in PHP file
- **Pagination**: Default 10 items per page, configurable in PHP

## 📱 Usage

### App Flow
1. **Splash Screen**: Shows app logo with loading spinner
2. **Main Screen**: Displays payment list with network status
3. **Offline Mode**: Shows cached data with offline banner
4. **Pull to Refresh**: Updates data and shows network status

### Features
- **Network Status Banner**: Shows online/offline status
- **Pagination**: Scroll to load more payments
- **Status Indicators**: Color-coded payment status
- **Date/Time Display**: Formatted date and time
- **Offline Support**: Works without internet connection

## 🧪 Testing

### Unit Tests
```bash
./gradlew test
```

### Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

## 📦 Dependencies

### Android Dependencies
- `androidx.appcompat:appcompat:1.7.1`
- `com.google.android.material:material:1.12.0`
- `androidx.room:room-runtime:2.6.1`
- `com.squareup.retrofit2:retrofit:2.9.0`
- `androidx.swiperefreshlayout:swiperefreshlayout:1.1.0`

### PHP Dependencies
- PHP 7.4+ with MySQL extension
- MySQL 5.7+ database

## 🔒 Security Considerations

- **SQL Injection**: Uses prepared statements in PHP
- **Network Security**: Configure HTTPS for production
- **Data Validation**: Input validation in both Android and PHP
- **Error Handling**: Comprehensive error handling and logging

## 🚀 Deployment

### Android App
1. Generate signed APK/Bundle
2. Upload to Google Play Store
3. Configure production API endpoints

### PHP Backend
1. Deploy to production web server
2. Configure SSL certificate
3. Set up database with proper permissions
4. Update Android app with production URL

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📞 Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the documentation

## 🔄 Version History

- **v1.0**: Initial release with basic payment tracking
- **v1.1**: Added offline support and network status
- **v1.2**: Implemented pagination and pull-to-refresh
- **v1.3**: Enhanced UI with Material Design 3

---

**Note**: This is a development version. For production use, ensure proper security configurations and testing. 