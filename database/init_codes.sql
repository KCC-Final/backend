-- ================================================
-- init_codes_ai_ci.sql
-- 완전 통합 초기화 스크립트 (utf8mb4_0900_ai_ci 통일 + FK 정리 + 중복 데이터 제거)
-- ================================================
-- 0. 데이터베이스 생성 및 선택
CREATE DATABASE IF NOT EXISTS kcc
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

USE kcc;

-- 1. 세션 문자셋 통일
SET NAMES utf8mb4 COLLATE utf8mb4_0900_ai_ci;
SET character_set_client = utf8mb4;
SET character_set_connection = utf8mb4;
SET character_set_results = utf8mb4;

ALTER DATABASE kcc CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- ================================================
-- 1. USERS
-- ================================================
CREATE TABLE IF NOT EXISTS users
(
    user_id
    VARCHAR
(
    50
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '사용자 아이디',
    password VARCHAR
(
    255
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '비밀번호',
    email VARCHAR
(
    100
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '이메일',
    nickname VARCHAR
(
    50
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '닉네임',
    profile_image LONGBLOB NULL COMMENT '프로필 이미지',
    introduction VARCHAR
(
    255
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '자기소개',
    gender CHAR
(
    1
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '성별',
    name VARCHAR
(
    50
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '이름',
    birth DATE NULL COMMENT '생년월일',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '계정 생성일',
    withdrawal_status BOOLEAN DEFAULT FALSE COMMENT '회원탈퇴 상태 (TRUE=탈퇴, FALSE=활성)',
    withdrawal_date DATETIME NULL COMMENT '회원탈퇴일',
    pwd_changed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '비밀번호 수정일',
    check_privacy BOOLEAN NOT NULL COMMENT '개인정보 이용 동의',
    check_service BOOLEAN NOT NULL COMMENT '서비스 이용 동의',
    email_verified BOOLEAN DEFAULT FALSE COMMENT '회원 인증 상태 (TRUE = 인증, FALSE = 미인증)',
    PRIMARY KEY (user_id),
    UNIQUE KEY uq_email (email),
    CONSTRAINT chk_users_gender CHECK (gender IS NULL OR UPPER(gender) IN ('F', 'M'))
    ) COMMENT='사용자 테이블';

-- 2. 팔로우 테이블 (users 참조)
CREATE TABLE IF NOT EXISTS follows (
                                       follow_id INT NOT NULL AUTO_INCREMENT COMMENT '팔로우 아이디',
                                       created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '팔로우 생성일',
                                       follower VARCHAR(50) NOT NULL COMMENT '팔로우 한 사람',
    followed VARCHAR(50) NOT NULL COMMENT '팔로우 받은 사람',
    PRIMARY KEY (follow_id),
    CONSTRAINT fk_follower_user_id FOREIGN KEY (follower) REFERENCES users(user_id),
    CONSTRAINT fk_followed_user_id FOREIGN KEY (followed) REFERENCES users(user_id)
    ) COMMENT = '팔로우 테이블';

-- 3. 코드 그룹 테이블 생성 (기본 테이블, 외래키 없음)
CREATE TABLE IF NOT EXISTS code_groups (
                                           group_code VARCHAR(50) NOT NULL COMMENT '그룹코드',
    group_name VARCHAR(100) NULL COMMENT '그룹이름',
    delete_status BOOLEAN DEFAULT FALSE COMMENT '코드 삭제 상태 (TRUE=삭제, FALSE=활성)',
    PRIMARY KEY (group_code)
    ) COMMENT='코드그룹';

-- 4. 코드 테이블 생성 (code_groups 참조)
CREATE TABLE IF NOT EXISTS codes (
                                     code_id INT NOT NULL AUTO_INCREMENT COMMENT '코드 ID',
                                     code_value VARCHAR(50) NOT NULL COMMENT '코드값',
    code_name VARCHAR(100) NOT NULL COMMENT '표시이름',
    group_code VARCHAR(50) NOT NULL COMMENT '그룹코드',
    delete_status BOOLEAN DEFAULT FALSE COMMENT '코드 삭제 상태 (TRUE=삭제, FALSE=활성)',
    PRIMARY KEY (code_id),
    CONSTRAINT fk_codes_group FOREIGN KEY (group_code) REFERENCES code_groups(group_code)
    ) COMMENT='코드';

-- 5. 독후감 테이블 생성 (users, codes 참조)
CREATE TABLE IF NOT EXISTS reviews (
                                       review_id INT NOT NULL AUTO_INCREMENT COMMENT '독후감 ID',
                                       ISBN VARCHAR(20) NOT NULL COMMENT 'ISBN',
    review_title VARCHAR(200) NOT NULL COMMENT '제목',
    review_content TEXT NOT NULL COMMENT '내용',
    secret BOOLEAN NULL DEFAULT FALSE COMMENT '비밀글',
    status BOOLEAN NULL DEFAULT TRUE COMMENT '삭제 상태',
    temporary BOOLEAN NULL DEFAULT FALSE COMMENT '임시 저장 여부',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '작성일',
    updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    user_id VARCHAR(50) NOT NULL COMMENT '사용자 ID',
    category VARCHAR(50) NOT NULL COMMENT '도서 카테고리',
    PRIMARY KEY (review_id),
    CONSTRAINT fk_reviews_user FOREIGN KEY (user_id) REFERENCES users(user_id)
    ) COMMENT='독후감';

-- 6. 좋아요 테이블 생성 (users, reviews 참조)
CREATE TABLE IF NOT EXISTS likes (
                                     user_id VARCHAR(50) NOT NULL COMMENT '사용자 ID',
    review_id INT NOT NULL COMMENT '독후감 ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    PRIMARY KEY (user_id, review_id),
    CONSTRAINT fk_likes_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_likes_review FOREIGN KEY (review_id) REFERENCES reviews(review_id)
    ) COMMENT='좋아요';

-- 독후감 댓글 테이블 생성 (reviews, users 참조, 자기 참조)
-- CASCADE DELETE 적용: 부모 댓글 삭제 시 대댓글 자동 삭제, 리뷰 삭제 시 댓글 자동 삭제
CREATE TABLE IF NOT EXISTS review_comments (
                                               comment_id  INT          NOT NULL AUTO_INCREMENT COMMENT '댓글 ID',
                                               content     VARCHAR(500) NOT NULL COMMENT '댓글 내용',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '작성일',
    updated_at  DATETIME     NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    review_id   INT          NOT NULL COMMENT '독후감 ID',
    user_id     VARCHAR(50)  NOT NULL COMMENT '사용자 ID',
    parent_id   INT          NULL COMMENT '부모 댓글 ID',
    PRIMARY KEY (comment_id),
    CONSTRAINT fk_comments_review FOREIGN KEY (review_id)
    REFERENCES reviews(review_id)
                                  ON DELETE CASCADE,  -- 리뷰 삭제 시 해당 리뷰의 모든 댓글 자동 삭제
    CONSTRAINT fk_comments_user FOREIGN KEY (user_id)
    REFERENCES users(user_id),
    CONSTRAINT fk_comments_parent FOREIGN KEY (parent_id)
    REFERENCES review_comments(comment_id)
                                  ON DELETE CASCADE  -- 부모 댓글 삭제 시 대댓글(자식 댓글) 자동 삭제
    ) COMMENT='독후감 댓글';

-- 8. 도전과제 뱃지 테이블 (기본 테이블, 외래키 없음)
CREATE TABLE IF NOT EXISTS badges (
                                      badge_id INT NOT NULL AUTO_INCREMENT COMMENT '뱃지 ID',
                                      badge_name VARCHAR(100) NOT NULL COMMENT '뱃지명',
    badge_description VARCHAR(255) NULL COMMENT '뱃지 설명',
    badge_conditions INT NOT NULL COMMENT '달성 조건',
    PRIMARY KEY (badge_id)
    ) COMMENT='도전과제 뱃지';

-- 9. 도전과제 달성 내역 테이블 (badges, users 참조)
CREATE TABLE IF NOT EXISTS challenge (
                                         challenge_id INT NOT NULL AUTO_INCREMENT COMMENT '챌린지 ID',
                                         badge_id INT NOT NULL COMMENT '뱃지 ID',
                                         user_id VARCHAR(50) NOT NULL COMMENT '사용자 ID',
    succeeded_at DATETIME NULL COMMENT '성공 시점',
    PRIMARY KEY (challenge_id),
    CONSTRAINT fk_challenge_badge FOREIGN KEY (badge_id) REFERENCES badges(badge_id),
    CONSTRAINT fk_challenge_user FOREIGN KEY (user_id) REFERENCES users(user_id)
    ) COMMENT='도전과제 달성 내역';

-- 11. 알림 테이블 (users 참조)
CREATE TABLE IF NOT EXISTS alerts (
                                      alert_id INT NOT NULL AUTO_INCREMENT COMMENT '알림 식별값',
                                      type VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '알림 구분',
    content VARCHAR(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '내용',
    sent_at DATETIME NULL COMMENT '전송일',
    sender_type VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '알림 발생자 분류',
    sender_id INT NOT NULL COMMENT '알림 발생자 ID',
    detail_sender_id INT NULL COMMENT '알림 발생자 세부 ID',
    user_id VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '사용자 ID',
    PRIMARY KEY (alert_id),
    CONSTRAINT fk_alerts_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='알림';


-- ================================================
-- 10. 오늘의 한 문장 테이블 (기본 테이블, 외래키 없음)
-- ================================================
CREATE TABLE IF NOT EXISTS sentences (
  sentence_id INT NOT NULL AUTO_INCREMENT COMMENT '오늘의 문장 ID',
  sentence_content VARCHAR(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '문장',
  ISBN VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'ISBN',
  selected_date DATE NULL COMMENT '오늘의 문장으로 선택된 날짜',
  PRIMARY KEY (sentence_id)
) COMMENT='오늘의 한 문장'
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE INDEX idx_sentences_selected_date ON sentences (selected_date);

-- 11. 알림 테이블 (users 참조)
CREATE TABLE IF NOT EXISTS alerts (
                                      alert_id INT NOT NULL AUTO_INCREMENT COMMENT '알림 식별값',
                                      type VARCHAR(50) NULL COMMENT '알림 구분',
    content VARCHAR(500) NULL COMMENT '내용',
    sent_at DATETIME NULL COMMENT '전송일',
    sender_type VARCHAR(50) NULL COMMENT '알림 발생자 분류',
    sender_id INT NOT NULL COMMENT '알림 발생자 ID',
    detail_sender_id INT NULL COMMENT '알림 발생자 세부 ID',
    user_id VARCHAR(50) NOT NULL COMMENT '사용자 ID',
    PRIMARY KEY (alert_id),
    CONSTRAINT fk_alerts_user FOREIGN KEY (user_id) REFERENCES users(user_id)
    ) COMMENT='알림';

-- 12. 책장 테이블 (users 참조)
CREATE TABLE IF NOT EXISTS bookshelf (
                                         bookshelf_id INT NOT NULL AUTO_INCREMENT COMMENT '책장 ID',
                                         name VARCHAR(100) NULL COMMENT '책장명',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    user_id VARCHAR(50) NOT NULL COMMENT '사용자 ID',
    PRIMARY KEY (bookshelf_id),
    CONSTRAINT fk_bookshelf_user FOREIGN KEY (user_id) REFERENCES users(user_id)
    ) COMMENT='책장';

-- 13. 스크랩 도서 테이블 (bookshelf 참조)
CREATE TABLE IF NOT EXISTS book (
                                    ISBN VARCHAR(20) NOT NULL COMMENT 'ISBN',
    bookshelf_id INT NOT NULL COMMENT '책장 ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    PRIMARY KEY (ISBN, bookshelf_id),
    CONSTRAINT fk_book_bookshelf FOREIGN KEY (bookshelf_id) REFERENCES bookshelf(bookshelf_id)
    ) COMMENT='스크랩 도서';

-- 14. 독서모임 테이블 (users, codes 참조)
CREATE TABLE IF NOT EXISTS `groups` (
                                        group_id INT NOT NULL AUTO_INCREMENT COMMENT '게시글 ID',
                                        group_name VARCHAR(100) NULL COMMENT '모임명',
    book_title VARCHAR(200) NULL COMMENT '도서 제목',
    headcount_min INT NULL COMMENT '모집 최소 인원',
    headcount_max INT NULL COMMENT '모집 최대 인원',
    content VARCHAR(1000) NULL COMMENT '내용',
    style VARCHAR(50) NULL COMMENT '진행 방식',
    status BOOLEAN NOT NULL COMMENT '모집 상태',
    end_date DATETIME NOT NULL COMMENT '모집 종료일',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '작성일',
    updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    user_id VARCHAR(50) NOT NULL COMMENT '사용자 ID',
    code_id INT NOT NULL COMMENT '코드 ID',
    PRIMARY KEY (group_id),
    CONSTRAINT fk_groups_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_groups_code FOREIGN KEY (code_id) REFERENCES codes(code_id)
    ) COMMENT='독서모임';

-- 15. 독서모임 스크랩 테이블 (users, groups 참조)
CREATE TABLE IF NOT EXISTS group_scraps (
                                            user_id VARCHAR(50) NOT NULL COMMENT '사용자 ID',
    group_id INT NOT NULL COMMENT '게시글 ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    PRIMARY KEY (user_id, group_id),
    CONSTRAINT fk_group_scraps_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_group_scraps_group FOREIGN KEY (group_id) REFERENCES `groups`(group_id)
    ) COMMENT='독서모임 스크랩';

-- 16. 독서모임 댓글 테이블 (groups, users 참조, 자기 참조)
CREATE TABLE IF NOT EXISTS group_comments (
                                              comment_id INT NOT NULL AUTO_INCREMENT COMMENT '댓글 ID',
                                              content VARCHAR(500) NULL COMMENT '댓글 내용',
    created_at DATETIME NULL COMMENT '작성일',
    updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    flag BOOLEAN NULL COMMENT '비밀 댓글',
    group_id INT NOT NULL COMMENT '게시글 ID',
    user_id VARCHAR(50) NOT NULL COMMENT '사용자 ID',
    parent_id INT NULL COMMENT '부모 댓글',
    PRIMARY KEY (comment_id),
    CONSTRAINT fk_group_comments_group FOREIGN KEY (group_id) REFERENCES `groups`(group_id),
    CONSTRAINT fk_group_comments_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_group_comments_parent FOREIGN KEY (parent_id) REFERENCES group_comments(comment_id)
    ) COMMENT='독서모임 댓글';

-- 코드 그룹 데이터 삽입 (중복 무시)

-- 1. 지역 코드 그룹 및 데이터 추가
INSERT IGNORE INTO code_groups (group_code, group_name) VALUES ('region', '지역코드');

INSERT IGNORE INTO codes (code_value, code_name, group_code) VALUES
('11', '서울특별시', 'region'),
('21', '부산광역시', 'region'),
('22', '대구광역시', 'region'),
('23', '인천광역시', 'region'),
('24', '광주광역시', 'region'),
('25', '대전광역시', 'region'),
('26', '울산광역시', 'region'),
('29', '세종특별자치시', 'region'),
('31', '경기도', 'region'),
('32', '강원특별자치도', 'region'),
('33', '충청북도', 'region'),
('34', '충청남도', 'region'),
('35', '전북특별자치도', 'region'),
('36', '전라남도', 'region'),
('37', '경상북도', 'region'),
('38', '경상남도', 'region'),
('39', '제주특별자치도', 'region');

-- 2. 세부지역 코드 그룹 및 데이터 추가 (전체)
INSERT IGNORE INTO code_groups (group_code, group_name) VALUES ('dtl_region', '세부지역코드');

INSERT IGNORE INTO codes (code_value, code_name, group_code) VALUES
-- 서울특별시
('11010', '종로구', 'dtl_region'),
('11020', '중구', 'dtl_region'),
('11030', '용산구', 'dtl_region'),
('11040', '성동구', 'dtl_region'),
('11050', '광진구', 'dtl_region'),
('11060', '동대문구', 'dtl_region'),
('11070', '중랑구', 'dtl_region'),
('11080', '성북구', 'dtl_region'),
('11090', '강북구', 'dtl_region'),
('11100', '도봉구', 'dtl_region'),
('11110', '노원구', 'dtl_region'),
('11120', '은평구', 'dtl_region'),
('11130', '서대문구', 'dtl_region'),
('11140', '마포구', 'dtl_region'),
('11150', '양천구', 'dtl_region'),
('11160', '강서구', 'dtl_region'),
('11170', '구로구', 'dtl_region'),
('11180', '금천구', 'dtl_region'),
('11190', '영등포구', 'dtl_region'),
('11200', '동작구', 'dtl_region'),
('11210', '관악구', 'dtl_region'),
('11220', '서초구', 'dtl_region'),
('11230', '강남구', 'dtl_region'),
('11240', '송파구', 'dtl_region'),
('11250', '강동구', 'dtl_region'),

-- 부산광역시
('21010', '중구', 'dtl_region'),
('21020', '서구', 'dtl_region'),
('21030', '동구', 'dtl_region'),
('21040', '영도구', 'dtl_region'),
('21050', '부산진구', 'dtl_region'),
('21060', '동래구', 'dtl_region'),
('21070', '남구', 'dtl_region'),
('21080', '북구', 'dtl_region'),
('21090', '해운대구', 'dtl_region'),
('21100', '사하구', 'dtl_region'),
('21110', '금정구', 'dtl_region'),
('21120', '강서구', 'dtl_region'),
('21130', '연제구', 'dtl_region'),
('21140', '수영구', 'dtl_region'),
('21150', '사상구', 'dtl_region'),
('21310', '기장군', 'dtl_region'),

-- 대구광역시
('22010', '중구', 'dtl_region'),
('22020', '동구', 'dtl_region'),
('22030', '서구', 'dtl_region'),
('22040', '남구', 'dtl_region'),
('22050', '북구', 'dtl_region'),
('22060', '수성구', 'dtl_region'),
('22070', '달서구', 'dtl_region'),
('22310', '달성군', 'dtl_region'),

-- 인천광역시
('23010', '중구', 'dtl_region'),
('23020', '동구', 'dtl_region'),
('23030', '남구', 'dtl_region'),
('23040', '연수구', 'dtl_region'),
('23050', '남동구', 'dtl_region'),
('23060', '부평구', 'dtl_region'),
('23070', '계양구', 'dtl_region'),
('23080', '서구', 'dtl_region'),
('23310', '강화군', 'dtl_region'),
('23320', '옹진군', 'dtl_region'),

-- 광주광역시
('24010', '동구', 'dtl_region'),
('24020', '서구', 'dtl_region'),
('24030', '남구', 'dtl_region'),
('24040', '북구', 'dtl_region'),
('24050', '광산구', 'dtl_region'),

-- 대전광역시
('25010', '동구', 'dtl_region'),
('25020', '중구', 'dtl_region'),
('25030', '서구', 'dtl_region'),
('25040', '유성구', 'dtl_region'),
('25050', '대덕구', 'dtl_region'),

-- 울산광역시
('26010', '중구', 'dtl_region'),
('26020', '남구', 'dtl_region'),
('26030', '동구', 'dtl_region'),
('26040', '북구', 'dtl_region'),
('26310', '울주군', 'dtl_region'),

-- 세종특별자치시
('29010', '세종시', 'dtl_region'),

-- 경기도
('31010', '수원시', 'dtl_region'),
('31011', '수원시 장안구', 'dtl_region'),
('31012', '수원시 권선구', 'dtl_region'),
('31013', '수원시 팔달구', 'dtl_region'),
('31014', '수원시 영통구', 'dtl_region'),
('31020', '성남시', 'dtl_region'),
('31021', '성남시 수정구', 'dtl_region'),
('31022', '성남시 중원구', 'dtl_region'),
('31023', '성남시 분당구', 'dtl_region'),
('31030', '의정부시', 'dtl_region'),
('31040', '안양시', 'dtl_region'),
('31041', '안양시 만안구', 'dtl_region'),
('31042', '안양시 동안구', 'dtl_region'),
('31050', '부천시', 'dtl_region'),
('31060', '광명시', 'dtl_region'),
('31070', '평택시', 'dtl_region'),
('31080', '동두천시', 'dtl_region'),
('31090', '안산시', 'dtl_region'),
('31091', '안산시 상록구', 'dtl_region'),
('31092', '안산시 단원구', 'dtl_region'),
('31100', '고양시', 'dtl_region'),
('31101', '고양시 덕양구', 'dtl_region'),
('31103', '고양시 일산동구', 'dtl_region'),
('31104', '고양시 일산서구', 'dtl_region'),
('31110', '과천시', 'dtl_region'),
('31120', '구리시', 'dtl_region'),
('31130', '남양주시', 'dtl_region'),
('31140', '오산시', 'dtl_region'),
('31150', '시흥시', 'dtl_region'),
('31160', '군포시', 'dtl_region'),
('31170', '의왕시', 'dtl_region'),
('31180', '하남시', 'dtl_region'),
('31190', '용인시', 'dtl_region'),
('31191', '용인시 처인구', 'dtl_region'),
('31192', '용인시 기흥구', 'dtl_region'),
('31193', '용인시 수지구', 'dtl_region'),
('31200', '파주시', 'dtl_region'),
('31210', '이천시', 'dtl_region'),
('31220', '안성시', 'dtl_region'),
('31230', '김포시', 'dtl_region'),
('31240', '화성시', 'dtl_region'),
('31250', '광주시', 'dtl_region'),
('31260', '양주시', 'dtl_region'),
('31270', '포천시', 'dtl_region'),
('31280', '여주시', 'dtl_region'),
('31350', '연천군', 'dtl_region'),
('31370', '가평군', 'dtl_region'),
('31380', '양평군', 'dtl_region'),

-- 강원특별자치도
('32010', '춘천시', 'dtl_region'),
('32020', '원주시', 'dtl_region'),
('32030', '강릉시', 'dtl_region'),
('32040', '동해시', 'dtl_region'),
('32050', '태백시', 'dtl_region'),
('32060', '속초시', 'dtl_region'),
('32070', '삼척시', 'dtl_region'),
('32310', '홍천군', 'dtl_region'),
('32320', '횡성군', 'dtl_region'),
('32330', '영월군', 'dtl_region'),
('32340', '평창군', 'dtl_region'),
('32350', '정선군', 'dtl_region'),
('32360', '철원군', 'dtl_region'),
('32370', '화천군', 'dtl_region'),
('32380', '양구군', 'dtl_region'),
('32390', '인제군', 'dtl_region'),
('32400', '고성군', 'dtl_region'),
('32410', '양양군', 'dtl_region'),

-- 충청북도
('33020', '충주시', 'dtl_region'),
('33030', '제천시', 'dtl_region'),
('33040', '청주시', 'dtl_region'),
('33041', '청주시 상당구', 'dtl_region'),
('33042', '청주시 서원구', 'dtl_region'),
('33043', '청주시 흥덕구', 'dtl_region'),
('33044', '청주시 청원구', 'dtl_region'),
('33320', '보은군', 'dtl_region'),
('33330', '옥천군', 'dtl_region'),
('33340', '영동군', 'dtl_region'),
('33350', '진천군', 'dtl_region'),
('33360', '괴산군', 'dtl_region'),
('33370', '음성군', 'dtl_region'),
('33380', '단양군', 'dtl_region'),
('33390', '증평군', 'dtl_region'),

-- 충청남도
('34010', '천안시', 'dtl_region'),
('34011', '천안시 동남구', 'dtl_region'),
('34012', '천안시 서북구', 'dtl_region'),
('34020', '공주시', 'dtl_region'),
('34030', '보령시', 'dtl_region'),
('34040', '아산시', 'dtl_region'),
('34050', '서산시', 'dtl_region'),
('34060', '논산시', 'dtl_region'),
('34070', '계룡시', 'dtl_region'),
('34080', '당진시', 'dtl_region'),
('34310', '금산군', 'dtl_region'),
('34330', '부여군', 'dtl_region'),
('34340', '서천군', 'dtl_region'),
('34350', '청양군', 'dtl_region'),
('34360', '홍성군', 'dtl_region'),
('34370', '예산군', 'dtl_region'),
('34380', '태안군', 'dtl_region'),

-- 전북특별자치도
('35010', '전주시', 'dtl_region'),
('35011', '전주시 완산구', 'dtl_region'),
('35012', '전주시 덕진구', 'dtl_region'),
('35020', '군산시', 'dtl_region'),
('35030', '익산시', 'dtl_region'),
('35040', '정읍시', 'dtl_region'),
('35050', '남원시', 'dtl_region'),
('35060', '김제시', 'dtl_region'),
('35310', '완주군', 'dtl_region'),
('35320', '진안군', 'dtl_region'),
('35330', '무주군', 'dtl_region'),
('35340', '장수군', 'dtl_region'),
('35350', '임실군', 'dtl_region'),
('35360', '순창군', 'dtl_region'),
('35370', '고창군', 'dtl_region'),
('35380', '부안군', 'dtl_region'),

-- 전라남도
('36010', '목포시', 'dtl_region'),
('36020', '여수시', 'dtl_region'),
('36030', '순천시', 'dtl_region'),
('36040', '나주시', 'dtl_region'),
('36060', '광양시', 'dtl_region'),
('36310', '담양군', 'dtl_region'),
('36320', '곡성군', 'dtl_region'),
('36330', '구례군', 'dtl_region'),
('36350', '고흥군', 'dtl_region'),
('36360', '보성군', 'dtl_region'),
('36370', '화순군', 'dtl_region'),
('36380', '장흥군', 'dtl_region'),
('36390', '강진군', 'dtl_region'),
('36400', '해남군', 'dtl_region'),
('36410', '영암군', 'dtl_region'),
('36420', '무안군', 'dtl_region'),
('36430', '함평군', 'dtl_region'),
('36440', '영광군', 'dtl_region'),
('36450', '장성군', 'dtl_region'),
('36460', '완도군', 'dtl_region'),
('36470', '진도군', 'dtl_region'),
('36480', '신안군', 'dtl_region'),

-- 경상북도
('37010', '포항시', 'dtl_region'),
('37011', '포항시 남구', 'dtl_region'),
('37012', '포항시 북구', 'dtl_region'),
('37020', '경주시', 'dtl_region'),
('37030', '김천시', 'dtl_region'),
('37040', '안동시', 'dtl_region'),
('37050', '구미시', 'dtl_region'),
('37060', '영주시', 'dtl_region'),
('37070', '영천시', 'dtl_region'),
('37080', '상주시', 'dtl_region'),
('37090', '문경시', 'dtl_region'),
('37100', '경산시', 'dtl_region'),
('37310', '군위군', 'dtl_region'),
('37320', '의성군', 'dtl_region'),
('37330', '청송군', 'dtl_region'),
('37340', '영양군', 'dtl_region'),
('37350', '영덕군', 'dtl_region'),
('37360', '청도군', 'dtl_region'),
('37370', '고령군', 'dtl_region'),
('37380', '성주군', 'dtl_region'),
('37390', '칠곡군', 'dtl_region'),
('37400', '예천군', 'dtl_region'),
('37410', '봉화군', 'dtl_region'),
('37420', '울진군', 'dtl_region'),
('37430', '울릉군', 'dtl_region'),

-- 경상남도
('38030', '진주시', 'dtl_region'),
('38050', '통영시', 'dtl_region'),
('38060', '사천시', 'dtl_region'),
('38070', '김해시', 'dtl_region'),
('38080', '밀양시', 'dtl_region'),
('38090', '거제시', 'dtl_region'),
('38100', '양산시', 'dtl_region'),
('38110', '창원시', 'dtl_region'),
('38111', '창원시 의창구', 'dtl_region'),
('38112', '창원시 성산구', 'dtl_region'),
('38113', '창원시 마산합포구', 'dtl_region'),
('38114', '창원시 마산회원구', 'dtl_region'),
('38115', '창원시 진해구', 'dtl_region'),
('38310', '의령군', 'dtl_region'),
('38320', '함안군', 'dtl_region'),
('38330', '창녕군', 'dtl_region'),
('38340', '고성군', 'dtl_region'),
('38350', '남해군', 'dtl_region'),
('38360', '하동군', 'dtl_region'),
('38370', '산청군', 'dtl_region'),
('38380', '함양군', 'dtl_region'),
('38390', '거창군', 'dtl_region'),
('38400', '합천군', 'dtl_region'),

-- 제주특별자치도
('39010', '제주시', 'dtl_region'),
('39020', '서귀포시', 'dtl_region');

COMMIT;