package org.astrologist.midea.controller;

import org.astrologist.midea.dto.PasswordDTO;
import org.astrologist.midea.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/midea")  // 이 컨트롤러의 기본 경로는 "/midea"
public class PasswordController {

    @Autowired
    private PasswordService passwordService;  // PasswordService를 통해 비밀번호 재설정 로직 처리

    // 비밀번호 찾기 팝업을 위한 엔드포인트
    @GetMapping("/forgot-password-popup")
    public String showForgotPasswordPopup() {
        return "userauth/forgot-password-popup";  // 비밀번호 찾기 팝업 화면 반환
    }

    // 이메일과 닉네임으로 비밀번호를 재설정하는 엔드포인트
    @PostMapping("/forgot-password")
    @ResponseBody
    public ResponseEntity<String> forgotPassword(@RequestBody PasswordDTO passwordDTO) {
        // PasswordService를 통해 비밀번호 재설정 시도
        String result = passwordService.resetPassword(passwordDTO.getEmail(), passwordDTO.getNickname());

        // 서비스에서 반환된 결과에 따라 응답 처리
        if (result == null) {
            // 해당 이메일과 닉네임을 가진 사용자가 없는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No matching user found.");
        } else if ("탈퇴한 회원입니다".equals(result)) {
            // 사용자가 GUEST인 경우
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
        } else {
            // 비밀번호 재설정이 성공적으로 이루어진 경우
            return ResponseEntity.ok(result);
        }
    }
}