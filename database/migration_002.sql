-- migration_003.sql
-- ============================================
-- 목적:
-- likes / review_comments 테이블의 FK를
-- 다시 기본 동작(RESTRICT)으로 복원
-- (리뷰 삭제 시 CASCADE 되지 않고 무결성 제약만 유지)
-- ============================================

-- 1. likes 테이블 FK 수정 (CASCADE 제거)
ALTER TABLE likes
DROP FOREIGN KEY fk_likes_review;

ALTER TABLE likes
ADD CONSTRAINT fk_likes_review
FOREIGN KEY (review_id) REFERENCES reviews(review_id);

-- 2. review_comments 테이블 FK 수정 (CASCADE 제거)
ALTER TABLE review_comments
DROP FOREIGN KEY fk_comments_review;

ALTER TABLE review_comments
ADD CONSTRAINT fk_comments_review
FOREIGN KEY (review_id) REFERENCES reviews(review_id);
