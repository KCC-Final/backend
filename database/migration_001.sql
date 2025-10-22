-- ================================================
-- migration_002_follows.sql
-- 팔로우 테이블 수정 (ON DELETE CASCADE, UNIQUE, SELF FOLLOW 제한)
-- ================================================

USE kcc;

-- 1 기존 follows 테이블이 존재한다면 삭제
DROP TABLE IF EXISTS follows;

-- 2 새로운 follows 테이블 생성
CREATE TABLE IF NOT EXISTS follows (
    follow_id INT NOT NULL AUTO_INCREMENT COMMENT '팔로우 아이디',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '팔로우 생성일',
    follower VARCHAR(50) NOT NULL COMMENT '팔로우 한 사람',
    followed VARCHAR(50) NOT NULL COMMENT '팔로우 받은 사람',

    PRIMARY KEY (follow_id),

    -- 제약 조건
    CONSTRAINT fk_follower_user_id FOREIGN KEY (follower)
        REFERENCES users(user_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_followed_user_id FOREIGN KEY (followed)
        REFERENCES users(user_id)
        ON DELETE CASCADE,
    CONSTRAINT chk_not_self_follow CHECK (follower <> followed),
    UNIQUE KEY uq_follows (follower, followed)
) COMMENT='팔로우 테이블';
