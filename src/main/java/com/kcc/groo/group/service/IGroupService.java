package com.kcc.groo.group.service;

import com.kcc.groo.group.data.dto.GroupDetailResponseDTO;
import com.kcc.groo.group.data.dto.GroupListResponseDTO;
import com.kcc.groo.group.data.dto.GroupRequestDTO;
import com.kcc.groo.group.data.model.Group;

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
    Group createGroup(GroupRequestDTO group, String userId);

    /**
     * 모든 독서 모임 게시글 목록 조회
     *
     * @param style    진행 방식
     * @param status   모집 상태
     * @param location 지역 코드
     * @param scrap    스크랩 여부
     * @param search   검색어
     * @param page     페이지 번호
     * @param userId   현재 사용자 ID
     * @return 독서 모임 게시글 리스트
     * @author YunSung
     * @created 2025-10-22
     * 
     * @modified 2025-11-01
     * @author kys
     * page -> int limit, int offset
     */
    GroupListResponseDTO readAllGroups(String style, Boolean status, Integer location, Boolean scrap, String search, int limit, int offset, String userId);

    /**
     * 독서 모임 ID를 통한 독서 모임 게시글 상세 조회
     *
     * @param groupId 조회할 독서 모임 ID
     * @param userId  조회 요청한 사용자 ID
     * @return 독서 모임 게시글 상세 정보 (댓글, 스크랩 정보 포함)
     * @author YunSung
     * @created 2025-10-22
     */
    GroupDetailResponseDTO readGroupByGroupId(int groupId, String userId);

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
    Group updateGroup(GroupRequestDTO group, int groupId, String userId);

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
