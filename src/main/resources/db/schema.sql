CREATE TABLE IF NOT EXISTS users (
    user_id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL,
    password VARCHAR(255) NOT NULL,
    profile_image VARCHAR(255),
    role VARCHAR(30) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS trucks (
    truck_id BIGSERIAL PRIMARY KEY,
    uploaded_by BIGINT NOT NULL REFERENCES users(user_id),
    truck_number VARCHAR(20) NOT NULL,
    truck_type VARCHAR(30) NOT NULL,
    driver_name VARCHAR(50) NOT NULL,
    driver_phone VARCHAR(15) NOT NULL,
    capacity DOUBLE PRECISION NOT NULL,
    available_capacity DOUBLE PRECISION NOT NULL,
    source VARCHAR(50) NOT NULL,
    destination VARCHAR(50) NOT NULL,
    departure_date DATE NOT NULL,
    return_date DATE NOT NULL,
    estimated_arrival_time TIMESTAMP,
    current_location VARCHAR(50),
    price_per_ton DOUBLE PRECISION NOT NULL,
    description TEXT,
    image_url VARCHAR(255),
    rating DOUBLE PRECISION DEFAULT 4.5,
    total_reviews INT DEFAULT 0,
    insurance_available BOOLEAN DEFAULT TRUE,
    route_type VARCHAR(30),
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bookings (
    booking_id BIGSERIAL PRIMARY KEY,
    truck_id BIGINT NOT NULL REFERENCES trucks(truck_id),
    customer_id BIGINT NOT NULL REFERENCES users(user_id),
    customer_name VARCHAR(50) NOT NULL,
    customer_phone VARCHAR(15) NOT NULL,
    goods_type VARCHAR(50) NOT NULL,
    weight DOUBLE PRECISION NOT NULL,
    pickup_location VARCHAR(50) NOT NULL,
    drop_location VARCHAR(50) NOT NULL,
    booking_date DATE NOT NULL,
    required_trucks INT NOT NULL,
    booking_notes TEXT,
    booking_status VARCHAR(20),
    total_price DOUBLE PRECISION,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_trucks_search ON trucks(source, destination, departure_date, available_capacity, status);
CREATE INDEX IF NOT EXISTS idx_bookings_customer ON bookings(customer_id);
CREATE INDEX IF NOT EXISTS idx_bookings_truck ON bookings(truck_id);
