package org.astrologist.midea.service;

import org.astrologist.midea.entity.User;
import org.astrologist.midea.repository.UserPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPageService {

    @Autowired
    private UserPageRepository userPageRepository;

    public User findByEmail(String email) {
        return userPageRepository.findByEmail(email).orElse(null);
    }

    public void updateUser(User user) {
        userPageRepository.save(user);
    }
}
