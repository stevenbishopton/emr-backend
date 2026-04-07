-- Quick fix for existing radiograph data
-- Run this in H2 console to fix the 500 errors

-- Update existing records to have proper default values for new required columns
UPDATE radiograph 
SET status = 'REQUESTED' 
WHERE status IS NULL OR status = '';

UPDATE radiograph 
SET requested_by = 1 
WHERE requested_by IS NULL;

UPDATE radiograph 
SET department_id = 1 
WHERE department_id IS NULL;

UPDATE radiograph 
SET version = 0 
WHERE version IS NULL;

-- Add the new columns if they don't exist (safer approach)
ALTER TABLE radiograph ADD COLUMN IF NOT EXISTS status VARCHAR(50) DEFAULT 'REQUESTED';
ALTER TABLE radiograph ADD COLUMN IF NOT EXISTS requested_by BIGINT DEFAULT 1;
ALTER TABLE radiograph ADD COLUMN IF NOT EXISTS department_id BIGINT DEFAULT 1;

-- Create catalog table if it doesn't exist
CREATE TABLE IF NOT EXISTS radiograph_catalog (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    type VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    description TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0
);

-- Create history table if it doesn't exist
CREATE TABLE IF NOT EXISTS radiograph_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    radiograph_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    notes TEXT,
    performed_by BIGINT NOT NULL,
    performed_by_name VARCHAR(255),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    previous_status VARCHAR(50),
    reason VARCHAR(255),
    department_name VARCHAR(255)
);
