-- ================================================
-- migration_003_sentences.sql
-- 오늘의 한 문장 (2025-10-13 ~ 2025-11-30) 데이터 추가
-- 기존 migration_002.sql 더미데이터 제거 후 삽입 (중복 방지)
-- ================================================

USE
kcc;

-- 1 기존 migration_002.sql의 더미 sentences 데이터 제거
DELETE
FROM sentences
WHERE sentence_content IN (
                           '부끄럼 많은 생애를 보내 왔습니다. 저는 인간의 삶이라는 것을 도무지 이해할 수 없습니다.',
                           '내 삶의 부피는 너무 얇다. 겨자씨 한 알 심을 만한 깊이도 없다. 이렇게 살아도 되는 것일까.',
                           '소리가 어디에서 오는지, 왜 들리는지 그는 알지 못했지만 야성의 부름은 계속되었다. 숲 속 깊은 곳으로부터 들리는 절체절명의 소리였기에 그는 어디로 그리고 왜라는 물음을 던지지도 않았다.',
                           '법을 지키고 구조되는 것과 사냥을 하고 모든 것을 파괴하는 것 중 어느 편이 좋으냔 말이야?'
    );

-- 2 AUTO_INCREMENT 시퀀스 초기화
ALTER TABLE sentences AUTO_INCREMENT = 1;

-- 3 새로운 오늘의 문장 데이터 삽입 (중복 방지) - 49개 전체 데이터로 교체
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '충분한 재물이 없는 것이 아니라 만족하는 마음이 없을 뿐이다',
       '9791171830541',
       '2025-10-13' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='충분한 재물이 없는 것이 아니라 만족하는 마음이 없을 뿐이다' AND ISBN='9791171830541');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '파란 피부는 새로운 가능성이겠지요. 생각해봐요. 언젠가 초록색 피부를 가진 인류가 태어날지도 몰라요',
       '9791172130879',
       '2025-10-14' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='파란 피부는 새로운 가능성이겠지요. 생각해봐요. 언젠가 초록색 피부를 가진 인류가 태어날지도 몰라요' AND ISBN='9791172130879');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '제발 살아 돌아오라고, 꼭 다시 돌아오라고. 마중을 나가겠다고.',
       '9791165347758',
       '2025-10-15' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='제발 살아 돌아오라고, 꼭 다시 돌아오라고. 마중을 나가겠다고.' AND ISBN='9791165347758');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '술이라는 섬에 갇혀 그게 세상의 전부라 착각하며 산다. 결국 문제는 술이 아니다',
       '9791198334305',
       '2025-10-16' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='술이라는 섬에 갇혀 그게 세상의 전부라 착각하며 산다. 결국 문제는 술이 아니다' AND ISBN='9791198334305');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '불행이 찾아오는 건 내 힘으로 바꿀 수 없지만 불행에서 빠져나오는 선택은 나만이 할 수 있으니까',
       '9791198530349',
       '2025-10-17' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='불행이 찾아오는 건 내 힘으로 바꿀 수 없지만 불행에서 빠져나오는 선택은 나만이 할 수 있으니까' AND ISBN='9791198530349');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '이제는 안다. 힘들어서 좋았다는 걸',
       '9791188605149',
       '2025-10-18' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='이제는 안다. 힘들어서 좋았다는 걸' AND ISBN='9791188605149');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '첫눈을 알아볼 수 있는 것처럼 마지막 눈을 알아볼 수 있었으면 좋겠습니다',
       '9791191891119',
       '2025-10-19' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='첫눈을 알아볼 수 있는 것처럼 마지막 눈을 알아볼 수 있었으면 좋겠습니다' AND ISBN='9791191891119');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '나는 마치 상처 난 몸에 붙일 약초 찾는 짐승처럼 조급하고도 간절하게 산속을 찾아 헤맸지만 싱아는 한 포기도 없었다',
       '9788901248202',
       '2025-10-20' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='나는 마치 상처 난 몸에 붙일 약초 찾는 짐승처럼 조급하고도 간절하게 산속을 찾아 헤맸지만 싱아는 한 포기도 없었다' AND ISBN='9788901248202');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '그러니까 나에게 행복에 대해 물어보면 나는 이렇게 답한다. "내 인격만큼 사랑의 행복이 있다."',
       '9791170402817',
       '2025-10-21' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='그러니까 나에게 행복에 대해 물어보면 나는 이렇게 답한다. "내 인격만큼 사랑의 행복이 있다."' AND ISBN='9791170402817');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '눈송이가 왠지 엄마의 목소리처럼 느껴졌다. 어떤 거짓은 용서해주고 어떤 진실은 조용히 승인해주는 작은 기척처럼',
       '9791141601300',
       '2025-10-22' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='눈송이가 왠지 엄마의 목소리처럼 느껴졌다. 어떤 거짓은 용서해주고 어떤 진실은 조용히 승인해주는 작은 기척처럼' AND ISBN='9791141601300');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '나는 행복해. 춤추는 기쁨을 스스로 깎아내리는 일은 절대 하지 않을 것이다',
       '9791194293187',
       '2025-10-23' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='나는 행복해. 춤추는 기쁨을 스스로 깎아내리는 일은 절대 하지 않을 것이다' AND ISBN='9791194293187');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '너와 내가 사는 세계의 시간들이, 그 모든 순간이 모여 있는 힘껏 너와 나를 이어 주고 있었다는 걸',
       '9788954675604',
       '2025-10-24' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='너와 내가 사는 세계의 시간들이, 그 모든 순간이 모여 있는 힘껏 너와 나를 이어 주고 있었다는 걸' AND ISBN='9788954675604');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '꿈의 부스러기가 내 발 옆에 떨어졌다. 죽은 지 이미 오래인 꿈이었다.',
       '9788932923437',
       '2025-10-25' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='꿈의 부스러기가 내 발 옆에 떨어졌다. 죽은 지 이미 오래인 꿈이었다.' AND ISBN='9788932923437');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '한참을 울고 난 새벽은 따스하고 아름다운 그의 손을 맞잡았다',
       '9791194293217',
       '2025-10-26' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='한참을 울고 난 새벽은 따스하고 아름다운 그의 손을 맞잡았다' AND ISBN='9791194293217');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '클라이밍의 장점이자 단점은 진짜 추락하지 않는다는 것이었다',
       '9791130647975',
       '2025-10-27' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='클라이밍의 장점이자 단점은 진짜 추락하지 않는다는 것이었다' AND ISBN='9791130647975');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '바다의 출렁임은 신선하고 환희에 찬 색을 띠었다. 그 색채는 베네치아의 가장 고유한 장식을 빼앗아 갈 것이다',
       '9791191059571',
       '2025-10-28' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='바다의 출렁임은 신선하고 환희에 찬 색을 띠었다. 그 색채는 베네치아의 가장 고유한 장식을 빼앗아 갈 것이다' AND ISBN='9791191059571');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '남과 다른 독창성에 열광하는 요즘 세대들은 자기만의 철학으로 승부하는 브랜드에 지갑을 열거든요',
       '9791169084178',
       '2025-10-29' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='남과 다른 독창성에 열광하는 요즘 세대들은 자기만의 철학으로 승부하는 브랜드에 지갑을 열거든요' AND ISBN='9791169084178');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '"내인데 양 그림 하나만 그래 줘요." "머이라고?!"',
       '9791197182273',
       '2025-10-30' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='"내인데 양 그림 하나만 그래 줘요." "머이라고?!"' AND ISBN='9791197182273');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '자선은 새로운 자선 행위를 불러온다. 신뢰는 또 다른 신뢰를 키운다',
       '9791169850964',
       '2025-10-31' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='자선은 새로운 자선 행위를 불러온다. 신뢰는 또 다른 신뢰를 키운다' AND ISBN='9791169850964');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '자신의 상처를 모른 척 할수록, 스스로 용서하지 못할수록 얕고 아슬아슬한 어른이 되는 거겠지.',
       '9788966074150',
       '2025-11-01' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='자신의 상처를 모른 척 할수록, 스스로 용서하지 못할수록 얕고 아슬아슬한 어른이 되는 거겠지.' AND ISBN='9788966074150');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '타인의 말을 경청하듯 자신의 마음도 경청하며 스스로의 목소리에 힘을 실어주세요.',
       '9791158512354',
       '2025-11-02' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='타인의 말을 경청하듯 자신의 마음도 경청하며 스스로의 목소리에 힘을 실어주세요.' AND ISBN='9791158512354');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '정답이 불확실하다고 해서 문제 해결의 우선순위까지 불확실한 건 아니다.',
       '9788958075868',
       '2025-11-03' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='정답이 불확실하다고 해서 문제 해결의 우선순위까지 불확실한 건 아니다.' AND ISBN='9788958075868');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '공정한 행동을 한 사람은 우리 기억에 깊은 흔적을 남긴다.',
       '9791169850964',
       '2025-11-04' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='공정한 행동을 한 사람은 우리 기억에 깊은 흔적을 남긴다.' AND ISBN='9791169850964');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '지루함과 무의미에 너무 많은 의미를 부여하지 말고, "내일의 나"에게 조금 더 친절하자는 마음으로, 그냥 하세요.',
       '9791193638392',
       '2025-11-05' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='지루함과 무의미에 너무 많은 의미를 부여하지 말고, "내일의 나"에게 조금 더 친절하자는 마음으로, 그냥 하세요.' AND ISBN='9791193638392');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '나는 내가 선택한 삶 그 자체다. 나라는 존재는 자유로운 선택에 의한 행위로 드러난다.',
       '9791193866146',
       '2025-11-06' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='나는 내가 선택한 삶 그 자체다. 나라는 존재는 자유로운 선택에 의한 행위로 드러난다.' AND ISBN='9791193866146');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '맞아. 나는 가을을 사랑한 거야. 겨울은 추운 계절이고, 아직은 겨울을 맞을 준비를 하고 싶지 않아요.',
       '9788901280684',
       '2025-11-07' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='맞아. 나는 가을을 사랑한 거야. 겨울은 추운 계절이고, 아직은 겨울을 맞을 준비를 하고 싶지 않아요.' AND ISBN='9788901280684');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '느릴수록 아름다운 저녁노을처럼 점점 짧아지는 봄가을처럼 조급한 마음을 내려놓아야만 깊이 담을 수 있는 것들이 있어요.',
       '9791162145043',
       '2025-11-08' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='느릴수록 아름다운 저녁노을처럼 점점 짧아지는 봄가을처럼 조급한 마음을 내려놓아야만 깊이 담을 수 있는 것들이 있어요.' AND ISBN='9791162145043');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '내 몸이 별빛으로 가득 차 있고 어디를 가든 가는 곳마다 반짝이고 빛난다는 걸 안다.',
       '9791136259240',
       '2025-11-09' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='내 몸이 별빛으로 가득 차 있고 어디를 가든 가는 곳마다 반짝이고 빛난다는 걸 안다.' AND ISBN='9791136259240');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '사랑을 주려고 마음만 먹는다면 밤하늘 별들의 위대한 무심함을 사랑하듯이 내 조그만 잉크병을 사랑하지 못할 이유도 없을 것이다.',
       '9788996997962',
       '2025-11-10' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='사랑을 주려고 마음만 먹는다면 밤하늘 별들의 위대한 무심함을 사랑하듯이 내 조그만 잉크병을 사랑하지 못할 이유도 없을 것이다.' AND ISBN='9788996997962');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '어제, 모든 것은 더 아름다웠다. 나무들 사이의 음악 내 머리카락 사이의 바람 그리고 네가 내민 손안의 태양.',
       '9791160401608',
       '2025-11-11' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='어제, 모든 것은 더 아름다웠다. 나무들 사이의 음악 내 머리카락 사이의 바람 그리고 네가 내민 손안의 태양.' AND ISBN='9791160401608');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '고독을 받아들이지 않으면 많은 것을 잃게 된다. 자신과의 대면을 회피할 때 우리는 자유를 잃는다.',
       '9791193866146',
       '2025-11-12' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='고독을 받아들이지 않으면 많은 것을 잃게 된다. 자신과의 대면을 회피할 때 우리는 자유를 잃는다.' AND ISBN='9791193866146');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '사람은 역시 함께 사는 게 맞아요. 누가 날 기다려줄 때, 나 또한 누군가를 간절히 원할 때 내 모습, 내 처지가 또렷해지지요',
       '9788901280684',
       '2025-11-13' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='사람은 역시 함께 사는 게 맞아요. 누가 날 기다려줄 때, 나 또한 누군가를 간절히 원할 때 내 모습, 내 처지가 또렷해지지요' AND ISBN='9788901280684');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '그러니 오늘도 괜찮다. 덜 걱정해도 될 일이고, 그만 불안해도 되는 날이다. 다 지나간 일이며, 지나갈 날이다.',
       '9791162145043',
       '2025-11-14' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='그러니 오늘도 괜찮다. 덜 걱정해도 될 일이고, 그만 불안해도 되는 날이다. 다 지나간 일이며, 지나갈 날이다.' AND ISBN='9791162145043');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '나는 더 이상 나를 사랑하기 위해 완벽해지기까지 기다리지 않는다. 나는 지금 이 순간의 나 자신을 있는 그대로 받아들인다.',
       '9791136259240',
       '2025-11-15' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='나는 더 이상 나를 사랑하기 위해 완벽해지기까지 기다리지 않는다. 나는 지금 이 순간의 나 자신을 있는 그대로 받아들인다.' AND ISBN='9791136259240');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '우리가 아는 모든 것은 우리의 감각이며, 그중에서도 우리의 존재는 우리가 모르는 낯선 감각이다.',
       '9788996997962',
       '2025-11-16' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='우리가 아는 모든 것은 우리의 감각이며, 그중에서도 우리의 존재는 우리가 모르는 낯선 감각이다.' AND ISBN='9788996997962');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '우리는 작가가 된다. 우리가 쓰는 것에 대한 믿음을 결코 잃지 않은 채, 끈질기고 고집스럽게 쓰면서.',
       '9791160401608',
       '2025-11-17' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='우리는 작가가 된다. 우리가 쓰는 것에 대한 믿음을 결코 잃지 않은 채, 끈질기고 고집스럽게 쓰면서.' AND ISBN='9791160401608');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '무심한 듯 쿨하게 지어지는 이름들을 보면 대충 지은 듯하지만, 사실 수많은 선택지 중에서 어렵게 지어낸 이름이다.',
       '9791172740085',
       '2025-11-18' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='무심한 듯 쿨하게 지어지는 이름들을 보면 대충 지은 듯하지만, 사실 수많은 선택지 중에서 어렵게 지어낸 이름이다.' AND ISBN='9791172740085');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '나를 목적지로 삼았다는 말이 어쩐지 벅차서 나는 한동안 우두커니 그 자리에 서 있었다.',
       '9788936439668',
       '2025-11-19' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='나를 목적지로 삼았다는 말이 어쩐지 벅차서 나는 한동안 우두커니 그 자리에 서 있었다.' AND ISBN='9788936439668');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '그리고 전 그 미래가 마음에 들었어요. 제가 결코 누리지 못할 지루하고 안정적인 삶 다음으로.',
       '9788936434342',
       '2025-11-20' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='그리고 전 그 미래가 마음에 들었어요. 제가 결코 누리지 못할 지루하고 안정적인 삶 다음으로.' AND ISBN='9788936434342');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '그럼에도 거북이들은 걸어나갈 것이다. 달빛이 없어도, 모래가 무거워도.',
       '9788925574462',
       '2025-11-21' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='그럼에도 거북이들은 걸어나갈 것이다. 달빛이 없어도, 모래가 무거워도.' AND ISBN='9788925574462');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '괜찮다는 것은, 가로가 아닌 세로로, 고개를 끄덕여본다는 것.',
       '9788954698658',
       '2025-11-22' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='괜찮다는 것은, 가로가 아닌 세로로, 고개를 끄덕여본다는 것.' AND ISBN='9788954698658');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '방법은 하나뿐이라는 건 이미 잘 알고 있었다. 그냥 계속해서 길을 걷는 것. 언제나 그랬듯이.',
       '9791169851008',
       '2025-11-23' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='방법은 하나뿐이라는 건 이미 잘 알고 있었다. 그냥 계속해서 길을 걷는 것. 언제나 그랬듯이.' AND ISBN='9791169851008');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '우리 같이 조금 더 많이 압시다. 그리고 조금 더 행복해집시다.',
       '9791169084178',
       '2025-11-24' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='우리 같이 조금 더 많이 압시다. 그리고 조금 더 행복해집시다.' AND ISBN='9791169084178');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '그날의 우리인 너를 만나면 꼭 말해 주고 싶다. 이제 집을 향해 함께 가자고.',
       '9788936431204',
       '2025-11-25' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='그날의 우리인 너를 만나면 꼭 말해 주고 싶다. 이제 집을 향해 함께 가자고.' AND ISBN='9788936431204');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '부정으로도 긍정을 만들 수 있다. 불행하기에 행복이 무엇인지 더 잘 설명할 수 있다.',
       '9791169851053',
       '2025-11-26' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='부정으로도 긍정을 만들 수 있다. 불행하기에 행복이 무엇인지 더 잘 설명할 수 있다.' AND ISBN='9791169851053');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '그것이 어떤 종류든 간에 우리는 사소한 성취라도 느껴야 큰 목표를 향해 계속해서 나아갈 수 있습니다.',
       '9788936479350',
       '2025-11-27' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='그것이 어떤 종류든 간에 우리는 사소한 성취라도 느껴야 큰 목표를 향해 계속해서 나아갈 수 있습니다.' AND ISBN='9788936479350');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '한 사람에게 한 겹의 마음만 품을 수 있다면 얼마나 좋을까.',
       '9791167552877',
       '2025-11-28' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='한 사람에게 한 겹의 마음만 품을 수 있다면 얼마나 좋을까.' AND ISBN='9791167552877');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '때로는 그냥 하는 것이 어쩌면 오랫동안 지속할 수 있는 가장 최선의 방법이 되기도 한다.',
       '9791192999616',
       '2025-11-29' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='때로는 그냥 하는 것이 어쩌면 오랫동안 지속할 수 있는 가장 최선의 방법이 되기도 한다.' AND ISBN='9791192999616');
INSERT INTO sentences (sentence_content, ISBN, selected_date)
SELECT '아무 생각 없이 서서 먹는 메밀국숫집에 들어갔다가, 오늘은 라면을 먹을걸, 할 때는 있죠.',
       '9791192403076',
       '2025-11-30' WHERE NOT EXISTS (SELECT 1 FROM sentences WHERE sentence_content='아무 생각 없이 서서 먹는 메밀국숫집에 들어갔다가, 오늘은 라면을 먹을걸, 할 때는 있죠.' AND ISBN='9791192403076');

COMMIT;

