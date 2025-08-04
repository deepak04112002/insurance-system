-- Create database
CREATE DATABASE IF NOT EXISTS insurance_system;

-- Use the database
USE insurance_system;

-- Users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- PolicyHolder table
CREATE TABLE policy_holder (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    dob DATE,
    phone VARCHAR(20)
);

-- Policy table
CREATE TABLE policy (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    policy_number VARCHAR(255) UNIQUE,
    type VARCHAR(100),
    coverage_amount BIGINT,
    start_date DATE,
    end_date DATE,
    policy_holder_id BIGINT,
    FOREIGN KEY (policy_holder_id) REFERENCES policy_holder(id)
);

-- Claim table
CREATE TABLE claim (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    claim_number VARCHAR(255) UNIQUE,
    claim_date DATE,
    amount_claimed BIGINT,
    status VARCHAR(50),
    file_path VARCHAR(500),
    policy_id BIGINT,
    policy_holder_id BIGINT,
    FOREIGN KEY (policy_id) REFERENCES policy(id),
    FOREIGN KEY (policy_holder_id) REFERENCES policy_holder(id)
);
