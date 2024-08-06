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
    private UserRepository userRepository;

    // 이메일과 닉네임으로 사용자를 찾고, 새 비밀번호를 설정하는 메서드
    public String resetPassword(String email, String nickname) {
        User user = userRepository.findByEmailAndNickname(email, nickname);
        if (user != null) {
            String newPassword = generateRandomPassword();
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            user.setPassword(hashedPassword);
            userRepository.save(user);
            return newPassword;
        } else {
            return null;
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
        return sb.toString();
    }
}
