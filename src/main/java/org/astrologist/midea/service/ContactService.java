package org.astrologist.midea.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(String userEmail, String subject, String content) {
        String adminEmail = "seoulmate01@naver.com"; // 관리자 이메일 주소

        // Create a SimpleMailMessage object
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(adminEmail);
        message.setTo(adminEmail); // You can set this to another recipient if needed
        message.setSubject("문의: " + subject);
        message.setText("문의자 이메일: " + userEmail + "\n\n내용:\n" + content);

        // Send the email
        emailSender.send(message);
    }
}
