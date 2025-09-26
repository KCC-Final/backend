-- 데이터베이스 초기화 스크립트 (멱등성 보장)
USE kcc;

-- ================================
-- 1. 유저 테이블
-- ================================
CREATE TABLE IF NOT EXISTS users (
    user_id         VARCHAR(50)   NOT NULL COMMENT '사용자 아이디',
    password        VARCHAR(255)  NOT NULL COMMENT '비밀번호',
    email           VARCHAR(100)  NOT NULL COMMENT '이메일',
    nickname        VARCHAR(50)   NOT NULL COMMENT '닉네임',
    profile_image   VARCHAR(255)  NULL COMMENT '프로필 이미지 주소',
    introduction    VARCHAR(255)  NULL COMMENT '자기소개',
    gender          CHAR(1)       NULL COMMENT '성별',
    name            VARCHAR(50)   NULL COMMENT '이름',
    birth           DATE          NULL COMMENT '생년월일',
    created_at      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '계정 생성일',
    status          BOOLEAN       DEFAULT FALSE COMMENT '회원탈퇴 상태 (TRUE=탈퇴, FALSE=활성)',
    withdrawal_date DATETIME      NULL COMMENT '회원탈퇴일',
    pwd_changed_at  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '비밀번호 수정일',
    check_privacy   BOOLEAN       NOT NULL COMMENT '개인정보 이용 동의',
    check_service   BOOLEAN       NOT NULL COMMENT '서비스 이용 동의',
    email_verified  BOOLEAN       DEFAULT FALSE COMMENT '회원 인증 상태 (TRUE = 인증, FALSE = 미인증)',
    PRIMARY KEY (user_id),
    UNIQUE KEY uq_email (email),
    CONSTRAINT chk_users_gender CHECK (gender IS NULL OR UPPER(gender) IN ('F', 'M'))
) COMMENT='사용자 테이블';

-- ================================
-- 2. 코드 그룹 / 코드 테이블
-- ================================
CREATE TABLE IF NOT EXISTS code_groups (
    group_code  VARCHAR(50)   NOT NULL COMMENT '그룹코드',
    group_name  VARCHAR(100)  NULL COMMENT '그룹이름',
    PRIMARY KEY (group_code)
) COMMENT='코드그룹';

CREATE TABLE IF NOT EXISTS codes (
    code_id     INT          NOT NULL AUTO_INCREMENT COMMENT '코드 ID',
    code_value  VARCHAR(50)  NOT NULL COMMENT '코드값',
    code_name   VARCHAR(100) NOT NULL COMMENT '표시이름',
    group_code  VARCHAR(50)  NOT NULL COMMENT '그룹코드',
    PRIMARY KEY (code_id),
    CONSTRAINT fk_codes_group FOREIGN KEY (group_code) REFERENCES code_groups(group_code)
) COMMENT='코드';

-- ================================
-- 3. 독후감 / 좋아요 / 댓글 테이블
-- ================================
CREATE TABLE IF NOT EXISTS reviews (
    review_id       INT          NOT NULL AUTO_INCREMENT COMMENT '독후감 ID',
    ISBN            VARCHAR(20)  NOT NULL COMMENT 'ISBN',
    review_title    VARCHAR(200) NOT NULL COMMENT '제목',
    review_content  TEXT         NOT NULL COMMENT '내용',
    view_cnt        INT          NULL DEFAULT 0 COMMENT '조회 수',
    secret          BOOLEAN      NULL DEFAULT FALSE COMMENT '비밀글',
    status          BOOLEAN      NULL DEFAULT TRUE COMMENT '삭제 상태',
    temporary       BOOLEAN      NULL DEFAULT FALSE COMMENT '임시 저장 여부',
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '작성일',
    updated_at      DATETIME     NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    user_id         VARCHAR(50)  NOT NULL COMMENT '사용자 ID',
    code_id         INT          NOT NULL COMMENT '코드 ID',
    PRIMARY KEY (review_id),
    CONSTRAINT fk_reviews_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_reviews_code FOREIGN KEY (code_id) REFERENCES codes(code_id)
) COMMENT='독후감';

CREATE TABLE IF NOT EXISTS likes (
    user_id     VARCHAR(50)  NOT NULL COMMENT '사용자 ID',
    review_id   INT          NOT NULL COMMENT '독후감 ID',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    PRIMARY KEY (user_id, review_id),
    CONSTRAINT fk_likes_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_likes_review FOREIGN KEY (review_id) REFERENCES reviews(review_id)
) COMMENT='좋아요';

CREATE TABLE IF NOT EXISTS review_comments (
    comment_id  INT          NOT NULL AUTO_INCREMENT COMMENT '댓글 ID',
    content     VARCHAR(500) NOT NULL COMMENT '댓글 내용',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '작성일',
    updated_at  DATETIME     NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    review_id   INT          NOT NULL COMMENT '독후감 ID',
    user_id     VARCHAR(50)  NOT NULL COMMENT '사용자 ID',
    parent_id   INT          NULL COMMENT '부모 댓글 ID',
    PRIMARY KEY (comment_id),
    CONSTRAINT fk_comments_review FOREIGN KEY (review_id) REFERENCES reviews(review_id),
    CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_comments_parent FOREIGN KEY (parent_id) REFERENCES review_comments(comment_id)
) COMMENT='독후감 댓글';

-- ================================
-- 4. 코드 그룹 & 데이터 (멱등성 보장)
-- ================================

-- 1. 대주제 코드 그룹 및 데이터 추가
INSERT INTO code_groups (group_code, group_name)
SELECT 'kdc', '대주제 코드'
WHERE NOT EXISTS (SELECT 1 FROM code_groups WHERE group_code = 'kdc');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '0', '총류', 'kdc'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '0' AND group_code = 'kdc');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '1', '철학', 'kdc'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '1' AND group_code = 'kdc');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '2', '종교', 'kdc'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '2' AND group_code = 'kdc');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '3', '사회과학', 'kdc'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '3' AND group_code = 'kdc');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '4', '자연과학', 'kdc'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '4' AND group_code = 'kdc');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '5', '기술과학', 'kdc'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '5' AND group_code = 'kdc');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '6', '예술', 'kdc'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '6' AND group_code = 'kdc');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '7', '언어', 'kdc'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '7' AND group_code = 'kdc');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '8', '문학', 'kdc'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '8' AND group_code = 'kdc');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '9', '역사', 'kdc'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '9' AND group_code = 'kdc');

-- 2. 지역 코드 그룹 및 데이터 추가
INSERT INTO code_groups (group_code, group_name)
SELECT 'region', '지역코드'
WHERE NOT EXISTS (SELECT 1 FROM code_groups WHERE group_code = 'region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '11', '서울특별시', 'region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11' AND group_code = 'region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '21', '부산광역시', 'region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '21' AND group_code = 'region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '22', '대구광역시', 'region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '22' AND group_code = 'region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '23', '인천광역시', 'region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '23' AND group_code = 'region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '24', '광주광역시', 'region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '24' AND group_code = 'region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '25', '대전광역시', 'region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '25' AND group_code = 'region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '26', '울산광역시', 'region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '26' AND group_code = 'region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '29', '세종특별자치시', 'region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '29' AND group_code = 'region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31', '경기도', 'region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31' AND group_code = 'region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '32', '강원특별자치도', 'region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '32' AND group_code = 'region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '33', '충청북도', 'region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '33' AND group_code = 'region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '34', '충청남도', 'region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '34' AND group_code = 'region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '35', '전북특별자치도', 'region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '35' AND group_code = 'region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '36', '전라남도', 'region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '36' AND group_code = 'region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '37', '경상북도', 'region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37' AND group_code = 'region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '38', '경상남도', 'region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '38' AND group_code = 'region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '39', '제주특별자치도', 'region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '39' AND group_code = 'region');

-- 3. 세부지역 코드 그룹 및 데이터 추가
INSERT INTO code_groups (group_code, group_name)
SELECT 'dtl_region', '세부지역코드'
WHERE NOT EXISTS (SELECT 1 FROM code_groups WHERE group_code = 'dtl_region');

-- 서울특별시
INSERT INTO codes (code_value, code_name, group_code)
SELECT '11010', '종로구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11010' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '11020', '중구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11020' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '11030', '용산구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11030' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '11040', '성동구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11040' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '11050', '광진구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11050' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '11060', '동대문구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11060' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '11070', '중랑구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11070' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '11080', '성북구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11080' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '11090', '강북구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11090' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '11100', '도봉구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11100' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '11110', '노원구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11110' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '11120', '은평구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11120' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '11130', '서대문구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11130' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '11140', '마포구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11140' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '11150', '양천구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11150' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '11160', '강서구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11160' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '11170', '구로구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11170' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '11180', '금천구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11180' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '11190', '영등포구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11190' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '11200', '동작구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11200' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '11210', '관악구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11210' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '11220', '서초구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11220' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '11230', '강남구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11230' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '11240', '송파구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11240' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '11250', '강동구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '11250' AND group_code = 'dtl_region');

-- 부산광역시
INSERT INTO codes (code_value, code_name, group_code)
SELECT '21010', '중구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '21010' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '21020', '서구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '21020' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '21030', '동구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '21030' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '21040', '영도구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '21040' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '21050', '부산진구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '21050' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '21060', '동래구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '21060' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '21070', '남구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '21070' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '21080', '북구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '21080' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '21090', '해운대구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '21090' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '21100', '사하구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '21100' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '21110', '금정구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '21110' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '21120', '강서구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '21120' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '21130', '연제구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '21130' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '21140', '수영구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '21140' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '21150', '사상구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '21150' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '21310', '기장군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '21310' AND group_code = 'dtl_region');

-- 대구광역시
INSERT INTO codes (code_value, code_name, group_code)
SELECT '22010', '중구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '22010' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '22020', '동구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '22020' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '22030', '서구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '22030' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '22040', '남구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '22040' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '22050', '북구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '22050' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '22060', '수성구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '22060' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '22070', '달서구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '22070' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '22310', '달성군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '22310' AND group_code = 'dtl_region');

-- 인천광역시
INSERT INTO codes (code_value, code_name, group_code)
SELECT '23010', '중구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '23010' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '23020', '동구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '23020' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '23030', '남구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '23030' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '23040', '연수구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '23040' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '23050', '남동구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '23050' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '23060', '부평구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '23060' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '23070', '계양구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '23070' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '23080', '서구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '23080' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '23310', '강화군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '23310' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '23320', '옹진군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '23320' AND group_code = 'dtl_region');

-- 광주광역시
INSERT INTO codes (code_value, code_name, group_code)
SELECT '24010', '동구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '24010' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '24020', '서구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '24020' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '24030', '남구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '24030' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '24040', '북구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '24040' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '24050', '광산구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '24050' AND group_code = 'dtl_region');

-- 대전광역시
INSERT INTO codes (code_value, code_name, group_code)
SELECT '25010', '동구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '25010' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '25020', '중구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '25020' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '25030', '서구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '25030' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '25040', '유성구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '25040' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '25050', '대덕구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '25050' AND group_code = 'dtl_region');

-- 울산광역시
INSERT INTO codes (code_value, code_name, group_code)
SELECT '26010', '중구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '26010' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '26020', '남구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '26020' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '26030', '동구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '26030' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '26040', '북구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '26040' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '26310', '울주군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '26310' AND group_code = 'dtl_region');

-- 세종특별자치시
INSERT INTO codes (code_value, code_name, group_code)
SELECT '29010', '세종시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '29010' AND group_code = 'dtl_region');

-- 경기도
INSERT INTO codes (code_value, code_name, group_code)
SELECT '31010', '수원시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31010' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31011', '수원시 장안구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31011' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31012', '수원시 권선구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31012' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31013', '수원시 팔달구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31013' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31014', '수원시 영통구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31014' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31020', '성남시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31020' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31021', '성남시 수정구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31021' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31022', '성남시 중원구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31022' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31023', '성남시 분당구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31023' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31030', '의정부시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31030' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31040', '안양시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31040' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31041', '안양시 만안구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31041' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31042', '안양시 동안구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31042' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31050', '부천시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31050' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31060', '광명시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31060' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31070', '평택시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31070' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31080', '동두천시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31080' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31090', '안산시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31090' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31091', '안산시 상록구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31091' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31092', '안산시 단원구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31092' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31100', '고양시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31100' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31101', '고양시 덕양구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31101' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31103', '고양시 일산동구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31103' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31104', '고양시 일산서구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31104' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31110', '과천시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31110' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31120', '구리시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31120' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31130', '남양주시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31130' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31140', '오산시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31140' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31150', '시흥시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31150' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31160', '군포시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31160' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31170', '의왕시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31170' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31180', '하남시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31180' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31190', '용인시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31190' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31191', '용인시 처인구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31191' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31192', '용인시 기흥구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31192' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31193', '용인시 수지구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31193' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31200', '파주시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31200' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31210', '이천시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31210' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31220', '안성시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31220' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31230', '김포시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31230' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31240', '화성시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31240' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31250', '광주시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31250' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31260', '양주시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31260' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31270', '포천시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31270' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31280', '여주시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31280' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31350', '연천군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31350' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31370', '가평군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31370' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '31380', '양평군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '31380' AND group_code = 'dtl_region');

-- 강원특별자치도
INSERT INTO codes (code_value, code_name, group_code)
SELECT '32010', '춘천시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '32010' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '32020', '원주시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '32020' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '32030', '강릉시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '32030' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '32040', '동해시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '32040' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '32050', '태백시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '32050' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '32060', '속초시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '32060' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '32070', '삼척시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '32070' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '32310', '홍천군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '32310' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '32320', '횡성군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '32320' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '32330', '영월군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '32330' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '32340', '평창군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '32340' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '32350', '정선군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '32350' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '32360', '철원군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '32360' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '32370', '화천군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '32370' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '32380', '양구군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '32380' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '32390', '인제군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '32390' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '32400', '고성군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '32400' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '32410', '양양군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '32410' AND group_code = 'dtl_region');

-- 충청북도
INSERT INTO codes (code_value, code_name, group_code)
SELECT '33020', '충주시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '33020' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '33030', '제천시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '33030' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '33040', '청주시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '33040' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '33041', '청주시 상당구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '33041' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '33042', '청주시 서원구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '33042' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '33043', '청주시 흥덕구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '33043' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '33044', '청주시 청원구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '33044' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '33320', '보은군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '33320' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '33330', '옥천군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '33330' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '33340', '영동군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '33340' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '33350', '진천군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '33350' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '33360', '괴산군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '33360' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '33370', '음성군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '33370' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '33380', '단양군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '33380' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '33390', '증평군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '33390' AND group_code = 'dtl_region');

-- 충청남도
INSERT INTO codes (code_value, code_name, group_code)
SELECT '34010', '천안시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '34010' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '34011', '천안시 동남구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '34011' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '34012', '천안시 서북구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '34012' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '34020', '공주시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '34020' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '34030', '보령시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '34030' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '34040', '아산시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '34040' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '34050', '서산시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '34050' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '34060', '논산시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '34060' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '34070', '계룡시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '34070' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '34080', '당진시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '34080' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '34310', '금산군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '34310' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '34330', '부여군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '34330' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '34340', '서천군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '34340' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '34350', '청양군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '34350' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '34360', '홍성군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '34360' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '34370', '예산군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '34370' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '34380', '태안군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '34380' AND group_code = 'dtl_region');

-- 전북특별자치도
INSERT INTO codes (code_value, code_name, group_code)
SELECT '35010', '전주시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '35010' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '35011', '전주시 완산구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '35011' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '35012', '전주시 덕진구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '35012' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '35020', '군산시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '35020' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '35030', '익산시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '35030' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '35040', '정읍시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '35040' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '35050', '남원시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '35050' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '35060', '김제시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '35060' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '35310', '완주군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '35310' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '35320', '진안군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '35320' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '35330', '무주군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '35330' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '35340', '장수군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '35340' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '35350', '임실군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '35350' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '35360', '순창군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '35360' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '35370', '고창군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '35370' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '35380', '부안군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '35380' AND group_code = 'dtl_region');

-- 전라남도
INSERT INTO codes (code_value, code_name, group_code)
SELECT '36010', '목포시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '36010' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '36020', '여수시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '36020' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '36030', '순천시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '36030' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '36040', '나주시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '36040' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '36060', '광양시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '36060' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '36310', '담양군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '36310' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '36320', '곡성군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '36320' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '36330', '구례군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '36330' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '36350', '고흥군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '36350' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '36360', '보성군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '36360' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '36370', '화순군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '36370' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '36380', '장흥군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '36380' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '36390', '강진군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '36390' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '36400', '해남군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '36400' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '36410', '영암군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '36410' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '36420', '무안군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '36420' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '36430', '함평군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '36430' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '36440', '영광군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '36440' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '36450', '장성군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '36450' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '36460', '완도군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '36460' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '36470', '진도군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '36470' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '36480', '신안군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '36480' AND group_code = 'dtl_region');

-- 경상북도
INSERT INTO codes (code_value, code_name, group_code)
SELECT '37010', '포항시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37010' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '37011', '포항시 남구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37011' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '37012', '포항시 북구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37012' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '37020', '경주시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37020' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '37030', '김천시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37030' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '37040', '안동시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37040' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '37050', '구미시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37050' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '37060', '영주시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37060' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '37070', '영천시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37070' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '37080', '상주시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37080' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '37090', '문경시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37090' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '37100', '경산시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37100' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '37310', '군위군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37310' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '37320', '의성군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37320' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '37330', '청송군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37330' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '37340', '영양군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37340' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '37350', '영덕군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37350' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '37360', '청도군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37360' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '37370', '고령군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37370' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '37380', '성주군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37380' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '37390', '칠곡군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37390' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '37400', '예천군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37400' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '37410', '봉화군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37410' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '37420', '울진군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37420' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '37430', '울릉군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '37430' AND group_code = 'dtl_region');

-- 경상남도
INSERT INTO codes (code_value, code_name, group_code)
SELECT '38030', '진주시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '38030' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '38050', '통영시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '38050' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '38060', '사천시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '38060' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '38070', '김해시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '38070' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '38080', '밀양시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '38080' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '38090', '거제시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '38090' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '38100', '양산시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '38100' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '38110', '창원시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '38110' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '38111', '창원시 의창구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '38111' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '38112', '창원시 성산구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '38112' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '38113', '창원시 마산합포구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '38113' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '38114', '창원시 마산회원구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '38114' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '38115', '창원시 진해구', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '38115' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '38310', '의령군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '38310' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '38320', '함안군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '38320' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '38330', '창녕군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '38330' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '38340', '고성군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '38340' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '38350', '남해군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '38350' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '38360', '하동군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '38360' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '38370', '산청군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '38370' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '38380', '함양군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '38380' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '38390', '거창군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '38390' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '38400', '합천군', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '38400' AND group_code = 'dtl_region');

-- 제주특별자치도
INSERT INTO codes (code_value, code_name, group_code)
SELECT '39010', '제주시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '39010' AND group_code = 'dtl_region');

INSERT INTO codes (code_value, code_name, group_code)
SELECT '39020', '서귀포시', 'dtl_region'
WHERE NOT EXISTS (SELECT 1 FROM codes WHERE code_value = '39020' AND group_code = 'dtl_region');

COMMIT;