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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final MideaLikeRepository mideaLikeRepository;

    public List<Community> getAllCommunities() {
        return communityRepository.findAll();
    }

    public List<Community> getCommunitiesBySubcategory(Community.Subcategory subcategory) {
        return communityRepository.findBySubcategoryOrderByTimestampAsc(subcategory);
    }

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
    // 추가된 메서드: 사용자가 특정 게시물에 좋아요를 눌렀는지 확인하는 메서드
    public boolean isPostLikedByUser(Long postId, User user) {
        Community post = communityRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));
        return mideaLikeRepository.existsByUserAndPost1(user, post);
    }
}