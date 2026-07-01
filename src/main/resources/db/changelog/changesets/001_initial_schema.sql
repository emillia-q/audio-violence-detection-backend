-- create users
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(254) UNIQUE NOT NULL,
    password VARCHAR(60) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    trusted_user_id BIGINT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_users_trusted_user FOREIGN KEY (trusted_user_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE INDEX idx_users_trusted_user ON users(trusted_user_id ASC);

-- create devices
CREATE TABLE devices (
    id BIGSERIAL PRIMARY KEY,
    mac_address VARCHAR(17) UNIQUE NOT NULL,
    name VARCHAR(100),
    user_id BIGINT,
    is_activated BOOLEAN DEFAULT FALSE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    activated_at TIMESTAMP WITH TIME ZONE,
    
    CONSTRAINT fk_devices_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- create alerts 
CREATE TABLE alerts (
    id BIGSERIAL PRIMARY KEY,
    device_id BIGINT NOT NULL,
    event_type VARCHAR(30) NOT NULL,
    is_read BOOLEAN DEFAULT FALSE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_alerts_device FOREIGN KEY (device_id) REFERENCES devices(id) ON DELETE CASCADE
);