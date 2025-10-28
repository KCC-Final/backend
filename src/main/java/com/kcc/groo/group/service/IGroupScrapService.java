package com.kcc.groo.group.service;

/**
 * 독서모임 스크랩 관련 비즈니스 로직을 처리하는 Service 인터페이스
 *
 * @author YunSung
 * @created 2025-10-23
 */
public interface IGroupScrapService {

    /**
     * 독서 모임 게시글 스크랩
     *
     * @param groupId 스크랩할 독서 모임 ID
     * @param userId  스크랩하는 사용자 ID
     * @author YunSung
     * @created 2025-10-23
     */
    void scrapGroup(int groupId, String userId);

    /**
     * 사용자가 특정 독서 모임 게시글을 스크랩했는지 여부 확인
     *
     * @param groupId 독서 모임 ID
     * @param userId  사용자 ID
     * @return 스크랩 여부 (true: 스크랩함, false: 스크랩하지 않음)
     * @author YunSung
     * @created 2025-10-23
     */
    boolean isGroupScrappedByUser(int groupId, String userId);

    /**
     * 독서 모임 게시글 스크랩 취소
     *
     * @param groupId 스크랩 취소할 독서 모임 ID
     * @param userId  스크랩 취소하는 사용자 ID
     * @author YunSung
     * @created 2025-10-23
     */
    void cancelScrapGroup(int groupId, String userId);
}
