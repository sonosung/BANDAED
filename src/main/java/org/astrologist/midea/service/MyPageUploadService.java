package org.astrologist.midea.service;

import org.astrologist.midea.dto.MyPageUploadDTO;
import org.astrologist.midea.entity.User;
import org.astrologist.midea.repository.MyPageUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class MyPageUploadService {

    @Autowired
    private MyPageUploadRepository myPageUploadRepository;

    @Value("${org.zerock.upload.path}")
    private String uploadDir;

    // 사용자의 정보와 프로필 이미지를 업데이트하는 메서드입니다..
    public void updateUser(MyPageUploadDTO myPageUploadDTO, MultipartFile profileImage, Long userId) throws IOException {
        // 중복 닉네임 확인
        if (isNicknameInUse(myPageUploadDTO.getNickname(), userId)) {
            throw new IllegalArgumentException("닉네임이 이미 존재합니다.");
        }

        // 중복 이메일 확인
        if (isEmailInUse(myPageUploadDTO.getEmail(), userId)) {
            throw new IllegalArgumentException("이메일이 이미 존재합니다.");
        }

        // userId를 통해 사용자를 조회하고, 없으면 예외를 던집니다.
        User user = myPageUploadRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        // 사용자의 정보를 DTO에서 받아와서 업데이트합니다.
        user.setNickname(myPageUploadDTO.getNickname());
        user.setEmail(myPageUploadDTO.getEmail());
        user.setHappy(myPageUploadDTO.isHappy());
        user.setSad(myPageUploadDTO.isSad());
        user.setCalm(myPageUploadDTO.isCalm());
        user.setStressed(myPageUploadDTO.isStressed());
        user.setJoyful(myPageUploadDTO.isJoyful());
        user.setEnergetic(myPageUploadDTO.isEnergetic());

        // 프로필 이미지가 비어있지 않으면 저장 경로에 저장합니다.
        if (profileImage != null && !profileImage.isEmpty()) {
            File uploadDir = new File(this.uploadDir);
            if (!uploadDir.exists()) {
                if (!uploadDir.mkdirs()) {
                    throw new IOException("Failed to create directory: " + this.uploadDir);
                }
            }
            String originalFilename = profileImage.getOriginalFilename();
            String newFilename = System.currentTimeMillis() + "_" + originalFilename;
            File destinationFile = new File(this.uploadDir, newFilename);
            profileImage.transferTo(destinationFile);

            user.setProfileImagePath("/upload/" + newFilename);
        }

        // 사용자의 정보를 데이터베이스에 저장합니다.
        myPageUploadRepository.save(user);
    }

    // 중복 닉네임을 확인하는 메서드
    private boolean isNicknameInUse(String nickname, Long userId) {
        return myPageUploadRepository.findByNickname(nickname)
                .map(existingUser -> !existingUser.getId().equals(userId)) // 다른 사용자의 닉네임인지 확인
                .orElse(false);
    }

    // 중복 이메일을 확인하는 메서드
    private boolean isEmailInUse(String email, Long userId) {
        return myPageUploadRepository.findByEmail(email)
                .map(existingUser -> !existingUser.getId().equals(userId)) // 다른 사용자의 이메일인지 확인
                .orElse(false);
    }

    // userId를 통해 사용자를 조회하는 메서드입니다.
    public User findById(Long id) {
        return myPageUploadRepository.findById(id).orElse(null);
    }
}