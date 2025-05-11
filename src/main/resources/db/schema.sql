-- users
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'USER'))
);

-- otp_configuration
CREATE TABLE otp_configuration (
    id SERIAL PRIMARY KEY,
    code_length INT NOT NULL,
    ttl_seconds INT NOT NULL,
    CONSTRAINT single_config CHECK (id = 1)
);

-- otp_codes
CREATE TABLE otp_codes (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    code VARCHAR(20) NOT NULL,
    operation_id VARCHAR(100),
    status VARCHAR(20) NOT NULL CHECK (status IN ('ACTIVE', 'EXPIRED', 'USED')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
