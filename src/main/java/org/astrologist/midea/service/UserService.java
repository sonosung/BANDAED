package org.astrologist.midea.service;

import org.astrologist.midea.entity.User;
import org.astrologist.midea.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user) {
        // 비밀번호를 해시로 암호화
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }

    public boolean isEmailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isNicknameExist(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null); // Optional 처리
    }

    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setUserRole(User.UserRole.GUEST);
            userRepository.save(user);
        }
    }

        // User ID로 사용자 조회 메서드 추가
        public Optional<User> findById (Long userId) {
            return userRepository.findById(userId);
        }

            // 현재 사용자의 감정 상태와 유사한 사용자 찾기 메서드
            public List<User> findSimilarUsers(User currentUser) {
                return userRepository.findUsersWithSameEmotions(
                        currentUser.isHappy(),
                        currentUser.isSad(),
                        currentUser.isCalm(),
                        currentUser.isStressed(),
                        currentUser.isJoyful(),
                        currentUser.isEnergetic(),
                        currentUser.getId()
                );
            }
    }