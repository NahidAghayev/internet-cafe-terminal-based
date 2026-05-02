CREATE TYPE user_role AS ENUM ('ADMIN', 'CUSTOMER');
CREATE TYPE station_status AS ENUM ('AVAILABLE', 'IN_USE', 'MAINTENANCE');
CREATE TYPE transaction_type AS ENUM ('TOP_UP', 'DEDUCTION');

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(64) NOT NULL,
    role user_role NOT NULL DEFAULT 'CUSTOMER',
    balance NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE stations (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    specs TEXT,
    status station_status NOT NULL DEFAULT 'AVAILABLE',
    hourly_rate NUMERIC(10, 2) NOT NULL
);

CREATE TABLE sessions (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id),
    station_id INT NOT NULL REFERENCES stations(id),
    start_time TIMESTAMP NOT NULL DEFAULT NOW(),
    end_time TIMESTAMP,
    cost NUMERIC(10, 2)
);

CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id),
    amount NUMERIC(10, 2) NOT NULL,
    type transaction_type NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);
