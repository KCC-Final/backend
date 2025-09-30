-- migration_001.sql
-- ============================================
-- 목적:
-- 1. likes 테이블 FK 수정 → 리뷰 삭제 시 좋아요 자동 삭제
-- 2. review_comments 테이블 FK 수정 → 리뷰 삭제 시 댓글 자동 삭제
-- ============================================

-- 1. likes 테이블 FK 수정
ALTER TABLE likes
DROP FOREIGN KEY fk_likes_review;

ALTER TABLE likes
ADD CONSTRAINT fk_likes_review
FOREIGN KEY (review_id) REFERENCES reviews(review_id)
ON DELETE CASCADE;

-- 2. review_comments 테이블 FK 수정
ALTER TABLE review_comments
DROP FOREIGN KEY fk_comments_review;

ALTER TABLE review_comments
ADD CONSTRAINT fk_comments_review
FOREIGN KEY (review_id) REFERENCES reviews(review_id)
ON DELETE CASCADE;
