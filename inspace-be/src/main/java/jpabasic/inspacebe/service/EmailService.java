package jpabasic.inspacebe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender; // JavaMailSender를 주입받습니다.

    // 이메일 전송 로직 구현
    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your-email@gmail.com"); // 발신 이메일 (예: Gmail 주소)
        message.setTo(toEmail); // 수신 이메일
        message.setSubject("비밀번호 재설정 링크"); // 이메일 제목
        message.setText("비밀번호를 재설정하려면 아래 링크를 클릭하세요:\n" + resetLink); // 이메일 내용

        // 이메일 전송
        javaMailSender.send(message);
        System.out.println("Password reset email sent to: " + toEmail);
    }
}
