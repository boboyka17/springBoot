package com.example.demo.controller;

import com.example.demo.dto.EmailDto;
import com.example.demo.dto.ResetPasswordDto;
import com.example.demo.dto.VerifyCodeDTO;
import com.example.demo.service.PasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("resetPassword")
public class ResetPasswordController {

    private final PasswordService service;

    public ResetPasswordController(PasswordService service) {
        this.service = service;
    }

    @GetMapping("/")
    public ResponseEntity<String> getData(){
        return ResponseEntity.ok("Tests");
    }


    @PostMapping("/sendEmail")
    public ResponseEntity<Map<String,String>> sendEmail(@RequestBody EmailDto request){
        return ResponseEntity.ok(service.resetPassword(request));
    }

    @PutMapping("/verifyCode")
    public ResponseEntity<Map<String, String>> verifyCode(@RequestBody VerifyCodeDTO request, @RequestParam String token){
        return ResponseEntity.ok(service.verifyCode(request,token));
    }

    @PutMapping("/changePassword")
    public  ResponseEntity<Map<String ,String >> resetPassword(@RequestBody ResetPasswordDto request,@RequestParam String token){
        return ResponseEntity.ok(service.resetPasswordConfirm(request,token));
    }
}
