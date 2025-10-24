-- ================================================
-- migration_006_cleanup_and_exists.sql
-- @author uyh
-- 데이터 중복 제거 + EXIST 조건 보강 + DB 문자셋 변경
-- ================================================

USE kcc;

-- 1. book.user_id 컬럼 추가
SET @col_exist := (
  SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA='kcc' AND TABLE_NAME='book' AND COLUMN_NAME='user_id'
);
SET @sql := IF(@col_exist=0,
  'ALTER TABLE book ADD COLUMN user_id VARCHAR(50) NULL AFTER bookshelf_id',
  'SELECT "user_id already exists"');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 2. 외래키 fk_book_user 존재 확인 후 추가
SET @fk_exist := (
  SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
  WHERE CONSTRAINT_NAME='fk_book_user' AND TABLE_SCHEMA='kcc' AND TABLE_NAME='book'
);
SET @sql := IF(@fk_exist=0,
  'ALTER TABLE book ADD CONSTRAINT fk_book_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE',
  'SELECT "fk_book_user already exists"');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 3. codes 테이블 중복 제거
DELETE FROM codes
WHERE code_id NOT IN (
  SELECT * FROM (
    SELECT MIN(code_id)
    FROM codes
    GROUP BY code_value, group_code
  ) AS keeper
);

-- 4. code_groups 중복 제거
DELETE FROM code_groups
WHERE group_code NOT IN (
  SELECT * FROM (
    SELECT MIN(group_code)
    FROM code_groups
    GROUP BY group_code
  ) AS keeper
);

-- 5. sentences 중복 제거
DELETE FROM sentences
WHERE sentence_id NOT IN (
  SELECT * FROM (
    SELECT MIN(sentence_id)
    FROM sentences
    GROUP BY sentence_content, ISBN
  ) AS keeper
);

-- 6. badges & challenge 중복 제거 (FK 고려)
SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM badges
WHERE badge_id NOT IN (
  SELECT * FROM (
    SELECT MIN(badge_id)
    FROM badges
    GROUP BY badge_name
  ) AS keeper
);

DELETE FROM challenge
WHERE challenge_id NOT IN (
  SELECT * FROM (
    SELECT MIN(challenge_id)
    FROM challenge
    GROUP BY user_id, badge_id
  ) AS keeper
);

SET FOREIGN_KEY_CHECKS = 1;

-- 7. alerts.sent_at NULL 보정 및 컬럼 속성 수정
UPDATE alerts SET sent_at = NOW() WHERE sent_at IS NULL AND alert_id > 0;
ALTER TABLE alerts
  MODIFY COLUMN sent_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '전송일';

-- 8. group_comments.created_at 기본값 보정
SET @default_exist := (
  SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA='kcc'
  AND TABLE_NAME='group_comments'
  AND COLUMN_NAME='created_at'
  AND COLUMN_DEFAULT='CURRENT_TIMESTAMP'
);
SET @sql := IF(@default_exist=0,
  'ALTER TABLE group_comments MODIFY COLUMN created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT "작성일"',
  'SELECT "already has default"');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 9. 데이터베이스 문자셋 및 정렬 방식 통일 (수정된 부분)
ALTER DATABASE kcc CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

COMMIT;
