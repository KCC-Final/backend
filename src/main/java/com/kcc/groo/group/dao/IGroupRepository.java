package com.kcc.groo.group.dao;

import com.kcc.groo.group.data.model.Group;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 독서모임 게시글 관련 데이터베이스 작업을 위한 Repository 인터페이스
 *
 * @author YunSung
 * @created 2025-10-22
 */
@Repository
@Mapper
public interface IGroupRepository {

    /**
     * 새로운 독서 모임 생성
     *
     * @param group 독서 모임 정보
     * @return 삽입된 행의 수
     * @author YunSung
     * @created 2025-10-22
     */
    int insertGroup(Group group);

    /**
     * 모든 독서 모임 목록 조회 (필터링 기능 포함)
     *
     * @param style    진행 방식
     * @param status   모집 상태
     * @param location 지역 코드
     * @param scrap    스크랩 여부
     * @param search   검색어
     * @param limit    페이지당 게시글 수
     * @param offset   조회 시작 위치
     * @param userId   현재 사용자 ID
     * @return 독서 모임 리스트
     * @author YunSung
     * @created 2025-10-22
     */
    List<Group> selectAllGroups(@Param("style") String style,
                                @Param("status") Boolean status,
                                @Param("location") Integer location,
                                @Param("scrap") Boolean scrap,
                                @Param("search") String search,
                                @Param("limit") int limit,
                                @Param("offset") int offset,
                                @Param("userId") String userId);

    /**
     * 필터링된 전체 독서 모임 게시글 수 조회
     *
     * @param style    진행 방식
     * @param status   모집 상태
     * @param location 지역 코드
     * @param scrap    스크랩 여부
     * @param search   검색어
     * @param userId   현재 사용자 ID
     * @return 독서 모임 게시글 수
     * @author YunSung
     * @created 2025-10-23
     */
    int countAllGroups(@Param("style") String style,
                       @Param("status") Boolean status,
                       @Param("location") Integer location,
                       @Param("scrap") Boolean scrap,
                       @Param("search") String search,
                       @Param("userId") String userId);

    /**
     * group_id로 특정 독서 모임 조회
     *
     * @param groupId 독서 모임 ID
     * @return 독서 모임 정보
     * @author YunSung
     * @created 2025-10-22
     */
    Group selectGroupByGroupId(int groupId);

    /**
     * 독서 모임 정보 수정
     *
     * @param group 수정할 독서 모임 정보
     * @return 수정된 행의 수
     * @author YunSung
     * @created 2025-10-22
     */
    int updateGroup(Group group);

    /**
     * group_id로 특정 독서 모임 삭제
     *
     * @param groupId 삭제할 독서 모임 ID
     * @return 삭제된 행의 수
     * @author YunSung
     * @created 2025-10-22
     */
    int deleteGroupByGroupId(int groupId);
}
