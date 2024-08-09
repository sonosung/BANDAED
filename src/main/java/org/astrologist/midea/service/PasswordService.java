package org.astrologist.midea.service;

import org.astrologist.midea.entity.User;
import org.astrologist.midea.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PasswordService {

    @Autowired
    private UserRepository userRepository;  // UserRepository를 통해 데이터베이스에 접근

    // 이메일과 닉네임으로 사용자를 찾고, 새 비밀번호를 설정하는 메서드
    public String resetPassword(String email, String nickname) {
        // 이메일과 닉네임으로 사용자 찾기
        User user = userRepository.findByEmailAndNickname(email, nickname);
        if (user != null) {
            // 사용자의 역할이 GUEST인지 확인
            if (user.getUserRole() == User.UserRole.GUEST) {
                // GUEST라면 비밀번호를 재설정하지 않고 null 반환
                return "탈퇴한 회원입니다";
            }

            // 새로운 비밀번호 생성 및 해시 처리
            String newPassword = generateRandomPassword();
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            user.setPassword(hashedPassword);  // 사용자 엔티티에 새 비밀번호 설정
            userRepository.save(user);  // 변경 사항 저장
            return newPassword;  // 새 비밀번호 반환
        } else {
            return null;  // 해당 이메일과 닉네임을 가진 사용자가 없을 경우 null 반환
        }
    }

    // 임의의 새 비밀번호를 생성하는 메서드
    public String generateRandomPassword() {
        int length = 8;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();  // 생성된 임의의 비밀번호 반환
    }
}