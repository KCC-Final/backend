-- ================================================
-- migration_005_badges_exists.sql
-- 도전과제 뱃지 데이터 삽입 (중복 방지: WHERE NOT EXISTS)
-- ================================================

USE
kcc;

-- 1. 첫 활동 시리즈
INSERT INTO badges (badge_name, badge_description, badge_conditions)
SELECT '첫 발자국',
       '첫 번째 독후감을 작성해보세요.',
       1 WHERE NOT EXISTS (SELECT 1 FROM badges WHERE badge_name = '첫 발자국');

INSERT INTO badges (badge_name, badge_description, badge_conditions)
SELECT '첫 공감',
       '다른 사람의 독후감에 첫 좋아요를 남겨보세요.',
       1 WHERE NOT EXISTS (SELECT 1 FROM badges WHERE badge_name = '첫 공감');

INSERT INTO badges (badge_name, badge_description, badge_conditions)
SELECT '모임의 시작',
       '첫 번째 독서모임을 개설해보세요.',
       1 WHERE NOT EXISTS (SELECT 1 FROM badges WHERE badge_name = '모임의 시작');

INSERT INTO badges (badge_name, badge_description, badge_conditions)
SELECT '첫 소통',
       '첫 번째 댓글을 작성해보세요.',
       1 WHERE NOT EXISTS (SELECT 1 FROM badges WHERE badge_name = '첫 소통');

INSERT INTO badges (badge_name, badge_description, badge_conditions)
SELECT '첫 인연',
       '다른 사용자를 팔로우해보세요.',
       1 WHERE NOT EXISTS (SELECT 1 FROM badges WHERE badge_name = '첫 인연');

INSERT INTO badges (badge_name, badge_description, badge_conditions)
SELECT '첫 발견',
       '마음에 드는 책을 처음으로 스크랩해보세요.',
       1 WHERE NOT EXISTS (SELECT 1 FROM badges WHERE badge_name = '첫 발견');

INSERT INTO badges (badge_name, badge_description, badge_conditions)
SELECT '첫인사',
       '프로필에 자기소개를 작성해보세요.',
       1 WHERE NOT EXISTS (SELECT 1 FROM badges WHERE badge_name = '첫인사');

INSERT INTO badges (badge_name, badge_description, badge_conditions)
SELECT '개척자',
       '아무도 작성하지 않은 새로운 도서에 대한 첫 독후감을 작성해보세요.',
       1 WHERE NOT EXISTS (SELECT 1 FROM badges WHERE badge_name = '개척자');

-- 2. 독후감 작성 횟수 시리즈
INSERT INTO badges (badge_name, badge_description, badge_conditions)
SELECT '독서가',
       '독후감을 10개 작성하여 꾸준함을 보여주세요.',
       10 WHERE NOT EXISTS (SELECT 1 FROM badges WHERE badge_name = '독서가');

INSERT INTO badges (badge_name, badge_description, badge_conditions)
SELECT '애독가',
       '독후감을 30개 작성하며 독서에 깊이를 더해보세요.',
       30 WHERE NOT EXISTS (SELECT 1 FROM badges WHERE badge_name = '애독가');

INSERT INTO badges (badge_name, badge_description, badge_conditions)
SELECT '열혈 독서가',
       '독후감을 50개 작성하여 열정을 증명하세요.',
       50 WHERE NOT EXISTS (SELECT 1 FROM badges WHERE badge_name = '열혈 독서가');

INSERT INTO badges (badge_name, badge_description, badge_conditions)
SELECT '책의 지배자',
       '독후감을 100개 작성하여 책의 세계를 정복하세요.',
       100 WHERE NOT EXISTS (SELECT 1 FROM badges WHERE badge_name = '책의 지배자');

-- 3. 특정 분야 독후감 작성 시리즈
INSERT INTO badges (badge_name, badge_description, badge_conditions)
SELECT '한 우물 파기 I',
       '하나의 분야에 대해 10개의 독후감을 작성해보세요.',
       10 WHERE NOT EXISTS (SELECT 1 FROM badges WHERE badge_name = '한 우물 파기 I');

INSERT INTO badges (badge_name, badge_description, badge_conditions)
SELECT '한 우물 파기 II',
       '하나의 분야에 대해 30개의 독후감을 작성해보세요.',
       30 WHERE NOT EXISTS (SELECT 1 FROM badges WHERE badge_name = '한 우물 파기 II');

INSERT INTO badges (badge_name, badge_description, badge_conditions)
SELECT '한 우물 파기 III',
       '하나의 분야에 대해 50개의 독후감을 작성해보세요.',
       50 WHERE NOT EXISTS (SELECT 1 FROM badges WHERE badge_name = '한 우물 파기 III');

INSERT INTO badges (badge_name, badge_description, badge_conditions)
SELECT '한 우물 파기 IV',
       '하나의 분야에 대해 100개의 독후감을 작성해보세요.',
       100 WHERE NOT EXISTS (SELECT 1 FROM badges WHERE badge_name = '한 우물 파기 IV');

-- 4. 여러 분야 독후감 작성 시리즈
INSERT INTO badges (badge_name, badge_description, badge_conditions)
SELECT '작은 탐험가',
       '3개 분야에 각각 3개 이상의 독후감을 작성해보세요.',
       3 WHERE NOT EXISTS (SELECT 1 FROM badges WHERE badge_name = '작은 탐험가');

INSERT INTO badges (badge_name, badge_description, badge_conditions)
SELECT '넓은 탐험가',
       '5개 분야에 각각 5개 이상의 독후감을 작성해보세요.',
       5 WHERE NOT EXISTS (SELECT 1 FROM badges WHERE badge_name = '넓은 탐험가');

INSERT INTO badges (badge_name, badge_description, badge_conditions)
SELECT '위대한 탐험가',
       '10개 분야에 각각 10개 이상의 독후감을 작성해보세요.',
       10 WHERE NOT EXISTS (SELECT 1 FROM badges WHERE badge_name = '위대한 탐험가');

-- 5. 특별 뱃지
INSERT INTO badges (badge_name, badge_description, badge_conditions)
SELECT '이달의 독서왕',
       '이달에 가장 많은 독후감을 작성한 사용자에게 수여됩니다.',
       1 WHERE NOT EXISTS (SELECT 1 FROM badges WHERE badge_name = '이달의 독서왕');

COMMIT;
