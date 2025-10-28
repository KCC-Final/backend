package com.kcc.groo.group.data.dto;

import com.kcc.groo.group.data.model.Group;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 독서모임 게시글 목록 조회 응답을 위한 DTO 클래스
 *
 * @author YunSung
 * @created 2025-10-23
 */
@Getter
@Builder
public class GroupListResponseDTO {

    private List<Group> groups;  // 독서모임 게시글 목록

    private int count;  // 조회된 독서모임 게시글 수
}
