-- ================================================
-- migration_004_alerts_if_exists.sql
-- alerts 테이블 sent_at 컬럼 보정, 상태 컬럼 및 sender_user_id 추가
-- ================================================

USE kcc;

-- 1. sent_at 컬럼 존재 시 NULL값 초기화
SET @col_exist := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = 'kcc'
    AND TABLE_NAME = 'alerts'
    AND COLUMN_NAME = 'sent_at'
);

SET @sql := IF(
  @col_exist > 0,
  'UPDATE alerts SET sent_at = NOW() WHERE sent_at IS NULL AND alert_id > 0;',
  'SELECT " alerts.sent_at 컬럼이 존재하지 않아 UPDATE 생략"'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


-- 2. sent_at 컬럼 존재 시 DEFAULT 및 NOT NULL 속성 보정
SET @sql := IF(
  @col_exist > 0,
  'ALTER TABLE alerts MODIFY COLUMN sent_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "전송일";',
  'SELECT " alerts.sent_at 컬럼이 존재하지 않아 ALTER 생략"'
);
PREPARE stmt2 FROM @sql;
EXECUTE stmt2;
DEALLOCATE PREPARE stmt2;


-- 3. alerts_check_status 컬럼이 없을 경우 추가
SET @status_col_exist := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = 'kcc'
    AND TABLE_NAME = 'alerts'
    AND COLUMN_NAME = 'alerts_check_status'
);

SET @sql3 := IF(
  @status_col_exist = 0,
  'ALTER TABLE alerts ADD COLUMN alerts_check_status BOOLEAN NOT NULL DEFAULT FALSE COMMENT "알림 확인 상태 (0: 미확인, 1: 확인됨)";',
  'SELECT " alerts_check_status 컬럼이 이미 존재하여 ALTER 생략"'
);
PREPARE stmt3 FROM @sql3;
EXECUTE stmt3;
DEALLOCATE PREPARE stmt3;


-- 4. sender_user_id 컬럼이 없을 경우 추가
SET @sender_col_exist := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = 'kcc'
    AND TABLE_NAME = 'alerts'
    AND COLUMN_NAME = 'sender_user_id'
);

SET @sql4 := IF(
  @sender_col_exist = 0,
  'ALTER TABLE alerts ADD COLUMN sender_user_id VARCHAR(50) NULL COMMENT "전송자 id";',
  'SELECT " sender_user_id 컬럼이 이미 존재하여 ALTER 생략"'
);
PREPARE stmt4 FROM @sql4;
EXECUTE stmt4;
DEALLOCATE PREPARE stmt4;


-- ================================================
-- END OF MIGRATION_004
-- ================================================
