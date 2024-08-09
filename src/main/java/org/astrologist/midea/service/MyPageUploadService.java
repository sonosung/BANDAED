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
    private MyPageUploadRepository myPageUploadRepository;  // MyPageUploadRepository를 주입받아 사용합니다.

    // 파일이 업로드될 경로를 외부 설정 파일에서 주입받습니다.
    @Value("${org.zerock.upload.path}")
    private String uploadDir;

    // 사용자의 정보와 프로필 이미지를 업데이트하는 메서드입니다.
    public void updateUser(MyPageUploadDTO myPageUploadDTO, MultipartFile profileImage, Long userId) throws IOException {
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
            // 업로드 디렉토리가 존재하지 않으면 생성합니다.
            File uploadDir = new File(this.uploadDir);
            if (!uploadDir.exists()) {
                if (!uploadDir.mkdirs()) {
                    throw new IOException("Failed to create directory: " + this.uploadDir);
                }
            }
            // 파일명을 유니크하게 하기 위해 현재 시간을 접두사로 붙입니다.
            String originalFilename = profileImage.getOriginalFilename();
            String newFilename = System.currentTimeMillis() + "_" + originalFilename;
            File destinationFile = new File(this.uploadDir, newFilename);
            profileImage.transferTo(destinationFile);  // 파일을 지정된 경로에 저장합니다.

            // 저장된 파일의 경로를 사용자의 프로필 이미지 경로로 설정합니다.
            user.setProfileImagePath("/upload/" + newFilename);
        }

        // 사용자의 정보를 데이터베이스에 저장합니다.
        myPageUploadRepository.save(user);
    }

    // userId를 통해 사용자를 조회하는 메서드입니다.
    public User findById(Long id) {
        return myPageUploadRepository.findById(id).orElse(null);
    }
}
