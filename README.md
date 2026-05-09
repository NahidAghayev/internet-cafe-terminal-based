# Internet Cafe Management System

A multi-module, terminal-based Java application for managing internet cafe operations, including user sessions, station management, and financial transactions.

## Features

### Customer Features
- **User Registration & Secure Login**: Passwords are encrypted using SHA-256.
- **Balance Management**: Top up balance and view transaction history.
- **Session Control**: Start and end sessions on available stations.
- **Automated Billing**: Real-time cost calculation based on station hourly rates.
- **History**: View past sessions and financial history.

### Admin Features
- **Station Management**: Add, update, delete, and monitor station statuses (AVAILABLE, IN_USE, MAINTENANCE).
- **System Monitoring**: View all active and past sessions across the entire system.
- **User Management**: View all registered users and their current balances.

## Prerequisites
- **Java 21** or higher.
- **PostgreSQL** database.
- **Maven 3.8+** for building.

## Setup Instructions

### 1. Database Setup
Create a PostgreSQL database named `internetcafe_db` (or your preferred name) and execute the `db.sql` script located in the project root:
```bash
psql -U your_user -d internetcafe_db -f db.sql
```

### 2. Build the Project
Use Maven to compile and package the application:
```bash
mvn clean install
```
This generates a "fat JAR" in the `presentation/target/` directory.

## Usage

Run the application by providing your database connection details as command-line arguments:

```bash
java -jar presentation/target/presentation-1.0-SNAPSHOT-jar-with-dependencies.jar "jdbc:postgresql://localhost:5432/internetcafe_db" "your_db_user" "your_db_password"
```

### Initial Admin Setup
By default, newly registered users have the `CUSTOMER` role. To perform administrative tasks:
1. Register a new user through the application.
2. Manually update their role in the database:
   ```sql
   UPDATE users SET role = 'ADMIN' WHERE username = 'your_username';
   ```

## Project Structure
- `domain`: Core entities, enums, repository interfaces, and custom exceptions.
- `data-access`: JDBC-based implementation of repositories with PostgreSQL-specific casting.
- `service`: Business logic, session management, and authentication services.
- `presentation`: Terminal-based UI (Console menus) and application entry point.
