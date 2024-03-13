package com.example.demo.Response;

import lombok.Data;

import java.util.Date;

@Data
public class JwtResponse {

    private String accessToken;
    private Long createAt;

    public JwtResponse(String accessToken, Long createAt) {
        this.accessToken = accessToken;
        this.createAt = createAt;
    }
}
