package com.kcc.groo.group.service;

import com.kcc.groo.group.dao.IGroupRepository;
import com.kcc.groo.group.data.model.Groups;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 독서모임 게시글 관련 비즈니스 로직을 처리하는 Service
 *
 * @author YunSung
 * @created 2025-10-22
 */
@RequiredArgsConstructor
@Service
public class GroupService implements IGroupService {

    private final IGroupRepository groupRepository;

    /**
     * 새로운 독서 모임 게시글 생성
     *
     * @param group 독서 모임 게시글 정보
     * @return 생성된 독서 모임 게시글 정보
     * @author YunSung
     * @created 2025-10-22
     */
    @Override
    public Groups createGroup(Groups group) {
        // 독서 모임 게시글 생성
        groupRepository.insertGroup(group);

        // 생성된 독서 모임 게시글 정보 반환 (ID 포함)
        return group;
    }

    /**
     * 모든 독서 모임 게시글 목록 조회
     *
     * @return 독서 모임 게시글 리스트
     * @author YunSung
     * @created 2025-10-22
     */
    @Override
    public List<Groups> readAllGroups() {
        // 조회한 독서 모임 게시글 리스트 반환
        return groupRepository.selectAllGroups();
    }

    /**
     * 독서 모임 ID를 통한 독서 모임 게시글 상세 조회
     *
     * @param groupId 조회할 독서 모임 ID
     * @return 독서 모임 게시글 정보
     * @author YunSung
     * @created 2025-10-22
     */
    @Override
    public Groups readGroupByGroupId(int groupId) {
        // 독서 모임 게시글 조회 및 반환
        return groupRepository.selectGroupByGroupId(groupId);
    }

    /**
     * 독서 모임 게시글 수정
     *
     * @param group 수정할 독서 모임 게시글 정보
     * @return 수정된 독서 모임 게시글 정보
     * @author YunSung
     * @created 2025-10-22
     */
    public Groups updateGroup(Groups group) {
        // 독서 모임 게시글 수정
        groupRepository.updateGroup(group);

        // 수정된 독서 모임 게시글 정보 반환
        return group;
    }

    /**
     * 특정 독서 모임 게시글 삭제
     *
     * @param groupId 삭제할 독서 모임 ID
     * @author YunSung
     * @created 2025-10-22
     */
    @Override
    public void deleteGroupByGroupId(int groupId) {
        // 독서 모임 게시글 삭제
        groupRepository.deleteGroupByGroupId(groupId);
    }
}
