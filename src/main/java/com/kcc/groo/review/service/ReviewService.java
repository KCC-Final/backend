package com.kcc.groo.review.service;

import com.kcc.groo.challenge.service.IChallengeService;
import com.kcc.groo.common.exception.GrooException;
import com.kcc.groo.notification.data.dto.NotificationRequest;
import com.kcc.groo.notification.service.INotificationService;
import com.kcc.groo.review.dao.IReviewCommentRepository;
import com.kcc.groo.review.dao.IReviewRepository;
import com.kcc.groo.review.data.dto.ReviewCreateRequest;
import com.kcc.groo.review.data.dto.ReviewResponse;
import com.kcc.groo.review.data.dto.ReviewUpdateRequest;
import com.kcc.groo.review.data.dto.ReviewWithCommentResponseDto;
import com.kcc.groo.review.exception.ReviewErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final IReviewRepository reviewRepository;
    private final IReviewCommentRepository commentRepository;
    private final IChallengeService challengeService; // 의존성 주입
    private final INotificationService notificationService; //added 2025-10-21 kys

    @Transactional
    @Override
    public void createReview(String userId, ReviewCreateRequest request) {
        // 입력 검증
        validateReviewRequest(request);
        validateUserId(userId);

        try {
            reviewRepository.insertReview(userId, request);
            log.info("[createReview] userId: {}, temporary: {}", userId, request.getTemporary());

            // 임시저장 글이 아닐 경우, 도전과제 달성 여부 확인
            if (request.getTemporary() == null || !request.getTemporary()) {
                challengeService.checkReviewRelatedBadges(userId);
                challengeService.checkPioneerBadge(userId, request.getIsbn());
            }
        } catch (Exception e) {
            log.error("[createReview] Failed - userId: {}, error: {}", userId, e.getMessage());
            throw new GrooException(ReviewErrorCode.REVIEW_CREATE_FAILED, e.getMessage());
        }
    }

    @Transactional
    @Override
    public void updateReview(String userId, Integer reviewId, ReviewUpdateRequest request) {
        // 입력 검증
        if (reviewId == null || reviewId <= 0) {
            throw new GrooException(ReviewErrorCode.INVALID_REVIEW_REQUEST, "Invalid review ID");
        }
        validateReviewUpdateRequest(request);
        validateUserId(userId);

        // 리뷰 존재 및 권한 확인
        ReviewResponse existingReview = reviewRepository.selectReviewById(userId, reviewId);
        if (existingReview == null) {
            throw new GrooException(ReviewErrorCode.REVIEW_NOT_FOUND);
        }

        // 작성자 확인
        if (!existingReview.getUserId().equals(userId)) {
            throw new GrooException(ReviewErrorCode.NOT_REVIEW_OWNER);
        }

        // 삭제된 리뷰는 수정 불가
        if (!existingReview.getStatus()) {
            throw new GrooException(ReviewErrorCode.CANNOT_ACCESS_DELETED_REVIEW);
        }

        try {
            reviewRepository.updateReview(userId, reviewId, request);
            log.info("[updateReview] reviewId: {}, userId: {}", reviewId, userId);
        } catch (Exception e) {
            log.error("[updateReview] Failed - reviewId: {}, userId: {}, error: {}",
                    reviewId, userId, e.getMessage());
            throw new GrooException(ReviewErrorCode.REVIEW_UPDATE_FAILED, e.getMessage());
        }
    }

    @Transactional
    @Override
    public void deleteReview(String userId, Integer reviewId) {
        // 입력 검증
        if (reviewId == null || reviewId <= 0) {
            throw new GrooException(ReviewErrorCode.INVALID_REVIEW_REQUEST, "Invalid review ID");
        }
        validateUserId(userId);

        // 리뷰 존재 및 권한 확인
        ReviewResponse existingReview = reviewRepository.selectReviewById(userId, reviewId);
        if (existingReview == null) {
            throw new GrooException(ReviewErrorCode.REVIEW_NOT_FOUND);
        }

        // 작성자 확인
        if (!existingReview.getUserId().equals(userId)) {
            throw new GrooException(ReviewErrorCode.NOT_REVIEW_OWNER);
        }

        // 이미 삭제된 리뷰
        if (!existingReview.getStatus()) {
            throw new GrooException(ReviewErrorCode.CANNOT_ACCESS_DELETED_REVIEW);
        }

        try {
            reviewRepository.deleteReview(userId, reviewId);
            log.info("[deleteReview] reviewId: {}, userId: {}", reviewId, userId);
        } catch (Exception e) {
            log.error("[deleteReview] Failed - reviewId: {}, userId: {}, error: {}",
                    reviewId, userId, e.getMessage());
            throw new GrooException(ReviewErrorCode.REVIEW_DELETE_FAILED, e.getMessage());
        }
    }

    @Override
    public ReviewResponse getReview(String userId, Integer reviewId) {
        // 입력 검증
        if (reviewId == null || reviewId <= 0) {
            throw new GrooException(ReviewErrorCode.INVALID_REVIEW_REQUEST, "Invalid review ID");
        }

        ReviewResponse review = reviewRepository.selectReviewById(userId, reviewId);

        // 리뷰 존재 확인
        if (review == null) {
            throw new GrooException(ReviewErrorCode.REVIEW_NOT_FOUND);
        }

        // 삭제된 리뷰 접근 제한
        if (!review.getStatus()) {
            throw new GrooException(ReviewErrorCode.CANNOT_ACCESS_DELETED_REVIEW);
        }

        // 임시저장 글 접근 제한
        if (review.getTemporary()) {
            if (userId == null || !review.getUserId().equals(userId)) {
                throw new GrooException(ReviewErrorCode.CANNOT_ACCESS_DRAFT_REVIEW);
            }
        }

        // 비밀글 접근 제한
        if (review.getSecret()) {
            if (userId == null || !review.getUserId().equals(userId)) {
                throw new GrooException(ReviewErrorCode.CANNOT_ACCESS_SECRET_REVIEW);
            }
        }

        // 작성자 여부 설정
        review.setIsOwner(userId != null && userId.equals(review.getUserId()));

        // 댓글 조회 시 userId 전달 (isOwner 계산을 위해)
        review.setComments(commentRepository.selectCommentsByReview(reviewId, userId));

        log.info("[getReview] reviewId: {}, userId: {}", reviewId, userId);
        return review;
    }

    @Override
    public List<ReviewResponse> getReviewsByUser(String userId) {
        validateUserId(userId);
        log.info("[getReviewsByUser] userId: {}", userId);
        return reviewRepository.selectReviewsByUser(userId);
    }

    @Override
    public List<ReviewResponse> getReviewsByLikes(String userId) {
        validateUserId(userId);
        log.info("[getReviewsByLikes] userId: {}", userId);
        return reviewRepository.selectReviewsByLikes(userId);
    }

    @Override
    public List<ReviewResponse> getAllReviews(String userId) {
        log.info("[getAllReviews] 호출 - userId: {}", userId);

        List<ReviewResponse> reviews = reviewRepository.selectAllReviews(userId);

        log.info("[getAllReviews] 조회된 독후감 수: {}", reviews != null ? reviews.size() : 0);

        if (reviews != null && !reviews.isEmpty()) {
            // 첫 번째 독후감의 프로필 이미지 정보 로그
            ReviewResponse firstReview = reviews.get(0);
            log.info("[getAllReviews] 첫 번째 독후감 정보:");
            log.info("  - reviewId: {}", firstReview.getReviewId());
            log.info("  - authorNickname: {}", firstReview.getAuthorNickname());
            log.info("  - authorProfileImage: {}",
                    firstReview.getAuthorProfileImage() != null
                            ? "존재 (길이: " + firstReview.getAuthorProfileImage().length() + ")"
                            : "null");

            if (firstReview.getAuthorProfileImage() != null) {
                // Base64 문자열 시작 부분 확인
                String profileImage = firstReview.getAuthorProfileImage();
                log.info("  - authorProfileImage 시작 문자: {}",
                        profileImage.length() > 20 ? profileImage.substring(0, 20) : profileImage);
            }
        }

        return reviews;
    }

    @Override
    public List<ReviewResponse> getDrafts(String userId) {
        validateUserId(userId);
        log.info("[getDrafts] userId: {}", userId);
        return reviewRepository.selectDrafts(userId);
    }

    @Override
    public ReviewResponse getDraft(int id, String userId) {
        // 입력 검증
        if (id <= 0) {
            throw new GrooException(ReviewErrorCode.INVALID_REVIEW_REQUEST, "Invalid draft ID");
        }
        validateUserId(userId);

        ReviewResponse draft = reviewRepository.selectDraft(id, userId);

        // 임시저장 글 존재 확인
        if (draft == null) {
            throw new GrooException(ReviewErrorCode.DRAFT_NOT_FOUND);
        }

        // 작성자 확인
        if (!draft.getUserId().equals(userId)) {
            throw new GrooException(ReviewErrorCode.NOT_DRAFT_OWNER);
        }

        log.info("[getDraft] draftId: {}, userId: {}", id, userId);
        return draft;
    }

    @Transactional
    @Override
    public void deleteDraft(int id, String userId) {
        // 입력 검증
        if (id <= 0) {
            throw new GrooException(ReviewErrorCode.INVALID_REVIEW_REQUEST, "Invalid draft ID");
        }
        validateUserId(userId);

        // 임시저장 글 존재 및 권한 확인
        ReviewResponse draft = reviewRepository.selectDraft(id, userId);
        if (draft == null) {
            throw new GrooException(ReviewErrorCode.DRAFT_NOT_FOUND);
        }

        // 작성자 확인
        if (!draft.getUserId().equals(userId)) {
            throw new GrooException(ReviewErrorCode.NOT_DRAFT_OWNER);
        }

        try {
            reviewRepository.deleteDraft(id, userId);
            log.info("[deleteDraft] draftId: {}, userId: {}", id, userId);
        } catch (Exception e) {
            log.error("[deleteDraft] Failed - draftId: {}, userId: {}, error: {}",
                    id, userId, e.getMessage());
            throw new GrooException(ReviewErrorCode.REVIEW_DELETE_FAILED, e.getMessage());
        }
    }

    @Transactional
    @Override
    public void likeReview(String userId, Integer reviewId) {
        // 입력 검증
        if (reviewId == null || reviewId <= 0) {
            throw new GrooException(ReviewErrorCode.INVALID_REVIEW_REQUEST, "Invalid review ID");
        }
        validateUserId(userId);

        // 리뷰 존재 확인
        ReviewResponse review = reviewRepository.selectReviewById(null, reviewId);
        if (review == null) {
            throw new GrooException(ReviewErrorCode.REVIEW_NOT_FOUND);
        }

        // 삭제된 리뷰는 좋아요 불가
        if (!review.getStatus()) {
            throw new GrooException(ReviewErrorCode.CANNOT_LIKE_DELETED_REVIEW);
        }

        // 임시저장 글은 좋아요 불가
        if (review.getTemporary()) {
            throw new GrooException(ReviewErrorCode.CANNOT_LIKE_DRAFT_REVIEW);
        }

        // 본인 글은 좋아요 불가
        if (review.getUserId().equals(userId)) {
            throw new GrooException(ReviewErrorCode.CANNOT_LIKE_OWN_REVIEW);
        }

        // 중복 좋아요 확인
        if (reviewRepository.existsLike(userId, reviewId) > 0) {
            throw new GrooException(ReviewErrorCode.DUPLICATE_LIKE);
        }

        try {
            reviewRepository.insertLike(userId, reviewId);
            log.info("[likeReview] reviewId: {}, userId: {}", reviewId, userId);

            //added 2025-10-21 kys
            if (!review.getUserId().equals(userId)) {
                try {
                    NotificationRequest req = new NotificationRequest();
                    req.setType("like");                // 알림 종류
                    req.setSenderType("review");        // 알림 출처 (리뷰)
                    req.setSenderId(reviewId);          // 어떤 리뷰에 좋아요 눌렀는가
                    req.setDetailSenderId(0);           // 좋아요는 세부 ID 없음
                    req.setUserId(review.getUserId());  // 수신자 (리뷰 작성자)
                    req.setSenderUserId(userId);        // 발신자 (좋아요 누른 사람)

                    notificationService.sendNotification(req);
                    log.info("like notification success: {} → {}", userId, review.getUserId());
                } catch (IOException e) {
                    log.error("like notification fail - reviewId: {}, sender: {}, receiver: {}",
                            reviewId, userId, review.getUserId(), e);
                }
            }

        } catch (Exception e) {
            log.error("[likeReview] Failed - reviewId: {}, userId: {}, error: {}",
                    reviewId, userId, e.getMessage());
            throw new GrooException(ReviewErrorCode.DATABASE_ERROR, e.getMessage());
        }
    }

    @Transactional
    @Override
    public void unlikeReview(String userId, Integer reviewId) {
        // 입력 검증
        if (reviewId == null || reviewId <= 0) {
            throw new GrooException(ReviewErrorCode.INVALID_REVIEW_REQUEST, "Invalid review ID");
        }
        validateUserId(userId);

        // 좋아요 존재 확인
        if (reviewRepository.existsLike(userId, reviewId) == 0) {
            throw new GrooException(ReviewErrorCode.LIKE_NOT_FOUND);
        }

        try {
            reviewRepository.deleteLike(userId, reviewId);
            log.info("[unlikeReview] reviewId: {}, userId: {}", reviewId, userId);
        } catch (Exception e) {
            log.error("[unlikeReview] Failed - reviewId: {}, userId: {}, error: {}",
                    reviewId, userId, e.getMessage());
            throw new GrooException(ReviewErrorCode.DATABASE_ERROR, e.getMessage());
        }
    }

    @Override
    public List<ReviewResponse> getLikedReviews(String userId) {
        validateUserId(userId);
        log.info("[getLikedReviews] userId: {}", userId);
        return reviewRepository.selectLikedReviews(userId);
    }

    @Override
    public List<ReviewResponse> getReviewsByFollowing(String userId) {
        validateUserId(userId);
        log.info("[getReviewsByFollowing] userId: {}", userId);
        return reviewRepository.selectReviewsByFollowing(userId);
    }

    @Override
    public List<ReviewResponse> getAllReviewsOrderByLikes(String userId) {
        log.info("[getAllReviewsOrderByLikes] userId: {}", userId);
        return reviewRepository.selectAllReviewsOrderByLikes(userId);
    }

    @Override
    public List<ReviewResponse> getReviewsByIsbn(String isbn, String userId) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new GrooException(ReviewErrorCode.INVALID_ISBN);
        }

        // ISBN 형식 검증 (10자리 또는 13자리)
        String cleanIsbn = isbn.replaceAll("-", "");
        if (!cleanIsbn.matches("^\\d{10}(\\d{3})?$")) {
            throw new GrooException(ReviewErrorCode.INVALID_ISBN, "ISBN must be 10 or 13 digits");
        }

        log.info("[getReviewsByIsbn] isbn: {}, userId: {}", isbn, userId);
        return reviewRepository.selectReviewsByIsbn(isbn, userId);
    }

    @Override
    public List<ReviewResponse> getReviewsByCategory(String category, String userId, int limit) {
        if (category == null || category.trim().isEmpty()) {
            throw new GrooException(ReviewErrorCode.INVALID_REVIEW_REQUEST, "Category is required");
        }

        if (limit <= 0 || limit > 100) {
            limit = 20; // 기본값
        }

        log.info("[getReviewsByCategory] category: {}, userId: {}, limit: {}", category, userId, limit);
        return reviewRepository.selectReviewsByCategory(category, userId, limit);
    }

    // ============ Private Validation Methods ============

    /**
     * 리뷰 생성 요청 검증
     */
    private void validateReviewRequest(ReviewCreateRequest request) {
        if (request == null) {
            throw new GrooException(ReviewErrorCode.INVALID_REVIEW_REQUEST);
        }

        // ISBN 검증
        if (request.getIsbn() == null || request.getIsbn().trim().isEmpty()) {
            throw new GrooException(ReviewErrorCode.INVALID_ISBN);
        }

        // ISBN 형식 검증 (10자리 또는 13자리)
        String isbn = request.getIsbn().replaceAll("-", "");
        if (!isbn.matches("^\\d{10}(\\d{3})?$")) {
            throw new GrooException(ReviewErrorCode.INVALID_ISBN, "ISBN must be 10 or 13 digits");
        }

        // 제목 검증
        if (request.getReviewTitle() == null || request.getReviewTitle().trim().isEmpty()) {
            throw new GrooException(ReviewErrorCode.INVALID_REVIEW_TITLE);
        }

        if (request.getReviewTitle().trim().length() < 2) {
            throw new GrooException(ReviewErrorCode.REVIEW_TITLE_TOO_SHORT);
        }

        if (request.getReviewTitle().length() > 200) {
            throw new GrooException(ReviewErrorCode.REVIEW_TITLE_TOO_LONG);
        }

        // 내용 검증
        if (request.getReviewContent() == null || request.getReviewContent().trim().isEmpty()) {
            throw new GrooException(ReviewErrorCode.INVALID_REVIEW_CONTENT);
        }

        // 1. DB 저장 크기 검증 (HTML 포함 원본)
        if (request.getReviewContent().length() > 30000) {
            throw new GrooException(ReviewErrorCode.REVIEW_CONTENT_DB_SIZE_EXCEEDED);
        }

        // 2. 순수 텍스트 길이 검증 (HTML 태그, 공백 제거)
        String contentText = request.getReviewContent()
                .replaceAll("<[^>]*>", "")
                .replaceAll("\\s", "")
                .trim();

        if (contentText.length() < 10) {
            throw new GrooException(ReviewErrorCode.REVIEW_CONTENT_TOO_SHORT);
        }

        if (contentText.length() > 10000) {
            throw new GrooException(ReviewErrorCode.REVIEW_CONTENT_TOO_LONG);
        }
    }

    /**
     * 리뷰 수정 요청 검증
     */
    private void validateReviewUpdateRequest(ReviewUpdateRequest request) {
        if (request == null) {
            throw new GrooException(ReviewErrorCode.INVALID_REVIEW_REQUEST);
        }

        // 제목이 있으면 검증
        if (request.getReviewTitle() != null) {
            if (request.getReviewTitle().trim().isEmpty()) {
                throw new GrooException(ReviewErrorCode.INVALID_REVIEW_TITLE);
            }

            if (request.getReviewTitle().trim().length() < 2) {
                throw new GrooException(ReviewErrorCode.REVIEW_TITLE_TOO_SHORT);
            }

            if (request.getReviewTitle().length() > 200) {
                throw new GrooException(ReviewErrorCode.REVIEW_TITLE_TOO_LONG);
            }
        }

        // 내용이 있으면 검증
        if (request.getReviewContent() != null) {
            if (request.getReviewContent().trim().isEmpty()) {
                throw new GrooException(ReviewErrorCode.INVALID_REVIEW_CONTENT);
            }

            // 1. DB 저장 크기 검증 (HTML 포함 원본)
            if (request.getReviewContent().length() > 30000) {
                throw new GrooException(ReviewErrorCode.REVIEW_CONTENT_DB_SIZE_EXCEEDED);
            }

            // 2. 순수 텍스트 길이 검증 (HTML 태그, 공백 제거)
            String contentText = request.getReviewContent()
                    .replaceAll("<[^>]*>", "")
                    .replaceAll("\\s", "")
                    .trim();

            if (contentText.length() < 10) {
                throw new GrooException(ReviewErrorCode.REVIEW_CONTENT_TOO_SHORT);
            }

            if (contentText.length() > 10000) {
                throw new GrooException(ReviewErrorCode.REVIEW_CONTENT_TOO_LONG);
            }
        }
    }

    /**
     * userId 검증
     */
    private void validateUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new GrooException(ReviewErrorCode.UNAUTHORIZED_ACCESS);
        }
    }

    @Override
    public List<ReviewResponse> getReviewsByUserWithAccess(String currentUserId, String targetUserId) {
        return reviewRepository.selectReviewsByUserWithAccess(currentUserId, targetUserId);
    }

    @Override
    public List<ReviewResponse> getLikedReviewsByUser(String currentUserId, String targetUserId) {
        return reviewRepository.selectLikedReviewsByUser(currentUserId, targetUserId);
    }
    @Override
    public List<ReviewWithCommentResponseDto> getReviewsWithCommentsByUser(String currentUserId, String targetUserId) {
        return reviewRepository.selectReviewsWithCommentsByUser(currentUserId, targetUserId);
    }
}
