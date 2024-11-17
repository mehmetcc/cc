-- Create the database
CREATE DATABASE credit;

-- Connect to the database
\c credit;

-- Create the users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

-- Create the credits table
CREATE TABLE credits (
    id SERIAL PRIMARY KEY,
    status BOOLEAN NOT NULL,
    amount NUMERIC(10,2) NOT NULL,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

-- Create the installments table
CREATE TABLE installments (
    id SERIAL PRIMARY KEY,
    amount NUMERIC(10,2) NOT NULL,
    credit_id INT NOT NULL REFERENCES credits(id) ON DELETE CASCADE,
    status BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);
