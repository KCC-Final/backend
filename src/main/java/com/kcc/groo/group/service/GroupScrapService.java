package com.kcc.groo.group.service;

import com.kcc.groo.common.exception.GrooException;
import com.kcc.groo.group.dao.IGroupRepository;
import com.kcc.groo.group.dao.IGroupScrapRepository;
import com.kcc.groo.group.data.model.GroupScrap;
import com.kcc.groo.group.exception.GroupErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 독서모임 스크랩 관련 비즈니스 로직을 처리하는 Service
 *
 * @author YunSung
 * @created 2025-10-23
 */
@Service
@RequiredArgsConstructor
public class GroupScrapService implements IGroupScrapService {

    private final IGroupRepository groupRepository;

    private final IGroupScrapRepository groupScrapRepository;

    /**
     * 독서 모임 게시글 스크랩
     *
     * @author YunSung
     * @created 2025-10-23
     */
    @Override
    public void scrapGroup(int groupId, String userId) {
        // 스크랩할 게시글이 존재하는지 확인
        if (groupRepository.selectGroupByGroupId(groupId) == null) {
            throw new GrooException(GroupErrorCode.NOT_FOUND_GROUP_TO_SCRAP);
        }

        // 이미 스크랩했는지 확인
        if (groupScrapRepository.selectGroupScrapByUserIdAndGroupId(userId, groupId) != null) {
            throw new GrooException(GroupErrorCode.ALREADY_SCRAPPED_GROUP);
        }

        // 새로운 스크랩 생성
        GroupScrap newScrap = new GroupScrap();
        newScrap.setUserId(userId);
        newScrap.setGroupId(groupId);

        // DB에 스크랩 삽입
        int result = groupScrapRepository.insertGroupScrap(newScrap);

        // 삽입 실패 시 예외 발생
        if (result == 0) {
            throw new GrooException(GroupErrorCode.FAILED_SCRAP_GROUP);
        }
    }

    /**
     * 사용자가 특정 독서 모임 게시글을 스크랩했는지 여부 확인
     *
     * @author YunSung
     * @created 2025-10-23
     */
    @Override
    public boolean isGroupScrappedByUser(int groupId, String userId) {
        // 게시글이 존재하는지 확인
        if (groupRepository.selectGroupByGroupId(groupId) == null) {
            throw new GrooException(GroupErrorCode.NOT_FOUND_GROUP_TO_CHECK_SCRAP);
        }

        // user_id와 group_id로 스크랩 조회
        GroupScrap scrap = groupScrapRepository.selectGroupScrapByUserIdAndGroupId(userId, groupId);

        // 스크랩이 존재하면 true, 없으면 false 반환
        return scrap != null;
    }

    /**
     * 독서 모임 게시글 스크랩 삭제
     *
     * @author YunSung
     * @created 2025-10-23
     */
    @Override
    public void cancelScrapGroup(int groupId, String userId) {
        // 삭제할 게시글이 존재하는지 확인
        if (groupRepository.selectGroupByGroupId(groupId) == null) {
            throw new GrooException(GroupErrorCode.NOT_FOUND_GROUP_TO_CANCEL_SCRAP);
        }

        // 게시글을 스크랩했는지 확인
        if (groupScrapRepository.selectGroupScrapByUserIdAndGroupId(userId, groupId) == null) {
            throw new GrooException(GroupErrorCode.NOT_SCRAPPED_GROUP);
        }

        // DB에서 스크랩 삭제
        int result = groupScrapRepository.deleteGroupScrapByUserIdAndGroupId(userId, groupId);

        // 삭제 실패 시 예외 발생
        if (result == 0) {
            throw new GrooException(GroupErrorCode.FAILED_CANCEL_SCRAP_GROUP);
        }
    }
}
