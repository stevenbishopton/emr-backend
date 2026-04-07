-- Radiograph Visit History Enhancement Migration Script
-- Version 2.0 - Adds comprehensive visit history tracking

-- Create radiograph visit history table
CREATE TABLE IF NOT EXISTS radiograph_visit_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    visit_id BIGINT,
    requested_by BIGINT NOT NULL,
    performed_by BIGINT,
    radiologist_id BIGINT,
    status VARCHAR(50) NOT NULL,
    visit_type VARCHAR(50) NOT NULL,
    clinical_notes TEXT,
    radiologist_report TEXT,
    technician_notes TEXT,
    report_url VARCHAR(500),
    image_url VARCHAR(500),
    visit_date TIMESTAMP NOT NULL,
    completed_date TIMESTAMP,
    scheduled_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    INDEX idx_radiograph_visit_history_patient_id (patient_id),
    INDEX idx_radiograph_visit_history_visit_id (visit_id),
    INDEX idx_radiograph_visit_history_requested_by (requested_by),
    INDEX idx_radiograph_visit_history_performed_by (performed_by),
    INDEX idx_radiograph_visit_history_radiologist_id (radiologist_id),
    INDEX idx_radiograph_visit_history_status (status),
    INDEX idx_radiograph_visit_history_visit_date (visit_date),
    INDEX idx_radiograph_visit_history_visit_type (visit_type),
    INDEX idx_radiograph_visit_history_completed_date (completed_date)
);

-- Create radiograph visit test table
CREATE TABLE IF NOT EXISTS radiograph_visit_test (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    visit_history_id BIGINT NOT NULL,
    catalog_item_id BIGINT,
    type VARCHAR(50) NOT NULL,
    test_name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    findings TEXT,
    impression TEXT,
    recommendation TEXT,
    image_url VARCHAR(500),
    report_url VARCHAR(500),
    performed_at TIMESTAMP,
    reported_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    INDEX idx_radiograph_visit_test_visit_history_id (visit_history_id),
    INDEX idx_radiograph_visit_test_catalog_item_id (catalog_item_id),
    INDEX idx_radiograph_visit_test_type (type),
    INDEX idx_radiograph_visit_test_status (status),
    INDEX idx_radiograph_visit_test_performed_at (performed_at),
    INDEX idx_radiograph_visit_test_reported_at (reported_at)
);

-- Add foreign key constraints (if tables exist)
-- Note: These will only be added if the referenced tables exist
ALTER TABLE radiograph_visit_history 
ADD CONSTRAINT IF NOT EXISTS fk_radiograph_visit_history_patient 
FOREIGN KEY (patient_id) REFERENCES patient(id);

ALTER TABLE radiograph_visit_history 
ADD CONSTRAINT IF NOT EXISTS fk_radiograph_visit_history_visit 
FOREIGN KEY (visit_id) REFERENCES visit(id);

ALTER TABLE radiograph_visit_history 
ADD CONSTRAINT IF NOT EXISTS fk_radiograph_visit_history_requested_by 
FOREIGN KEY (requested_by) REFERENCES personnel(id);

ALTER TABLE radiograph_visit_history 
ADD CONSTRAINT IF NOT EXISTS fk_radiograph_visit_history_performed_by 
FOREIGN KEY (performed_by) REFERENCES personnel(id);

ALTER TABLE radiograph_visit_history 
ADD CONSTRAINT IF NOT EXISTS fk_radiograph_visit_history_radiologist 
FOREIGN KEY (radiologist_id) REFERENCES personnel(id);

ALTER TABLE radiograph_visit_test 
ADD CONSTRAINT IF NOT EXISTS fk_radiograph_visit_test_visit_history 
FOREIGN KEY (visit_history_id) REFERENCES radiograph_visit_history(id);

ALTER TABLE radiograph_visit_test 
ADD CONSTRAINT IF NOT EXISTS fk_radiograph_visit_test_catalog_item 
FOREIGN KEY (catalog_item_id) REFERENCES radiograph_catalog(id);

-- Create view for comprehensive visit history
CREATE OR REPLACE VIEW radiograph_visit_history_summary AS
SELECT 
    rvh.id,
    rvh.patient_id,
    CONCAT(p.first_name, ' ', p.last_name) as patient_name,
    rvh.visit_id,
    rvh.visit_date,
    rvh.status,
    rvh.visit_type,
    rvh.clinical_notes,
    rvh.radiologist_report,
    rvh.technician_notes,
    rvh.report_url,
    rvh.image_url,
    rvh.completed_date,
    rvh.scheduled_date,
    COUNT(rvt.id) as number_of_tests,
    SUM(rvt.price) as total_cost,
    GROUP_CONCAT(DISTINCT rvt.test_name ORDER BY rvt.test_name SEPARATOR ', ') as tests_performed
FROM radiograph_visit_history rvh
LEFT JOIN patient p ON rvh.patient_id = p.id
LEFT JOIN radiograph_visit_test rvt ON rvh.id = rvt.visit_history_id
GROUP BY rvh.id, rvh.patient_id, p.first_name, p.last_name, rvh.visit_id, rvh.visit_date, 
         rvh.status, rvh.visit_type, rvh.clinical_notes, rvh.radiologist_report, 
         rvh.technician_notes, rvh.report_url, rvh.image_url, rvh.completed_date, rvh.scheduled_date;

-- Create view for patient radiograph summary
CREATE OR REPLACE VIEW patient_radiograph_summary AS
SELECT 
    p.id as patient_id,
    CONCAT(p.first_name, ' ', p.last_name) as patient_name,
    COUNT(rvh.id) as total_visits,
    COUNT(CASE WHEN rvh.status = 'COMPLETED' THEN 1 END) as completed_visits,
    COUNT(CASE WHEN rvh.status IN ('REQUESTED', 'SCHEDULED', 'IN_PROGRESS') THEN 1 END) as pending_visits,
    SUM(CASE WHEN rvt.id IS NOT NULL THEN rvt.price ELSE 0 END) as total_spent,
    MAX(rvh.visit_date) as last_visit_date,
    MIN(rvh.visit_date) as first_visit_date
FROM patient p
LEFT JOIN radiograph_visit_history rvh ON p.id = rvh.patient_id
LEFT JOIN radiograph_visit_test rvt ON rvh.id = rvt.visit_history_id
GROUP BY p.id, p.first_name, p.last_name;

-- Create view for radiograph statistics
CREATE OR REPLACE VIEW radiograph_statistics AS
SELECT 
    rvh.status,
    COUNT(*) as count,
    COUNT(CASE WHEN rvh.completed_date IS NOT NULL THEN 1 END) as completed_count,
    AVG(CASE WHEN rvt.id IS NOT NULL THEN rvt.price ELSE NULL END) as average_cost,
    SUM(CASE WHEN rvt.id IS NOT NULL THEN rvt.price ELSE 0 END) as total_revenue
FROM radiograph_visit_history rvh
LEFT JOIN radiograph_visit_test rvt ON rvh.id = rvt.visit_history_id
GROUP BY rvh.status;

-- Insert sample visit history data (if tables are empty)
INSERT IGNORE INTO radiograph_visit_history (
    patient_id, visit_id, requested_by, status, visit_type, clinical_notes, visit_date
) VALUES 
(1, 1, 1, 'COMPLETED', 'ROUTINE', 'Patient complains of chest pain and difficulty breathing', '2026-03-15 10:30:00'),
(2, 2, 1, 'COMPLETED', 'EMERGENCY', 'Emergency case - abdominal trauma', '2026-03-16 14:20:00'),
(3, 3, 2, 'IN_PROGRESS', 'ROUTINE', 'Follow-up examination for previous fracture', '2026-03-17 09:15:00'),
(1, 4, 1, 'SCHEDULED', 'ROUTINE', 'Routine checkup - annual screening', '2026-03-20 11:00:00');

-- Insert sample test data (if tables are empty)
INSERT IGNORE INTO radiograph_visit_test (
    visit_history_id, catalog_item_id, type, test_name, description, price, status, performed_at, reported_at
) VALUES 
(1, 7, 'X_RAY', 'CHEST PA', 'Chest posteroanterior view', 12000.00, 'COMPLETED', '2026-03-15 10:45:00', '2026-03-15 16:30:00'),
(1, 8, 'X_RAY', 'CHEST PA/LAT', 'Chest with PA and lateral views', 15000.00, 'COMPLETED', '2026-03-15 10:45:00', '2026-03-15 16:30:00'),
(2, 11, 'X_RAY', 'PLAIN ABDOMEN', 'Plain abdomen examination', 12000.00, 'COMPLETED', '2026-03-16 14:35:00', '2026-03-16 18:45:00'),
(3, 22, 'X_RAY', 'KNEE JOINT AP/LAT', 'Knee joint with AP and lateral views', 15000.00, 'IN_PROGRESS', '2026-03-17 09:30:00', NULL);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_radiograph_visit_history_composite ON radiograph_visit_history(patient_id, visit_date DESC);
CREATE INDEX IF NOT EXISTS idx_radiograph_visit_test_composite ON radiograph_visit_test(visit_history_id, status);

-- Verify table creation
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME IN ('RADIOGRAPH_VISIT_HISTORY', 'RADIOGRAPH_VISIT_TEST')
ORDER BY TABLE_NAME, ORDINAL_POSITION;
