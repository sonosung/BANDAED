package org.astrologist.midea.service;

import org.astrologist.midea.entity.User;
import org.astrologist.midea.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveMultipleUsers_ShouldPersist20UsersInDatabase() {
        // Given
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            User user = User.builder()
                    .email("user" + i + "@daum.com")
                    .password(BCrypt.hashpw("password" + i, BCrypt.gensalt())) // 비밀번호 해시
                    .nickname("MideaUser" + i)
                    .happy(i % 2 == 0)
                    .sad(i % 3 == 0)
                    .calm(i % 4 == 0)
                    .stressed(i % 5 == 0)
                    .joyful(i % 6 == 0)
                    .energetic(i % 7 == 0)
                    .profileImagePath(null)
                    .build();
            users.add(user);
        }

        // When
        userRepository.saveAll(users);

        // Then
        assertEquals(23, userRepository.count()); // 데이터베이스에 23개의 유저가 저장되었는지 확인
    }
}