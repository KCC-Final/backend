package com.kcc.groo.challenge.dao;

import com.kcc.groo.challenge.data.model.Badge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 도전과제(뱃지) 및 달성 현황 데이터에 접근하기 위한 MyBatis 매퍼 인터페이스.
 * @author uyh
 */
@Mapper
public interface IBadgeRepository {

    List<Badge> findAllBadges();
    List<Integer> findBadgeIdsByUserId(String userId);
    void awardBadgeToUser(@Param("userId") String userId, @Param("badgeId") int badgeId);
    int countReviewsByUserId(String userId);
    int countDistinctCategoriesByUserId(String userId);
    Integer getMaxReviewCountForSingleCategory(String userId);
    int getCategoryCountWithMinReviews(@Param("userId") String userId, @Param("minReviewCount") int minReviewCount);

    /**
     * 뱃지 이름으로 뱃지 정보 조회
     * @param badgeName 뱃지 이름
     * @return Badge
     * @author uyh
     */
    Badge findBadgeByName(@Param("badgeName") String badgeName);
}