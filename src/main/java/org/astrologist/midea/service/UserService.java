package org.astrologist.midea.service;

import org.astrologist.midea.entity.User;
import org.astrologist.midea.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean isEmailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isNicknameExist(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}
