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

    @Value("${org.zerock.default.profile-image}")
    private String defaultProfileImagePath; // 디폴트 이미지 경로를 설정하는 새로운 프로퍼티

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

        // 프로필 이미지 파일이 null이 아니고, 비어있지 않으면 다음 작업을 수행합니다.
        if (profileImage != null && !profileImage.isEmpty()) {
            // 파일을 저장할 디렉토리를 나타내는 File 객체를 생성합니다.
            File uploadDir = new File(this.uploadDir);
            // 만약 uploadDir 디렉토리가 존재하지 않으면 디렉토리를 생성합니다.
            if (!uploadDir.exists()) {
                // 디렉토리 생성에 실패하면 예외를 발생시킵니다.
                if (!uploadDir.mkdirs()) {
                    throw new IOException("Failed to create directory: " + this.uploadDir);
                }
            }
            // 원본 파일 이름을 가져옵니다.
            String originalFilename = profileImage.getOriginalFilename();
            // 현재 시간(밀리초)와 원본 파일 이름을 결합하여 새로운 파일 이름을 생성합니다.
            // 이렇게 하면 파일 이름이 고유해져 중복을 피할 수 있습니다.
            String newFilename = System.currentTimeMillis() + "_" + originalFilename;
            // 생성된 파일 이름을 사용하여 파일의 최종 저장 경로를 나타내는 File 객체를 생성합니다.
            File destinationFile = new File(this.uploadDir, newFilename);
            // 프로필 이미지를 지정된 경로에 저장합니다.
            profileImage.transferTo(destinationFile);
            // 데이터베이스에 저장될 이미지 파일 경로를 설정합니다.
            // 이 경로는 웹에서 접근할 수 있는 URL 경로로 사용됩니다.
            user.setProfileImagePath("/upload/" + newFilename);
        }else if (user.getProfileImagePath() == null || user.getProfileImagePath().isEmpty()) {
                // 프로필 이미지가 null이거나 비어있을 경우 디폴트 이미지로 설정
                user.setProfileImagePath("/default.images/" + defaultProfileImagePath);
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