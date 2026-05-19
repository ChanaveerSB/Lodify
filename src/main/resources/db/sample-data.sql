INSERT INTO users(full_name, email, phone, password, role)
VALUES
('Arjun Transport Co', 'provider@loadify.com', '9876543210', '$2a$10$cw2rkU5WpC4Lv3u4Z7UoeeENK9XeJ0w5q7MqmfUsQutguHPZhn7dm', 'TRANSPORT_PROVIDER'),
('Meera Foods', 'customer@loadify.com', '9123456780', '$2a$10$cw2rkU5WpC4Lv3u4Z7UoeeENK9XeJ0w5q7MqmfUsQutguHPZhn7dm', 'CUSTOMER')
ON CONFLICT (email) DO NOTHING;

INSERT INTO trucks(uploaded_by, truck_number, truck_type, driver_name, driver_phone, capacity, available_capacity, source, destination, departure_date, return_date, price_per_ton, description, rating, total_reviews, route_type, status)
SELECT user_id, 'KA01AB4587', 'Container', 'Ravi Kumar', '9988776655', 18, 18, 'Chennai', 'Bangalore', CURRENT_DATE + INTERVAL '2 days', CURRENT_DATE, 4200, 'Return-load container available for FMCG and packed goods.', 4.8, 42, 'RETURN', 'AVAILABLE'
FROM users WHERE email = 'provider@loadify.com'
ON CONFLICT DO NOTHING;
