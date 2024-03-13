package com.example.demo.controller;

import com.example.demo.Response.JwtResponse;
import com.example.demo.model.User;
import com.example.demo.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody User request) {
        return ResponseEntity.ok(authService.auth(request));
    }

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(@RequestBody User request){
        return ResponseEntity.ok(authService.register(request));
    }


}
