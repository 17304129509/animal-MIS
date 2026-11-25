-- Run against the existing campus_animal database. You can run this again safely.
SET @migration_sql = IF(
    EXISTS (SELECT 1 FROM information_schema.columns
            WHERE table_schema = DATABASE() AND table_name = 'animal' AND column_name = 'gender'),
    'DO 0', 'ALTER TABLE animal ADD COLUMN gender VARCHAR(10)');
PREPARE migration FROM @migration_sql;
EXECUTE migration;
DEALLOCATE PREPARE migration;

SET @migration_sql = IF(
    EXISTS (SELECT 1 FROM information_schema.columns
            WHERE table_schema = DATABASE() AND table_name = 'animal' AND column_name = 'health'),
    'DO 0', 'ALTER TABLE animal ADD COLUMN health VARCHAR(20)');
PREPARE migration FROM @migration_sql;
EXECUTE migration;
DEALLOCATE PREPARE migration;

SET @migration_sql = IF(
    EXISTS (SELECT 1 FROM information_schema.columns
            WHERE table_schema = DATABASE() AND table_name = 'rescue_order' AND column_name = 'urgency'),
    'DO 0', 'ALTER TABLE rescue_order ADD COLUMN urgency VARCHAR(10)');
PREPARE migration FROM @migration_sql;
EXECUTE migration;
DEALLOCATE PREPARE migration;
