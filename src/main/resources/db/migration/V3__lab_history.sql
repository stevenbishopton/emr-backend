-- Lab History Enhancement Migration Script
-- Version 3.0 - Adds comprehensive lab history tracking

-- Create lab history table
CREATE TABLE IF NOT EXISTS lab_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lab_test_request_id BIGINT,
    lab_test_result_id BIGINT,
    status VARCHAR(50) NOT NULL,
    notes TEXT,
    performed_by BIGINT NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    previous_status VARCHAR(50),
    reason VARCHAR(255),
    performed_by_name VARCHAR(255),
    department_name VARCHAR(500),
    INDEX idx_lab_history_request_id (lab_test_request_id),
    INDEX idx_lab_history_result_id (lab_test_result_id),
    INDEX idx_lab_history_status (status),
    INDEX idx_lab_history_performed_by (performed_by),
    INDEX idx_lab_history_timestamp (timestamp)
);

-- Add foreign key constraints (if tables exist)
ALTER TABLE lab_history 
ADD CONSTRAINT IF NOT EXISTS fk_lab_history_request 
FOREIGN KEY (lab_test_request_id) REFERENCES lab_test_request(id);

ALTER TABLE lab_history 
ADD CONSTRAINT IF NOT EXISTS fk_lab_history_result 
FOREIGN KEY (lab_test_result_id) REFERENCES lab_test_results(id);

ALTER TABLE lab_history 
ADD CONSTRAINT IF NOT EXISTS fk_lab_history_performed_by 
FOREIGN KEY (performed_by) REFERENCES personnel(id);

-- Create view for lab request history summary
CREATE OR REPLACE VIEW lab_request_history_summary AS
SELECT 
    lh.id,
    lh.lab_test_request_id,
    ltr.patient_id,
    ltr.visit_id,
    lh.status,
    lh.notes,
    lh.performed_by,
    lh.performed_by_name,
    lh.timestamp,
    lh.previous_status,
    lh.reason,
    lh.department_name
FROM lab_history lh
LEFT JOIN lab_test_request ltr ON lh.lab_test_request_id = ltr.id
WHERE lh.lab_test_request_id IS NOT NULL;

-- Create view for lab result history summary
CREATE OR REPLACE VIEW lab_result_history_summary AS
SELECT 
    lh.id,
    lh.lab_test_result_id,
    ltr.patient_id,
    ltr.visit_id,
    lh.status,
    lh.notes,
    lh.performed_by,
    lh.performed_by_name,
    lh.timestamp,
    lh.previous_status,
    lh.reason,
    lh.department_name
FROM lab_history lh
LEFT JOIN lab_test_results ltr ON lh.lab_test_result_id = ltr.id
WHERE lh.lab_test_result_id IS NOT NULL;

-- Create view for comprehensive lab history by patient
CREATE OR REPLACE VIEW patient_lab_history_summary AS
SELECT 
    p.id as patient_id,
    CONCAT(p.first_name, ' ', p.last_name) as patient_name,
    COUNT(DISTINCT lh.lab_test_request_id) as total_requests,
    COUNT(DISTINCT lh.lab_test_result_id) as total_results,
    COUNT(CASE WHEN lh.status = 'COMPLETED' THEN 1 END) as completed_count,
    COUNT(CASE WHEN lh.status IN ('PENDING', 'IN_PROGRESS') THEN 1 END) as pending_count,
    MAX(lh.timestamp) as last_activity
FROM patient p
LEFT JOIN lab_history lh ON 
    (lh.lab_test_request_id IN (SELECT id FROM lab_test_request WHERE patient_id = p.id) OR
     lh.lab_test_result_id IN (SELECT id FROM lab_test_results WHERE patient_id = p.id))
GROUP BY p.id, p.first_name, p.last_name;

-- Verify table creation
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'LAB_HISTORY'
ORDER BY ORDINAL_POSITION;
