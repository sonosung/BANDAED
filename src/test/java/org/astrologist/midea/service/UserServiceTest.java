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
/*나와 같은 유저 찾기 테스트 데이터 삽입x*/
/*package org.astrologist.midea.service;

import org.astrologist.midea.entity.User;
import org.astrologist.midea.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        // 중복되지 않는 테스트 데이터를 위한 기본 사용자 데이터 추가
        User user1 = User.builder()
                .email("unique_testuser1@daum.com") // 중복되지 않도록 이메일 수정
                .password(BCrypt.hashpw("password1", BCrypt.gensalt()))
                .nickname("UniqueTestUser1") // 중복되지 않도록 닉네임 수정
                .happy(true)
                .sad(false)
                .calm(true)
                .stressed(false)
                .joyful(true)
                .energetic(false)
                .build();

        User user2 = User.builder()
                .email("unique_testuser2@daum.com") // 중복되지 않도록 이메일 수정
                .password(BCrypt.hashpw("password2", BCrypt.gensalt()))
                .nickname("UniqueTestUser2") // 중복되지 않도록 닉네임 수정
                .happy(false)
                .sad(true)
                .calm(false)
                .stressed(true)
                .joyful(false)
                .energetic(true)
                .build();

        User user3 = User.builder()
                .email("unique_testuser3@daum.com") // 중복되지 않도록 이메일 수정
                .password(BCrypt.hashpw("password3", BCrypt.gensalt()))
                .nickname("UniqueTestUser3") // 중복되지 않도록 닉네임 수정
                .happy(true)
                .sad(false)
                .calm(true)
                .stressed(false)
                .joyful(true)
                .energetic(false)
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }

    @Test
    void saveMultipleUsers_ShouldPersist20UsersInDatabase() {
        // Given
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            User user = User.builder()
                    .email("user" + i + "@ddaum.com") // 중복되지 않는 이메일 사용
                    .password(BCrypt.hashpw("password" + i, BCrypt.gensalt())) // 비밀번호 해시
                    .nickname("MideaUser" + i) // 중복되지 않는 닉네임 사용
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
        assertEquals(26, userRepository.count()); // 기존 데이터 3명 + 새로운 데이터 23명 = 총 26명
    }

    @Test
    void findSimilarUsers_ShouldReturnCorrectUsersBasedOnEmotions() {
        // Given: 감정 상태가 설정된 현재 사용자
        User currentUser = User.builder()
                .email("unique_currentuser@daum.com") // 중복되지 않는 이메일 사용
                .password(BCrypt.hashpw("password", BCrypt.gensalt()))
                .nickname("UniqueCurrentUser") // 중복되지 않는 닉네임 사용
                .happy(true)
                .sad(false)
                .calm(true)
                .stressed(false)
                .joyful(true)
                .energetic(false)
                .build();
        userRepository.save(currentUser); // 현재 사용자 저장

        // When: 현재 사용자의 감정 상태와 유사한 사용자 찾기
        List<User> similarUsers = userService.findSimilarUsers(currentUser);

        // Then: 결과 검증 - 유사한 사용자로 "UniqueTestUser1"과 "UniqueTestUser3"을 기대
        assertEquals(2, similarUsers.size());
        assertEquals("UniqueTestUser1", similarUsers.get(0).getNickname());
        assertEquals("UniqueTestUser3", similarUsers.get(1).getNickname());
    }
}*/
