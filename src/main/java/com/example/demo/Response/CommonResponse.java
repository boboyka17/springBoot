package com.example.demo.Response;

import lombok.Data;

import java.util.Date;

@Data
public class CommonResponse {
    private String message;
    private String time;
    public CommonResponse(String message) {
        this.message = message;
        this.time = new Date(System.currentTimeMillis()).toString();
    }
}
