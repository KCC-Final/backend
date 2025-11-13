package com.kcc.groo.user.service;

import java.time.LocalDateTime;
import java.util.List;

import com.kcc.groo.challenge.service.IChallengeService;
import com.kcc.groo.review.dao.IReviewRepository;
import com.kcc.groo.review.data.dto.ReviewResponse;
import com.kcc.groo.search.service.ISearchService;
import com.kcc.groo.user.dao.IFollowsRepository;
import com.kcc.groo.user.data.dto.UserFeedDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.kcc.groo.jwt.JwtTokenProvider;
import com.kcc.groo.user.dao.IUsersRepository;
import com.kcc.groo.user.data.dto.SignupRequest;
import com.kcc.groo.user.data.dto.UserProfileUpdateRequest;
import com.kcc.groo.user.data.dto.UserUpdateRequest;
import com.kcc.groo.user.data.model.Users;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class UserService implements IUserService {

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@Autowired
	IUsersRepository usersRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	EmailVerificationService emailVerificationService;

	@Autowired
	MailService mailService;

    @Autowired  // 추가
    private IReviewRepository reviewRepository;

    @Autowired  // 추가
    private IFollowsRepository followsRepository;

    @Autowired
    private IChallengeService challengeService;
    
    @Autowired
    private ISearchService searchService;

	@Override
	public Users loginUser(String userId, String password) {
		// TODO Auto-generated method stub
		Users user = usersRepository.selectUserByUserId(userId);

		if (user == null) {
			throw new IllegalArgumentException("can not found account");
		}
		if (!user.isEmailVerified()) {
			throw new IllegalStateException("need to verified email");
		}
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new IllegalArgumentException("password does not match");
		}

		return user;
	}

	@Override
	public Users requestInsertUser(SignupRequest signupRequest) {
		Users newUser = new Users();
		newUser.setUserId(signupRequest.getUserId());
		newUser.setPassword(passwordEncoder.encode(signupRequest.getPassword1()));
		newUser.setEmail(signupRequest.getEmail());
		newUser.setNickname(signupRequest.getNickname());
		newUser.setGender(signupRequest.getGender());
		newUser.setName(signupRequest.getName());
		newUser.setBirth(signupRequest.getBirth());
		newUser.setCheckPrivacy(signupRequest.isCheckPrivacy());
		newUser.setCheckService(signupRequest.isCheckService());
		newUser.setEmailVerified(true);

		int result = usersRepository.insertUser(newUser);
		
		if (result <= 0) {
			throw new RuntimeException("failed signup");
		}
		
		Users savedUser = usersRepository.selectUserByUserId(newUser.getUserId());
		log.info(">>> calling searchService.insertUserIndex");
		searchService.insertUserIndex(savedUser);
		log.info(">>> after searchService.insertUserIndex");
		return savedUser;
	}

	@Override
	public boolean confirmEmail(String purpose, String email, String code) {
		return emailVerificationService.verifyCode(purpose, email, code);
	}

	@Override
	public String findUserIdByNameAndEmail(String name, String email) {
		String userId = usersRepository.findUserIdByNameAndEmail(name, email);

		if (userId == null) {
			throw new IllegalArgumentException("can not found name or email");
		}
		return userId;
	}

	@Override
	public List<Users> selectAllUserId() {
		// TODO Auto-generated method stub
		return usersRepository.selectAllUserId();
	}

	@Override
	public int existsByUserName(String name) {
		// TODO Auto-generated method stub
		return usersRepository.existsByUserName(name);
	}

	@Override
	public int existsByUserId(String userId) {
		// TODO Auto-generated method stub
		return usersRepository.existsByUserId(userId);
	}
	@Override
	public Users resetPassword(String userId, String rawPassword) {
		Users user = usersRepository.selectUserByUserId(userId);
		int result = usersRepository.resetPassword(userId, passwordEncoder.encode(rawPassword));
		if (result > 0) {
			return user;
		} else {
			throw new RuntimeException("failed update Password");
		}
	}

	public Users findByUserId(String userId) {
		return usersRepository.selectUserByUserId(userId);
	}

    @Override
    public Users requestUpdateUser(String userId, UserUpdateRequest updateRequest) {
        // set userId
        Users updateUser = usersRepository.selectUserByUserId(userId);

        // check pw
        if (!updateUser.getPassword().equals(updateRequest.getPassword1())) { // 기존 비밀번호와 password1 다를 경우
            if (StringUtils.hasText(updateRequest.getPassword1())
                    && StringUtils.hasText(updateRequest.getPassword2())) { // not null, length > 0, is not empty
                updateUser.setPassword(passwordEncoder.encode(updateRequest.getPassword1()));
                updateUser.setPwdChangedAt(LocalDateTime.now()); // set pwd change date
            }
        }

        // email
        if (!updateUser.getEmail().equals(updateRequest.getEmail())) { // 기존 이메일과 새로 입력된 이메일이 다를 경우
            if (StringUtils.hasText(updateRequest.getEmail())) { // not null, length > 0, is not empty
                updateUser.setEmail(updateRequest.getEmail()); // 새로 입력된 이메일 set
                updateUser.setEmailVerified(true);
            }
        }

        // nickname, introduction, name
        if (StringUtils.hasText(updateRequest.getNickname())) { // not null, length > 0, is not empty
            updateUser.setNickname(updateRequest.getNickname());
        }
        if (StringUtils.hasText(updateRequest.getIntroduction())) { // not null, length > 0, is not empty
            updateUser.setIntroduction(updateRequest.getIntroduction());
        }
        if (StringUtils.hasText(updateRequest.getName())) { // not null, length > 0, is not empty
            updateUser.setName(updateRequest.getName());
        }

        // profileImage
        if (updateRequest.getProfileImage() != null) { // not null
            updateUser.setProfileImage(updateRequest.getProfileImage()); // convert to byte type
        }

        int result = usersRepository.updateUser(updateUser); // check result

        if (result > 0) { // success
            try {
                // check first introduction badge
                challengeService.checkAndAwardBadges(userId);
            } catch (Exception e) {
                System.err.println("Failed to check badge after profile update: " + e.getMessage());
            }

            Users updatedUser = usersRepository.selectUserByUserId(updateUser.getUserId());
    		log.info(">>> calling searchService.updateUserIndex");
    		searchService.updateUserIndex(updatedUser);
    		log.info(">>> after searchService.updateUserIndex");
            return usersRepository.selectUserByUserId(userId);
        } else { // fail
            throw new RuntimeException("failed update user information");
        }
    }



    @Override
	public int existsByUserEmail(String email) {
		// TODO Auto-generated method stub
		return usersRepository.existsByUserEmail(email);
	}

	@Override
	public Users requestUpdateUserProfileImage(String userId, UserProfileUpdateRequest updateRequest) {
		// TODO Auto-generated method stub
		Users updateUser = usersRepository.selectUserByUserId(userId);

		if (updateRequest.getProfileImage() == null) { //not null, length > 0, is not empty
			updateUser.setUserId(userId);
			usersRepository.deleteUserProfileImage(updateUser);
	}
		return usersRepository.selectUserByUserId(userId);
	}

    /**
 * @param currentUserId 현재 로그인한 사용자 ID (null 가능)
 * @param targetUserId 조회할 사용자 ID
 * @return UserFeedDTO
 * @author uyh
 * @created 2025-10-20
 * @modified 2025-10-21
 * 사용자 피드 통합 정보 조회
 */
@Override
public UserFeedDTO getUserFeed(String currentUserId, String targetUserId) {
    if (targetUserId == null || targetUserId.trim().isEmpty()) {
        throw new IllegalArgumentException("사용자 ID는 필수입니다.");
    }

    Users user = usersRepository.selectUserByUserId(targetUserId);
    if (user == null) {
        throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
    }

    UserFeedDTO feedDTO = new UserFeedDTO();

    UserFeedDTO.UserInfo userInfo = new UserFeedDTO.UserInfo();
    userInfo.setUserId(user.getUserId());
    userInfo.setNickname(user.getNickname());

    String profileImageBase64 = null;
    if (user.getProfileImage() != null && user.getProfileImage().length > 0) {
        profileImageBase64 = java.util.Base64.getEncoder().encodeToString(user.getProfileImage());
    }
    userInfo.setProfileImage(profileImageBase64);

    userInfo.setIntroduction(user.getIntroduction());
    feedDTO.setUser(userInfo);

    UserFeedDTO.UserStats stats = new UserFeedDTO.UserStats();

    List<ReviewResponse> reviews = reviewRepository.selectReviewsByUserWithAccess(
            currentUserId, targetUserId
    );
    stats.setReviewCount(reviews.size());
    feedDTO.setReviews(reviews);

    stats.setFollowerCount(followsRepository.countFollower(targetUserId));
    stats.setFollowingCount(followsRepository.countFollowing(targetUserId));

    feedDTO.setStats(stats);

    List<ReviewResponse> likedReviews = reviewRepository.selectLikedReviewsByUser(
            currentUserId, targetUserId
    );
    feedDTO.setLikedReviews(likedReviews);

    return feedDTO;
}

	@Override
	public String getUserNicknameByUserId(String userId) {
		// TODO Auto-generated method stub
		return usersRepository.getUserNickName(userId);
	}

}