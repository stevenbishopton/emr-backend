-- Lab Test Request Comments Field Fix
-- Version 4.0 - Changes comments column from VARCHAR(255) to TEXT to support longer comments

-- Alter comments column to TEXT type
ALTER TABLE lab_test_request 
ALTER COLUMN comments TYPE TEXT;

-- Verify the change
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    DATA_TYPE,
    CHARACTER_MAXIMUM_LENGTH,
    IS_NULLABLE
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'LAB_TEST_REQUEST' 
AND COLUMN_NAME = 'COMMENTS';
