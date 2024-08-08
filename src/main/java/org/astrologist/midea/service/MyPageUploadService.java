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

    // 파일 업로드 경로를 설정합니다. 여기서는 프로젝트의 루트 디렉토리에 저장합니다.
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/uploaded/images/";

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
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                if (!uploadDir.mkdirs()) {
                    throw new IOException("Failed to create directory: " + UPLOAD_DIR);
                }
            }
            String originalFilename = profileImage.getOriginalFilename();
            String newFilename = System.currentTimeMillis() + "_" + originalFilename;
            File destinationFile = new File(UPLOAD_DIR, newFilename);
            profileImage.transferTo(destinationFile);

            user.setProfileImagePath("/uploaded/images/" + newFilename);
        }

        myPageUploadRepository.save(user);
    }

    public User findById(Long id) {
        return myPageUploadRepository.findById(id).orElse(null);
    }
}