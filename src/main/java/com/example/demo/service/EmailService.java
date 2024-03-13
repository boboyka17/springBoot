package com.example.demo.service;

import com.example.demo.Response.EmailResponse;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String fromEMail;

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public EmailResponse sendMail(EmailResponse request){
        try {
            EmailResponse emailResponse = new EmailResponse();
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessage.setFrom(fromEMail);
            mimeMessageHelper.setTo(request.getTo());
            mimeMessageHelper.setSubject(request.getSubject());
            mimeMessageHelper.setText(request.getBody());
            javaMailSender.send(mimeMessage);
            return request;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
