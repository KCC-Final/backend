package com.kcc.groo.group.data.dto;

import com.kcc.groo.group.data.model.Group;
import com.kcc.groo.group.data.model.GroupComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 독서모임 상세 조회 응답을 위한 DTO 클래스
 *
 * @author YunSung
 * @created 2025-10-23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupDetailResponseDTO {

    private Group group;  // 독서모임 게시글 정보

    private List<GroupComment> comments;  // 해당 독서모임 게시글의 댓글 목록

    private ScrapInfo scrap;  // 스크랩 정보

    /**
     * 스크랩 정보를 담는 내부 DTO 클래스
     *
     * @author YunSung
     * @created 2025-10-23
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScrapInfo {

        private boolean isScrapped;  // 스크랩 여부

        private int count;  // 스크랩 수
    }
}
