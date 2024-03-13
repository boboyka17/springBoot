package com.example.demo.Response;

import lombok.Data;

@Data
public class EmailResponse {
    private String to;
    private  String subject;
    private  String body;
}
