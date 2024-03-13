package com.example.demo.service;

import com.example.demo.Response.EmailResponse;
import com.example.demo.dto.EmailDto;
import com.example.demo.dto.ResetPasswordDto;
import com.example.demo.dto.VerifyCodeDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PasswordService {
    private final UserRepository repository;
    private  final EmailService emailService;

    private enum verify{
        TRUE,
        FALSE
    }
    private  final PasswordEncoder passwordEncoder;

    public PasswordService(UserRepository repository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public Map<String, String> resetPassword(EmailDto request){
        String email = request.getEmail();
        Optional<User> user = repository.findByEmail(email);
        if(user.isEmpty()){
            throw new UsernameNotFoundException(email + " not found");
        }
        User userUpdate = user.get();
        String token = generateToken();
        String code = generateOTP();
        userUpdate.setResetToken(token);
        userUpdate.setTokenExpired(String.valueOf(System.currentTimeMillis()+1000 * 60 * 3));
        userUpdate.setCode(code);
        userUpdate.setCodeVerify(false);
        repository.save(userUpdate);
        EmailResponse emailResponse = new EmailResponse();
        emailResponse.setBody("Verify code is " + code);
        emailResponse.setTo(email);
        emailResponse.setSubject("reset Password");
        emailService.sendMail(emailResponse);
        Map<String,String> map = new HashMap<>();
        map.put("message","Email has been send to " + email);
        map.put("resetToken",token);
        return  map;
    }

    public Map<String,String> verifyCode(VerifyCodeDTO request,String token){
        User userUpdate = validationToken(repository.findByResetToken(token));
        if(!userUpdate.getCode().equals(request.getVerifyCode())){
            throw new RuntimeException("Verify Code is invalid");
        }
        userUpdate.setCodeVerify(true);
        repository.save(userUpdate);
        Map<String,String > map =  new HashMap<>();
        map.put("message","Verify code is valid");
        return map;
    }

    public Map<String,String> resetPasswordConfirm(ResetPasswordDto request,String token){
        String password = request.getPassword();

        User userUpdate = validationToken(repository.findByResetToken(token));
        if (!userUpdate.isCodeVerify()){
            throw  new RuntimeException("Verify Code is not verify");
        }
        userUpdate.setResetToken(null);
        userUpdate.setTokenExpired(null);
        userUpdate.setCodeVerify(false);
        userUpdate.setPassword(passwordEncoder.encode(password));
        repository.save(userUpdate);
        Map<String,String> map = new HashMap<>();
        map.put("message","password has been change");
        return  map;

    }


    private User validationToken(Optional<User> user){
        if (user.isEmpty()){
            throw  new RuntimeException("Token not found");
        }
        if(isTokenExpired(Long.valueOf(user.get().getTokenExpired()))){
            throw  new RuntimeException("Token is expired");
        }
        return user.get();
    }
    private String generateOTP(){
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }


    private Boolean isTokenExpired(Long tokenExpired){
        return System.currentTimeMillis() > tokenExpired;
    }
    private String generateToken(){
        StringBuilder token = new StringBuilder();
        return token.append(UUID.randomUUID().toString())
                .append(UUID.randomUUID().toString()).toString();
    }
}
