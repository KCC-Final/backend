-- database/migration_003_add_reviews_indexes.sql
-- @author uyh
-- @created 2025-11-17
-- reviews 테이블 성능 최적화를 위한 인덱스 추가

-- 전체 리뷰 조회 최적화 (status, temporary, secret, created_at)
CREATE INDEX idx_reviews_status_query
    ON reviews(status, temporary, secret, created_at DESC);

-- created_at 단독 인덱스 (정렬 최적화)
CREATE INDEX idx_reviews_created_at
    ON reviews(created_at DESC);

-- user_id + status 조합 (특정 유저 리뷰 조회)
CREATE INDEX idx_reviews_user_status
    ON reviews(user_id, status, temporary);

-- likes 테이블 인덱스
CREATE INDEX idx_likes_review_id ON likes(review_id);

-- review_comments 테이블 인덱스
CREATE INDEX idx_comments_review_id ON review_comments(review_id);