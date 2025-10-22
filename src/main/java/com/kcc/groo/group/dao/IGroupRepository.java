package com.kcc.groo.group.dao;

import com.kcc.groo.group.data.model.Groups;
import org.apache.ibatis.annotations.Mapper;
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
    int insertGroup(Groups group);

    /**
     * 모든 독서 모임 목록 조회
     *
     * @return 독서 모임 리스트
     * @author YunSung
     * @created 2025-10-22
     */
    List<Groups> selectAllGroups();

    /**
     * group_id로 특정 독서 모임 조회
     *
     * @param groupId 독서 모임 ID
     * @return 독서 모임 정보
     * @author YunSung
     * @created 2025-10-22
     */
    Groups selectGroupByGroupId(int groupId);

    /**
     * 독서 모임 정보 수정
     *
     * @param group 수정할 독서 모임 정보
     * @return 수정된 행의 수
     * @author YunSung
     * @created 2025-10-22
     */
    int updateGroup(Groups group);

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
