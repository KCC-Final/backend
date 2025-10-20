package com.kcc.groo.dashboard.dao;

import java.util.List;

import com.kcc.groo.dashboard.data.dto.YearlyStat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.kcc.groo.dashboard.data.dto.MonthlyStat;
import com.kcc.groo.dashboard.data.dto.CategoryStat;

/**
 * @author uyh
 * @created 2025-01-16
 * Dashboard Repository Interface
 */
@Repository
@Mapper
public interface IDashboardRepository {

    /**
     * @param userId
     * @return
     * @author uyh
     * @created 2025-01-16
     * 특정 유저의 작성한 독후감 총 개수
     */
    int countReviewsByUser(@Param("userId") String userId);

    /**
     * @param userId
     * @return
     * @author uyh
     * @created 2025-01-16
     * 특정 유저가 스크랩한 도서 총 개수
     */
    int countScrappedBooks(@Param("userId") String userId);

    /**
     * @param userId
     * @return
     * @author uyh
     * @created 2025-01-16
     * 특정 유저가 좋아요 누른 독후감 총 개수
     */
    int countLikedReviews(@Param("userId") String userId);

    /**
     * @param userId
     * @return
     * @author uyh
     * @created 2025-01-16
     * 카테고리별 독서 통계
     */
    List<CategoryStat> getCategoryStats(@Param("userId") String userId);

    /**
     * @param userId
     * @param year
     * @return
     * @author uyh
     * @created 2025-01-16
     * 월별 독서량 통계 (특정 연도 1-12월)
     */
    List<MonthlyStat> getMonthlyReviewStats(@Param("userId") String userId, @Param("year") int year);

    /**
     * @param userId
     * @param year
     * @param month
     * @return
     * @author uyh
     * @created 2025-01-16
     * 특정 연월의 내 독서량
     */
    int getMonthlyReviewCount(@Param("userId") String userId, @Param("year") int year, @Param("month") int month);

    /**
     * @param year
     * @param month
     * @return
     * @author uyh
     * @created 2025-01-16
     * 특정 연월의 전체 사용자 평균 독서량
     */
    Double getAverageMonthlyReviewCount(@Param("year") int year, @Param("month") int month);

    /**
     * @param userId 사용자 아이디
     * @return 최근 5년간 연도별 독후감 개수 리스트
     *         - 각 연도별로 COUNT(*) 결과 반환
     *         - 가장 오래된 연도부터 최신 연도 순으로 정렬
     * @author uyh
     * @created 2025-10-17
     * 최근 5년 기준의 연도별 독후감 작성 통계 조회
     */
    List<YearlyStat> findYearlyStats(@Param("userId") String userId);

}