package org.astrologist.midea.controller;

import org.astrologist.midea.dto.PasswordDTO;
import org.astrologist.midea.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/midea")
public class PasswordController {

    @Autowired
    private PasswordService passwordService;

    // 비밀번호 찾기 팝업을 위한 엔드포인트
    @GetMapping("/forgot-password-popup")
    public String showForgotPasswordPopup() {
        return "midea/forgot-password-popup"; // forgot-password-popup 뷰 반환
    }

    // 이메일과 닉네임으로 비밀번호를 재설정하는 엔드포인트
    @PostMapping("/forgot-password")
    @ResponseBody
    public ResponseEntity<String> forgotPassword(@RequestBody PasswordDTO passwordDTO) {
        String newPassword = passwordService.resetPassword(passwordDTO.getEmail(), passwordDTO.getNickname());
        if (newPassword != null) {
            // 새로운 비밀번호를 사용자가 확인할 수 있도록 반환 (실제 서비스에서는 이메일로 전송)
            return ResponseEntity.ok(newPassword);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No matching user found.");
        }
    }
}
