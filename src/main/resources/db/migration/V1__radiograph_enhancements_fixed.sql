-- Radiograph Section Enhancement Migration Script - FIXED VERSION
-- Version 1.0 - Fixed for existing data

-- Create radiograph catalog table
CREATE TABLE IF NOT EXISTS radiograph_catalog (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    type VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    description TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    INDEX idx_radiograph_catalog_type (type),
    INDEX idx_radiograph_catalog_active (active)
);

-- Create radiograph history table
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
    department_name VARCHAR(255),
    INDEX idx_radiograph_history_radiograph_id (radiograph_id),
    INDEX idx_radiograph_history_timestamp (timestamp),
    INDEX idx_radiograph_history_performed_by (performed_by),
    INDEX idx_radiograph_history_status (status)
);

-- Add new columns to radiograph table with proper defaults for existing data
-- First add columns as nullable to avoid constraint violations
ALTER TABLE radiograph 
ADD COLUMN IF NOT EXISTS status VARCHAR(50) DEFAULT 'REQUESTED',
ADD COLUMN IF NOT EXISTS scheduled_date VARCHAR(20),
ADD COLUMN IF NOT EXISTS scheduled_time VARCHAR(10),
ADD COLUMN IF NOT EXISTS technician_notes TEXT,
ADD COLUMN IF NOT EXISTS radiologist_notes TEXT,
ADD COLUMN IF NOT EXISTS report_url VARCHAR(500),
ADD COLUMN IF NOT EXISTS image_url VARCHAR(500),
ADD COLUMN IF NOT EXISTS requested_by BIGINT DEFAULT 1,
ADD COLUMN IF NOT EXISTS department_id BIGINT DEFAULT 1,
ADD COLUMN IF NOT EXISTS version BIGINT DEFAULT 0;

-- Update existing records to have default values
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

-- Now make columns NOT NULL (only if they're all populated)
-- This approach is safer for existing data
-- ALTER TABLE radiograph ALTER COLUMN status SET NOT NULL;
-- ALTER TABLE radiograph ALTER COLUMN requested_by SET NOT NULL;
-- ALTER TABLE radiograph ALTER COLUMN department_id SET NOT NULL;

-- Add indexes for new radiograph columns
CREATE INDEX IF NOT EXISTS idx_radiograph_status ON radiograph(status);
CREATE INDEX IF NOT EXISTS idx_radiograph_requested_by ON radiograph(requested_by);
CREATE INDEX IF NOT EXISTS idx_radiograph_department_id ON radiograph(department_id);
CREATE INDEX IF NOT EXISTS idx_radiograph_scheduled_date ON radiograph(scheduled_date);

-- Insert default catalog items (only if table is empty)
INSERT IGNORE INTO radiograph_catalog (name, type, price, description) VALUES
('SKULL PA AND AP/LAT', 'X_RAY', 15000.00, 'Skull examination with PA and lateral views'),
('PARANASAL SINUSES (PNS)', 'X_RAY', 12000.00, 'Paranasal sinuses examination'),
('MANDIBLE', 'X_RAY', 12000.00, 'Mandible examination'),
('CERVICAL SPINE AP/LAT', 'X_RAY', 15000.00, 'Cervical spine with AP and lateral views'),
('POSTNATAL SPACE', 'X_RAY', 10000.00, 'Postnatal space examination'),
('CLAVICLE', 'X_RAY', 10000.00, 'Clavicle examination'),
('CHEST PA', 'X_RAY', 12000.00, 'Chest posteroanterior view'),
('CHEST PA/LAT', 'X_RAY', 15000.00, 'Chest with PA and lateral views'),
('THORACOLUMBER SPINE AP/LAT', 'X_RAY', 15000.00, 'Thoracolumbar spine with AP and lateral views'),
('THORACIC', 'X_RAY', 12000.00, 'Thoracic spine examination'),
('PLAIN ABDOMEN', 'X_RAY', 12000.00, 'Plain abdomen examination'),
('ABDOMINAL ERECT/SPINE', 'X_RAY', 15000.00, 'Abdominal erect and spine views'),
('LUMBOSACRAL SPINE AP/LAT', 'X_RAY', 15000.00, 'Lumbosacral spine with AP and lateral views'),
('SHOULDER JOINT AP/LAT', 'X_RAY', 15000.00, 'Shoulder joint with AP and lateral views'),
('HUMERUS AP/LAT', 'X_RAY', 15000.00, 'Humerus with AP and lateral views'),
('ELBOW JOINT AP/LAT', 'X_RAY', 15000.00, 'Elbow joint with AP and lateral views'),
('FOREARM AP/LAT', 'X_RAY', 15000.00, 'Forearm with AP and lateral views'),
('WRIST AP/LAT', 'X_RAY', 15000.00, 'Wrist with AP and lateral views'),
('HAND AP/LAT', 'X_RAY', 15000.00, 'Hand with AP and lateral views'),
('PELVIS AP/LAT', 'X_RAY', 15000.00, 'Pelvis with AP and lateral views'),
('HIP JOINT AP/LAT', 'X_RAY', 15000.00, 'Hip joint with AP and lateral views'),
('FEMUR AP/LAT', 'X_RAY', 15000.00, 'Femur with AP and lateral views'),
('KNEE JOINT AP/LAT', 'X_RAY', 15000.00, 'Knee joint with AP and lateral views'),
('TIBIA/FIBULA AP/LAT', 'X_RAY', 15000.00, 'Tibia/Fibula with AP and lateral views'),
('ANKLE JOINT AP/LAT', 'X_RAY', 15000.00, 'Ankle joint with AP and lateral views'),
('FOOT AP/LAT/OBLIQUE', 'X_RAY', 15000.00, 'Foot with AP, lateral, and oblique views'),
('ABDOMINAL ULTRASOUND', 'ULTRASOUND', 18000.00, 'Complete abdominal ultrasound examination'),
('PELVIC ULTRASOUND', 'ULTRASOUND', 15000.00, 'Pelvic ultrasound examination'),
('OBSTETRIC ULTRASOUND', 'ULTRASOUND', 20000.00, 'Obstetric ultrasound examination'),
('TRANSVAGINAL ULTRASOUND', 'ULTRASOUND', 18000.00, 'Transvaginal ultrasound examination'),
('BREAST ULTRASOUND', 'ULTRASOUND', 15000.00, 'Breast ultrasound examination'),
('THYROID ULTRASOUND', 'ULTRASOUND', 12000.00, 'Thyroid ultrasound examination'),
('SCROTAL ULTRASOUND', 'ULTRASOUND', 15000.00, 'Scrotal ultrasound examination'),
('CAROTID DOPPLER', 'ULTRASOUND', 18000.00, 'Carotid doppler ultrasound examination'),
('LOWER LIMB DOPPLER', 'ULTRASOUND', 20000.00, 'Lower limb doppler ultrasound examination'),
('UPPER LIMB DOPPLER', 'ULTRASOUND', 18000.00, 'Upper limb doppler ultrasound examination'),
('RENAL ULTRASOUND', 'ULTRASOUND', 15000.00, 'Renal ultrasound examination'),
('PROSTATE ULTRASOUND', 'ULTRASOUND', 18000.00, 'Prostate ultrasound examination'),
('HEART ECHOCARDIOGRAM', 'ULTRASOUND', 25000.00, 'Heart echocardiogram examination');

-- Create view for radiograph with history (if not exists)
CREATE OR REPLACE VIEW radiograph_with_history AS
SELECT 
    r.*,
    GROUP_CONCAT(
        CONCAT(h.status, '|', h.notes, '|', h.timestamp, '|', h.performed_by_name) 
        ORDER BY h.timestamp DESC 
        SEPARATOR ';;'
    ) as history_summary
FROM radiograph r
LEFT JOIN radiograph_history h ON r.id = h.radiograph_id
GROUP BY r.id;
