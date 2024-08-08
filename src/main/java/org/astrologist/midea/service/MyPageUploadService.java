package org.astrologist.midea.service;

import org.astrologist.midea.dto.MyPageUploadDTO;
import org.astrologist.midea.entity.User;
import org.astrologist.midea.repository.MyPageUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class MyPageUploadService {

    @Autowired
    private MyPageUploadRepository myPageUploadRepository;

    public void updateUser(MyPageUploadDTO myPageUploadDTO, MultipartFile profileImage, Long userId) throws IOException {
        User user = myPageUploadRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        user.setNickname(myPageUploadDTO.getNickname());
        user.setEmail(myPageUploadDTO.getEmail());
        user.setHappy(myPageUploadDTO.isHappy());
        user.setSad(myPageUploadDTO.isSad());
        user.setCalm(myPageUploadDTO.isCalm());
        user.setStressed(myPageUploadDTO.isStressed());
        user.setJoyful(myPageUploadDTO.isJoyful());
        user.setEnergetic(myPageUploadDTO.isEnergetic());

        if (profileImage != null && !profileImage.isEmpty()) {
            String uploadDir = "C:/upload/profileImages"; // 절대 경로로 설정
            File uploadDirectory = new File(uploadDir);
            if (!uploadDirectory.exists()) {
                boolean isCreated = uploadDirectory.mkdirs();
                if (!isCreated) {
                    throw new IOException("Failed to create directory: " + uploadDir);
                }
            }

            String originalFilename = profileImage.getOriginalFilename();
            String newFilename = System.currentTimeMillis() + "_" + originalFilename;
            File destinationFile = new File(uploadDir, newFilename);
            profileImage.transferTo(destinationFile);

            user.setProfileImagePath("/uploaded/images/" + newFilename);
        }

        myPageUploadRepository.save(user);
    }

    public User findById(Long id) {
        return myPageUploadRepository.findById(id).orElse(null);
    }
}
