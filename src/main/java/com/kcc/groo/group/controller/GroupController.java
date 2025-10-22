package com.kcc.groo.group.controller;

import com.kcc.groo.common.dto.CommonResponse;
import com.kcc.groo.group.data.dto.GroupRequestDTO;
import com.kcc.groo.group.data.model.Groups;
import com.kcc.groo.group.service.IGroupService;
import com.kcc.groo.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 독서모임 게시글 관련 API를 처리하는 컨트롤러
 *
 * @author YunSung
 * @created 2025-10-22
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {

    private final IGroupService groupService;

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 새로운 독서 모임 게시글 생성
     *
     * @param group   독서 모임 생성 정보
     * @param request HTTP 요청 객체
     * @return 생성된 독서 모임 게시글 ID
     * @author YunSung
     * @created 2025-10-22
     */
    @PostMapping
    public ResponseEntity<CommonResponse<?>> createGroup(@Valid @RequestBody GroupRequestDTO group, HttpServletRequest request) {
        // JWT 토큰에서 사용자 ID 추출
        String userId = jwtTokenProvider.getUserId(jwtTokenProvider.resolveAccessToken(request));

        // 독서 모임 게시글 생성
        Groups createdGroup = groupService.createGroup(group, userId);

        // 201 응답. 생성된 독서 모임 게시글 ID 반환
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>("독서 모임 게시글 작성 성공", createdGroup.getGroupId()));
    }

    /**
     * 모든 독서 모임 게시글 목록 조회
     *
     * @return 모든 독서 모임 게시글 목록
     * @author YunSung
     * @created 2025-10-22
     */
    @GetMapping
    public ResponseEntity<CommonResponse<List<Groups>>> findAllGroups() {
        // 독서 모임 게시글 DB에서 조회
        List<Groups> groups = groupService.readAllGroups();

        // 200 응답. 독서 모임 게시글 목록 반환
        return ResponseEntity.ok(new CommonResponse<>("독서 모임 게시글 목록 조회 성공", groups));
    }

    /**
     * 특정 독서 모임 게시글 상세 조회
     *
     * @param groupId 조회할 독서 모임 ID
     * @return 특정 독서 모임 게시글 정보
     * @author YunSung
     * @created 2025-10-22
     */
    @GetMapping("/{groupId}")
    public ResponseEntity<CommonResponse<Groups>> getGroupById(@PathVariable int groupId) {
        // 독서 모임 게시글 DB에서 조회
        Groups group = groupService.readGroupByGroupId(groupId);

        // 200 응답. 독서 모임 게시글 정보 반환
        return ResponseEntity.ok(new CommonResponse<>("독서 모임 게시글 상세 조회 성공", group));
    }

    /**
     * 독서 모임 게시글 수정 (작성자 확인)
     *
     * @param groupId 수정할 독서 모임 ID
     * @param group   수정할 독서 모임 정보
     * @param request HTTP 요청 객체
     * @return 응답 엔티티
     * @author YunSung
     * @created 2025-10-22
     */
    @PutMapping("/{groupId}")
    public ResponseEntity<CommonResponse<?>> updateGroup(@PathVariable int groupId, @Valid @RequestBody GroupRequestDTO group, HttpServletRequest request) {
        // JWT 토큰에서 사용자 ID 추출
        String userId = jwtTokenProvider.getUserId(jwtTokenProvider.resolveAccessToken(request));

        // 기존 독서 모임 게시글 수정
        Groups updatedGroup = groupService.updateGroup(group, groupId, userId);

        // 200 응답. 수정된 독서 모임 게시글 ID 반환
        return ResponseEntity.ok(new CommonResponse<>("독서 모임 게시글 수정 성공", updatedGroup.getGroupId()));
    }

    /**
     * 독서 모임 게시글 삭제 (작성자 확인)
     *
     * @param groupId 삭제할 독서 모임 ID
     * @param request HTTP 요청 객체
     * @return 응답 엔티티
     * @author YunSung
     * @created 2025-10-22
     */
    @DeleteMapping("/{groupId}")
    public ResponseEntity<CommonResponse<?>> deleteGroup(@PathVariable int groupId, HttpServletRequest request) {
        // JWT 토큰에서 사용자 ID 추출
        String userId = jwtTokenProvider.getUserId(jwtTokenProvider.resolveAccessToken(request));

        // 독서 모임 게시글 삭제
        groupService.deleteGroupByGroupId(groupId, userId);

        // 200 응답. 독서 모임 게시글 삭제 완료 메시지 반환
        return ResponseEntity.ok(new CommonResponse<>("독서 모임 게시글 삭제 성공", null));
    }
}
