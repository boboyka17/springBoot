package com.example.demo.service;

import com.example.demo.Response.JwtResponse;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService {

    private final long currentTime = new Date(System.currentTimeMillis()).getTime();
    private final UserRepository repository;
    private final JwtService jwtService;
    private final UserServiceImp userService;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    public AuthService(UserRepository repository, JwtService jwtService, UserServiceImp userService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.jwtService = jwtService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public JwtResponse auth(User request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        User user = repository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.generateToken(user);
        return new JwtResponse(token, currentTime);
    }

    public User getInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public JwtResponse register(User request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullname(request.getFullname());
        user.setRole(request.getRole());
        repository.save(user);
        String token = jwtService.generateToken(user);
        return new JwtResponse(token, currentTime);
    }
}
