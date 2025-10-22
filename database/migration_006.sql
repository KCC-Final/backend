-- ================================================
-- migration_006_add_userid_and_cleanup_codes.sql
-- @author uyh
-- book.user_id 컬럼 추가 + FK 제약 + codes/sentences/group_comments/alerts 정리
-- ================================================

USE kcc;

-- 1 book.user_id 컬럼 추가 (존재하지 않을 때만)
SET @column_exists = (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = 'kcc' AND TABLE_NAME = 'book' AND COLUMN_NAME = 'user_id'
);
SET @sql = IF(@column_exists = 0,
    'ALTER TABLE book ADD COLUMN user_id VARCHAR(50) NULL AFTER bookshelf_id',
    'SELECT ''user_id column already exists, skipping...'' AS info'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 2 user_id 외래키 제약 조건 추가 (존재하지 않을 때만)
SET @fk_exists = (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
    WHERE TABLE_SCHEMA = 'kcc' AND TABLE_NAME = 'book' AND CONSTRAINT_NAME = 'fk_book_user'
);
SET @sql = IF(@fk_exists = 0,
    'ALTER TABLE book ADD CONSTRAINT fk_book_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE',
    'SELECT ''fk_book_user constraint already exists, skipping...'' AS info'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 3 codes 테이블 중복 데이터 제거
DELETE FROM codes
WHERE code_id NOT IN (
    SELECT * FROM (
        SELECT MIN(code_id)
        FROM codes
        GROUP BY code_value, group_code
    ) AS keeper
);

-- 4 sentences 테이블 중복 데이터 제거
DELETE FROM sentences
WHERE sentence_id NOT IN (
    SELECT * FROM (
        SELECT MIN(sentence_id)
        FROM sentences
        GROUP BY sentence_content, ISBN
    ) AS keeper
);

-- 5 group_comments.created_at 기본값 추가 (없을 경우만)
SET @has_default := (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = 'kcc'
      AND TABLE_NAME = 'group_comments'
      AND COLUMN_NAME = 'created_at'
      AND COLUMN_DEFAULT = 'CURRENT_TIMESTAMP'
);
SET @sql := IF(@has_default = 0,
    'ALTER TABLE group_comments MODIFY COLUMN created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''작성일''',
    'SELECT ''group_comments.created_at already has DEFAULT CURRENT_TIMESTAMP, skipping...'' AS info'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 6 alerts.sent_at 데이터 보정 및 컬럼 수정
-- (1) sent_at 값이 NULL인 데이터는 현재 시각으로 업데이트
UPDATE alerts
SET sent_at = NOW()
WHERE sent_at IS NULL AND alert_id > 0;

-- (2) 컬럼 속성 수정: NOT NULL + DEFAULT CURRENT_TIMESTAMP
ALTER TABLE alerts
MODIFY COLUMN sent_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '전송일';

COMMIT;
