package org.astrologist.midea;

import org.astrologist.midea.entity.User;
import org.astrologist.midea.entity.UserPost;
import org.astrologist.midea.entity.Like;
import org.astrologist.midea.repository.UserRepository;
import org.astrologist.midea.repository.UserPostRepository;
import org.astrologist.midea.repository.LikeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TestDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserPostRepository userPostRepository;
    private final LikeRepository likeRepository;

    public TestDataLoader(UserRepository userRepository, UserPostRepository userPostRepository, LikeRepository likeRepository) {
        this.userRepository = userRepository;
        this.userPostRepository = userPostRepository;
        this.likeRepository = likeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 샘플 사용자 생성
        User user1 = User.builder()
                .email("user198@example.com")
                .password("password1")
                .nickname("user1")
                .build();

        User user2 = User.builder()
                .email("user1999@example.com")
                .password("password2")
                .nickname("user2")
                .build();

        User user3 = User.builder()
                .email("user2870@example.com")
                .password("password3")
                .nickname("user3")
                .build();

        User user4 = User.builder()
                .email("user2570@example.com")
                .password("password4")
                .nickname("user4")
                .build();

        User user5 = User.builder()
                .email("user1580@example.com")
                .password("password5")
                .nickname("user5")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);

        // 샘플 게시물 생성
        UserPost post1 = UserPost.builder()
                .title("제목1")
                .content("내용1")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user1)
                .build();

        UserPost post2 = UserPost.builder()
                .title("제목2")
                .content("내용2")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user2)
                .build();

        UserPost post3 = UserPost.builder()
                .title("제목3")
                .content("내용3")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user3)
                .build();

        UserPost post4 = UserPost.builder()
                .title("제목4")
                .content("내용4")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user4)
                .build();

        UserPost post5 = UserPost.builder()
                .title("제목5")
                .content("내용5")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user5)
                .build();

        userPostRepository.save(post1);
        userPostRepository.save(post2);
        userPostRepository.save(post3);
        userPostRepository.save(post4);
        userPostRepository.save(post5);

        // 샘플 좋아요 생성 및 저장
        saveLike(user1, post1);
        saveLike(user2, post1);
        saveLike(user3, post1);
        saveLike(user4, post1);
        saveLike(user5, post1);

        // 각 게시물의 좋아요 수를 업데이트
        updateLikeCount(post1);
    }

    private void saveLike(User user, UserPost post) {
        Like like = Like.builder()
                .user(user)
                .post(post)
                .build();
        likeRepository.save(like);
    }

    private void updateLikeCount(UserPost post) {
        long likeCount = likeRepository.countByPost_Id(post.getId());
        post.setLikeCount((int) likeCount);
        userPostRepository.save(post);
    }
}