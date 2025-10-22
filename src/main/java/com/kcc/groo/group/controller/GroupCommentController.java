package com.kcc.groo.group.controller;

import com.kcc.groo.common.dto.CommonResponse;
import com.kcc.groo.group.data.model.GroupComment;
import com.kcc.groo.group.service.IGroupCommentService;
import com.kcc.groo.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 독서모임 댓글 관련 API를 처리하는 컨트롤러
 *
 * @author YunSung
 * @created 2025-10-22
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/groups/comments")
public class GroupCommentController {

    private final IGroupCommentService groupCommentService;

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 특정 독서 모임 게시글에 새로운 댓글 생성
     *
     * @param groupId 독서 모임 ID
     * @param comment 생성할 댓글 정보
     * @param request HTTP 요청 객체
     * @return 생성된 댓글 정보
     * @author YunSung
     * @created 2025-10-22
     */
    @PostMapping("/{groupId}")
    public ResponseEntity<CommonResponse<?>> createGroupComment(@PathVariable int groupId, @RequestBody GroupComment comment, HttpServletRequest request) {
        // JWT 토큰에서 사용자 ID 추출 후 댓글 작성자로 설정 및 독서 모임 ID 설정
        String userId = jwtTokenProvider.getUserId(jwtTokenProvider.resolveAccessToken(request));
        comment.setUserId(userId);
        comment.setGroupId(groupId);

        // 독서 모임 댓글 생성
        GroupComment createdComment = groupCommentService.createGroupComment(comment);

        // 201 응답. 생성된 댓글 정보 반환
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>("Comment created successfully", createdComment.getCommentId()));
    }

    /**
     * 특정 독서 모임 게시글의 모든 댓글 조회
     *
     * @param groupId 독서 모임 ID
     * @return 댓글 목록
     * @author YunSung
     * @created 2025-10-22
     */
    @GetMapping("/{groupId}")
    public ResponseEntity<CommonResponse<List<GroupComment>>> getGroupCommentsByGroupId(@PathVariable int groupId) {
        // 특정 독서 모임 게시글의 모든 댓글 조회
        List<GroupComment> comments = groupCommentService.readAllGroupCommentsByGroupId(groupId);

        // 200 응답. 댓글 목록 반환
        return ResponseEntity.ok(new CommonResponse<>("Comments fetched successfully", comments));
    }

    /**
     * 독서 모임 게시글의 특정 댓글 수정
     *
     * @param commentId 수정할 댓글 ID
     * @param comment   수정할 댓글 내용
     * @param request   HTTP 요청 객체
     * @return 응답 엔티티
     * @author YunSung
     * @created 2025-10-22
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<CommonResponse<?>> updateGroupComment(@PathVariable int commentId, @RequestBody GroupComment comment, HttpServletRequest request) {
        // 댓글 ID 설정
        comment.setCommentId(commentId);

        // 기존 독서 모임 게시글의 댓글 수정
        GroupComment updatedComment = groupCommentService.updateGroupComment(comment);

        // 200 응답. 수정된 댓글 정보 반환
        return ResponseEntity.ok(new CommonResponse<>("Comment updated successfully", updatedComment.getCommentId()));
    }

    /**
     * 독서 모임 게시글의 댓글 삭제
     *
     * @param commentId 삭제할 댓글 ID
     * @param request   HTTP 요청 객체
     * @return 응답 엔티티
     * @author YunSung
     * @created 2025-10-22
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommonResponse<?>> deleteGroupComment(@PathVariable int commentId, HttpServletRequest request) {
        // 독서 모임 게시글의 특정 댓글 삭제
        groupCommentService.deleteGroupCommentByCommentId(commentId);

        // 200 응답. 댓글 삭제 성공 메시지 반환
        return ResponseEntity.ok(new CommonResponse<>("Comment deleted successfully", null));
    }
}
