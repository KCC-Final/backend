USE kcc;

-- ================================================
-- migration_002_sentences.sql
-- sentences 테이블 컬럼 추가 + 중복 없는 데이터 삽입
-- ================================================

-- 1 selected_date 컬럼 추가 (있으면 무시)
ALTER TABLE sentences
ADD COLUMN IF NOT EXISTS selected_date DATE NULL COMMENT '오늘의 문장으로 선택된 날짜'
AFTER ISBN;

-- 2 중복 방지 삽입 구문
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT "부끄럼 많은 생애를 보내 왔습니다. 저는 인간의 삶이라는 것을 도무지 이해할 수 없습니다.", "9788937461033", CURDATE()
WHERE NOT EXISTS (
    SELECT 1 FROM sentences
    WHERE sentence_content = "부끄럼 많은 생애를 보내 왔습니다. 저는 인간의 삶이라는 것을 도무지 이해할 수 없습니다."
      AND ISBN = "9788937461033"
);

INSERT INTO sentences (sentence_content, ISBN)
SELECT "내 삶의 부피는 너무 얇다. 겨자씨 한 알 심을 만한 깊이도 없다. 이렇게 살아도 되는 것일까.", "9788998441012"
WHERE NOT EXISTS (
    SELECT 1 FROM sentences
    WHERE sentence_content = "내 삶의 부피는 너무 얇다. 겨자씨 한 알 심을 만한 깊이도 없다. 이렇게 살아도 되는 것일까."
      AND ISBN = "9788998441012"
);

INSERT INTO sentences (sentence_content, ISBN)
SELECT "소리가 어디에서 오는지, 왜 들리는지 그는 알지 못했지만 야성의 부름은 계속되었다. 숲 속 깊은 곳으로부터 들리는 절체절명의 소리였기에 그는 어디로 그리고 왜라는 물음을 던지지도 않았다.", "9788937426926"
WHERE NOT EXISTS (
    SELECT 1 FROM sentences
    WHERE sentence_content = "소리가 어디에서 오는지, 왜 들리는지 그는 알지 못했지만 야성의 부름은 계속되었다. 숲 속 깊은 곳으로부터 들리는 절체절명의 소리였기에 그는 어디로 그리고 왜라는 물음을 던지지도 않았다."
      AND ISBN = "9788937426926"
);

INSERT INTO sentences (sentence_content, ISBN)
SELECT "법을 지키고 구조되는 것과 사냥을 하고 모든 것을 파괴하는 것 중 어느 편이 좋으냔 말이야?", "9788937460197"
WHERE NOT EXISTS (
    SELECT 1 FROM sentences
    WHERE sentence_content = "법을 지키고 구조되는 것과 사냥을 하고 모든 것을 파괴하는 것 중 어느 편이 좋으냔 말이야?"
      AND ISBN = "9788937460197"
);

COMMIT;
