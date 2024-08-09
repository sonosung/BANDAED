package org.astrologist.midea.service;

import org.astrologist.midea.entity.User;
import org.astrologist.midea.repository.UserPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPageService {

    @Autowired
    private UserPageRepository userPageRepository;  // UserPageRepository를 주입받아 사용합니다.

    // 이메일을 통해 사용자를 찾는 메서드입니다.
    public User findByEmail(String email) {
        return userPageRepository.findByEmail(email).orElse(null);
    }

    // 사용자의 정보를 업데이트하는 메서드입니다.
    public void updateUser(User user) {
        userPageRepository.save(user);
    }
}
