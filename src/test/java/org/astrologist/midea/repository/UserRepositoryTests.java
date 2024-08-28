package org.astrologist.midea.repository;

import org.astrologist.midea.entity.User;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void insertAdmins(){

        IntStream.rangeClosed(1, 100).forEach(i -> {

            User user = User.builder()
                    .email("admin" + i + "@naver.com")
                    .password(BCrypt.hashpw("password" + i, BCrypt.gensalt()))
                    .nickname("admin" + i)
                    .happy(i % 2 == 0)
                    .sad(i % 3 == 0)
                    .calm(i % 4 == 0)
                    .stressed(i % 5 == 0)
                    .joyful(i % 6 == 0)
                    .energetic(i % 7 == 0)
                    .profileImagePath(null)
                    .emailActive(true)
                    .nicknameActive(true)
                    .userRole(User.UserRole.ADMIN)
                    .build();

            userRepository.save(user);
        });
    }

    @Test
    public void insertMembers(){

        IntStream.rangeClosed(1, 100).forEach(i -> {

            User user = User.builder()
                    .email("user" + i + "@naver.com")
                    .password(BCrypt.hashpw("password" + i, BCrypt.gensalt()))
                    .nickname("nickname" + i)
                    .happy(i % 2 == 0)
                    .sad(i % 3 == 0)
                    .calm(i % 4 == 0)
                    .stressed(i % 5 == 0)
                    .joyful(i % 6 == 0)
                    .energetic(i % 7 == 0)
                    .profileImagePath(null)
                    .emailActive(true)
                    .nicknameActive(true)
                    .userRole(User.UserRole.MEMBER)
                    .build();

            userRepository.save(user);
        });
    }

}
