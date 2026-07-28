-- Add first and last name to existing users table
ALTER TABLE users ADD COLUMN first_name VARCHAR(100);
ALTER TABLE users ADD COLUMN last_name VARCHAR(100);

-- Set default values for existing records to prevent constraint violations
UPDATE users SET first_name = 'Unknown', last_name = 'User' WHERE first_name IS NULL;

-- Enforce mandatory requirement for all future registrations
ALTER TABLE users ALTER COLUMN first_name SET NOT NULL;
ALTER TABLE users ALTER COLUMN last_name SET NOT NULL;