package org.astrologist.midea.service;

import lombok.RequiredArgsConstructor;
import org.astrologist.midea.dto.CommunityDTO;
import org.astrologist.midea.dto.MideaLikeDTO;
import org.astrologist.midea.entity.Community;
import org.astrologist.midea.entity.MideaLike;
import org.astrologist.midea.entity.User;
import org.astrologist.midea.repository.CommunityRepository;
import org.astrologist.midea.repository.MideaLikeRepository;
import org.astrologist.midea.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final MideaLikeRepository mideaLikeRepository;

    private static final Logger logger = LoggerFactory.getLogger(CommunityService.class);

    // 모든 커뮤니티 게시물 가져오기
    public List<Community> getAllCommunities() {
        return communityRepository.findAll();
    }

    // 특정 서브카테고리에 해당하는 게시물 가져오기
    public List<Community> getCommunitiesBySubcategory(Community.Subcategory subcategory) {
        return communityRepository.findBySubcategoryOrderByTimestampAsc(subcategory);
    }

    // 커뮤니티 게시물 생성
    public Community createCommunityPost(CommunityDTO communityDTO) {
        User user = userRepository.findById(communityDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + communityDTO.getUserId()));

        String subcategory = communityDTO.getSubcategory().replace(" ", "");
        Community.Subcategory subcategoryEnum = Community.Subcategory.valueOf(subcategory);

        Community community = Community.builder()
                .composer(communityDTO.getComposer())
                .content(communityDTO.getContent())
                .subcategory(subcategoryEnum)
                .user(user)
                .timestamp(LocalDateTime.now())
                .build();

        return communityRepository.save(community); // 저장 후 Community 객체 반환
    }

    // 로그 메시지를 한 곳에서 관리하는 메서드
    private void logUserAndPostDetails(String methodName, User user, Community post) {
        logger.info("{} - Session User ID: {}, Post User ID: {}, Post ID: {}",
                methodName, user.getId(), post.getUser().getId(), post.getId());
    }
    @Transactional
    //게시물 수정
    public void updatePost(Long postId, User user, String newContent) {
        try {
            Community post = communityRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

            logUserAndPostDetails("Update Post", user, post);

            if (!post.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("이 게시물을 수정할 권한이 없습니다.");
            }

            post.setContent(newContent);
            communityRepository.save(post); // 이 부분에서 커밋 여부 확인
            logger.info("게시글이 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            logger.error("게시글 수정 중 오류 발생", e);
            throw e; // 예외를 다시 던져 트랜잭션이 롤백되도록 함
        }
    }
    @Transactional
    // 게시물 삭제
    public void deletePost(Long postId, User user) {
        try {
            Community post = communityRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

            logUserAndPostDetails("Delete Post", user, post);

            if (!post.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("이 게시물을 삭제할 권한이 없습니다.");
            }

            communityRepository.delete(post); // 게시물 삭제
            logger.info("게시글이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            logger.error("게시글 삭제 중 오류 발생", e);
            throw e; // 예외를 다시 던져 트랜잭션이 롤백되도록 함
        }
    }

    // 게시물 좋아요 토글
    public void toggleLike(MideaLikeDTO mideaLikeDTO, User user) {
        if (mideaLikeDTO.getPostId() == null) {
            System.err.println("Post ID is null in toggleLike method!");
            throw new IllegalArgumentException("Post ID must not be null");
        }
        Community post = communityRepository.findById(mideaLikeDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        Optional<MideaLike> existingLike = mideaLikeRepository.findByUserAndPost1(user, post);

        if (existingLike.isPresent()) {
            mideaLikeRepository.delete(existingLike.get());
            post.setLikeCount(post.getLikeCount() - 1);
        } else {
            MideaLike newLike = MideaLike.builder()
                    .user(user)
                    .post1(post)
                    .build();
            mideaLikeRepository.save(newLike);
            post.setLikeCount(post.getLikeCount() + 1);
        }

        communityRepository.save(post);
    }

    // 사용자가 특정 게시물에 좋아요를 눌렀는지 확인하는 메서드
    public boolean isPostLikedByUser(Long postId, User user) {
        Community post = communityRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));
        return mideaLikeRepository.existsByUserAndPost1(user, post);
    }
}