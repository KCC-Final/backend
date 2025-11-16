-- alerts 테이블에 is_deleted 컬럼 추가
-- @author uyh
-- @created 2025-11-16

ALTER TABLE alerts
    ADD COLUMN is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '삭제 여부 (0: 미삭제, 1: 삭제됨)';

-- 기존 데이터는 모두 is_deleted = 0으로 설정됨 (DEFAULT)

-- 삭제된 알림 조회를 위한 인덱스 추가 (선택사항, 성능 최적화)
CREATE INDEX idx_alerts_user_deleted ON alerts(user_id, is_deleted, send_at DESC);