-- V2__Add_Sample_Data.sql
-- Example migration showing how to add initial data

-- Insert sample users (optional - remove if not needed)
INSERT INTO app_user (name, email, password) VALUES
                                                 ('John Doe', 'john@example.com', 'password123'),
                                                 ('Jane Smith', 'jane@example.com', 'password456'),
                                                 ('Admin User', 'admin@example.com', 'admin123');

-- Insert sample memberships (optional)
INSERT INTO membership (uid, type, number) VALUES
                                               (1, 'Premium', 12345),
                                               (2, 'Basic', 12346),
                                               (3, 'VIP', 12347);

-- Insert sample perks (optional)
INSERT INTO perk (title, discount, product, expiry_date, mid) VALUES
                                                                  ('10% Off Electronics', '10%', 'Electronics', '2025-12-31', 1),
                                                                  ('Free Shipping', '100%', 'Shipping', '2025-12-31', 1),
                                                                  ('Buy 1 Get 1 Free', 'BOGO', 'Food', '2025-06-30', 2);