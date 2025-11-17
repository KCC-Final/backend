package com.kcc.groo.recommendation.dao;

import com.kcc.groo.recommendation.data.dto.UserBookActivity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 추천 시스템 데이터 접근 인터페이스
 * @author uyh
 * @created 2025-11-16
 */
@Mapper
@Repository
public interface IRecommendationRepository {

    /**
     * 특정 사용자의 모든 도서 활동 조회 (독후감 + 스크랩)
     * @param userId 사용자 ID
     * @return 사용자의 도서 활동 목록
     * @author uyh
     * @created 2025-11-16
     */
    List<UserBookActivity> selectUserBookActivities(@Param("userId") String userId);

    /**
     * 특정 사용자가 활동한 ISBN 목록 조회
     * @param userId 사용자 ID
     * @return ISBN 목록
     * @author uyh
     * @created 2025-11-16
     */
    List<String> selectUserIsbnList(@Param("userId") String userId);

    /**
     * 특정 ISBN을 가진 도서를 읽은/스크랩한 모든 사용자 조회
     * @param isbn 도서 ISBN
     * @return 사용자의 도서 활동 목록
     * @author uyh
     * @created 2025-11-16
     */
    List<UserBookActivity> selectUsersByIsbn(@Param("isbn") String isbn);

    /**
     * 모든 사용자의 도서 활동 조회 (유사도 계산용)
     * @return 전체 사용자의 도서 활동 목록
     * @author uyh
     * @created 2025-11-16
     */
    List<UserBookActivity> selectAllUserBookActivities();

    /**
     * 특정 사용자가 이미 읽었거나 스크랩한 도서인지 확인
     * @param userId 사용자 ID
     * @param isbn 도서 ISBN
     * @return 활동 여부 (0: 없음, 1 이상: 있음)
     * @author uyh
     * @created 2025-11-16
     */
    int checkUserHasBook(@Param("userId") String userId, @Param("isbn") String isbn);

    /**
     * 리뷰 + 스크랩에서 가장 많이 등장한 ISBN 상위 N개 조회
     * @param limit 조회 개수
     * @return ISBN 리스트
     */
    List<String> selectTopIsbnByCount(@Param("limit") int limit);

}