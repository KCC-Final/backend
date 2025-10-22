package com.kcc.groo.group.service;

import com.kcc.groo.common.exception.GrooException;
import com.kcc.groo.group.dao.IGroupRepository;
import com.kcc.groo.group.data.dto.GroupRequestDTO;
import com.kcc.groo.group.data.model.Groups;
import com.kcc.groo.group.exception.GroupErrorCode;
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
     * @author YunSung
     * @created 2025-10-22
     * @modified 2025-10-23
     * 게시글 생성 성공/실패 확인 과정 추가
     */
    @Override
    public Groups createGroup(GroupRequestDTO group, String userId) {
        // 작성할 독서 모임 게시글 정보 설정
        Groups newGroup = new Groups();
        newGroup.setUserId(userId);
        newGroup.setGroupName(group.getGroupName());
        newGroup.setBookTitle(group.getBookTitle());
        newGroup.setHeadcountMin(group.getHeadcountMin());
        newGroup.setHeadcountMax(group.getHeadcountMax());
        newGroup.setContent(group.getContent());
        newGroup.setStyle(group.getStyle());
        newGroup.setStatus(group.getStatus());
        newGroup.setEndDate(group.getEndDate());
        newGroup.setCodeId(group.getCodeId());

        // 독서 모임 게시글 생성
        int result = groupRepository.insertGroup(newGroup);

        // 생성 실패 시 예외 발생
        if (result == 0) {
            throw new GrooException(GroupErrorCode.FAILED_CREATE_GROUP);
        }

        // 생성된 독서 모임 게시글 정보 반환 (ID 포함)
        return newGroup;
    }

    /**
     * 모든 독서 모임 게시글 목록 조회
     *
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
     * @author YunSung
     * @created 2025-10-22
     * @modified 2025-10-23
     * 존재하지 않는 게시글 조회 시 예외 처리 추가
     */
    @Override
    public Groups readGroupByGroupId(int groupId) {
        // 독서 모임 게시글 조회
        Groups readGroup = groupRepository.selectGroupByGroupId(groupId);

        // 독서 모임 게시글이 존재하지 않을 경우 예외 발생
        if (readGroup == null) {
            throw new GrooException(GroupErrorCode.NOT_FOUND_GROUP_TO_READ);
        }

        // 조회한 독서 모임 게시글 정보 반환
        return readGroup;
    }

    /**
     * 독서 모임 게시글 수정
     *
     * @author YunSung
     * @created 2025-10-22
     * @modified 2025-10-23
     * 수정할 게시글 존재 여부, 수정 권한, 수정 성공/실패 확인 과정 추가
     */
    public Groups updateGroup(GroupRequestDTO group, int groupId, String userId) {
        // 수정할 독서 모임 게시글 조회
        Groups targetGroup = groupRepository.selectGroupByGroupId(groupId);

        // 수정할 독서 모임 게시글 ID의 게시글이 존재하지 않으면 예외 발생
        if (targetGroup == null) {
            throw new GrooException(GroupErrorCode.NOT_FOUND_GROUP_TO_UPDATE);
        }

        // 수정할 독서 모임 작성자와 기존 작성자가 다르면 예외 발생
        if (!targetGroup.getUserId().equals(userId)) {
            throw new GrooException(GroupErrorCode.FORBIDDEN_UPDATE_GROUP);
        }

        // 수정할 독서 모임 게시글 정보 설정
        targetGroup.setGroupName(group.getGroupName());
        targetGroup.setBookTitle(group.getBookTitle());
        targetGroup.setHeadcountMin(group.getHeadcountMin());
        targetGroup.setHeadcountMax(group.getHeadcountMax());
        targetGroup.setContent(group.getContent());
        targetGroup.setStyle(group.getStyle());
        targetGroup.setStatus(group.getStatus());
        targetGroup.setEndDate(group.getEndDate());
        targetGroup.setCodeId(group.getCodeId());

        // 독서 모임 게시글 수정
        int result = groupRepository.updateGroup(targetGroup);

        // 수정 실패 시 예외 발생
        if (result == 0) {
            throw new GrooException(GroupErrorCode.FAILED_UPDATE_GROUP);
        }

        // 수정된 독서 모임 게시글 정보 반환
        return targetGroup;
    }

    /**
     * 특정 독서 모임 게시글 삭제
     *
     * @author YunSung
     * @created 2025-10-22
     * @modified 2025-10-23
     * 삭제할 게시글 존재 여부, 삭제 권한, 삭제 성공/실패 확인 과정 추가
     */
    @Override
    public void deleteGroupByGroupId(int groupId, String userId) {
        // 삭제할 독서 모임 게시글 조회
        Groups deletedGroup = groupRepository.selectGroupByGroupId(groupId);

        // 해당 ID의 독서 모임 게시글이 존재하지 않으면 예외 발생
        if (deletedGroup == null) {
            throw new GrooException(GroupErrorCode.NOT_FOUND_GROUP_TO_DELETE);
        }

        // 해당 ID의 독서 모임 게시글 삭제 작성자와 기존 작성자가 다르면 예외 발생
        if (!deletedGroup.getUserId().equals(userId)) {
            throw new GrooException(GroupErrorCode.FORBIDDEN_DELETE_GROUP);
        }

        // 독서 모임 게시글 삭제
        int result = groupRepository.deleteGroupByGroupId(groupId);

        // 삭제 실패 시 예외 발생
        if (result == 0) {
            throw new GrooException(GroupErrorCode.FAILED_DELETE_GROUP);
        }
    }
}
