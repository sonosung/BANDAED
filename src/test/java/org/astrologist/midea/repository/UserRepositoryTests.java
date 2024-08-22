package org.astrologist.midea.repository;

import org.astrologist.midea.entity.User;
import org.junit.jupiter.api.Test;
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
                    .password("123456"+i)
                    .nickname("admin" + i)
                    .calm(true)
                    .energetic(true)
                    .happy(true)
                    .joyful(true)
                    .sad(true)
                    .stressed(true)
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
                    .password("123456" + i)
                    .nickname("nickname" + i)
                    .calm(true)
                    .energetic(true)
                    .happy(true)
                    .joyful(true)
                    .sad(true)
                    .stressed(true)
                    .profileImagePath(null)
                    .emailActive(true)
                    .nicknameActive(true)
                    .userRole(User.UserRole.MEMBER)
                    .build();

            userRepository.save(user);
        });
    }

}
