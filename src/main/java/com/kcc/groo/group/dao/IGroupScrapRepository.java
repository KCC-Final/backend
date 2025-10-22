package com.kcc.groo.group.dao;

import com.kcc.groo.group.data.model.GroupScrap;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 독서모임 스크랩 관련 데이터베이스 작업을 위한 Repository 인터페이스
 *
 * @author YunSung
 * @created 2025-10-23
 */
@Repository
@Mapper
public interface IGroupScrapRepository {

    /**
     * 새로운 독서 모임 스크랩 생성
     *
     * @param groupScrap 스크랩 정보
     * @return 삽입된 행의 수
     * @author YunSung
     * @created 2025-10-23
     */
    int insertGroupScrap(GroupScrap groupScrap);

    /**
     * user_id와 group_id로 특정 독서 모임 스크랩 조회
     *
     * @param userId  사용자 ID
     * @param groupId 게시글 ID
     * @return 스크랩 정보
     * @author YunSung
     * @created 2025-10-23
     */
    GroupScrap selectGroupScrapByUserIdAndGroupId(@Param("userId") String userId, @Param("groupId") int groupId);

    /**
     * user_id와 group_id로 특정 독서 모임 스크랩 삭제
     *
     * @param userId  삭제할 스크랩의 사용자 ID
     * @param groupId 삭제할 스크랩의 게시글 ID
     * @return 삭제된 행의 수
     * @author YunSung
     * @created 2025-10-23
     */
    int deleteGroupScrapByUserIdAndGroupId(@Param("userId") String userId, @Param("groupId") int groupId);
}
