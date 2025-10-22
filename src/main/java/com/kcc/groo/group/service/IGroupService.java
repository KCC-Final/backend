package com.kcc.groo.group.service;

import com.kcc.groo.group.data.dto.GroupRequestDTO;
import com.kcc.groo.group.data.model.Groups;

import java.util.List;

/**
 * 독서모임 게시글 관련 비즈니스 로직을 처리하는 Service 인터페이스
 *
 * @author YunSung
 * @created 2025-10-22
 */
public interface IGroupService {

    /**
     * 새로운 독서 모임 게시글 생성
     *
     * @param group  독서 모임 게시글 정보
     * @param userId 독서 모임 작성자 사용자 ID
     * @return 생성된 독서 모임 게시글 정보
     * @author YunSung
     * @created 2025-10-22
     */
    Groups createGroup(GroupRequestDTO group, String userId);

    /**
     * 모든 독서 모임 게시글 목록 조회
     *
     * @return 독서 모임 게시글 리스트
     * @author YunSung
     * @created 2025-10-22
     */
    List<Groups> readAllGroups();

    /**
     * 독서 모임 ID를 통한 독서 모임 게시글 상세 조회
     *
     * @param groupId 조회할 독서 모임 ID
     * @return 독서 모임 게시글 정보
     * @author YunSung
     * @created 2025-10-22
     */
    Groups readGroupByGroupId(int groupId);

    /**
     * 독서 모임 게시글 수정
     *
     * @param group   수정할 독서 모임 게시글 정보
     * @param groupId 수정할 독서 모임 ID
     * @param userId  수정 요청한 사용자 ID
     * @return 수정된 독서 모임 게시글 정보
     * @author YunSung
     * @created 2025-10-22
     */
    Groups updateGroup(GroupRequestDTO group, int groupId, String userId);

    /**
     * 특정 독서 모임 게시글 삭제
     *
     * @param groupId 삭제할 독서 모임 ID
     * @param userId  삭제 요청한 사용자 ID
     * @author YunSung
     * @created 2025-10-22
     */
    void deleteGroupByGroupId(int groupId, String userId);
}
