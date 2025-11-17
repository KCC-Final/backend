package com.kcc.groo.recommendation.service;

import com.kcc.groo.recommendation.dao.IRecommendationRepository;
import com.kcc.groo.recommendation.data.dto.RecommendationResponse;
import com.kcc.groo.recommendation.data.dto.UserBookActivity;
import com.kcc.groo.recommendation.data.dto.UserSimilarity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 추천 시스템 서비스 구현체
 * @author uyh
 * @created 2025-11-16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationService implements IRecommendationService {

    private final IRecommendationRepository recommendationRepository;

    /**
     * 특정 사용자에게 맞춤 도서를 추천
     * @param userId 사용자 ID
     * @param limit 추천할 도서 개수
     * @return 추천 도서 목록
     * @author uyh
     * @created 2025-11-16
     */
    @Override
    public List<RecommendationResponse> getRecommendations(String userId, int limit) {
        log.info("사용자 {}에 대한 도서 추천 시작 (limit: {})", userId, limit);

        // 1. 사용자가 읽은/스크랩한 ISBN 목록 조회
        List<String> userIsbnList = recommendationRepository.selectUserIsbnList(userId);

        if (userIsbnList.isEmpty()) {
            log.warn("사용자 {}의 도서 활동 내역이 없어 추천 불가", userId);
            return Collections.emptyList();
        }

        // 2. 협업 필터링: 유사한 사용자 찾기
        List<UserSimilarity> similarUsers = findSimilarUsers(userId, userIsbnList);

        // 3. 협업 필터링 기반 추천 점수 계산
        Map<String, Double> collaborativeScores = calculateCollaborativeScores(
                userId, similarUsers, userIsbnList
        );

        // 4. 아이템 기반 추천 점수 계산
        Map<String, Double> itemBasedScores = calculateItemBasedScores(
                userIsbnList, userId
        );

        // 5. 두 점수를 결합 (협업 필터링 70%, 아이템 기반 30%)
        Map<String, Double> finalScores = combineScores(
                collaborativeScores, itemBasedScores, 0.7, 0.3
        );

        // 6. 점수 기준으로 정렬하여 상위 N개 반환
        List<RecommendationResponse> recommendations = finalScores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> RecommendationResponse.builder()
                        .isbn(entry.getKey())
                        .score(entry.getValue())
                        .reason(determineReason(entry.getKey(), collaborativeScores, itemBasedScores))
                        .similarUserCount(getSimilarUserCount(entry.getKey(), similarUsers))
                        .build())
                .collect(Collectors.toList());

        log.info("사용자 {}에 대해 {} 권의 도서 추천 완료", userId, recommendations.size());
        return recommendations;
    }

    /**
     * 유사한 사용자 찾기 (협업 필터링)
     * @param userId 대상 사용자 ID
     * @param userIsbnList 대상 사용자의 ISBN 목록
     * @return 유사도 순으로 정렬된 사용자 목록
     * @author uyh
     * @created 2025-11-16
     */
    private List<UserSimilarity> findSimilarUsers(String userId, List<String> userIsbnList) {
        // 전체 사용자의 활동 데이터 가져오기
        List<UserBookActivity> allActivities = recommendationRepository.selectAllUserBookActivities();

        // 사용자별로 ISBN 목록 그룹화
        Map<String, List<String>> userIsbnMap = allActivities.stream()
                .filter(activity -> !activity.getUserId().equals(userId))
                .collect(Collectors.groupingBy(
                        UserBookActivity::getUserId,
                        Collectors.mapping(UserBookActivity::getIsbn, Collectors.toList())
                ));

        // 각 사용자와의 유사도 계산
        List<UserSimilarity> similarities = new ArrayList<>();
        Set<String> userIsbnSet = new HashSet<>(userIsbnList);

        for (Map.Entry<String, List<String>> entry : userIsbnMap.entrySet()) {
            String otherUserId = entry.getKey();
            List<String> otherIsbnList = entry.getValue();

            // 공통 ISBN 개수 계산
            long commonCount = otherIsbnList.stream()
                    .filter(userIsbnSet::contains)
                    .count();

            if (commonCount > 0) {
                // 코사인 유사도 계산
                double similarity = commonCount /
                        Math.sqrt(userIsbnList.size() * otherIsbnList.size());

                similarities.add(UserSimilarity.builder()
                        .userId(otherUserId)
                        .similarity(similarity)
                        .commonBookCount((int) commonCount)
                        .build());
            }
        }

        // 유사도 기준 내림차순 정렬하여 상위 20명만 반환
        return similarities.stream()
                .sorted(Comparator.comparing(UserSimilarity::getSimilarity).reversed())
                .limit(20)
                .collect(Collectors.toList());
    }

    /**
     * 협업 필터링 기반 추천 점수 계산
     * @param userId 대상 사용자 ID
     * @param similarUsers 유사한 사용자 목록
     * @param userIsbnList 대상 사용자의 ISBN 목록
     * @return ISBN별 추천 점수
     * @author uyh
     * @created 2025-11-16
     */
    private Map<String, Double> calculateCollaborativeScores(
            String userId,
            List<UserSimilarity> similarUsers,
            List<String> userIsbnList
    ) {
        Map<String, Double> scores = new HashMap<>();
        Set<String> userIsbnSet = new HashSet<>(userIsbnList);

        for (UserSimilarity similarity : similarUsers) {
            // 유사 사용자의 도서 활동 가져오기
            List<UserBookActivity> activities = recommendationRepository
                    .selectUserBookActivities(similarity.getUserId());

            for (UserBookActivity activity : activities) {
                String isbn = activity.getIsbn();

                // 이미 읽은 책은 제외
                if (userIsbnSet.contains(isbn)) {
                    continue;
                }

                // 유사도 * 활동 가중치를 점수에 추가
                double score = similarity.getSimilarity() * activity.getWeight();
                scores.merge(isbn, score, Double::sum);
            }
        }

        return scores;
    }

    /**
     * 아이템 기반 추천 점수 계산
     * @param userIsbnList 대상 사용자의 ISBN 목록
     * @param userId 대상 사용자 ID
     * @return ISBN별 추천 점수
     * @author uyh
     * @created 2025-11-16
     */
    private Map<String, Double> calculateItemBasedScores(
            List<String> userIsbnList,
            String userId
    ) {
        Map<String, Double> scores = new HashMap<>();

        // 사용자가 읽은 각 책에 대해
        for (String isbn : userIsbnList) {
            // 이 책을 읽은 다른 사용자들 찾기
            List<UserBookActivity> usersWithBook = recommendationRepository
                    .selectUsersByIsbn(isbn);

            // 그 사용자들이 읽은 다른 책들 수집
            for (UserBookActivity activity : usersWithBook) {
                if (activity.getUserId().equals(userId)) {
                    continue;
                }

                // 그 사용자의 다른 활동들 가져오기
                List<UserBookActivity> otherActivities = recommendationRepository
                        .selectUserBookActivities(activity.getUserId());

                for (UserBookActivity otherActivity : otherActivities) {
                    String otherIsbn = otherActivity.getIsbn();

                    // 이미 읽은 책은 제외
                    if (userIsbnList.contains(otherIsbn)) {
                        continue;
                    }

                    // 활동 가중치를 점수에 추가
                    scores.merge(otherIsbn, otherActivity.getWeight(), Double::sum);
                }
            }
        }

        return scores;
    }

    /**
     * 두 점수 맵을 결합
     * @param scores1 첫 번째 점수 맵
     * @param scores2 두 번째 점수 맵
     * @param weight1 첫 번째 가중치
     * @param weight2 두 번째 가중치
     * @return 결합된 점수 맵
     * @author uyh
     * @created 2025-11-16
     */
    private Map<String, Double> combineScores(
            Map<String, Double> scores1,
            Map<String, Double> scores2,
            double weight1,
            double weight2
    ) {
        Map<String, Double> combined = new HashMap<>();

        // 모든 ISBN 수집
        Set<String> allIsbns = new HashSet<>();
        allIsbns.addAll(scores1.keySet());
        allIsbns.addAll(scores2.keySet());

        // 각 ISBN에 대해 가중 평균 계산
        for (String isbn : allIsbns) {
            double score1 = scores1.getOrDefault(isbn, 0.0);
            double score2 = scores2.getOrDefault(isbn, 0.0);

            double combinedScore = (score1 * weight1) + (score2 * weight2);
            combined.put(isbn, combinedScore);
        }

        return combined;
    }

    /**
     * 추천 사유 결정
     * @param isbn 도서 ISBN
     * @param collaborativeScores 협업 필터링 점수
     * @param itemBasedScores 아이템 기반 점수
     * @return 추천 사유
     * @author uyh
     * @created 2025-11-16
     */
    private String determineReason(
            String isbn,
            Map<String, Double> collaborativeScores,
            Map<String, Double> itemBasedScores
    ) {
        double colScore = collaborativeScores.getOrDefault(isbn, 0.0);
        double itemScore = itemBasedScores.getOrDefault(isbn, 0.0);

        if (colScore > itemScore) {
            return "비슷한 취향의 독자들이 좋아한 책";
        } else if (itemScore > colScore) {
            return "회원님이 읽은 책과 함께 읽은 책";
        } else {
            return "회원님을 위한 추천 도서";
        }
    }

    /**
     * 유사 사용자 수 계산
     * @param isbn 도서 ISBN
     * @param similarUsers 유사한 사용자 목록
     * @return 이 책을 읽은 유사 사용자 수
     * @author uyh
     * @created 2025-11-16
     */
    private Integer getSimilarUserCount(String isbn, List<UserSimilarity> similarUsers) {
        int count = 0;
        for (UserSimilarity similarity : similarUsers) {
            int hasBook = recommendationRepository.checkUserHasBook(
                    similarity.getUserId(), isbn
            );
            if (hasBook > 0) {
                count++;
            }
        }
        return count;
    }

    @Override
    public List<RecommendationResponse> getTopIsbnRecommendations(int limit) {

        // 가장 많이 등장한 ISBN 목록 조회
        List<String> isbnList = recommendationRepository.selectTopIsbnByCount(limit);

        // 결과 매핑
        return isbnList.stream()
                .map(isbn -> RecommendationResponse.builder()
                        .isbn(isbn)                         // 인기 ISBN
                        .score(0.0)                         // 점수는 일단 0.0 또는 null 등으로 기본값
                        .reason("전체 인기 기반 추천")       // 추천 사유
                        .similarUserCount(0)                // 유사 사용자 수 기본값
                        .build())
                .collect(Collectors.toList());
    }

}