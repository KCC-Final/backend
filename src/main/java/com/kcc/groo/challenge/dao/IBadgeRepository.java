package com.kcc.groo.challenge.dao;

import com.kcc.groo.challenge.data.dto.UserBadgeResponse;
import com.kcc.groo.challenge.data.model.Badge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 도전과제(뱃지) 및 달성 현황 데이터에 접근하기 위한 MyBatis 매퍼 인터페이스
 * @author uyh
 * @created 2025-10-16
 */
@Mapper
public interface IBadgeRepository {

    /**
     * @return List<Badge>
     * @author uyh
     * @created 2025-10-16
     * 모든 뱃지 조회
     */
    List<Badge> findAllBadges();

    /**
     * @param userId 사용자 ID
     * @return List<Integer>
     * @author uyh
     * @created 2025-10-16
     * 특정 사용자가 획득한 뱃지 ID 목록 조회
     */
    List<Integer> findBadgeIdsByUserId(String userId);

    /**
     * @param userId 사용자 ID
     * @param badgeId 뱃지 ID
     * @return void
     * @author uyh
     * @created 2025-10-16
     * 사용자에게 뱃지 부여
     */
    int awardBadgeToUser(@Param("userId") String userId, @Param("badgeId") int badgeId);

    /**
     * @param userId 사용자 ID
     * @return int
     * @author uyh
     * @created 2025-10-16
     * 특정 사용자의 리뷰 총 개수 조회
     */
    int countReviewsByUserId(String userId);

    /**
     * @param userId 사용자 ID
     * @return int
     * @author uyh
     * @created 2025-10-16
     * 특정 사용자가 리뷰를 작성한 카테고리 총 개수 조회
     */
    int countDistinctCategoriesByUserId(String userId);

    /**
     * @param userId 사용자 ID
     * @return Integer
     * @author uyh
     * @created 2025-10-16
     * 특정 사용자가 단일 카테고리에서 작성한 최대 리뷰 개수 조회
     */
    Integer getMaxReviewCountForSingleCategory(String userId);

    /**
     * @param userId 사용자 ID
     * @param minReviewCount 최소 리뷰 개수
     * @return int
     * @author uyh
     * @created 2025-10-16
     * 최소 리뷰 개수 이상을 만족하는 카테고리 개수 조회
     */
    int getCategoryCountWithMinReviews(@Param("userId") String userId, @Param("minReviewCount") int minReviewCount);

    /**
     * @param badgeName 뱃지 이름
     * @return Badge
     * @author uyh
     * @created 2025-10-16
     * 뱃지 이름으로 뱃지 정보 조회
     */
    Badge findBadgeByName(@Param("badgeName") String badgeName);

    /**
     * @param userId 사용자 ID
     * @return List<UserBadgeResponse>
     * @author uyh
     * @created 2025-10-16
     * 특정 사용자가 획득한 모든 뱃지의 상세 정보 조회
     */
    List<UserBadgeResponse> findBadgesByUserId(@Param("userId") String userId);
    
    /**
     * @param userId
     * @return
     * @author kys
     * @created 2025-10-21
     * 뱃지명 확인
     */
    String getBadgeNameByBadgeId (@Param("badgeId") int badgeId);

    /**
     * 특정 달(YYYY-MM)에 이미 월간 배지를 받은 적이 있는지 확인
     */
    boolean existsMonthlyBadge(@Param("userId") String userId,
                               @Param("badgeId") int badgeId,
                               @Param("month") String month);

    /**
     * @param @param userId
     * @param @param badgeId
     * @param @return
     * @return List<UserBadgeResponse>
     * @author uyh
     * @created 2025. 10. 31. TODO
     */
    List<UserBadgeResponse> findBadgeHistoryByUserIdAndBadgeId(
            @Param("userId") String userId,
            @Param("badgeId") int badgeId
    );

}